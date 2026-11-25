package by.it.group510902.konchatov.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();

        Map<String, List<String>> graph = new TreeMap<>();
        Map<String, Integer> coutvhod = new TreeMap<>();

        if (!line.isEmpty()) {
            for (String edge : line.split(",")) {
                String[] parts = edge.trim().split("->");
                String from = parts[0].trim();
                String to = parts[1].trim();

                graph.putIfAbsent(from, new ArrayList<>());
                graph.putIfAbsent(to, new ArrayList<>());
                coutvhod.putIfAbsent(from, 0);
                coutvhod.putIfAbsent(to, 0);

                graph.get(from).add(to);
                coutvhod.put(to, coutvhod.get(to) + 1);
            }
        }

        PriorityQueue<String> q = new PriorityQueue<>();
        for (String v : coutvhod.keySet()) {
            if (coutvhod.get(v) == 0) q.add(v);
        }

        List<String> result = new ArrayList<>();
        while (!q.isEmpty()) {
            String v = q.poll();
            result.add(v);
            for (String a : graph.get(v)) {
                coutvhod.put(a, coutvhod.get(a) - 1);
                if (coutvhod.get(a) == 0) {
                    q.add(a);
                }
            }
        }

        System.out.println(String.join(" ", result));
    }
}
