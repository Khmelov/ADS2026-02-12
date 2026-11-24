package by.it.group551003.klimuk.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return;
        }

        Map<String, List<String>> adjList = new HashMap<>();

        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] vertices = edge.split("->");
            if (vertices.length == 2) {
                String from = vertices[0].trim();
                String to = vertices[1].trim();

                adjList.putIfAbsent(from, new ArrayList<>());
                adjList.putIfAbsent(to, new ArrayList<>());
                inDegree.putIfAbsent(from, 0);
                inDegree.putIfAbsent(to, 0);

                adjList.get(from).add(to);
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        List<String> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            for (String neighbor : adjList.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        System.out.println(String.join(" ", result));
    }
}