package by.it.group510902.sorokoletov.lesson13;

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
        Map<String, Set<String>> reversed = new TreeMap<>();

        String[] edges = input.split("\\s*,\\s*");
        for (String edge : edges) {
            if (edge.isEmpty()) {
                continue;
            }
            String[] parts = edge.split("\\s*->\\s*");
            if (parts.length != 2) {
                continue;
            }
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new TreeSet<>()).add(to);
            graph.computeIfAbsent(to, k -> new TreeSet<>());

            reversed.computeIfAbsent(to, k -> new TreeSet<>()).add(from);
            reversed.computeIfAbsent(from, k -> new TreeSet<>());
        }


        Set<String> visited = new HashSet<>();
        List<String> finishOrder = new ArrayList<>();

        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfs1(vertex, graph, visited, finishOrder);
            }
        }

        visited.clear();
        List<List<String>> components = new ArrayList<>();

        for (int i = finishOrder.size() - 1; i >= 0; i--) {
            String vertex = finishOrder.get(i);
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfs2(vertex, reversed, visited, component);
                Collections.sort(component);
                components.add(component);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (List<String> comp : components) {
            for (String v : comp) {
                sb.append(v);
            }
            sb.append('\n');
        }
        System.out.print(sb);
    }

    private static void dfs1(String vertex, Map<String, Set<String>> graph,
                             Set<String> visited, List<String> finishOrder) {
        visited.add(vertex);
        for (String next : graph.getOrDefault(vertex, Collections.emptySet())) {
            if (!visited.contains(next)) {
                dfs1(next, graph, visited, finishOrder);
            }
        }
        finishOrder.add(vertex);
    }

    private static void dfs2(String vertex, Map<String, Set<String>> reversed,
                             Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);
        for (String next : reversed.getOrDefault(vertex, Collections.emptySet())) {
            if (!visited.contains(next)) {
                dfs2(next, reversed, visited, component);
            }
        }
    }
}