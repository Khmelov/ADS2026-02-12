package by.it.group551001.bochilo.lesson13;

import java.util.*;

public class GraphB {

    private static final int WHITE = 0, GRAY = 1, BLACK = 2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.hasNextLine() ? scanner.nextLine() : "";

        Map<String, List<String>> graph = new TreeMap<>();
        for (String edge : line.split(",")) {
            String[] parts = edge.split("->");
            if (parts.length != 2) continue;
            String from = parts[0].trim();
            String to = parts[1].trim();
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>());
        }

        Map<String, Integer> color = new HashMap<>();
        boolean cycle = false;
        for (String v : graph.keySet()) {
            if (color.getOrDefault(v, WHITE) == WHITE && hasCycle(v, graph, color)) {
                cycle = true;
                break;
            }
        }
        System.out.println(cycle ? "yes" : "no");
    }

    private static boolean hasCycle(String v, Map<String, List<String>> graph, Map<String, Integer> color) {
        color.put(v, GRAY);
        for (String next : graph.get(v)) {
            int c = color.getOrDefault(next, WHITE);
            if (c == GRAY) return true;
            if (c == WHITE && hasCycle(next, graph, color)) return true;
        }
        color.put(v, BLACK);
        return false;
    }
}
