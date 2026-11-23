package by.it.group551001.Barusevich.lesson13;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        Map<String, TreeSet<String>> graph = new TreeMap<>();
        Map<String, Integer> indegree = new TreeMap<>();
        if (!input.isEmpty()) {
            for (String edge : input.split(",")) {
                String[] vertices = edge.trim().split("\\s*->\\s*");
                if (vertices.length != 2) continue;
                String from = vertices[0].trim();
                String to = vertices[1].trim();
                graph.computeIfAbsent(from, key -> new TreeSet<>());
                graph.computeIfAbsent(to, key -> new TreeSet<>());
                indegree.putIfAbsent(from, 0);
                indegree.putIfAbsent(to, 0);
                if (graph.get(from).add(to)) indegree.put(to, indegree.get(to) + 1);
            }
        }
        PriorityQueue<String> ready = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : indegree.entrySet()) {
            if (entry.getValue() == 0) ready.add(entry.getKey());
        }
        StringBuilder result = new StringBuilder();
        int processed = 0;
        while (!ready.isEmpty()) {
            String vertex = ready.poll();
            if (processed++ > 0) result.append(' ');
            result.append(vertex);
            for (String next : graph.get(vertex)) {
                int degree = indegree.get(next) - 1;
                indegree.put(next, degree);
                if (degree == 0) ready.add(next);
            }
        }
        if (processed == graph.size()) System.out.println(result);
        else System.out.println();
    }
}
