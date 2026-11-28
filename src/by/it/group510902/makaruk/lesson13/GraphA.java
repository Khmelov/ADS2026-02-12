package by.it.group510902.makaruk.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        // Читаем строку графа из консоли
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine();

        // Граф: списки смежности и множество всех вершин
        Map<String, List<String>> adj = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        // Парсим строку "0 -> 2, 1 -> 3"
        if (line != null && !line.trim().isEmpty()) {
            for (String part : line.split(",")) {
                String[] sides = part.split("->");
                if (sides.length != 2) continue;

                String from = sides[0].trim();
                String to = sides[1].trim();

                adj.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                vertices.add(from);
                vertices.add(to);
            }
        }

        // Считаем входящие степени (сколько стрелок входит в вершину)
        Map<String, Integer> inDegree = new HashMap<>();
        for (String v : vertices) inDegree.put(v, 0);
        for (List<String> neighbors : adj.values()) {
            for (String to : neighbors) {
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        // Приоритетная очередь для лексикографического порядка
        PriorityQueue<String> pq = new PriorityQueue<>();

        // Кладем в очередь вершины-истоки (степень = 0)
        for (String v : vertices) {
            if (inDegree.get(v) == 0) pq.offer(v);
        }

        // Алгоритм Кана: топологическая сортировка
        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            String v = pq.poll(); // Берем вершину с наименьшим приоритетом
            result.add(v);

            // Убираем вершину -> уменьшаем входящие степени соседей
            for (String to : adj.getOrDefault(v, Collections.emptyList())) {
                inDegree.put(to, inDegree.get(to) - 1);

                // Если у соседа нет больше зависимостей - в очередь
                if (inDegree.get(to) == 0) {
                    pq.offer(to);
                }
            }
        }

        // Вывод через пробел
        System.out.print(String.join(" ", result));
    }
}