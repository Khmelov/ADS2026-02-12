package by.it.group510902.kudan.lesson13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GraphB {

    //список смежности орграфа
    private static Map<String, List<String>> graph;
    //цвет вершины, 0 белая еще не заходили, 1 серая сейчас в пути обхода, 2 черная полностью обработана
    private static Map<String, Integer> color;

    //обход в глубину, если пришли в серую вершину значит вернулись по пути назад и это цикл
    private static boolean dfs(String v) {
        color.put(v, 1);

        for (String u : graph.get(v)) {
            if (color.get(u) == 1) {
                return true;
            }
            if (color.get(u) == 0 && dfs(u)) {
                return true;
            }
        }

        //все потомки обработаны, вершина больше не в текущем пути
        color.put(v, 2);
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        //создаем заново в main, иначе при повторном запуске останется старый граф
        graph = new HashMap<>();
        color = new HashMap<>();

        //разбираем строку на ребра и заполняем граф
        for (String edge : line.split(",")) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);
            color.put(from, 0);
            color.put(to, 0);
        }

        //граф может быть несвязным, поэтому запускаем обход из каждой белой вершины
        boolean cycle = false;
        for (String v : graph.keySet()) {
            if (color.get(v) == 0 && dfs(v)) {
                cycle = true;
                break;
            }
        }

        System.out.println(cycle ? "yes" : "no");
    }
}