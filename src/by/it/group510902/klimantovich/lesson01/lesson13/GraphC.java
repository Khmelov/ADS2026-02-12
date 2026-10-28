package by.it.group510902.klimantovich.lesson01.lesson13;
import java.util.*;
public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String input = scanner.nextLine();
        //граф и обратный граф,нужны для алгоритма Косараджу
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();
        Set<String> allNodes = new TreeSet<>(); // TreeSet для автоматической сортировки
        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            if (parts.length != 2) continue;
            String from = parts[0].trim();
            String to = parts[1].trim();
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            allNodes.add(from);
            allNodes.add(to);
        }
        //алгоритм Косараджу
        //обход в глубину, чтобы заполнить стек порядком завершения вершин
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();
        for (String node : allNodes) {
            if (!visited.contains(node)) {
                dfs1(node, graph, visited, stack);
            }
        }
        //обход в глубину по обратному графу, извлекая вершины из стека
        visited.clear();
        List<List<String>> components = new ArrayList<>();
        while (!stack.isEmpty()) {
            String node = stack.pop();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfs2(node, reverseGraph, visited, component);
                Collections.sort(component); //сортируем вершины внутри компоненты
                components.add(component);
            }
        }
        //вывод
        for (List<String> comp : components) {
            System.out.println(String.join("", comp));
        }
    }
    private static void dfs1(String node, Map<String, List<String>> graph, Set<String> visited, Stack<String> stack) {
        visited.add(node);
        if (graph.containsKey(node)) {
            for (String neighbor : graph.get(node)) {
                if (!visited.contains(neighbor)) {
                    dfs1(neighbor, graph, visited, stack);
                }
            }
        }
        stack.push(node); // добавляем в стек после обработки всех соседей
    }
    private static void dfs2(String node, Map<String, List<String>> reverseGraph, Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node);
        if (reverseGraph.containsKey(node)) {
            for (String neighbor : reverseGraph.get(node)) {
                if (!visited.contains(neighbor)) {
                    dfs2(neighbor, reverseGraph, visited, component);
                }
            }
        }
    }
}