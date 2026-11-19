package by.it.group510901.jalilova.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            System.out.println("no");
            return;
        }
        String input = scanner.nextLine();

        // Мапы для хранения графа и степеней захода вершин
        Map<String, Set<String>> adjMap = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        // Парсинг входной строки,
        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            if (edge.isEmpty()) continue;

            String[] parts = edge.split("->");
            if (parts.length != 2) continue;

            String u = parts[0].trim();
            String v = parts[1].trim();

            // Инициализируем вершины в мапах
            adjMap.putIfAbsent(u, new HashSet<>());
            adjMap.putIfAbsent(v, new HashSet<>());
            inDegree.putIfAbsent(u, 0);
            inDegree.putIfAbsent(v, 0);

            // Добавляем ребро u -> v (избегая дубликатов)
            if (adjMap.get(u).add(v)) {
                inDegree.put(v, inDegree.get(v) + 1);
            }
        }

        // Очередь для вершин с нулевой степенью захода
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        // Счетчик посещенных вершин
        int visitedCount = 0;

        // Обход графа
        while (!queue.isEmpty()) {
            String u = queue.poll();
            visitedCount++;

            if (adjMap.containsKey(u)) {
                for (String v : adjMap.get(u)) {
                    inDegree.put(v, inDegree.get(v) - 1);
                    if (inDegree.get(v) == 0) {
                        queue.add(v);
                    }
                }
            }
        }

        // Если количество посещенных вершин меньше общего числа уникальных вершин — есть цикл
        if (visitedCount < inDegree.size()) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }
}

