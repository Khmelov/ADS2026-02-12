package by.it.group510902.makaruk.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) return; // если ввода нет — выходим
        String line = sc.nextLine();

        // 2. СТРУКТУРЫ ДЛЯ ГРАФА
        // adj — списки смежности: откуда -> [куда1, куда2...]
        Map<String, List<String>> adj = new HashMap<>();
        // vertices — множество всех вершин (уникальные)
        Set<String> vertices = new HashSet<>();

        // разбор текста в граф
        if (line != null && !line.trim().isEmpty()) {
            for (String part : line.split(",")) {
                String[] sides = part.split("->");
                if (sides.length != 2) continue;

                String from = sides[0].trim();
                String to = sides[1].trim();

                // Добавляем ребро from -> to
                adj.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                vertices.add(from);
                vertices.add(to);
            }
        }


        // Если цикл есть — печатаем "yes", иначе "no"
        System.out.println(hasCycle(adj, vertices) ? "yes" : "no");
    }


    // Цвета: 0 = белый (не посещена), 1 = серый (в текущем пути), 2 = чёрный (обработана)
    private static boolean hasCycle(Map<String, List<String>> adj, Set<String> vertices) {
        // Все вершины изначально белые
        Map<String, Integer> color = new HashMap<>();
        for (String v : vertices) color.put(v, 0);

        // Запускаем DFS из каждой непосещённой вершины (граф может быть несвязным)
        for (String v : vertices) {
            if (color.get(v) == 0 && dfs(v, adj, color)) {
                return true; // нашли цикл
            }
        }
        return false;
    }

    // Рекурсивный обход в глубину
    private static boolean dfs(String v, Map<String, List<String>> adj, Map<String, Integer> color) {
        color.put(v, 1); // зашли в вершину — красим в серый

        // Смотрим на всех соседей (куда идут стрелки из v)
        for (String to : adj.getOrDefault(v, Collections.emptyList())) {
            int c = color.get(to); // смотрим цвет соседа

            // Сосед серый -> вернулись туда, откуда пришли -> ЦИКЛ!
            if (c == 1) return true;

            // Сосед белый -> идём вглубь
            if (c == 0 && dfs(to, adj, color)) return true;

            // Сосед чёрный -> уже проверен ранее, пропускаем
        }

        color.put(v, 2); // все соседи проверены — красим в чёрный
        return false;    // цикла через эту вершину нет
    }
}