package by.it.group551004.podvitelskiymichael.lesson13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                if (!sb.isEmpty()) {
                    sb.append(", ");
                }
                sb.append(line);
            }
        }
        String input = sb.toString();
        if (input.isEmpty()) {
            return;
        }

        Map<String, Set<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        for (String edge : input.split(",")) {
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                adj.putIfAbsent(from, new TreeSet<>());
                adj.putIfAbsent(to, new TreeSet<>());
                inDegree.putIfAbsent(from, 0);
                inDegree.putIfAbsent(to, 0);

                if (adj.get(from).add(to)) {
                    inDegree.put(to, inDegree.get(to) + 1);
                }
            }
        }

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String u = queue.poll();
            result.add(u);
            for (String v : adj.getOrDefault(u, Collections.emptySet())) {
                inDegree.put(v, inDegree.get(v) - 1);
                if (inDegree.get(v) == 0) {
                    queue.add(v);
                }
            }
        }

        System.out.println(String.join(" ", result));
    }
}