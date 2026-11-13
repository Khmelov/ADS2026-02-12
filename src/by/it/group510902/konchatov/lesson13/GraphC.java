package by.it.group510902.konchatov.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();

        Map<String, List<String>> graph = new TreeMap<>();
        Map<String, List<String>> reverse = new TreeMap<>();

        if (!line.isEmpty()) {
            for (String edge : line.split(",")) {
                String[] parts = edge.trim().split("->");
                String from = parts[0].trim();
                String to = parts[1].trim();

                graph.putIfAbsent(from, new ArrayList<>());
                graph.putIfAbsent(to, new ArrayList<>());
                reverse.putIfAbsent(from, new ArrayList<>());
                reverse.putIfAbsent(to, new ArrayList<>());

                graph.get(from).add(to);
                reverse.get(to).add(from);
            }
        }

        Set<String> visited = new HashSet<>();
        Deque<String> order = new ArrayDeque<>();
        for (String v : graph.keySet()) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, order);
            }
        }

        visited.clear();
        List<List<String>> components = new ArrayList<>();
        while (!order.isEmpty()) {
            String v = order.pop();
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, reverse, visited, comp);
                Collections.sort(comp);
                components.add(comp);
            }
        }

        for (List<String> comp : components) {
            System.out.println(String.join("", comp));
        }
    }

    private static void dfs1(String v, Map<String, List<String>> graph,
                             Set<String> visited, Deque<String> order) {
        visited.add(v);
        for (String neighbor : graph.get(v)) {
            if (!visited.contains(neighbor)) {
                dfs1(neighbor, graph, visited, order);
            }
        }
        order.push(v);
    }

    private static void dfs2(String v, Map<String, List<String>> reverse,
                             Set<String> visited, List<String> comp) {
        visited.add(v);
        comp.add(v);
        for (String neighbor : reverse.get(v)) {
            if (!visited.contains(neighbor)) {
                dfs2(neighbor, reverse, visited, comp);
            }
        }
    }
}
