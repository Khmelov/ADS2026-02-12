package by.it.group551001.zlotnikov.lesson13;

import java.util.*;

public class GraphB {
    private static boolean hasCycle(String u, Map<String, List<String>> adj, Map<String, Integer> state) {
        state.put(u, 1);
        for (String v : adj.getOrDefault(u, Collections.emptyList())) {
            int vState = state.getOrDefault(v, 0);
            if (vState == 1) {
                return true;
            }
            if (vState == 0 && hasCycle(v, adj, state)) {
                return true;
            }
        }
        state.put(u, 2);
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            System.out.println("no");
            return;
        }
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            System.out.println("no");
            return;
        }

        Map<String, List<String>> adj = new HashMap<>();
        Set<String> vertices = new LinkedHashSet<>();

        String[] edges = line.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length != 2) {
                continue;
            }
            String u = parts[0].trim();
            String v = parts[1].trim();
            vertices.add(u);
            vertices.add(v);
            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
        }

        Map<String, Integer> state = new HashMap<>();
        for (String v : vertices) {
            if (state.getOrDefault(v, 0) == 0) {
                if (hasCycle(v, adj, state)) {
                    System.out.println("yes");
                    return;
                }
            }
        }
        System.out.println("no");
    }
}
