package by.it.group551002.kuzmenia.lesson13;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.hasNextLine() ? scanner.nextLine() : "";

        Map<String, TreeSet<String>> edges = new TreeMap<>();
        Map<String, Integer> inDegree = new TreeMap<>();

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
                if (edges.get(from).add(to)) {
                    inDegree.put(to, inDegree.get(to) + 1);
                }
            }
        }

        TreeSet<String> ready = new TreeSet<>();
        for (Map.Entry<String, Integer> e : inDegree.entrySet()) {
            if (e.getValue() == 0) {
                ready.add(e.getKey());
            }
        }

        StringBuilder result = new StringBuilder();
        while (!ready.isEmpty()) {
            String v = ready.pollFirst();
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(v);

            for (String next : edges.get(v)) {
                int degree = inDegree.get(next) - 1;
                inDegree.put(next, degree);
                if (degree == 0) {
                    ready.add(next);
                }
            }
        }

        System.out.println(result);
    }

    private static void registerVertex(String v,
                                       Map<String, TreeSet<String>> edges,
                                       Map<String, Integer> inDegree) {
        if (!edges.containsKey(v)) {
            edges.put(v, new TreeSet<>());
            inDegree.put(v, 0);
        }
    }
}