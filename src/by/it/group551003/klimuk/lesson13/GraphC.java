package by.it.group551003.klimuk.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return;
        }

        Map<String, Set<String>> graph = new TreeMap<>();
        Map<String, Set<String>> revGraph = new TreeMap<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length == 2) {
                String u = parts[0].trim();
                String v = parts[1].trim();

                graph.computeIfAbsent(u, k -> new TreeSet<>()).add(v);
                graph.computeIfAbsent(v, k -> new TreeSet<>());

                revGraph.computeIfAbsent(v, k -> new TreeSet<>()).add(u);
                revGraph.computeIfAbsent(u, k -> new TreeSet<>());
            }
        }

        Set<String> visited = new HashSet<>();
        Deque<String> order = new ArrayDeque<>();

        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfs1(vertex, graph, visited, order);
            }
        }

        visited.clear();
        while (!order.isEmpty()) {
            String vertex = order.pop();
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfs2(vertex, revGraph, visited, component);

                Collections.sort(component);

                for (String v : component) {
                    System.out.print(v);
                }
                System.out.println();
            }
        }
    }

    private static void dfs1(String u, Map<String, Set<String>> graph, Set<String> visited, Deque<String> order) {
        visited.add(u);
        for (String v : graph.get(u)) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, order);
            }
        }
        order.push(u);
    }

    private static void dfs2(String u, Map<String, Set<String>> revGraph, Set<String> visited, List<String> component) {
        visited.add(u);
        component.add(u);
        for (String v : revGraph.get(u)) {
            if (!visited.contains(v)) {
                dfs2(v, revGraph, visited, component);
            }
        }
    }
}