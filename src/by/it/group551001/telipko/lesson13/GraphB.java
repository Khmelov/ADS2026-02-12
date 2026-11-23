package by.it.group551001.telipko.lesson13;

import java.util.*;

public class GraphB {

    private static final int WHITE = 0, GRAY = 1, BLACK = 2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.hasNextLine() ? scanner.nextLine() : "";

        Map<String, List<String>> graph = new TreeMap<>();
        for (String edge : line.split(",")) {
            edge = edge.trim();
            if (edge.isEmpty()) continue;
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>());
        }

        Map<String, Integer> color = new HashMap<>();
        for (String v : graph.keySet()) color.put(v, WHITE);

        boolean hasCycle = false;
        for (String v : graph.keySet()) {
            if (color.get(v) == WHITE && dfs(v, graph, color)) {
                hasCycle = true;
                break;
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfs(String v, Map<String, List<String>> graph, Map<String, Integer> color) {
        color.put(v, GRAY);
        for (String next : graph.get(v)) {
            int c = color.get(next);
            if (c == GRAY) return true;
            if (c == WHITE && dfs(next, graph, color)) return true;
        }
        color.put(v, BLACK);
        return false;
    }
}
