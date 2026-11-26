package by.it.group551003.popko.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphB {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) {
            System.out.println("no");
            return;
        }

        Map<String, List<String>> adj = new HashMap<>();
        Set<String> vertices = new TreeSet<>();

        String[] edges = line.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length != 2) continue;
            String u = parts[0].trim();
            String v = parts[1].trim();

            vertices.add(u);
            vertices.add(v);

            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
        }

        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();
        boolean hasCycle = false;

        List<String> sortedVertices = new ArrayList<>(vertices);
        for (String u : sortedVertices) {
            if (!visited.contains(u)) {
                if (dfs(u, adj, visited, recStack)) {
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

    private static boolean dfs(String u, Map<String, List<String>> adj, Set<String> visited, Set<String> recStack) {
        visited.add(u);
        recStack.add(u);

        if (adj.containsKey(u)) {
            List<String> neighbors = new ArrayList<>(adj.get(u));
            Collections.sort(neighbors);
            for (String v : neighbors) {
                if (!visited.contains(v)) {
                    if (dfs(v, adj, visited, recStack)) {
                        return true;
                    }
                } else if (recStack.contains(v)) {
                    return true;
                }
            }
        }

        recStack.remove(u);
        return false;
    }
}