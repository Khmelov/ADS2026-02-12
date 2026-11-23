package by.it.group551001.belozorchik.lesson13;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        String line = scanner.nextLine().trim();

        // LinkedHashMap запоминает порядок добавления вершин
        Map<String, ArrayList<String>> graph = new LinkedHashMap<>();
        Map<String, ArrayList<String>> reversedGraph = new LinkedHashMap<>();

        if (!line.isEmpty()) {
            for (String edge : line.split(",")) {
                String[] vertices = edge.split("->");
                String from = vertices[0].trim();
                String to = vertices[1].trim();

                addVertex(graph, from);
                addVertex(graph, to);
                addVertex(reversedGraph, from);
                addVertex(reversedGraph, to);
                graph.get(from).add(to);
                reversedGraph.get(to).add(from);
            }
        }

        // 1ый обход: запоминаем вершины в порядке завершения DFS
        // HashSet - хранит посещенные верщины
        Set<String> visited = new HashSet<>();
        ArrayList<String> finishOrder = new ArrayList<>();
        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfsFinish(vertex, graph, visited, finishOrder);
            }
        }

        // 2ой обход: идём с конца списка, но уже по обратному графу
        visited.clear();
        for (int i = finishOrder.size() - 1; i >= 0; i--) {
            String vertex = finishOrder.get(i);
            if (!visited.contains(vertex)) {
                ArrayList<String> component = new ArrayList<>();
                dfsCollect(vertex, reversedGraph, visited, component);
                Collections.sort(component);
                System.out.println(String.join("", component));
            }
        }
    }

    // хранит отметку о посещении
    private static void addVertex(Map<String, ArrayList<String>> graph, String vertex) {
        if (!graph.containsKey(vertex)) {
            graph.put(vertex, new ArrayList<>());
        }
    }

    private static void dfsFinish(String vertex, Map<String, ArrayList<String>> graph,
                                  Set<String> visited, ArrayList<String> finishOrder) {
        visited.add(vertex);
        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsFinish(neighbor, graph, visited, finishOrder);
            }
        }
        // запис вершину ПОСЛЕ обработки всех её соседей
        finishOrder.add(vertex);
    }

    private static void dfsCollect(String vertex, Map<String, ArrayList<String>> graph,
                                   Set<String> visited, ArrayList<String> component) {
        visited.add(vertex);
        component.add(vertex);
        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsCollect(neighbor, graph, visited, component);
            }
        }
    }
}
