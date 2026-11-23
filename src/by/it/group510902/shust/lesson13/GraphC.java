package by.it.group510902.shust.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\\A");
        if (!scanner.hasNext()) return;
        String input = scanner.next();

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, List<String>> revAdj = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        for (String edge : input.split(",")) {
            String[] parts = edge.trim().split("\\s*->\\s*");
            String u = parts[0];
            String v = parts[1];

            vertices.add(u);
            vertices.add(v);

            adj.putIfAbsent(u, new ArrayList<>());
            adj.putIfAbsent(v, new ArrayList<>());
            revAdj.putIfAbsent(u, new ArrayList<>());
            revAdj.putIfAbsent(v, new ArrayList<>());

            adj.get(u).add(v);
            revAdj.get(v).add(u);
        }

        for (List<String> list : adj.values()) {
            Collections.sort(list);
        }
        for (List<String> list : revAdj.values()) {
            Collections.sort(list);
        }

        List<String> sortedVertices = new ArrayList<>(vertices);
        Collections.sort(sortedVertices);

        Stack<String> order = new Stack<>();
        Set<String> visited = new HashSet<>();

        for (String v : sortedVertices) {
            if (!visited.contains(v)) {
                dfs1(v, adj, visited, order);
            }
        }

        visited.clear();
        List<List<String>> sccs = new ArrayList<>();

        while (!order.isEmpty()) {
            String v = order.pop();
            if (!visited.contains(v)) {
                List<String> component = new ArrayList<>();
                dfs2(v, revAdj, visited, component);
                Collections.sort(component);
                sccs.add(component);
            }
        }

        for (List<String> scc : sccs) {
            for (String v : scc) {
                System.out.print(v);
            }
            System.out.println();
        }
    }

    private static void dfs1(String v, Map<String, List<String>> adj, Set<String> visited, Stack<String> order) {
        visited.add(v);
        for (String neighbor : adj.get(v)) {
            if (!visited.contains(neighbor)) {
                dfs1(neighbor, adj, visited, order);
            }
        }
        order.push(v);
    }

    private static void dfs2(String v, Map<String, List<String>> revAdj, Set<String> visited, List<String> component) {
        visited.add(v);
        component.add(v);
        for (String neighbor : revAdj.get(v)) {
            if (!visited.contains(neighbor)) {
                dfs2(neighbor, revAdj, visited, component);
            }
        }
    }
}