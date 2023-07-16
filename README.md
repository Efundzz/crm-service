# CRM Service

```bash
aws ecr get-login-password --region ap-south-1 --profile efundzz | docker login --username AWS --password-stdin 247423452789.dkr.ecr.ap-south-1.amazonaws.com
```

```bash
docker push 247423452789.dkr.ecr.ap-south-1.amazonaws.com/crm-service:0.0.1-SNAPSHOT
```

```xml
<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<image>
						<name>${project.ecr-base}/${project.name}:${project.version}</name>
					</image>
				</configuration>
				<executions>
					<execution>
						<goals>
							<goal>repackage</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
```