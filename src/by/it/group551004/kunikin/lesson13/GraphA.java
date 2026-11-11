package by.it.group551004.kunikin.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder inputBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                if (inputBuilder.length() > 0) {
                    inputBuilder.append(", ");
                }
                inputBuilder.append(line);
            }
        }

        String raw = inputBuilder.toString().trim();
        if (raw.isEmpty()) {
            return;
        }

        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegrees = new HashMap<>();

        for (String token : raw.split(",")) {
            String[] edge = token.split("->");
            if (edge.length == 2) {
                String src = edge[0].trim();
                String dst = edge[1].trim();

                graph.computeIfAbsent(src, k -> new HashSet<>());
                graph.computeIfAbsent(dst, k -> new HashSet<>());
                inDegrees.putIfAbsent(src, 0);
                inDegrees.putIfAbsent(dst, 0);

                if (graph.get(src).add(dst)) {
                    inDegrees.put(dst, inDegrees.get(dst) + 1);
                }
            }
        }

        PriorityQueue<String> readyQueue = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : inDegrees.entrySet()) {
            if (entry.getValue() == 0) {
                readyQueue.offer(entry.getKey());
            }
        }

        StringJoiner result = new StringJoiner(" ");
        while (!readyQueue.isEmpty()) {
            String current = readyQueue.poll();
            result.add(current);

            for (String neighbor : graph.get(current)) {
                int updated = inDegrees.get(neighbor) - 1;
                inDegrees.put(neighbor, updated);
                if (updated == 0) {
                    readyQueue.offer(neighbor);
                }
            }
        }

        System.out.println(result);
    }
}
