package by.it.group551002.efimchikov.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphC {
    static Map<String, Set<String>> graph = new HashMap<>();
    static Map<String, Set<String>> reverse = new HashMap<>();
    static Set<String> visited = new HashSet<>();
    static List<String> order = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        graph.clear();
        reverse.clear();
        visited.clear();
        order.clear();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        if (input == null) input = "";

        for (String edge : input.split(",")) {
            edge = edge.trim();
            if (edge.isEmpty()) continue;

            String[] parts = edge.split("->");
            if (parts.length != 2) continue;

            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new HashSet<>()).add(to);
            reverse.computeIfAbsent(to, k -> new HashSet<>()).add(from);

            graph.computeIfAbsent(to, k -> new HashSet<>());
            reverse.computeIfAbsent(from, k -> new HashSet<>());
        }

        List<String> vertices = new ArrayList<>(graph.keySet());
        Collections.sort(vertices);

        for (String vertex : vertices) {
            if (!visited.contains(vertex)) {
                dfs1(vertex);
            }
        }

        visited.clear();
        List<List<String>> components = new ArrayList<>();

        for (int i = order.size() - 1; i >= 0; i--) {
            String vertex = order.get(i);
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfs2(vertex, component);
                Collections.sort(component);
                components.add(component);
            }
        }

        int n = components.size();

        Map<String, Integer> componentId = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (String vertex : components.get(i)) {
                componentId.put(vertex, i);
            }
        }

        List<Set<Integer>> dag = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            dag.add(new HashSet<>());
        }

        int[] indegree = new int[n];

        for (Map.Entry<String, Set<String>> entry : graph.entrySet()) {
            String from = entry.getKey();
            int fromComponent = componentId.get(from);

            for (String to : entry.getValue()) {
                int toComponent = componentId.get(to);

                if (fromComponent != toComponent && dag.get(fromComponent).add(toComponent)) {
                    indegree[toComponent]++;
                }
            }
        }

        PriorityQueue<Integer> queue = new PriorityQueue<>(
                Comparator.comparing(i -> String.join("", components.get(i)))
        );

        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> componentOrder = new ArrayList<>();

        while (!queue.isEmpty()) {
            int current = queue.poll();
            componentOrder.add(current);

            for (int next : dag.get(current)) {
                if (--indegree[next] == 0) {
                    queue.add(next);
                }
            }
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < componentOrder.size(); i++) {
            if (i > 0) {
                result.append('\n');
            }
            result.append(String.join("", components.get(componentOrder.get(i))));
        }

        System.out.print(result);
    }

    static void dfs1(String vertex) {
        visited.add(vertex);

        for (String next : graph.getOrDefault(vertex, Collections.emptySet())) {
            if (!visited.contains(next)) {
                dfs1(next);
            }
        }

        order.add(vertex);
    }

    static void dfs2(String vertex, List<String> component) {
        visited.add(vertex);
        component.add(vertex);

        for (String next : reverse.getOrDefault(vertex, Collections.emptySet())) {
            if (!visited.contains(next)) {
                dfs2(next, component);
            }
        }
    }
}
