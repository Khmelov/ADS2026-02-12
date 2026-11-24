package by.it.group551003.parkhaniuk.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }

        String input = scanner.nextLine();

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Set<String> allNodes = new TreeSet<>();


        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length != 2) continue;

            String u = parts[0].trim();
            String v = parts[1].trim();

            allNodes.add(u);
            allNodes.add(v);

            adj.putIfAbsent(u, new ArrayList<>());
            adj.get(u).add(v);

            inDegree.putIfAbsent(u, 0);
            inDegree.putIfAbsent(v, 0);

            inDegree.put(v, inDegree.get(v) + 1);
        }

        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String node : allNodes) {
            if (inDegree.getOrDefault(node, 0) == 0) {
                pq.add(node);
            }
        }

        List<String> topoOrder = new ArrayList<>();

        while (!pq.isEmpty()) {
            String u = pq.poll();
            topoOrder.add(u);

            if (adj.containsKey(u)) {
                List<String> neighbors = adj.get(u);
                for (String v : neighbors) {
                    inDegree.put(v, inDegree.get(v) - 1);
                    if (inDegree.get(v) == 0) {
                        pq.add(v);
                    }
                }
            }
        }

        System.out.println(String.join(" ", topoOrder));
    }
}
