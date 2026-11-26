package by.it.group551003.kakhno.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allVertices = new TreeSet<>();

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            allVertices.add(from);
            allVertices.add(to);
        }

        List<String> result = topologicalSort(graph, allVertices);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            if (i > 0)
                sb.append(" ");
            sb.append(result.get(i));
        }
        System.out.println(sb.toString());
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph, Set<String> vertices) {
        Map<String, Integer> inDegree = new HashMap<>();
        for (String v : vertices)
            inDegree.put(v, 0);

        for (String from : graph.keySet())
            for (String to : graph.get(from))
                inDegree.put(to, inDegree.get(to) + 1);

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String v : vertices)
            if (inDegree.get(v) == 0)
                queue.offer(v);

        List<String> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            result.add(vertex);

            if (graph.containsKey(vertex))
                for (String neighbor : graph.get(vertex)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0)
                        queue.offer(neighbor);
                }
        }

        return result;
    }
}