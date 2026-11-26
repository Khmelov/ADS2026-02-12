package by.it.group510902.slesar.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<Integer, List<Integer>> graph = new HashMap<>();
        Map<Integer, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(",");

        for (String edge : edges) {
            String[] vertices = edge.trim().split("\\s*->\\s*");

            int from = Integer.parseInt(vertices[0]);
            int to = Integer.parseInt(vertices[1]);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);

            inDegree.putIfAbsent(from, 0);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
        }

        Queue<Integer> queue = new LinkedList<>();

        for (int vertex : graph.keySet()) {
            if (inDegree.getOrDefault(vertex, 0) == 0) {
                queue.add(vertex);
            }
        }

        int processed = 0;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            processed++;

            for (int neighbor : graph.get(current)) {
                int degree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, degree);

                if (degree == 0) {
                    queue.add(neighbor);
                }
            }
        }

        System.out.println(processed == graph.size() ? "no" : "yes");
    }
}