package by.it.group551001.telipko.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.hasNextLine() ? scanner.nextLine() : "";

        Map<String, Set<String>> graph = new TreeMap<>();
        Map<String, Set<String>> reversed = new TreeMap<>();

        for (String edge : line.split(",")) {
            edge = edge.trim();
            if (edge.isEmpty()) continue;
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();
            graph.computeIfAbsent(from, k -> new TreeSet<>()).add(to);
            graph.computeIfAbsent(to, k -> new TreeSet<>());
            reversed.computeIfAbsent(to, k -> new TreeSet<>()).add(from);
            reversed.computeIfAbsent(from, k -> new TreeSet<>());
        }

        Set<String> visited = new HashSet<>();
        Deque<String> order = new ArrayDeque<>();
        for (String v : graph.keySet()) {
            if (!visited.contains(v)) fillOrder(v, graph, visited, order);
        }

        visited.clear();
        Map<String, Integer> compOf = new HashMap<>();
        List<TreeSet<String>> components = new ArrayList<>();
        while (!order.isEmpty()) {
            String v = order.pop();
            if (!visited.contains(v)) {
                TreeSet<String> comp = new TreeSet<>();
                collect(v, reversed, visited, comp);
                for (String u : comp) compOf.put(u, components.size());
                components.add(comp);
            }
        }

        int n = components.size();
        String[] names = new String[n];
        for (int i = 0; i < n; i++) names[i] = String.join("", components.get(i));

        List<Set<Integer>> dag = new ArrayList<>();
        for (int i = 0; i < n; i++) dag.add(new HashSet<>());
        int[] inDegree = new int[n];
        for (Map.Entry<String, Set<String>> e : graph.entrySet()) {
            int a = compOf.get(e.getKey());
            for (String to : e.getValue()) {
                int b = compOf.get(to);
                if (a != b && dag.get(a).add(b)) inDegree[b]++;
            }
        }

        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparing(i -> names[i]));
        for (int i = 0; i < n; i++) if (inDegree[i] == 0) queue.add(i);

        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            int c = queue.poll();
            sb.append(names[c]).append('\n');
            for (int next : dag.get(c)) {
                if (--inDegree[next] == 0) queue.add(next);
            }
        }

        System.out.print(sb);
    }

    private static void fillOrder(String v, Map<String, Set<String>> graph, Set<String> visited, Deque<String> order) {
        visited.add(v);
        for (String next : graph.get(v)) {
            if (!visited.contains(next)) fillOrder(next, graph, visited, order);
        }
        order.push(v);
    }

    private static void collect(String v, Map<String, Set<String>> reversed, Set<String> visited, Set<String> comp) {
        visited.add(v);
        comp.add(v);
        for (String next : reversed.get(v)) {
            if (!visited.contains(next)) collect(next, reversed, visited, comp);
        }
    }
}
