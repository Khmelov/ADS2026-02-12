package by.it.group510901.poltorak.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            System.out.println("no");
            return;
        }

        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            System.out.println("no");
            return;
        }

        Map<String, List<String>> adj = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        String[] edges = line.split(",");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                vertices.add(from);
                vertices.add(to);

                adj.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            }
        }

        Map<String, Integer> state = new HashMap<>();
        for (String v : vertices) {
            state.put(v, 0);
        }

        boolean hasCycle = false;
        for (String v : vertices) {
            if (state.get(v) == 0) {
                if (dfsHasCycle(v, adj, state)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfsHasCycle(String current, Map<String, List<String>> adj, Map<String, Integer> state) {
        state.put(current, 1);

        List<String> neighbors = adj.get(current);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                int neighborState = state.getOrDefault(neighbor, 0);
                if (neighborState == 1) {
                    return true;
                }
                if (neighborState == 0 && dfsHasCycle(neighbor, adj, state)) {
                    return true;
                }
            }
        }

        state.put(current, 2);
        return false;
    }
}