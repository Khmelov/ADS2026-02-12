package by.it.group551001.bochilo.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.hasNextLine() ? scanner.nextLine() : "";

        Map<String, TreeSet<String>> graph = new TreeMap<>();
        Map<String, TreeSet<String>> reversed = new TreeMap<>();
        for (String edge : line.split(",")) {
            String[] parts = edge.split("->");
            if (parts.length != 2) continue;
            String from = parts[0].trim();
            String to = parts[1].trim();
            graph.computeIfAbsent(from, k -> new TreeSet<>()).add(to);
            graph.computeIfAbsent(to, k -> new TreeSet<>());
            reversed.computeIfAbsent(to, k -> new TreeSet<>()).add(from);
            reversed.computeIfAbsent(from, k -> new TreeSet<>());
        }

        // алгоритм Косарайю: 1) порядок выхода в исходном графе
        Set<String> visited = new HashSet<>();
        Deque<String> order = new ArrayDeque<>();
        for (String v : graph.keySet()) {
            if (!visited.contains(v)) dfsOrder(v, graph, visited, order);
        }

        // 2) обход транспонированного графа в порядке убывания времени выхода
        //    даёт компоненты от истока к стоку
        visited.clear();
        StringBuilder out = new StringBuilder();
        while (!order.isEmpty()) {
            String v = order.pop();
            if (visited.contains(v)) continue;
            TreeSet<String> component = new TreeSet<>();
            dfsCollect(v, reversed, visited, component);
            for (String s : component) out.append(s);
            out.append('\n');
        }
        System.out.print(out);
    }

    private static void dfsOrder(String v, Map<String, TreeSet<String>> graph,
                                 Set<String> visited, Deque<String> order) {
        visited.add(v);
        for (String next : graph.get(v)) {
            if (!visited.contains(next)) dfsOrder(next, graph, visited, order);
        }
        order.push(v);
    }

    private static void dfsCollect(String v, Map<String, TreeSet<String>> graph,
                                   Set<String> visited, Set<String> component) {
        visited.add(v);
        component.add(v);
        for (String next : graph.get(v)) {
            if (!visited.contains(next)) dfsCollect(next, graph, visited, component);
        }
    }
}
