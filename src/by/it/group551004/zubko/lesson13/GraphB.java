package by.it.group551004.zubko.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(line);
            }
        }
        String input = sb.toString();
        if (input.isEmpty()) {
            System.out.println("no");
            return;
        }

        Map<String, Set<String>> adj = new HashMap<>();
        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                adj.putIfAbsent(from, new HashSet<>());
                adj.putIfAbsent(to, new HashSet<>());
                adj.get(from).add(to);
            }
        }

        Map<String, Integer> state = new HashMap<>(); // 0: unvisited, 1: visiting, 2: visited
        for (String node : adj.keySet()) {
            state.put(node, 0);
        }

        boolean hasCycle = false;
        for (String node : adj.keySet()) {
            if (state.get(node) == 0) {
                if (dfs(node, adj, state)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfs(String u, Map<String, Set<String>> adj, Map<String, Integer> state) {
        state.put(u, 1);
        for (String v : adj.getOrDefault(u, Collections.emptySet())) {
            int s = state.getOrDefault(v, 0);
            if (s == 1) return true;
            if (s == 0 && dfs(v, adj, state)) return true;
        }
        state.put(u, 2);
        return false;
    }
}
