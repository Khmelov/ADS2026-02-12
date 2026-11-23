package by.it.group551003.parkhaniuk.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }

        String input = scanner.nextLine();

        Map<String, List<String>> adj = new HashMap<>();
        Set<String> allNodes = new TreeSet<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length != 2) continue;

            String u = parts[0].trim();
            String v = parts[1].trim();

            allNodes.add(u);
            allNodes.add(v);

            adj.putIfAbsent(u, new ArrayList<>());
            if (!adj.get(u).contains(v)) {
                adj.get(u).add(v);
            }
        }

        Map<String, Integer> state = new HashMap<>();
        for (String node : allNodes) {
            state.put(node, 0);
        }

        boolean hasCycle = false;

        for (String node : allNodes) {
            if (state.get(node) == 0) {
                if (dfs(node, adj, state)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        if (hasCycle) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }

    private static boolean dfs(String u, Map<String, List<String>> adj, Map<String, Integer> state) {
        state.put(u, 1);

        if (adj.containsKey(u)) {
            for (String v : adj.get(u)) {
                int vState = state.getOrDefault(v, 0);
                if (vState == 1) {
                    return true;
                }
                if (vState == 0) {
                    if (dfs(v, adj, state)) {
                        return true;
                    }
                }
            }
        }

        state.put(u, 2);
        return false;
    }
}