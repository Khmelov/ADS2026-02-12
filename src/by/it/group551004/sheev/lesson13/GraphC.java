package by.it.group551004.sheev.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class GraphC {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();

        Set<String> vertices = new HashSet<>();
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> revGraph = new HashMap<>();

        if (line != null && !line.trim().isEmpty()) {
            String[] edges = line.split(",");
            for (String edge : edges) {
                String[] parts = edge.trim().split("->");
                String from = parts[0].trim();
                String to = parts[1].trim();
                vertices.add(from);
                vertices.add(to);
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                revGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from); // Транспонированный
            }
        }

        // 1. DFS для порядка завершения
        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();
        for (String v : vertices) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, order);
            }
        }
        Collections.reverse(order);

        // 2. DFS на транспонированном графе для поиска компонент
        visited.clear();
        List<List<String>> components = new ArrayList<>();
        for (String v : order) {
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, revGraph, visited, comp);
                Collections.sort(comp); // Лексикографический порядок внутри компоненты
                components.add(comp);
            }
        }

        // 3. Строим граф конденсата (компоненты как вершины)
        int n = components.size();
        Map<String, Integer> compIndex = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (String v : components.get(i)) {
                compIndex.put(v, i);
            }
        }

        List<Set<Integer>> condGraph = new ArrayList<>();
        int[] indegree = new int[n];
        for (int i = 0; i < n; i++) {
            condGraph.add(new HashSet<>());
        }

        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                int c1 = compIndex.get(from);
                int c2 = compIndex.get(to);
                if (c1 != c2 && !condGraph.get(c1).contains(c2)) {
                    condGraph.get(c1).add(c2);
                    indegree[c2]++;
                }
            }
        }

        // 4. Топологическая сортировка конденсата
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> {
            // Сравниваем первые элементы компонент для лексикографического порядка
            return components.get(a).get(0).compareTo(components.get(b).get(0));
        });

        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) pq.add(i);
        }

        // 5. Вывод
        StringBuilder sb = new StringBuilder();
        while (!pq.isEmpty()) {
            int c = pq.poll();
            for (String v : components.get(c)) {
                sb.append(v);
            }
            sb.append("\n");
            for (int neighbor : condGraph.get(c)) {
                indegree[neighbor]--;
                if (indegree[neighbor] == 0) {
                    pq.add(neighbor);
                }
            }
        }
        System.out.print(sb.toString());
    }

    private static void dfs1(String v, Map<String, List<String>> graph, Set<String> visited, List<String> order) {
        visited.add(v);
        List<String> neighbors = graph.get(v);
        if (neighbors != null) {
            for (String u : neighbors) {
                if (!visited.contains(u)) {
                    dfs1(u, graph, visited, order);
                }
            }
        }
        order.add(v);
    }

    private static void dfs2(String v, Map<String, List<String>> revGraph, Set<String> visited, List<String> comp) {
        visited.add(v);
        comp.add(v);
        List<String> neighbors = revGraph.get(v);
        if (neighbors != null) {
            for (String u : neighbors) {
                if (!visited.contains(u)) {
                    dfs2(u, revGraph, visited, comp);
                }
            }
        }
    }
}