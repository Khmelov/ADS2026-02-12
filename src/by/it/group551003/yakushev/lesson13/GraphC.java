package by.it.group551003.yakushev.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim();

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, List<String>> radj = new HashMap<>(); // Транспонированный граф
        Set<String> vertices = new TreeSet<>();

        if (!line.isEmpty()) {
            String[] edges = line.split(",");
            for (String edge : edges) {
                String[] parts = edge.trim().split("->");
                String from = parts[0].trim();
                String to = parts[1].trim();

                adj.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                radj.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
                vertices.add(from);
                vertices.add(to);
            }
        }

        // 1-й проход DFS: порядок завершения
        Set<String> visited = new HashSet<>();
        Deque<String> order = new ArrayDeque<>();
        for (String v : vertices) {
            if (!visited.contains(v)) dfs1(v, adj, visited, order);
        }

        // 2-й проход DFS по транспонированному графу
        visited.clear();
        List<List<String>> sccs = new ArrayList<>();
        while (!order.isEmpty()) {
            String v = order.pop();
            if (!visited.contains(v)) {
                List<String> component = new ArrayList<>();
                dfs2(v, radj, visited, component);
                Collections.sort(component); // Лексикографическая сортировка внутри компонента
                sccs.add(component);
            }
        }

        // Вывод: каждый компонент с новой строки, без пробелов
        for (List<String> comp : sccs) {
            StringBuilder sb = new StringBuilder();
            for (String v : comp) sb.append(v);
            System.out.println(sb.toString());
        }
    }

    private static void dfs1(String v, Map<String, List<String>> adj,
                             Set<String> visited, Deque<String> order) {
        visited.add(v);
        for (String u : adj.getOrDefault(v, Collections.emptyList())) {
            if (!visited.contains(u)) dfs1(u, adj, visited, order);
        }
        order.push(v);
    }

    private static void dfs2(String v, Map<String, List<String>> radj,
                             Set<String> visited, List<String> component) {
        visited.add(v);
        component.add(v);
        for (String u : radj.getOrDefault(v, Collections.emptyList())) {
            if (!visited.contains(u)) dfs2(u, radj, visited, component);
        }
    }
}