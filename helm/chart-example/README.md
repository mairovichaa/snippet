Install and start minikube

```
brew install minikube
minikube start
```

Install helm 
```
brew install helm
```

Start chart museum (any other chart repository)
```
# https://github.com/helm/chartmuseum#docker-image

docker run --rm -it \
  -p 8080:8080 \
  -e DEBUG=1 \
  -e STORAGE=local \
  -e STORAGE_LOCAL_ROOTDIR=/charts \
  -v <path-to-dir-with-chars>:/charts \
  ghcr.io/helm/chartmuseum:v0.13.1 
  
```

Add repository to list of known repositories
```
helm repo add <repo-name> http://localhost:8080
# example
helm repo add local-charts http://localhost:8080
```

Create chart (Chart.yaml, values.yaml, etc)

Check correctness

```
helm template .
```

Create package
```
helm package .

# example of output
# Successfully packaged chart and saved it to: ./<Chart-name>-<Chart version>.tgz
```

Push package to repository
```
curl --data-binary "@<Chart-name>-<Chart version>.tgz" http://localhost:8080/api/charts
# curl --data-binary "@test-chart-0.0.1.tgz" http://localhost:8080/api/charts
```

Check presence of the chart in the repo via repository server API
```
curl http://localhost:8080/api/charts
```

Update helm information about content in repositories
```
helm repo update
helm search repo <Chart-name>
```

Check content of the chart
```
helm template <repo-name>/<Chart-name>
# helm template local-charts/test-chart

helm template <repo-name>/<Chart-name> --version=<Chart version>
# helm template local-charts/test-chart --version=0.0.2

helm template  --set <val name>=<val override> <repo-name>/<Chart-name>
# helm template --set custom.val1=val1 local-charts/test-chart 
```

Install chart
```
helm install <repo-name>/<Chart-name> --generate-name
# helm install local-charts/test-chart --generate-name
```

Check status of chart release
```
helm list
```

Check resource in k8s
```
kubectl get configmaps
kubectl get configmaps chart-example -o json
```

