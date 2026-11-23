package by.it.group551001.Barusevich.lesson13;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphB {
    private static final int WHITE = 0;
    private static final int GRAY = 1;
    private static final int BLACK = 2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        Map<String, TreeSet<String>> graph = new TreeMap<>();
        if (!input.isEmpty()) {
            for (String edge : input.split(",")) {
                String[] vertices = edge.trim().split("\\s*->\\s*");
                if (vertices.length != 2) continue;
                String from = vertices[0].trim();
                String to = vertices[1].trim();
                graph.computeIfAbsent(from, key -> new TreeSet<>()).add(to);
                graph.computeIfAbsent(to, key -> new TreeSet<>());
            }
        }
        Map<String, Integer> colors = new TreeMap<>();
        for (String vertex : graph.keySet()) colors.put(vertex, WHITE);
        for (String vertex : graph.keySet()) {
            if (colors.get(vertex) == WHITE && hasCycle(vertex, graph, colors)) {
                System.out.println("yes");
                return;
            }
        }
        System.out.println("no");
    }

    private static boolean hasCycle(String vertex, Map<String, TreeSet<String>> graph, Map<String, Integer> colors) {
        colors.put(vertex, GRAY);
        for (String next : graph.get(vertex)) {
            int color = colors.get(next);
            if (color == GRAY) return true;
            if (color == WHITE && hasCycle(next, graph, colors)) return true;
        }
        colors.put(vertex, BLACK);
        return false;
    }
}
