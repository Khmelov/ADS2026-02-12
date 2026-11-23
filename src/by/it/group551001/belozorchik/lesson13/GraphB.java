package by.it.group551001.belozorchik.lesson13;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        String line = scanner.nextLine().trim();

        Map<String, ArrayList<String>> graph = new HashMap<>();
        Map<String, Integer> incoming = new HashMap<>();

        if (!line.isEmpty()) {
            for (String edge : line.split(",")) {
                String[] vertices = edge.split("->");
                String from = vertices[0].trim();
                String to = vertices[1].trim();

                addVertex(graph, incoming, from);
                addVertex(graph, incoming, to);
                graph.get(from).add(to);
                incoming.put(to, incoming.get(to) + 1);
            }
        }

        // порядок не важен - поэтому обычная очередь
        Queue<String> ready = new ArrayDeque<>();
        for (String vertex : graph.keySet()) {
            if (incoming.get(vertex) == 0) {
                ready.add(vertex);
            }
        }

        int processed = 0;
        while (!ready.isEmpty()) {
            String vertex = ready.poll();
            processed++;

            for (String neighbor : graph.get(vertex)) {
                int remaining = incoming.get(neighbor) - 1;
                incoming.put(neighbor, remaining);
                if (remaining == 0) {
                    ready.add(neighbor);
                }
            }
        }

        // если какие-то вершины остались - цикл есть
        if (processed < graph.size()) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }

    private static void addVertex(Map<String, ArrayList<String>> graph,
                                  Map<String, Integer> incoming, String vertex) {
        if (!graph.containsKey(vertex)) {
            graph.put(vertex, new ArrayList<>());
            incoming.put(vertex, 0);
        }
    }
}
