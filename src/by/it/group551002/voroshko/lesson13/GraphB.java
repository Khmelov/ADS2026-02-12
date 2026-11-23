package by.it.group551002.voroshko.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphB {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) return;

        Map<String, Set<String>> adj = new HashMap<>();

        for (String part : line.split(",")) {
            String[] sides = part.split("->");
            if (sides.length != 2) continue;
            String from = sides[0].trim();
            String to   = sides[1].trim();

            adj.putIfAbsent(from, new HashSet<>());
            adj.putIfAbsent(to, new HashSet<>());
            adj.get(from).add(to);
        }

        Map<String, Integer> color = new HashMap<>();
        for (String v : adj.keySet()) color.put(v, 0);

        boolean hasCycle = false;
        for (String start : adj.keySet()) {
            if (color.get(start) == 0 && dfs(start, adj, color)) {
                hasCycle = true;
                break;
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfs(String v, Map<String, Set<String>> adj, Map<String, Integer> color) {
        color.put(v, 1);
        for (String u : adj.getOrDefault(v, Collections.emptySet())) {
            int c = color.getOrDefault(u, 0);
            if (c == 1) return true;
            if (c == 0 && dfs(u, adj, color)) return true;
        }
        color.put(v, 2);
        return false;
    }
}