package by.it.group551002.litovchenko.lesson13;

import java.util.*;

public class GraphC {
    private static void fillOrder(String u, Map<String, Set<String>> graph,
                                  Set<String> visited, Deque<String> order) {
        visited.add(u);

        for (String next : graph.getOrDefault(u, Collections.emptySet())) {
            if (!visited.contains(next)) {
                fillOrder(next, graph, visited, order);
            }
        }
        order.push(u);
    }

    private static Set<String> collectComponent(String u, Map<String, Set<String>> invertGraph,
                                                Set<String> visited, Set<String> component) {
        visited.add(u);
        component.add(u);

        for (String next : invertGraph.getOrDefault(u, Collections.emptySet())) {
            if (!visited.contains(next)) {
                collectComponent(next, invertGraph, visited, component);
            }
        }
        return component;
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Set<String>> invertGraph = new HashMap<>();
        Deque<String> order = new ArrayDeque<>();

        for (String str: scanner.nextLine().split(",")){
            if (str.trim().isEmpty()) {
                continue;
            }
            String[] vertexes = str.split("->");
            String outVert = vertexes[0].trim();
            String inVert = vertexes[1].trim();

            graph.computeIfAbsent(outVert, k -> new HashSet<>());
            graph.computeIfAbsent(inVert, k -> new HashSet<>());
            graph.get(outVert).add(inVert);

            // Граф с перевернутыми стрелками
            invertGraph.computeIfAbsent(outVert, k -> new HashSet<>());
            invertGraph.computeIfAbsent(inVert, k -> new HashSet<>());
            invertGraph.get(inVert).add(outVert);
        }

        Set<String> visited = new HashSet<>();

        for (String vert: graph.keySet()) {
            if (!visited.contains(vert)) {
                fillOrder(vert, graph, visited, order);
            }
        }

        visited.clear();
        while (!order.isEmpty()) {
            String vert = order.pop();
            if (!visited.contains(vert)) {
                Set<String> component = collectComponent(vert, invertGraph, visited, new TreeSet<>());
                System.out.println(String.join("", component));
            }
        }
    }
}
