package by.it.group551001.artobolevskiy.lesson13;

import java.util.*;

public class GraphB {
    private static final Map<String, List<String>> adj = new HashMap<>();
    private static final Map<String, Integer> state = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String input = scanner.nextLine();

        for (String edge : input.split(",\\s*")) {
            if (edge.trim().isEmpty()) continue;
            String[] parts = edge.split("\\s*->\\s*");

            String u = parts[0].trim();
            String v = parts[1].trim();

            adj.putIfAbsent(u, new ArrayList<>());
            adj.putIfAbsent(v, new ArrayList<>());
            adj.get(u).add(v);

            state.put(u, 0);
            state.put(v, 0);
        }

        for (String vertex : state.keySet()) {
            if (state.get(vertex) == 0) {
                if (hasCycle(vertex)) {
                    System.out.println("yes");
                    return;
                }
            }
        }
        System.out.println("no");
    }

    private static boolean hasCycle(String u) {
        state.put(u, 1);
        for (String v : adj.get(u)) {
            if (state.get(v) == 1) return true;
            if (state.get(v) == 0 && hasCycle(v)) return true;
        }

        state.put(u, 2);
        return false;
    }
}
