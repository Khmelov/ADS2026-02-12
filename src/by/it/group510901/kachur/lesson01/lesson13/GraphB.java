package by.it.group510901.kachur.lesson01.lesson13;

import java.util.*;

public class GraphB {

    // Цвета вершин
    private static final int WHITE = 0; // не посещена
    private static final int GRAY  = 1; // в текущем стеке DFS
    private static final int BLACK = 2; // полностью обработана

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите структуру орграфа:");
        String line = scanner.nextLine().trim();

        // 1. Строим граф: Map<вершина, список соседей>
        Map<Integer, List<Integer>> graph = new TreeMap<>();

        if (!line.isEmpty()) {
            for (String edge : line.split(",")) {
                String[] parts = edge.split("->");
                int from = Integer.parseInt(parts[0].trim());
                int to   = Integer.parseInt(parts[1].trim());

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                graph.computeIfAbsent(to,   k -> new ArrayList<>()); // вершина может быть только приёмником
            }
        }

        // 2. Запускаем DFS из каждой непосещённой вершины
        Map<Integer, Integer> color = new HashMap<>();
        for (Integer v : graph.keySet()) {
            color.put(v, WHITE);
        }

        boolean hasCycle = false;
        for (Integer v : graph.keySet()) {
            if (color.get(v) == WHITE) {
                if (dfs(v, graph, color)) {
                    hasCycle = true;
                    break; // нашли — дальше искать не нужно
                }
            }
        }

        // 3. Вывод
        System.out.println(hasCycle ? "yes" : "no");
    }


    private static boolean dfs(int u, Map<Integer, List<Integer>> graph,
                               Map<Integer, Integer> color) {
        color.put(u, GRAY); // вошли в вершину — она в стеке

        for (int v : graph.getOrDefault(u, Collections.emptyList())) {
            if (color.get(v) == GRAY) {
                // Ребро в серую вершину = back edge = цикл!
                return true;
            }
            if (color.get(v) == WHITE) {
                if (dfs(v, graph, color)) {
                    return true;
                }
            }
            // Если BLACK — это уже завершённая ветка, цикла через неё нет
        }

        color.put(u, BLACK); // вышли из вершины — больше не в стеке
        return false;
    }
}