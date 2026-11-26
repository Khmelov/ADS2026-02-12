package by.it.group510901.chibisov.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, Set<String>> graph = new TreeMap<>(Comparator.reverseOrder());

        String input = scanner.nextLine();
        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String u = parts[0];
            String v = parts[1];

            if (!graph.containsKey(u)) {
                graph.put(u, new TreeSet<>(Comparator.reverseOrder()));
            }
            if (!graph.containsKey(v)) {
                graph.put(v, new TreeSet<>(Comparator.reverseOrder()));
            }
            graph.get(u).add(v);
        }

        List<String> topoSort = topoSort(graph);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < topoSort.size(); i++) {
            if (i > 0) sb.append(" ");
            sb.append(topoSort.get(i));
        }

        System.out.println(sb);
    }

    private static List<String> topoSort(Map<String, Set<String>> graph) {
        Set<String> visited = new HashSet<>(graph.size());
        List<String> order = new ArrayList<>(graph.size());
        for (String vertex : graph.keySet()) {
            if (visited.contains(vertex)) continue;
            dfs(graph, vertex, visited, order);
        }
        Collections.reverse(order);
        return order;
    }

    private static void dfs(Map<String, Set<String>> graph, String vertex, Set<String> visited, List<String> order) {
        visited.add(vertex);

        for (String adj : graph.get(vertex)) {
            if (visited.contains(adj)) continue;
            dfs(graph, adj, visited, order);
        }
        order.add(vertex);
    }
}
