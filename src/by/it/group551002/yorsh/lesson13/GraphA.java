package by.it.group551002.yorsh.lesson13;
import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String line = scanner.nextLine();

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Set<String> nodes = new TreeSet<>();

        String[] edges = line.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            if (edge.isEmpty()) continue;
            String[] parts = edge.split("->");
            String u = parts[0].trim();
            String v = parts[1].trim();

            nodes.add(u);
            nodes.add(v);

            adj.putIfAbsent(u, new ArrayList<>());
            adj.get(u).add(v);

            inDegree.put(v, inDegree.getOrDefault(v, 0) + 1);
        }

        for (String node : nodes) {
            inDegree.putIfAbsent(node, 0);
        }

        // PriorityQueue гарантирует лексикографический порядок для вершин со степенью входа 0
        PriorityQueue<String> pq = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                pq.add(entry.getKey());
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            String u = pq.poll();
            result.add(u);

            if (adj.containsKey(u)) {
                for (String v : adj.get(u)) {
                    inDegree.put(v, inDegree.get(v) - 1);
                    if (inDegree.get(v) == 0) {
                        pq.add(v);
                    }
                }
            }
        }

        System.out.println(String.join(" ", result));
    }
}