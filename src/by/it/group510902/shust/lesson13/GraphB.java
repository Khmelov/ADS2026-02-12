package by.it.group510902.shust.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\\A");
        if (!scanner.hasNext()) return;
        String input = scanner.next();

        Map<String, List<String>> adj = new HashMap<>();
        for (String edge : input.split(",")) {
            String[] parts = edge.trim().split("\\s*->\\s*");
            String u = parts[0];
            String v = parts[1];
            adj.putIfAbsent(u, new ArrayList<>());
            adj.putIfAbsent(v, new ArrayList<>());
            adj.get(u).add(v);
        }

        Map<String, Integer> state = new HashMap<>();
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

    private static boolean dfs(String node, Map<String, List<String>> adj, Map<String, Integer> state) {
        state.put(node, 1);
        for (String neighbor : adj.get(node)) {
            int neighborState = state.get(neighbor);
            if (neighborState == 1) {
                return true;
            } else if (neighborState == 0) {
                if (dfs(neighbor, adj, state)) {
                    return true;
                }
            }
        }
        state.put(node, 2);
        return false;
    }
}