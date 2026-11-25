package by.it.group551002.voroshko.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphC {

    static final Comparator<String> CMP = (a, b) -> {
        try {
            return Integer.compare(Integer.parseInt(a), Integer.parseInt(b));
        } catch (NumberFormatException e) {
            return a.compareTo(b);
        }
    };

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) return;

        Map<String, Set<String>> adj     = new HashMap<>(); // from -> {to}
        Map<String, Set<String>> adjRev  = new HashMap<>(); // to   -> {from}

        for (String part : line.split(",")) {
            String[] sides = part.split("->");
            if (sides.length != 2) continue;
            String from = sides[0].trim();
            String to   = sides[1].trim();

            adj.putIfAbsent(from, new HashSet<>());
            adj.putIfAbsent(to, new HashSet<>());
            adjRev.putIfAbsent(from, new HashSet<>());
            adjRev.putIfAbsent(to, new HashSet<>());

            adj.get(from).add(to);
            adjRev.get(to).add(from);
        }

        Set<String> visited = new HashSet<>();
        Deque<String> order = new ArrayDeque<>();

        List<String> allVertices = new ArrayList<>(adj.keySet());
        allVertices.sort(CMP);

        for (String v : allVertices) {
            if (!visited.contains(v)) {
                dfs1(v, adj, visited, order);
            }
        }

        visited.clear();
        List<List<String>> components = new ArrayList<>();

        while (!order.isEmpty()) {
            String v = order.pop();
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, adjRev, visited, comp);
                comp.sort(CMP);
                components.add(comp);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (List<String> comp : components) {
            for (String v : comp) sb.append(v);
            sb.append('\n');
        }
        System.out.print(sb);
    }

    private static void dfs1(String v, Map<String, Set<String>> adj,
                             Set<String> visited, Deque<String> order) {
        visited.add(v);
        for (String u : adj.getOrDefault(v, Collections.emptySet())) {
            if (!visited.contains(u)) dfs1(u, adj, visited, order);
        }
        order.push(v);
    }

    private static void dfs2(String v, Map<String, Set<String>> adjRev,
                             Set<String> visited, List<String> comp) {
        visited.add(v);
        comp.add(v);
        for (String u : adjRev.getOrDefault(v, Collections.emptySet())) {
            if (!visited.contains(u)) dfs2(u, adjRev, visited, comp);
        }
    }
}
