package by.it.group551003.klimuk.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }

        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("no");
            return;
        }

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                vertices.add(from);
                vertices.add(to);
            }
        }

        Map<String, Integer> state = new HashMap<>();
        boolean hasCycle = false;

        for (String vertex : vertices) {
            if (state.getOrDefault(vertex, 0) == 0) {
                if (dfs(vertex, graph, state)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfs(String current, Map<String, List<String>> graph, Map<String, Integer> state) {
        state.put(current, 1);

        List<String> neighbors = graph.get(current);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                int neighborState = state.getOrDefault(neighbor, 0);

                if (neighborState == 1) {
                    return true;
                }

                if (neighborState == 0) {
                    if (dfs(neighbor, graph, state)) {
                        return true;
                    }
                }
            }
        }

        state.put(current, 2);
        return false;
    }
}