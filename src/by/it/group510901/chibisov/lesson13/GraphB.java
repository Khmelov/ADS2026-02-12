package by.it.group510901.chibisov.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, Set<String>> graph = new HashMap<>();

        String input = scanner.nextLine();
        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String u = parts[0];
            String v = parts[1];

            if (!graph.containsKey(u)) {
                graph.put(u, new HashSet<>());
            }
            if (!graph.containsKey(v)) {
                graph.put(v, new HashSet<>());
            }
            graph.get(u).add(v);
        }

        String result = isCyclic(graph) ? "yes" : "no";
        System.out.println(result);
    }

    private static boolean isCyclic(Map<String, Set<String>> graph) {
        Set<String> visited = new HashSet<>(graph.size());
        Set<String> finished = new HashSet<>(graph.size());
        for (String vertex : graph.keySet()) {
            if (visited.contains(vertex)) continue;
            if (dfs(graph, vertex, visited, finished)) {
                return true;
            }
        }
        return false;
    }

    private static boolean dfs(Map<String, Set<String>> graph, String vertex, Set<String> visited, Set<String> finished) {
        visited.add(vertex);
        for (String adj : graph.get(vertex)) {
            if (visited.contains(adj) && !finished.contains(adj)) {
                return true;
            }
            if (!visited.contains(adj)) {
                if (dfs(graph, adj, visited, finished)) {
                    return true;
                }
            }
        }
        finished.add(vertex);
        return false;
    }
}
