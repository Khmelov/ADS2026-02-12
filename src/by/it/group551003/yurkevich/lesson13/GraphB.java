package by.it.group551003.yurkevich.lesson13;

import java.util.*;

public class GraphB {

    private static boolean hasCycle(Map<String, List<String>> graph) {
        Map<String, Integer> color = new HashMap<>();

        for (String v : graph.keySet()) {
            if (!color.containsKey(v)) {
                if (dfs(v, graph, color)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfs(String v, Map<String, List<String>> graph, Map<String, Integer> color) {
        color.put(v, 1);

        for (String next : graph.getOrDefault(v, Collections.emptyList())) {
            int c = color.getOrDefault(next, 0);
            if (c == 1) {
                return true;
            }
            if (c == 0 && dfs(next, graph, color)) {
                return true;
            }
        }

        color.put(v, 2);
        return false;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) return;

        String line = sc.nextLine().trim();
        Map<String, List<String>> graph = new TreeMap<>();

        if (!line.isEmpty()) {
            String[] edges = line.split(",");
            for (String edge : edges) {
                String[] parts = edge.split("->");
                String from = parts[0].trim();
                String to = parts[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                graph.computeIfAbsent(to, k -> new ArrayList<>());
            }
        }

        System.out.println(hasCycle(graph) ? "yes" : "no");
    }
}