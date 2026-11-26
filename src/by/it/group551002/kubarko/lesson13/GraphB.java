package by.it.group551002.kubarko.lesson13;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder input = new StringBuilder();
        while (scanner.hasNextLine()) {
            input.append(input.length() == 0 ? "" : ", ").append(scanner.nextLine());
        }

        Map<String, TreeSet<String>> graph = new TreeMap<>();
        for (String edge : input.toString().split(",")) {
            String trimmed = edge.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            String[] vertices = trimmed.split("\\s*->\\s*");
            if (vertices.length != 2) {
                throw new IllegalArgumentException("Invalid edge: " + trimmed);
            }
            graph.computeIfAbsent(vertices[0].trim(), ignored -> new TreeSet<>())
                    .add(vertices[1].trim());
            graph.computeIfAbsent(vertices[1].trim(), ignored -> new TreeSet<>());
        }

        Map<String, Integer> state = new TreeMap<>();
        boolean cycle = false;
        for (String vertex : graph.keySet()) {
            if (!state.containsKey(vertex) && hasCycle(vertex, graph, state)) {
                cycle = true;
                break;
            }
        }
        System.out.println(cycle ? "yes" : "no");
    }

    private static boolean hasCycle(String vertex, Map<String, TreeSet<String>> graph,
                                    Map<String, Integer> state) {
        state.put(vertex, 1);
        for (String next : graph.get(vertex)) {
            Integer nextState = state.get(next);
            if (nextState != null && nextState == 1) {
                return true;
            }
            if (nextState == null && hasCycle(next, graph, state)) {
                return true;
            }
        }
        state.put(vertex, 2);
        return false;
    }
}
