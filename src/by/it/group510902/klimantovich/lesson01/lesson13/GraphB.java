package by.it.group510902.klimantovich.lesson01.lesson13;
import java.util.*;
public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String input = scanner.nextLine();
        //строим граф смежности, только исходящие ребра
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allNodes = new HashSet<>();
        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            if (parts.length != 2) continue;
            String from = parts[0].trim();
            String to = parts[1].trim();
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            allNodes.add(from);
            allNodes.add(to);
        }
        //цвета вершин для поиска в глубину:0 белый не посещали, 1 серый сейчас в стеке мы внутри нее, 2 черный полностью обработана
        Map<String, Integer> color = new HashMap<>();
        for (String node : allNodes) {
            color.put(node, 0);
        }
        boolean hasCycle = false;
        //проверяем каждую непосещенную вершину
        for (String node : allNodes) {
            if (color.get(node) == 0) {
                if (dfs(node, graph, color)) {
                    hasCycle = true;
                    break;
                }
            }
        }
        System.out.println(hasCycle ? "yes" : "no");
    }
    // рекурсивный обход в глубину
    private static boolean dfs(String node, Map<String, List<String>> graph, Map<String, Integer> color) {
        //если мы зашли в серую вершину, значит, нашли цикл (вернулись в ту, откуда пришли)
        if (color.get(node) == 1) return true;
        if (color.get(node) == 2) return false;
        color.put(node, 1); // красим в серый начинаем обработку
        if (graph.containsKey(node)) {
            for (String neighbor : graph.get(node)) {
                if (dfs(neighbor, graph, color)) {
                    return true; //пробрасываем true наверх, если нашли цикл
                }
            }
        }

        color.put(node, 2); // Красим в черный (закончили обработку)
        return false;
    }
}