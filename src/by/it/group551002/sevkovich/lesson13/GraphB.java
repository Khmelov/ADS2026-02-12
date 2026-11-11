package by.it.group551002.sevkovich.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String arg) {
        String[] edges = arg.split(", ");
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            String from = parts[0];
            String to = parts[1];
            allVertices.add(from);
            allVertices.add(to);
        }

        for (String v : allVertices) {
            graph.put(v, new ArrayList<>());
        }
        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            String from = parts[0];
            String to = parts[1];
            graph.get(from).add(to);
        }

        // DFS
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();

        for (String v : allVertices) {
            if (!visited.contains(v)) {
                if (hasCycle(v, graph, visited, recStack)) {
                    System.out.println("yes");
                    return;
                }
            }
        }

        System.out.println("no");
    }

    private static boolean hasCycle(String v, Map<String, List<String>> graph,
                                    Set<String> visited, Set<String> recStack) {
        visited.add(v);
        recStack.add(v);

        for (String neighbor : graph.get(v)) {
            if (!visited.contains(neighbor)) {
                if (hasCycle(neighbor, graph, visited, recStack)) {
                    return true;
                }
            } else if (recStack.contains(neighbor)) {
                return true;
            }
        }

        recStack.remove(v);
        return false;
    }
}