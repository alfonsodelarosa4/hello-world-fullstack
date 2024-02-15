# Hello World FullStack Project
## Overview
This project showcases a fullstack web application leveraging React for the frontend and Java Spring for the backend. It utilizes Docker and Kubernetes for containerization and orchestration, allowing for efficient development and deployment workflows.

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