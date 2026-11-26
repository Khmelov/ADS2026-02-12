package by.it.group551001.Barusevich.lesson13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        Map<String, TreeSet<String>> graph = new TreeMap<>();
        Map<String, TreeSet<String>> reverse = new TreeMap<>();
        if (!input.isEmpty()) {
            for (String edge : input.split(",")) {
                String[] vertices = edge.trim().split("\\s*->\\s*");
                if (vertices.length != 2) continue;
                String from = vertices[0].trim();
                String to = vertices[1].trim();
                graph.computeIfAbsent(from, key -> new TreeSet<>()).add(to);
                graph.computeIfAbsent(to, key -> new TreeSet<>());
                reverse.computeIfAbsent(to, key -> new TreeSet<>()).add(from);
                reverse.computeIfAbsent(from, key -> new TreeSet<>());
            }
        }
        List<String> order = new ArrayList<>();
        Map<String, Boolean> visited = new TreeMap<>();
        for (String vertex : graph.keySet()) visited.put(vertex, false);
        for (String vertex : graph.keySet()) {
            if (!visited.get(vertex)) firstDfs(vertex, graph, visited, order);
        }
        Collections.reverse(order);
        visited.replaceAll((key, value) -> false);
        List<List<String>> components = new ArrayList<>();
        Map<String, Integer> componentOf = new TreeMap<>();
        for (String vertex : order) {
            if (!visited.get(vertex)) {
                List<String> component = new ArrayList<>();
                secondDfs(vertex, reverse, visited, component);
                Collections.sort(component);
                int index = components.size();
                components.add(component);
                for (String item : component) componentOf.put(item, index);
            }
        }
        List<TreeSet<Integer>> condensed = new ArrayList<>();
        int[] indegree = new int[components.size()];
        for (int i = 0; i < components.size(); i++) condensed.add(new TreeSet<>());
        for (Map.Entry<String, TreeSet<String>> entry : graph.entrySet()) {
            int from = componentOf.get(entry.getKey());
            for (String next : entry.getValue()) {
                int to = componentOf.get(next);
                if (from != to && condensed.get(from).add(to)) indegree[to]++;
            }
        }
        PriorityQueue<Integer> ready = new PriorityQueue<>((first, second) ->
                componentName(components.get(first)).compareTo(componentName(components.get(second))));
        for (int i = 0; i < indegree.length; i++) if (indegree[i] == 0) ready.add(i);
        StringBuilder result = new StringBuilder();
        boolean firstLine = true;
        while (!ready.isEmpty()) {
            int current = ready.poll();
            if (!firstLine) result.append('\n');
            firstLine = false;
            result.append(componentName(components.get(current)));
            for (int next : condensed.get(current)) {
                if (--indegree[next] == 0) ready.add(next);
            }
        }
        System.out.println(result);
    }

    private static void firstDfs(String vertex, Map<String, TreeSet<String>> graph,
                                 Map<String, Boolean> visited, List<String> order) {
        visited.put(vertex, true);
        for (String next : graph.get(vertex)) if (!visited.get(next)) firstDfs(next, graph, visited, order);
        order.add(vertex);
    }

    private static void secondDfs(String vertex, Map<String, TreeSet<String>> graph,
                                  Map<String, Boolean> visited, List<String> component) {
        visited.put(vertex, true);
        component.add(vertex);
        for (String next : graph.get(vertex)) if (!visited.get(next)) secondDfs(next, graph, visited, component);
    }

    private static String componentName(List<String> component) {
        StringBuilder name = new StringBuilder();
        for (String vertex : component) name.append(vertex);
        return name.toString();
    }
}
