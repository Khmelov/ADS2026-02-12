package by.it.group551004.sheev.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class GraphA {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();

        // Используем String для вершин, чтобы поддерживать буквы и числа
        // TreeSet и PriorityQueue сортируют строки лексикографически
        Set<String> vertices = new TreeSet<>();
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        // Разбор строки вида "0 -> 2, 1 -> 3, ..." или "A -> B, ..."
        if (line != null && !line.trim().isEmpty()) {
            String[] edges = line.split(",");
            for (String edge : edges) {
                String[] parts = edge.trim().split("->");
                String from = parts[0].trim();
                String to = parts[1].trim();

                vertices.add(from);
                vertices.add(to);
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to); // ребро from -> to
                indegree.merge(to, 1, Integer::sum);                         // +1 входящая к to
                indegree.putIfAbsent(from, 0);                               // from точно есть в indegree
            }
        }

        // Иниц. нулевую степ. для верш., встречавшихся только как "to"
        for (String v : vertices) {
            indegree.putIfAbsent(v, 0);
        }

        // Алг. Кана: стартуем с верш. без входящих рёбер
        // PriorityQueue даёт лексикограф. порядок при равнозначности
        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String v : vertices) {
            if (indegree.get(v) == 0) {
                pq.add(v);
            }
        }

        StringBuilder sb = new StringBuilder();
        while (!pq.isEmpty()) {
            String v = pq.poll();           // берём верш. с нулевой входящей степ.
            sb.append(v).append(" ");       // добавл. в рез.

            // "Удаляем" исходящие рёбра v, уменьшая степени соседей
            List<String> neighbors = graph.get(v);
            if (neighbors != null) {
                for (String u : neighbors) {
                    indegree.merge(u, -1, Integer::sum);
                    if (indegree.get(u) == 0) {   // сосед готов — в очередь
                        pq.add(u);
                    }
                }
            }
        }

        System.out.println(sb.toString().trim());
    }
}