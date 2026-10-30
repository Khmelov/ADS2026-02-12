package by.it.group510902.kudan.lesson13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeSet;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        //список смежности, у каждой вершины множество соседей
        //treeset сразу убирает повторные ребра, если одно ребро ввели два раза
        Map<String, TreeSet<String>> graph = new HashMap<>();
        //полустепень захода, сколько ребер входит в вершину
        Map<String, Integer> in = new HashMap<>();

        //разбираем строку по запятым, каждое ребро делим по стрелке
        for (String edge : line.split(",")) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.putIfAbsent(from, new TreeSet<>());
            graph.putIfAbsent(to, new TreeSet<>());
            in.putIfAbsent(from, 0);
            in.putIfAbsent(to, 0);

            //степень увеличиваем только если такого ребра еще не было
            if (graph.get(from).add(to)) {
                in.put(to, in.get(to) + 1);
            }
        }

        //алгоритм кана, в очередь кладем вершины в которые ничего не входит
        //очередь с приоритетом всегда отдает минимальную по алфавиту вершину
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String v : in.keySet()) {
            if (in.get(v) == 0) {
                queue.add(v);
            }
        }

        List<String> order = new ArrayList<>();

        while (!queue.isEmpty()) {
            String v = queue.poll();
            order.add(v);

            //убираем ребра из v, если у соседа степень стала 0 то он готов к выводу
            for (String u : graph.get(v)) {
                in.put(u, in.get(u) - 1);
                if (in.get(u) == 0) {
                    queue.add(u);
                }
            }
        }

        //выводим вершины через пробел в найденном порядке
        System.out.println(String.join(" ", order));
    }
}