package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class Task10 {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Neighbor {

        private String name;
        private int distance;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class NeighborDijkstra implements Comparable<NeighborDijkstra> {

        private String name;
        private int distance;
        private boolean visited;

        @Override
        public int compareTo(NeighborDijkstra o) {
            if (!visited && o.visited) {
                return -1;
            }

            if (visited && !o.visited) {
                return 1;
            }

//            visited && o.visited
            return Integer.compare(distance, o.distance);
        }

        public boolean isNotReachable() {
            return distance == Integer.MAX_VALUE;
        }

    }


    public static List<Neighbor> algo(Map<String, Set<Neighbor>> graph, String startCityName) {
        if (graph.size() == 1) {
            return Collections.singletonList(new Neighbor(startCityName, 0));
        }

        Map<String, NeighborDijkstra> map = new HashMap<>();
        PriorityQueue<NeighborDijkstra> vector = new PriorityQueue<>();

        init(graph, map, vector, startCityName);

        while (!vector.isEmpty()) {
            NeighborDijkstra currentV = vector.poll();
            String name = currentV.getName();
            NeighborDijkstra neighborDijkstra = map.get(name);

            if (neighborDijkstra.isVisited()) {
                continue;
            }

            Set<Neighbor> neighbors = graph.get(name);

            for (Neighbor neighbor : neighbors) {
                String neighborName = neighbor.getName();
                NeighborDijkstra neighborValue = map.get(neighborName);

                int lengthThroughCurrentV = currentV.getDistance() + neighbor.getDistance();
                int currentMinV = neighborValue.getDistance();
                if (lengthThroughCurrentV < currentMinV) {
                    neighborValue.setDistance(lengthThroughCurrentV);
                    vector.add(new NeighborDijkstra(neighborName, lengthThroughCurrentV, false));
                }
            }

            neighborDijkstra.setVisited(true);
        }


        return map.values()
                .stream()
                .map(n -> new Neighbor(n.getName(), n.isNotReachable() ? -1 : n.getDistance()))
                .collect(toList());
    }

    private static void init(Map<String, Set<Neighbor>> graph, Map<String, NeighborDijkstra> map, PriorityQueue<NeighborDijkstra> vector, String startCityName) {
        graph.keySet()
                .forEach(cityName -> map.put(cityName, new NeighborDijkstra(cityName, Integer.MAX_VALUE, false)));
        map.get(startCityName).setDistance(0);

        vector.add(new NeighborDijkstra(startCityName, 0, false));
    }

}
