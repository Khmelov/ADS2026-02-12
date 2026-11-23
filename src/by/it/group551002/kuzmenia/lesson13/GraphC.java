package by.it.group551002.kuzmenia.lesson13;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Весь граф вводится одной строкой
        String line = scanner.hasNextLine() ? scanner.nextLine() : "";

        // Граф и развернутый граф (все ребра наоборот)
        Map<String, TreeSet<String>> edges = new TreeMap<>();
        Map<String, TreeSet<String>> reversed = new TreeMap<>();

        // Читаем ребра вида "A->B", разделитель - запятая
        for (String part : line.split(",")) {
            part = part.trim();
            if (part.isEmpty()) {
                continue;
            }
            String[] ends = part.split("->");
            String from = ends[0].trim();
            registerVertex(from, edges, reversed);

            if (ends.length > 1) {
                String to = ends[1].trim();
                registerVertex(to, edges, reversed);
                edges.get(from).add(to);
                reversed.get(to).add(from);
            }
        }

        // Первый проход DFS: порядок выхода из вершин
        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();
        for (String v : edges.keySet()) {
            if (!visited.contains(v)) {
                dfsOrder(v, edges, visited, order);
            }
        }

        // Второй проход DFS уже по развернутому графу, с конца order
        Map<String, Integer> componentOf = new HashMap<>();
        List<TreeSet<String>> components = new ArrayList<>();
        for (int i = order.size() - 1; i >= 0; i--) {
            String v = order.get(i);
            if (!componentOf.containsKey(v)) {
                TreeSet<String> component = new TreeSet<>();
                dfsCollect(v, reversed, componentOf, components.size(), component);
                components.add(component);
            }
        }

        // Конденсация: компоненты становятся вершинами
        int count = components.size();
        List<Set<Integer>> condEdges = new ArrayList<>();
        int[] inDegree = new int[count];
        for (int i = 0; i < count; i++) {
            condEdges.add(new HashSet<>());
        }
        for (Map.Entry<String, TreeSet<String>> e : edges.entrySet()) {
            int a = componentOf.get(e.getKey());
            for (String w : e.getValue()) {
                int b = componentOf.get(w);
                if (a != b && condEdges.get(a).add(b)) {
                    inDegree[b]++;
                }
            }
        }

        // Готовые компоненты - без входящих ребер, порядок по первой вершине
        TreeSet<Integer> ready = new TreeSet<>(
                Comparator.comparing((Integer i) -> components.get(i).first()));
        for (int i = 0; i < count; i++) {
            if (inDegree[i] == 0) {
                ready.add(i);
            }
        }

        // Постройка строки результата
        StringBuilder result = new StringBuilder();
        while (!ready.isEmpty()) {
            int c = ready.pollFirst();
            // Вершины компоненты идут подряд, без пробелов
            for (String v : components.get(c)) {
                result.append(v);
            }
            result.append('\n');

            for (int next : condEdges.get(c)) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    ready.add(next);
                }
            }
        }

        System.out.print(result);
    }

    // Метод для создания пустого списка ребер у новой вершины
    private static void registerVertex(String v,
                                       Map<String, TreeSet<String>> edges,
                                       Map<String, TreeSet<String>> reversed) {
        if (!edges.containsKey(v)) {
            edges.put(v, new TreeSet<>());
            reversed.put(v, new TreeSet<>());
        }
    }

    // Метод для обхода графа, вершина добавляется после всех потомков
    private static void dfsOrder(String v,
                                 Map<String, TreeSet<String>> edges,
                                 Set<String> visited,
                                 List<String> order) {
        visited.add(v);
        for (String next : edges.get(v)) {
            if (!visited.contains(next)) {
                dfsOrder(next, edges, visited, order);
            }
        }
        order.add(v);
    }

    // Метод для сбора одной компоненты сильной связности
    private static void dfsCollect(String v,
                                   Map<String, TreeSet<String>> edges,
                                   Map<String, Integer> componentOf,
                                   int id,
                                   TreeSet<String> component) {
        componentOf.put(v, id);
        component.add(v);
        for (String next : edges.get(v)) {
            if (!componentOf.containsKey(next)) {
                dfsCollect(next, edges, componentOf, id, component);
            }
        }
    }
}
