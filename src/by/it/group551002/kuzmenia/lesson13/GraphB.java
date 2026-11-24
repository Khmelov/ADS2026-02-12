package by.it.group551002.kuzmenia.lesson13;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.hasNextLine() ? scanner.nextLine() : "";

        Map<String, List<String>> edges = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        for (String part : line.split(",")) {
            part = part.trim();
            if (part.isEmpty()) {
                continue;
            }
            String[] ends = part.split("->");
            String from = ends[0].trim();
            registerVertex(from, edges, inDegree);

            if (ends.length > 1) {
                String to = ends[1].trim();
                registerVertex(to, edges, inDegree);
                edges.get(from).add(to);
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        Deque<String> ready = new ArrayDeque<>();
        for (Map.Entry<String, Integer> e : inDegree.entrySet()) {
            if (e.getValue() == 0) {
                ready.add(e.getKey());
            }
        }

        int processed = 0;
        while (!ready.isEmpty()) {
            String v = ready.poll();
            processed++;

            for (String next : edges.get(v)) {
                int degree = inDegree.get(next) - 1;
                inDegree.put(next, degree);
                if (degree == 0) {
                    ready.add(next);
                }
            }
        }

        boolean hasCycle = processed < inDegree.size();
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static void registerVertex(String v,
                                       Map<String, List<String>> edges,
                                       Map<String, Integer> inDegree) {
        if (!edges.containsKey(v)) {
            edges.put(v, new ArrayList<>());
            inDegree.put(v, 0);
        }
    }
}