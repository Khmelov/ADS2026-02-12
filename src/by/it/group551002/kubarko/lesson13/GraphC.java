package by.it.group551002.kubarko.lesson13;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder input = new StringBuilder();
        while (scanner.hasNextLine()) {
            input.append(input.length() == 0 ? "" : ", ").append(scanner.nextLine());
        }

        Map<String, TreeSet<String>> graph = new TreeMap<>();
        Map<String, TreeSet<String>> reverse = new TreeMap<>();
        for (String edge : input.toString().split(",")) {
            String trimmed = edge.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            String[] vertices = trimmed.split("\\s*->\\s*");
            if (vertices.length != 2) {
                throw new IllegalArgumentException("Invalid edge: " + trimmed);
            }
            String from = vertices[0].trim();
            String to = vertices[1].trim();
            graph.computeIfAbsent(from, ignored -> new TreeSet<>()).add(to);
            graph.computeIfAbsent(to, ignored -> new TreeSet<>());
            reverse.computeIfAbsent(from, ignored -> new TreeSet<>());
            reverse.computeIfAbsent(to, ignored -> new TreeSet<>()).add(from);
        }

        List<String> finished = new ArrayList<>();
        TreeSet<String> visited = new TreeSet<>();
        for (String vertex : graph.keySet()) {
            finishOrder(vertex, graph, visited, finished);
        }

        visited.clear();
        List<TreeSet<String>> components = new ArrayList<>();
        for (int i = finished.size() - 1; i >= 0; i--) {
            String vertex = finished.get(i);
            if (!visited.contains(vertex)) {
                TreeSet<String> component = new TreeSet<>();
                collectComponent(vertex, reverse, visited, component);
                components.add(component);
            }
        }

        Map<String, Integer> componentOf = new TreeMap<>();
        for (int i = 0; i < components.size(); i++) {
            for (String vertex : components.get(i)) {
                componentOf.put(vertex, i);
            }
        }

        List<TreeSet<Integer>> condensed = new ArrayList<>();
        int[] inDegree = new int[components.size()];
        for (int i = 0; i < components.size(); i++) {
            condensed.add(new TreeSet<>());
        }
        for (Map.Entry<String, TreeSet<String>> entry : graph.entrySet()) {
            int from = componentOf.get(entry.getKey());
            for (String neighbor : entry.getValue()) {
                int to = componentOf.get(neighbor);
                if (from != to && condensed.get(from).add(to)) {
                    inDegree[to]++;
                }
            }
        }

        Comparator<Integer> componentOrder =
                Comparator.comparing(index -> components.get(index).first());
        PriorityQueue<Integer> sources = new PriorityQueue<>(componentOrder);
        for (int i = 0; i < inDegree.length; i++) {
            if (inDegree[i] == 0) {
                sources.add(i);
            }
        }

        StringBuilder result = new StringBuilder();
        while (!sources.isEmpty()) {
            int index = sources.remove();
            if (result.length() > 0) {
                result.append('\n');
            }
            for (String vertex : components.get(index)) {
                result.append(vertex);
            }
            for (int next : condensed.get(index)) {
                if (--inDegree[next] == 0) {
                    sources.add(next);
                }
            }
        }
        System.out.println(result);
    }

    private static void finishOrder(String vertex, Map<String, TreeSet<String>> graph,
                                    TreeSet<String> visited, List<String> finished) {
        if (!visited.add(vertex)) {
            return;
        }
        for (String next : graph.get(vertex)) {
            finishOrder(next, graph, visited, finished);
        }
        finished.add(vertex);
    }

    private static void collectComponent(String vertex, Map<String, TreeSet<String>> reverse,
                                         TreeSet<String> visited, TreeSet<String> component) {
        if (!visited.add(vertex)) {
            return;
        }
        component.add(vertex);
        for (String next : reverse.get(vertex)) {
            collectComponent(next, reverse, visited, component);
        }
    }
}
