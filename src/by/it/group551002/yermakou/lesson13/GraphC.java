package by.it.group551002.yermakou.lesson13;

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

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> revGraph = new HashMap<>();
        Set<String> allVertices = new TreeSet<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                revGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

                allVertices.add(from);
                allVertices.add(to);
            }
        }

        for (List<String> neighbors : graph.values()) {
            Collections.sort(neighbors);
        }
        for (List<String> neighbors : revGraph.values()) {
            Collections.sort(neighbors);
        }

        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();

        for (String vertex : allVertices) {
            if (!visited.contains(vertex)) {
                dfs1(vertex, graph, visited, stack);
            }
        }

        visited.clear();
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                Set<String> component = new TreeSet<>();
                dfs2(vertex, revGraph, visited, component);

                StringBuilder sb = new StringBuilder();
                for (String v : component) {
                    sb.append(v);
                }
                System.out.println(sb.toString());
            }
        }
    }

    private static void dfs1(String current, Map<String, List<String>> graph, Set<String> visited, Deque<String> stack) {
        visited.add(current);
        List<String> neighbors = graph.get(current);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    dfs1(neighbor, graph, visited, stack);
                }
            }
        }
        stack.push(current);
    }

    private static void dfs2(String current, Map<String, List<String>> revGraph, Set<String> visited, Set<String> component) {
        visited.add(current);
        component.add(current);
        List<String> neighbors = revGraph.get(current);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    dfs2(neighbor, revGraph, visited, component);
                }
            }
        }
    }
}
