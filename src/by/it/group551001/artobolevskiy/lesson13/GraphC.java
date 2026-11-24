package by.it.group551001.artobolevskiy.lesson13;

import java.util.*;

public class GraphC {
    private final static Map<String, List<String>> adj = new HashMap<>();
    private final static Map<String, List<String>> adjRev = new HashMap<>();
    private final static Set<String> visited = new HashSet<>();
    private final static List<String> order = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String input = scanner.nextLine();

        Set<String> allVertices = new TreeSet<>();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            if (edge.trim().isEmpty()) continue;
            String[] parts = edge.split("\\s*->\\s*");
            if (parts.length != 2) continue;

            String u = parts[0].trim();
            String v = parts[1].trim();

            allVertices.add(u);
            allVertices.add(v);

            adj.putIfAbsent(u, new ArrayList<>());
            adj.get(u).add(v);

            adjRev.putIfAbsent(v, new ArrayList<>());
            adjRev.get(v).add(u);
        }

        for (String v : allVertices) {
            if (!visited.contains(v)) {
                dfs1(v);
            }
        }

        visited.clear();
        Collections.reverse(order);

        for (String v : order) {
            if (!visited.contains(v)) {
                List<String> component = new ArrayList<>();
                dfs2(v, component);

                Collections.sort(component);

                StringBuilder sb = new StringBuilder();
                for (String vertex : component) {
                    sb.append(vertex);
                }
                System.out.println(sb.toString());
            }
        }
    }

    private static void dfs1(String u) {
        visited.add(u);
        if (adj.containsKey(u)) {
            for (String v : adj.get(u)) {
                if (!visited.contains(v)) {
                    dfs1(v);
                }
            }
        }
        order.add(u);
    }

    private static void dfs2(String u, List<String> component) {
        visited.add(u);
        component.add(u);
        if (adjRev.containsKey(u)) {
            for (String v : adjRev.get(u)) {
                if (!visited.contains(v)) {
                    dfs2(v, component);
                }
            }
        }
    }
}

