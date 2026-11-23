package by.it.group551003.yakushev.lesson13;

import java.util.*;

public class GraphB {
    private static final int WHITE = 0, GRAY = 1, BLACK = 2;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim();

        Map<String, List<String>> adj = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        if (!line.isEmpty()) {
            String[] edges = line.split(",");
            for (String edge : edges) {
                String[] parts = edge.trim().split("->");
                String from = parts[0].trim();
                String to = parts[1].trim();
                adj.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                vertices.add(from);
                vertices.add(to);
            }
        }

        Map<String, Integer> color = new HashMap<>();
        for (String v : vertices) color.put(v, WHITE);

        boolean hasCycle = false;
        for (String v : vertices) {
            if (color.get(v) == WHITE) {
                if (hasCycleDfs(v, adj, color)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycleDfs(String v, Map<String, List<String>> adj,
                                       Map<String, Integer> color) {
        color.put(v, GRAY);
        for (String u : adj.getOrDefault(v, Collections.emptyList())) {
            if (color.get(u) == GRAY) return true;
            if (color.get(u) == WHITE && hasCycleDfs(u, adj, color)) return true;
        }
        color.put(v, BLACK);
        return false;
    }
}