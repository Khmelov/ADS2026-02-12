package by.it.group551001.docenko.lesson13;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TreeMap<String, TreeSet<String>> graph = new TreeMap<>();
        TreeMap<String, TreeSet<String>> reversed = new TreeMap<>();
        for (String edge : scanner.nextLine().trim().split("\\s*,\\s*")) {
            String[] vertices = edge.split("\\s*->\\s*");
            addEdge(graph, vertices[0], vertices[1]);
            addEdge(reversed, vertices[1], vertices[0]);
        }
        System.out.print(findComponents(graph, reversed));
    }

    private static void addEdge(TreeMap<String, TreeSet<String>> graph, String from, String to) {
        graph.computeIfAbsent(from, key -> new TreeSet<>()).add(to);
        graph.computeIfAbsent(to, key -> new TreeSet<>());
    }

    private static String findComponents(TreeMap<String, TreeSet<String>> graph,
                                         TreeMap<String, TreeSet<String>> reversed) {
        Deque<String> finishOrder = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        for (String vertex : graph.descendingKeySet()) {
            if (visited.add(vertex)) {
                fillFinishOrder(vertex, graph, visited, finishOrder);
            }
        }

        StringJoiner components = new StringJoiner("\n", "", "\n");
        visited.clear();
        for (String vertex : finishOrder) {
            if (visited.add(vertex)) {
                TreeSet<String> component = new TreeSet<>();
                collectComponent(vertex, reversed, visited, component);
                components.add(String.join("", component));
            }
        }
        return components.toString();
    }

    private static void fillFinishOrder(String vertex,
                                        TreeMap<String, TreeSet<String>> graph,
                                        Set<String> visited,
                                        Deque<String> finishOrder) {
        for (String next : graph.get(vertex).descendingSet()) {
            if (visited.add(next)) {
                fillFinishOrder(next, graph, visited, finishOrder);
            }
        }
        finishOrder.addFirst(vertex);
    }

    private static void collectComponent(String vertex,
                                         TreeMap<String, TreeSet<String>> reversed,
                                         Set<String> visited,
                                         TreeSet<String> component) {
        component.add(vertex);
        for (String next : reversed.get(vertex)) {
            if (visited.add(next)) {
                collectComponent(next, reversed, visited, component);
            }
        }
    }
}
