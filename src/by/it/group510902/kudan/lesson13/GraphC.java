package by.it.group510902.kudan.lesson13;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphC {

    //исходный граф и транспонированный граф, где все ребра развернуты
    //treemap и treeset чтобы вершины перебирались по алфавиту и ответ был всегда одинаковый
    private static Map<String, TreeSet<String>> graph;
    private static Map<String, TreeSet<String>> back;

    private static Set<String> used;
    //вершины в порядке выхода из первого обхода
    private static List<String> order;

    //первый обход по исходному графу, вершину добавляем когда закончили всех ее потомков
    private static void dfs1(String v) {
        used.add(v);
        for (String u : graph.get(v)) {
            if (!used.contains(u)) {
                dfs1(u);
            }
        }
        order.add(v);
    }

    //второй обход по развернутому графу, собирает одну компоненту сильной связности
    private static void dfs2(String v, TreeSet<String> comp) {
        used.add(v);
        comp.add(v);
        for (String u : back.get(v)) {
            if (!used.contains(u)) {
                dfs2(u, comp);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        //создаем заново в main, иначе при повторном запуске останется старый граф
        graph = new TreeMap<>();
        back = new TreeMap<>();
        used = new HashSet<>();
        order = new ArrayList<>();

        //разбираем ребра, в back кладем ребро в обратную сторону
        for (String edge : line.split(",")) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.putIfAbsent(from, new TreeSet<>());
            graph.putIfAbsent(to, new TreeSet<>());
            back.putIfAbsent(from, new TreeSet<>());
            back.putIfAbsent(to, new TreeSet<>());
            graph.get(from).add(to);
            back.get(to).add(from);
        }

        //алгоритм косарайю, сначала считаем времена выхода
        for (String v : graph.keySet()) {
            if (!used.contains(v)) {
                dfs1(v);
            }
        }

        //потом идем по вершинам с самого позднего выхода по развернутому графу
        //так компоненты получаются в топологическом порядке, первая исток, последняя сток
        used.clear();
        StringBuilder sb = new StringBuilder();
        for (int i = order.size() - 1; i >= 0; i--) {
            String v = order.get(i);
            if (!used.contains(v)) {
                TreeSet<String> comp = new TreeSet<>();
                dfs2(v, comp);

                //вершины компоненты уже по алфавиту, склеиваем их без пробелов
                for (String x : comp) {
                    sb.append(x);
                }
                sb.append("\n");
            }
        }

        System.out.print(sb);
    }
}