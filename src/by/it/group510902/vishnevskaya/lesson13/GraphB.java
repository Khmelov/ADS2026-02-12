package by.it.group510902.vishnevskaya.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Проверяем, есть ли входная строка
        if (!scanner.hasNextLine()) {
            return;
        }

        // Считываем строку и убираем лишние пробелы
        String input = scanner.nextLine().trim();

        // Если строка пустая, выводим "no" и завершаем программу
        if (input.isEmpty()) {
            System.out.println("no");
            return;
        }

        // Граф: каждой вершине соответствуют вершины,
        // в которые из неё идут стрелки
        Map<String, Set<String>> graph = new HashMap<>();

        // Храним количество входящих стрелок для каждой вершины
        Map<String, Integer> indegree = new HashMap<>();

        // Разделяем входную строку на отдельные связи по запятой
        String[] edges = input.split("\\s*,\\s*");

        // Перебираем все связи
        for (String edge : edges) {

            // Пропускаем пустые элементы
            if (edge.isEmpty()) {
                continue;
            }

            // Разделяем связь на начальную и конечную вершины
            String[] parts = edge.split("\\s*->\\s*");

            // Если связь записана неправильно, пропускаем её
            if (parts.length != 2) {
                continue;
            }

            // Получаем названия вершин
            String from = parts[0].trim();
            String to = parts[1].trim();

            // Добавляем вершины в граф, если их ещё нет
            graph.computeIfAbsent(from, k -> new TreeSet<>());
            graph.computeIfAbsent(to, k -> new TreeSet<>());

            // Изначально у каждой новой вершины 0 входящих стрелок
            indegree.putIfAbsent(from, 0);
            indegree.putIfAbsent(to, 0);

            // Добавляем связь, если её ещё не было
            // Увеличиваем количество входящих стрелок конечной вершины
            if (graph.get(from).add(to)) {
                indegree.put(to, indegree.get(to) + 1);
            }
        }

        // Создаём очередь для вершин с нулём входящих стрелок
        Deque<String> queue = new ArrayDeque<>();

        // Проверяем количество входящих стрелок у каждой вершины
        for (Map.Entry<String, Integer> entry : indegree.entrySet()) {

            // Если входящих стрелок нет, добавляем вершину в очередь
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        // Счётчик обработанных вершин
        int processed = 0;

        // Пока очередь не пуста, продолжаем обработку
        while (!queue.isEmpty()) {

            // Достаём вершину из начала очереди
            String current = queue.poll();

            // Увеличиваем количество обработанных вершин
            processed++;

            // Перебираем все вершины, в которые ведут стрелки из текущей
            for (String next : graph.getOrDefault(current, Collections.emptySet())) {

                // Уменьшаем количество входящих стрелок на 1
                indegree.put(next, indegree.get(next) - 1);

                // Если входящих стрелок больше нет, добавляем вершину в очередь
                if (indegree.get(next) == 0) {
                    queue.add(next);
                }
            }
        }

        // Если обработали все вершины, цикла нет — выводим "no"
        // Если обработали не все вершины, значит, цикл есть — выводим "yes"
        System.out.println(processed == indegree.size() ? "no" : "yes");
    }
}