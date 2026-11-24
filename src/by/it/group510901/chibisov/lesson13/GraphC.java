package by.it.group510901.chibisov.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, Set<String>> graph = new HashMap<>();

        String input = scanner.nextLine();
        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split("->");
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

        List<List<String>> SCCs = findSCC(graph);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < SCCs.size(); i++) {
            for (String vertex : SCCs.get(i)) {
                sb.append(vertex);
            }
            if (i < SCCs.size() - 1) sb.append("\n");
        }

        System.out.println(sb);
    }

    private static List<List<String>> findSCC(Map<String, Set<String>> graph) {
        Set<String> visited = new HashSet<>(graph.size());
        List<String> postOrder = new ArrayList<>(graph.size());

        for (String vertex : graph.keySet()) {
            if (visited.contains(vertex)) continue;
            dfs(graph, vertex, visited, postOrder);
        }
        Collections.reverse(postOrder);

        Map<String, Set<String>> transposed = new HashMap<>();
        for (String u : graph.keySet()) {
            if (!transposed.containsKey(u)) {
                transposed.put(u, new HashSet<>());
            }
            for (String v : graph.get(u)) {
                if (!transposed.containsKey(v)) {
                    transposed.put(v, new HashSet<>());
                }
                transposed.get(v).add(u);
            }
        }

        visited.clear();
        List<List<String>> result = new ArrayList<>();

        for (String vertex : postOrder) {
            if (visited.contains(vertex)) continue;
            List<String> component = new ArrayList<>();
            dfs(transposed, vertex, visited, component);
            Collections.sort(component);
            result.add(component);
        }

        return result;
    }

    private static void dfs(Map<String, Set<String>> graph,
                            String vertex, Set<String> visited,
                            List<String> order) {
        visited.add(vertex);

        for (String adj : graph.get(vertex)) {
            if (visited.contains(adj)) continue;
            dfs(graph, adj, visited, order);
        }
        order.add(vertex);
    }
}
