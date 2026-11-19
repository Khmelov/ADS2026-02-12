package by.it.group510901.jalilova.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        String input = scanner.nextLine();

        // Мапы для хранения графа и степеней захода вершин
        Map<String, Set<String>> adjMap = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        // Парсинг входной строки
        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            if (edge.isEmpty()) continue;

            String[] parts = edge.split("->");
            if (parts.length != 2) continue;

            String u = parts[0].trim();
            String v = parts[1].trim();

            // Инициализируем вершины в мапах, если их еще нет
            adjMap.putIfAbsent(u, new HashSet<>());
            adjMap.putIfAbsent(v, new HashSet<>());
            inDegree.putIfAbsent(u, 0);
            inDegree.putIfAbsent(v, 0);

            // Добавляем ребро u -> v (избегая дубликатов для корректного подсчета степеней)
            if (adjMap.get(u).add(v)) {
                inDegree.put(v, inDegree.get(v) + 1);
            }
        }

        // Очередь с приоритетом обеспечивает лексикографический порядок среди равнозначных вершин
        PriorityQueue<String> pq = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                pq.add(entry.getKey());
            }
        }

        List<String> result = new ArrayList<>();

        // Алгоритм Кана
        while (!pq.isEmpty()) {
            String u = pq.poll();
            result.add(u);

            if (adjMap.containsKey(u)) {
                for (String v : adjMap.get(u)) {
                    inDegree.put(v, inDegree.get(v) - 1);
                    if (inDegree.get(v) == 0) {
                        pq.add(v);
                    }
                }
            }
        }

        // Вывод результата: если обработаны не все вершины, значит в графе есть цикл
        if (result.size() != inDegree.size()) {
            System.out.println("Граф содержит цикл, топологическая сортировка невозможна.");
        } else {
            System.out.println(String.join(" ", result));
        }
    }
}

