package by.it.group551001.bogush.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphA {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        Map<String, List<String>> adjList = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Set<String> allNodes = new HashSet<>();

        String[] edges = line.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length == 2) {
                String u = parts[0].trim();
                String v = parts[1].trim();

                allNodes.add(u);
                allNodes.add(v);

                adjList.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
                inDegree.put(v, inDegree.getOrDefault(v, 0) + 1);
                inDegree.putIfAbsent(u, 0);
            }
        }

        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String node : allNodes) {
            if (inDegree.getOrDefault(node, 0) == 0) {
                pq.add(node);
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            String curr = pq.poll();
            result.add(curr);

            List<String> neighbors = adjList.get(curr);
            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) {
                        pq.add(neighbor);
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String node : result) {
            sb.append(node).append(" ");
        }
        System.out.println(sb.toString().trim());
    }
}
