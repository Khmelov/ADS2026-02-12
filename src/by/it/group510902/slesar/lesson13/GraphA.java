package by.it.group510902.slesar.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<Character, List<Character>> graph = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(",");

        for (String edge : edges) {
            String[] vertices = edge.trim().split("\\s*->\\s*");

            char from = vertices[0].trim().charAt(0);
            char to = vertices[1].trim().charAt(0);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);

            inDegree.putIfAbsent(from, 0);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
        }

        PriorityQueue<Character> queue = new PriorityQueue<>();

        for (char vertex : graph.keySet()) {
            if (inDegree.getOrDefault(vertex, 0) == 0) {
                queue.add(vertex);
            }
        }

        List<Character> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            char current = queue.poll();
            result.add(current);

            for (char neighbor : graph.get(current)) {
                int degree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, degree);

                if (degree == 0) {
                    queue.add(neighbor);
                }
            }
        }

        for (int i = 0; i < result.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(result.get(i));
        }
    }
}