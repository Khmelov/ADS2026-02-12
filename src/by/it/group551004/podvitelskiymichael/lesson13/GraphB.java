package by.it.group551004.podvitelskiymichael.lesson13;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                if (!sb.isEmpty()) {
                    sb.append(", ");
                }
                sb.append(line);
            }
        }
        String input = sb.toString();
        if (input.isEmpty()) {
            System.out.println("no");
            return;
        }

        Map<String, Set<String>> adj = new HashMap<>();
        for (String edge : input.split(",")) {
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                adj.putIfAbsent(from, new HashSet<>());
                adj.putIfAbsent(to, new HashSet<>());
                adj.get(from).add(to);
            }
        }

        // 0 - не посещена, 1 - в обработке, 2 - обработана
        Map<String, Integer> state = new HashMap<>();
        for (String node : adj.keySet()) {
            state.put(node, 0);
        }

        boolean hasCycle = false;
        for (String node : adj.keySet()) {
            if (state.get(node) == 0 && dfs(node, adj, state)) {
                hasCycle = true;
                break;
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfs(String u, Map<String, Set<String>> adj, Map<String, Integer> state) {
        state.put(u, 1);
        for (String v : adj.getOrDefault(u, Collections.emptySet())) {
            int s = state.getOrDefault(v, 0);
            if (s == 1) {
                return true;
            }
            if (s == 0 && dfs(v, adj, state)) {
                return true;
            }
        }
        state.put(u, 2);
        return false;
    }
}