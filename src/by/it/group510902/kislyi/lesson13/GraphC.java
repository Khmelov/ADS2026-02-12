package by.it.group510902.kislyi.lesson13;

import java.util.*;

public class GraphC {

    private static void dfs1(String v,
                             Map<String, List<String>> graph,
                             Set<String> visited,
                             List<String> order) {
        visited.add(v);
        for (String next : graph.getOrDefault(v, Collections.emptyList())) {
            if (!visited.contains(next)) {
                dfs1(next, graph, visited, order);
            }
        }
        order.add(v);
    }

    private static void dfs2(String v,
                             Map<String, List<String>> graph,
                             Set<String> visited,
                             List<String> component) {
        visited.add(v);
        component.add(v);
        for (String next : graph.getOrDefault(v, Collections.emptyList())) {
            if (!visited.contains(next)) {
                dfs2(next, graph, visited, component);
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) return;

        String line = sc.nextLine().trim();
        Map<String, List<String>> graph = new TreeMap<>();
        Map<String, List<String>> reverse = new TreeMap<>();
        Set<String> allVertices = new TreeSet<>();

        if (!line.isEmpty()) {
            for (String edge : line.split(",")) {
                String[] parts = edge.split("->");
                String from = parts[0].trim();
                String to = parts[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                reverse.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

                allVertices.add(from);
                allVertices.add(to);
            }
        }

        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();
        for (String v : allVertices) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, order);
            }
        }

        visited.clear();
        List<List<String>> components = new ArrayList<>();

        for (int i = order.size() - 1; i >= 0; i--) {
            String v = order.get(i);
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, reverse, visited, comp);
                Collections.sort(comp);
                components.add(comp);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (List<String> comp : components) {
            for (String v : comp) {
                sb.append(v);
            }
            sb.append("\n");
        }
        System.out.print(sb);
    }
}