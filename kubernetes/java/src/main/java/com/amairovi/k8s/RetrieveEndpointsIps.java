package com.amairovi.k8s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1EndpointAddress;
import io.kubernetes.client.openapi.models.V1EndpointSubset;
import io.kubernetes.client.openapi.models.V1Endpoints;
import io.kubernetes.client.openapi.models.V1EndpointsList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class RetrieveEndpointsIps {

    public static void main(String[] args) throws IOException {
        System.out.println("#checkReadiness: starting");

        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        System.out.println("#checkReadiness: client is built");

        io.kubernetes.client.openapi.Configuration.setDefaultApiClient(client);
        System.out.println("#checkReadiness: setDefaultApiClient");

        CoreV1Api api = new CoreV1Api();
        V1EndpointsList v1EndpointsList = null;
        try {
            v1EndpointsList = api.listNamespacedEndpoints("default",
                    null, null, null,
                    "metadata.name=hello-service",
                    null,
                    null,
                    null,
                    null,
                    null);
        } catch (ApiException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("#checkReadiness: api.listNamespacedEndpoints");
        System.out.println("v1EndpointsList: " + v1EndpointsList);

        List<V1Endpoints> endpoints = v1EndpointsList.getItems();

        // fieldSelector and namespace should filter out irrelevant endpoints
        V1Endpoints endpoint = endpoints.get(0);
        List<V1EndpointSubset> subsets = endpoint.getSubsets();
        // all service's pods share same set of ports
        List<V1EndpointAddress> addresses = subsets.get(0).getAddresses();
        addresses.stream()
                .map(addr -> addr.getIp())
                .forEach(System.out::println);
    }
}
