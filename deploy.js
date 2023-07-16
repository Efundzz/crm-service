const shell = require('shelljs');
const fs = require('fs');
const xml2js = require('xml2js');

// Get the current branch name
let branch = shell.exec('git rev-parse --abbrev-ref HEAD', {silent:true}).stdout.trim();

// Define the image repository
const region = 'ap-south-1';
const accountId = '247423452789';

// Read the version from pom.xml
let pomStr = fs.readFileSync('pom.xml', 'utf8');
let pom;
xml2js.parseString(pomStr, (err, result) => {
    if (err) {
        console.error('Failed to parse pom.xml');
        process.exit(1);
    }
    pom = result;
});

let version = pom.project.version[0];
let repositoryName = pom.project.artifactId[0];

let tag;

if (branch === 'main' || branch.startsWith('release')) {
    // Release build
    // Remove -SNAPSHOT if present
    version = version.replace('-SNAPSHOT', '');
    tag = version;
    let [major, minor, patch] = version.split('.').map(Number);
    minor++;
    const nextVersion = `${major}.${minor}.${patch}`;
    if (shell.exec(`./mvnw versions:set -DnewVersion=${nextVersion}-SNAPSHOT`).code !== 0) {
        shell.echo('Error: Failed to update version in pom.xml');
        shell.exit(1);
    }
    // Commit the version update
    if (shell.exec(`git commit -am "prepare for next development iteration"`).code !== 0) {
        shell.echo('Error: Failed to commit version update');
        shell.exit(1);
    }
} else {
    // Snapshot build
    // Add -SNAPSHOT if not present
    if (!version.endsWith('-SNAPSHOT')) {
        version += '-SNAPSHOT';
    }
    tag = version;
}

// Set imageName
const imageName = `${accountId}.dkr.ecr.${region}.amazonaws.com/${repositoryName}:${tag}`;
shell.echo(`Image name: ${imageName}`);
// Build the Docker image
if (shell.exec(`./mvnw clean package spring-boot:build-image -Dspring-boot.build-image.imageName=${imageName}`).code !== 0) {
    shell.echo('Error: Maven build or Image build failed');
    shell.exit(1);
}

// Authenticate Docker to ECR
if (shell.exec(`aws ecr get-login-password --region ${region} --profile efundzz | docker login --username AWS --password-stdin ${accountId}.dkr.ecr.${region}.amazonaws.com`).code !== 0) {
    shell.echo('Error: Docker login failed');
    shell.exit(1);
}

// Push the image to ECR
if (shell.exec(`docker push ${imageName}`).code !== 0) {
    shell.echo('Error: Docker push failed');
    shell.exit(1);
}

shell.echo('Image successfully pushed to ECR');
