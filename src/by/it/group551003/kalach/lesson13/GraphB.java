package by.it.group551003.kalach.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphB {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) {
            return;
        }

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

        // Цикла нет, если вершины можно последовательно удалить (алгоритм Кана):
        // удаляем вершины без входящих дуг; вершины цикла так удалить не получится.
        Deque<String> ready = new ArrayDeque<>();
        for (Map.Entry<String, Integer> e : inDegree.entrySet()) {
            if (e.getValue() == 0) {
                ready.add(e.getKey());
            }
        }

        int removed = 0;
        while (!ready.isEmpty()) {
            String v = ready.poll();
            removed++;
            for (String u : next.get(v)) {
                int d = inDegree.get(u) - 1;
                inDegree.put(u, d);
                if (d == 0) {
                    ready.add(u);
                }
            }
        }

        System.out.println(removed == inDegree.size() ? "no" : "yes");
    }
}
