package by.it.group551001.docenko.lesson13;

import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Set<String>> graph = parseGraph(scanner.nextLine());
        System.out.println(hasCycle(graph) ? "yes" : "no");
    }

    private static Map<String, Set<String>> parseGraph(String line) {
        Map<String, Set<String>> graph = new TreeMap<>();
        for (String edge : line.trim().split("\\s*,\\s*")) {
            String[] vertices = edge.split("\\s*->\\s*");
            graph.computeIfAbsent(vertices[0], key -> new TreeSet<>()).add(vertices[1]);
            graph.computeIfAbsent(vertices[1], key -> new TreeSet<>());
        }
        return graph;
    }

    private static boolean hasCycle(Map<String, Set<String>> graph) {
        Set<String> inProgress = new HashSet<>();
        Set<String> finished = new HashSet<>();
        for (String vertex : graph.keySet()) {
            if (reachesCycle(vertex, graph, inProgress, finished)) {
                return true;
            }
        }
        return false;
    }

    private static boolean reachesCycle(String vertex,
                                        Map<String, Set<String>> graph,
                                        Set<String> inProgress,
                                        Set<String> finished) {
        if (inProgress.contains(vertex)) {
            return true;
        }
        if (finished.contains(vertex)) {
            return false;
        }
        inProgress.add(vertex);
        for (String next : graph.get(vertex)) {
            if (reachesCycle(next, graph, inProgress, finished)) {
                return true;
            }
        }
        inProgress.remove(vertex);
        finished.add(vertex);
        return false;
    }
}
