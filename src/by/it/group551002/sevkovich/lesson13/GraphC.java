package by.it.group551002.sevkovich.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String arg) {
        String[] edges = arg.split(", ");
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allVertices = new TreeSet<>();

        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();
            allVertices.add(from);
            allVertices.add(to);
        }

        for (String v : allVertices) {
            graph.put(v, new ArrayList<>());
        }
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();
            graph.get(from).add(to);
        }
        for (List<String> neighbors : graph.values()) {
            Collections.sort(neighbors);
        }

        Set<String> visited = new HashSet<>();
        LinkedList<String> finishOrder = new LinkedList<>();

        for (String v : allVertices) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, finishOrder);
            }
        }

        Map<String, List<String>> reversedGraph = new HashMap<>();
        for (String v : allVertices) {
            reversedGraph.put(v, new ArrayList<>());
        }
        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            String from = entry.getKey();
            for (String to : entry.getValue()) {
                reversedGraph.get(to).add(from);
            }
        }
        for (List<String> neighbors : reversedGraph.values()) {
            Collections.sort(neighbors);
        }

        visited.clear();
        List<List<String>> components = new ArrayList<>();

        for (String v : finishOrder) {
            if (!visited.contains(v)) {
                List<String> component = new ArrayList<>();
                dfs2(v, reversedGraph, visited, component);
                Collections.sort(component);
                components.add(component);
            }
        }

        for (List<String> component : components) {
            for (String vertex : component) {
                System.out.print(vertex);
            }
            System.out.println();
        }
    }

    private static void dfs1(String v, Map<String, List<String>> graph,
                             Set<String> visited, LinkedList<String> finishOrder) {
        visited.add(v);
        for (String neighbor : graph.get(v)) {
            if (!visited.contains(neighbor)) {
                dfs1(neighbor, graph, visited, finishOrder);
            }
        }
        finishOrder.addFirst(v);
    }

    private static void dfs2(String v, Map<String, List<String>> reversedGraph,
                             Set<String> visited, List<String> component) {
        visited.add(v);
        component.add(v);
        for (String neighbor : reversedGraph.get(v)) {
            if (!visited.contains(neighbor)) {
                dfs2(neighbor, reversedGraph, visited, component);
            }
        }
    }
}