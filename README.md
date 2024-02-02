# Hello World FullStack Project
This is a local deployment of a full stack project

# Steps to operate fullstack project


## Deploying Kubernetes cluster

Run local container registry
```
docker run -d -p 5010:5000 --name registry registry:2
```

Build Docker images of apps
```
docker build -t react-client:v1 ./frontend/
docker build -t java-spring-server:v1 ./backend/
```

Tag and push Docker images of apps to local container registry
```
docker tag react-client:v1 localhost:5010/react-client:v1
docker push localhost:5010/react-client:v1
docker tag java-spring-server:v1 localhost:5010/java-spring-server:v1
docker push localhost:5010/java-spring-server:v1
```

Deploy pods and services of app
```
kubectl apply -f ./infrastructure/kubernetes/frontend-deployment.yaml
kubectl apply -f ./infrastructure/kubernetes/backend-deployment.yaml
```

## Shutting down Kubernetes cluster

```
kubectl delete deployment java-spring-pod
kubectl delete service java-spring-service
kubectl delete deployment react-pod
kubectl delete service react-service
docker rm -f registry
```