package by.it.group551002.voroshko.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphA {

    static final Comparator<String> CMP = (a, b) -> {
        try {
            int ia = Integer.parseInt(a);
            int ib = Integer.parseInt(b);
            return Integer.compare(ia, ib);
        } catch (NumberFormatException e) {
            return a.compareTo(b);
        }
    };

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) return;

        Map<String, Set<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();


        for (String part : line.split(",")) {
            String[] sides = part.split("->");
            if (sides.length != 2) continue;
            String from = sides[0].trim();
            String to   = sides[1].trim();

            adj.putIfAbsent(from, new HashSet<>());
            adj.putIfAbsent(to, new HashSet<>());
            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);

            if (adj.get(from).add(to)) {
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        PriorityQueue<String> pq = new PriorityQueue<>(CMP);
        for (Map.Entry<String, Integer> e : inDegree.entrySet()) {
            if (e.getValue() == 0) pq.add(e.getKey());
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            String v = pq.poll();
            result.add(v);
            for (String u : adj.getOrDefault(v, Collections.emptySet())) {
                inDegree.put(u, inDegree.get(u) - 1);
                if (inDegree.get(u) == 0) pq.add(u);
            }
        }

        if (result.size() != inDegree.size()) {
            System.out.println("Граф содержит цикл, топологическая сортировка невозможна");
            return;
        }

        System.out.println(String.join(" ", result));
    }
}
