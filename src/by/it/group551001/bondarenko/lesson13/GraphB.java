package by.it.group551001.bondarenko.lesson13;

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

        Map<String, List<String>> adjList = new HashMap<>();
        Set<String> allNodes = new HashSet<>();

        String[] edges = line.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length == 2) {
                String u = parts[0].trim();
                String v = parts[1].trim();
                allNodes.add(u);
                allNodes.add(v);
                adjList.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            }
        }

        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();
        boolean hasCycle = false;

        for (String node : allNodes) {
            if (!visited.contains(node)) {
                if (dfs(node, adjList, visited, recStack)) {
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

    private static boolean dfs(String node, Map<String, List<String>> adjList, Set<String> visited, Set<String> recStack) {
        visited.add(node);
        recStack.add(node);

        List<String> neighbors = adjList.get(node);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    if (dfs(neighbor, adjList, visited, recStack)) {
                        return true;
                    }
                } else if (recStack.contains(neighbor)) {
                    return true;
                }
            }
        }

        recStack.remove(node);
        return false;
    }
}