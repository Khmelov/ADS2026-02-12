package by.it.group551002.yorsh.lesson13;
import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String line = scanner.nextLine();

        Map<String, List<String>> adj = new HashMap<>();
        Set<String> nodes = new TreeSet<>();

        String[] edges = line.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            if (edge.isEmpty()) continue;
            String[] parts = edge.split("->");
            String u = parts[0].trim();
            String v = parts[1].trim();

            nodes.add(u);
            nodes.add(v);

            adj.putIfAbsent(u, new ArrayList<>());
            adj.get(u).add(v);
        }

        // Состояния DFS: 0 - не посещен, 1 - в процессе посещения (в стеке рекурсии), 2 - полностью обработан
        Map<String, Integer> state = new HashMap<>();
        for (String node : nodes) {
            state.put(node, 0);
        }

        boolean hasCycle = false;
        for (String node : nodes) {
            if (state.get(node) == 0) {
                if (dfs(node, adj, state)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfs(String u, Map<String, List<String>> adj, Map<String, Integer> state) {
        state.put(u, 1);
        if (adj.containsKey(u)) {
            for (String v : adj.get(u)) {
                if (state.get(v) == 1) return true;
                if (state.get(v) == 0) {
                    if (dfs(v, adj, state)) return true;
                }
            }
        }
        state.put(u, 2);
        return false;
    }
}