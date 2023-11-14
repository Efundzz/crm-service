package com.efundzz.crmservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class Auth0ManagementService {

    private final String domain;
    private final String apiClientId;
    private final String apiClientSecret;
    private final String connectionId;
    private final RestTemplate restTemplate;


    // Constructor-based injection of the configuration properties
    public Auth0ManagementService(
            @Value("${auth0.domain}") String domain,
            @Value("${auth0.apiClientId}") String apiClientId,
            @Value("${auth0.apiClientSecret}") String apiClientSecret,
            @Value("${auth0.connectionId}") String connectionId,
            RestTemplateBuilder restTemplateBuilder) {
        this.domain = domain;
        this.apiClientId = apiClientId;
        this.apiClientSecret = apiClientSecret;
        this.connectionId = connectionId;
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getManagementApiToken() {
        String tokenUrl = "https://" + domain + "/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("client_id", apiClientId);
        body.put("client_secret", apiClientSecret);
        body.put("audience", "https://" + domain + "/api/v2/");
        body.put("grant_type", "client_credentials");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        return (String) response.getBody().get("access_token");
    }

    public String createOrganization(String name, String displayName, String franchisePrefix) {
        String organizationsUrl = "https://" + domain + "/api/v2/organizations";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getManagementApiToken());

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("display_name", displayName);
        // Add franchisePrefix to metadata if needed
        body.put("metadata", Collections.singletonMap("franchise_prefix", franchisePrefix));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(organizationsUrl, request, Map.class);

        if (!name.matches("[A-Za-z0-9\\-\\_]+")) {
            throw new IllegalArgumentException("Organization name does not meet Auth0 requirements.");
        }

        String orgId = (String) response.getBody().get("id");
        enableConnectionForOrganization(orgId, connectionId);

        return orgId;
    }

    private void enableConnectionForOrganization(String orgId, String connectionId) {
        String token = getManagementApiToken();
        String enableConnectionUrl = "https://" + domain + "/api/v2/organizations/" + orgId + "/enabled_connections";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> body = new HashMap<>();
        body.put("connection_id", connectionId);
        body.put("assign_membership_on_login", false); // Automatically assign users to the organization

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(enableConnectionUrl, request, Map.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to enable connection for organization: " + response.getBody());
        }
        // Handle the response, check for errors, etc.
    }

    public String createAdminUser(String email, String phoneNumber, String organizationId) {
        String usersUrl = "https://" + domain + "/api/v2/users";
        String token = getManagementApiToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        //String tempPassword = generateStrongPassword();
        // Create user payload
        Map<String, Object> createUserBody = new HashMap<>();
        createUserBody.put("email", email);
        createUserBody.put("email_verified", false);
        createUserBody.put("password", "Passw0rd"); // TODO Update Create Password
        createUserBody.put("connection", "Username-Password-Authentication"); // Replace with your actual connection name

        HttpEntity<Map<String, Object>> createUserRequest = new HttpEntity<>(createUserBody, headers);
        ResponseEntity<Map> createUserResponse = restTemplate.postForEntity(usersUrl, createUserRequest, Map.class);

        // Check if the user was created successfully
        if (!createUserResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to create user: " + createUserResponse.getBody());
        }

        String userId = (String) createUserResponse.getBody().get("user_id");

        // Assign the user to the organization
        String orgsUrl = "https://" + domain + "/api/v2/organizations/" + organizationId + "/members";

        Map<String, Object> assignUserBody = new HashMap<>();
        assignUserBody.put("members", Collections.singletonList(userId));

        HttpEntity<Map<String, Object>> assignUserRequest = new HttpEntity<>(assignUserBody, headers);
        ResponseEntity<Map> assignUserResponse = restTemplate.postForEntity(orgsUrl, assignUserRequest, Map.class);

        // Check if the user was assigned successfully
        if (!assignUserResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to assign user to organization: " + assignUserResponse.getBody());
        }
        return userId;
    }

    private void triggerPasswordReset(String email) {
        String changePasswordUrl = "https://" + domain + "/dbconnections/change_password";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("client_id", apiClientId);
        body.put("email", email);
        body.put("connection", "Username-Password-Authentication");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(changePasswordUrl, request, String.class);
    }
}

