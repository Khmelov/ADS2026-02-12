package by.it.group510901.kachur.lesson01.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();

        Map<Character, List<Character>> graph = new TreeMap<>();
        Map<Character, Integer> inDegree = new TreeMap<>();

        if (!line.isEmpty()) {
            for (String edge : line.split(",")) {
                String[] parts = edge.split("->");
                char from = parts[0].trim().charAt(0);
                char to   = parts[1].trim().charAt(0);

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                graph.computeIfAbsent(to,   k -> new ArrayList<>());

                inDegree.putIfAbsent(from, 0);
                inDegree.merge(to, 1, Integer::sum);
            }
        }

        PriorityQueue<Character> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> e : inDegree.entrySet()) {
            if (e.getValue() == 0) {
                pq.add(e.getKey());
            }
        }

        List<Character> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            char u = pq.poll();
            result.add(u);
            for (char v : graph.getOrDefault(u, Collections.emptyList())) {
                inDegree.put(v, inDegree.get(v) - 1);
                if (inDegree.get(v) == 0) {
                    pq.add(v);
                }
            }
        }

        if (result.size() != inDegree.size()) {
            System.out.println("Граф содержит цикл");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(result.get(i));
        }
        System.out.println(sb);
    }
}
