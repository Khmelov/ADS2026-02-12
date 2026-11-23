package by.it.group551001.maxim_belyak.lesson13;


import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();

        Map<String, List<String>> graph = new TreeMap<>();
        Map<String, Integer> inDegree = new TreeMap<>();

        String[] edges = input.split(",");

        for (String edge : edges) {
            String[] vertices = edge.trim().split("\\s*->\\s*");

            String from = vertices[0];
            String to = vertices[1];

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);

            graph.get(from).add(to);

            inDegree.put(to, inDegree.get(to) + 1);
        }// Увеличиваем количество входящих стрелок у to.

        PriorityQueue<String> queue = new PriorityQueue<>();

        for (String vertex : graph.keySet()) {
            if (inDegree.get(vertex) == 0) {
                queue.add(vertex);
            }
        }

        List<String> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            String vertex = queue.poll();

            result.add(vertex);

            for (String neighbor : graph.get(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);

                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        System.out.println(String.join(" ", result));
    }
}
