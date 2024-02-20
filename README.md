# Hello World FullStack Project
## Overview
This project showcases a fullstack web application leveraging React for the frontend and Java Spring for the backend. It is a small fullstack project that receives


It utilizes Docker and Kubernetes for containerization and orchestration, allowing for efficient development and deployment workflows. In addition, this project contains a Jenkins server for CI/CD.

## Purpose
The purpose of this project is to provide an example of building and deploying a full-stack web application using modern technologies and best practices. It acts as a simulation of full-stack project in production. The React client is run as a Docker container which communicates to the pods of the Java Spring server.

# Requirements:
- node.js
- docker
- kubernetes
- jdk 21
- gradle

# Steps to operate fullstack project
## Deploying React client in a Docker container
Build Docker image of React client
```
docker build -t react-client:v1 ./frontend/ --no-cache
```

Run React client as a Docker container
```
docker run -d -p 3000:3000 react-client:v1
```

## Build JAR file of Java Spring server
```
cd ./backend/hello-world-server/
gradle build
cd ../..
```

## Deploying Kubernetes cluster of backend

**Note:** Make sure to build the jar file before deploying cluster!!

Run local container registry
```
docker run -d -p 5010:5000 --name registry registry:2
```

Build the JAR file
```
gradlew run ./backend/hello-world-server/
```

Build Docker image of Java Spring server
```
docker build -t java-spring-server:v1 ./backend/ --no-cache
```

Tag and push Docker images of apps to local container registry
```
docker tag java-spring-server:v1 localhost:5010/java-spring-server:v1
docker push localhost:5010/java-spring-server:v1
```

Deploy pods and services of app
```
kubectl apply -f ./infrastructure/kubernetes/backend-deployment.yaml
```

# Steps to shutdown fullstack project
## Shutting down React client
Find the container id or name of the react-client container
```
docker ps
```

Stop the react-client container
```
docker stop <container_id_or_name>
```

## Shutting down Kubernetes cluster of backend
Shutdown the deployment of the pods and service
Deletes image registry
```
kubectl delete deployment java-spring-pod
kubectl delete service java-spring-service
docker rm -f registry
```

# Steps to create and deploy Jenkins server locally
The following will provide instructions to create and deploy a Jenkins server locally. The purpose of this Jenkins server is to run tests for both the frontend and backend every time someone makes a pull request. (To test this specific project on your system, it would be best to fork the project as your own).

## Get personal access token of project
***Why?:*** This allows Jenkins to have access to your project.
1. Go to **GitHub**
2. Go to **Settings**
3. Go to **Developer settings**
4. Go to **Fine-grained tokens**
5. Click **Generate new token**
6. Provide token name, expiration, select this repo (assuming your forked it)
7. Click **Generate token**
8. Save this token somewhere safe: **[personal-access-token]**

## Installation
Create and run Jenkins server
```
cd infrastructure\jenkins-compose\Dockerfile
docker-compose up
```
Save auth token from command line

## Setup Jenkins server
1. Open the following link: https://localhost/8090
2. Enter auth token on page
3. Create profile
4. Install the following plugins:
    * Blue Ocean
    * Docker
    * Docker Commons
    * NodeJS
    * Generic Webhook Trigger
5. Install the following tools (choose download/automatic installation settings):
    * gradle as 'Gradle-8.5'
    * nodejs as 'NodeJS-21.76.1'

## Create Jenkins pipeline
1. Select create pipeline
2. Check ✅ **Generic Webhook Trigger**
3. Create **branch name** variable under **Generic Webhook Trigger**
    ![](https://github.com/alfonsodelarosa4/hello-world-fullstack/blob/main/captures/generic-webhook-screenshot.JPG)
    1. Click **Add** under **Post content parameters**
    2. Name of variable -> branchName
    3. Expression -> .ref
    4. Check ✅ **JSONPath**
    5. Value filter -> \\[\"refs\/heads\/|\"\\]
5. Create **pull request author** variable under **Generic Webhook Trigger**
    1. Click **Add** under **Post content parameters**
    2. Name of variable -> author
    3. Expression -> .pusher.name
    4. Check ✅ **JSONPath**
6. Create **commit message** variable under **Generic Webhook Trigger**
    1. Click **Add** under **Post content parameters**
    2. Name of variable -> commitMessage
    3. Expression -> .head_commit.message
    4. Check ✅ **JSONPath**
7. Create Generic Cause value
    1. Generic Cause -> $commitMessage commit from $author
8. Create token under **Token** 
    * save this token: **[generic-webhook-token]**
9. Definition -> Pipeline script from SCM
10. SCM -> Git
11. Repository URL -> https://**[personal-access-token]**@github.com/**[project-link]**.git
12. Script Path -> infrastructure\jenkins-testing\Jenkinsfile (relative location of Jenkinsfile)
13. branch specifier -> $branchName
14. uncheck **lightweight checkout**

## Create ngrok tunnel from GitHub webhook to local Jenkins server
1. Create ngrok account online
2. Find your ngork auth token online
3. Download ngrok
4. Open command line where ngrok is located
5. Run the following command to log into ngrok
```
ngrok.exe authtoken <ngrok-auth-token>
```
6. Run the following command to start Ngrok tunnel
```
ngrok.exe http 8090
```
7. Save the ngrok link address: **ngrok-link**

## Setup GitHub Webhook
1. Go to **Settings** of GitHub repository
2. Go to **WehHooks**
3. Click **Add webhook**
4. Payload URL -> **[ngrok-link]**/generic-webhook-trigger/invoke?token=**[generic-webhook-token]**
5. Ensure the following is selected: **Just the push event** and **Active**
