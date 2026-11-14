package by.it.group551002.yermakou.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }

        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return;
        }

        // Структуры для графа
        Map<String, List<String>> adj = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        // Разбор введенной строки по запятым
        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length != 2) continue;

            String u = parts[0].trim();
            String v = parts[1].trim();

            vertices.add(u);
            vertices.add(v);

            adj.putIfAbsent(u, new ArrayList<>());
            adj.get(u).add(v);
        }

        // Вычисление входящих степеней вершин (in-degree)
        Map<String, Integer> inDegree = new HashMap<>();
        for (String v : vertices) {
            inDegree.put(v, 0);
        }

        for (Map.Entry<String, List<String>> entry : adj.entrySet()) {
            for (String neighbor : entry.getValue()) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        // PriorityQueue автоматически обеспечивает лексикографический порядок (по алфавиту)
        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String v : vertices) {
            if (inDegree.get(v) == 0) {
                pq.add(v);
            }
        }

        // Топологическая сортировка
        List<String> topologicalOrder = new ArrayList<>();
        while (!pq.isEmpty()) {
            String u = pq.poll();
            topologicalOrder.add(u);

            if (adj.containsKey(u)) {
                for (String neighbor : adj.get(u)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) {
                        pq.add(neighbor);
                    }
                }
            }
        }

        // Вывод результата через пробел
        System.out.println(String.join(" ", topologicalOrder));
    }
}