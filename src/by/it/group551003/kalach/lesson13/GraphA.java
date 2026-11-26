package by.it.group551003.kalach.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;

public class GraphA {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        // Вершины хранятся строками, TreeSet сам упорядочивает их лексикографически.
        Map<String, Set<String>> next = new HashMap<>();   // вершина -> её потомки
        Map<String, Integer> inDegree = new HashMap<>();   // вершина -> число входящих дуг

        for (String part : line.split(",")) {
            part = part.trim();
            if (part.isEmpty()) {
                continue;
            }
            String[] ends = part.split("->");
            if (ends.length != 2) {
                System.out.println("Неверная дуга: " + part);
                return;
            }
            String from = ends[0].trim();
            String to = ends[1].trim();

            next.computeIfAbsent(from, k -> new HashSet<>());
            next.computeIfAbsent(to, k -> new HashSet<>());
            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);

            // повторяющаяся дуга считается один раз
            if (next.get(from).add(to)) {
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        // Алгоритм Кана: из готовых вершин всегда берём лексикографически наименьшую.
        TreeSet<String> ready = new TreeSet<>();
        for (Map.Entry<String, Integer> e : inDegree.entrySet()) {
            if (e.getValue() == 0) {
                ready.add(e.getKey());
            }
        }

        StringJoiner result = new StringJoiner(" ");
        int printed = 0;
        while (!ready.isEmpty()) {
            String v = ready.pollFirst();
            result.add(v);
            printed++;
            for (String u : next.get(v)) {
                int d = inDegree.get(u) - 1;
                inDegree.put(u, d);
                if (d == 0) {
                    ready.add(u);
                }
            }
        }

        if (printed != inDegree.size()) {
            System.out.println("Топологическая сортировка невозможна: в графе есть цикл");
        } else {
            System.out.println(result);
        }
    }
}
