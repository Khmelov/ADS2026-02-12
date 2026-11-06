package by.it.group510902.kislyi.lesson13;
import java.util.*;

public class GraphB {

    public static void main(String[] args) {
               Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();

        Set<String> vertices = new HashSet<>();
        List<String[]> edges = new ArrayList<>();

        if (!line.isEmpty()) {
            String[] parts = line.split(",");
            for (String part : parts) {
                part = part.trim();
                if (part.isEmpty()) continue;
                String[] halves = part.split("->");
                String from = halves[0].trim();
                String to = halves[1].trim();
                vertices.add(from);
                vertices.add(to);
                edges.add(new String[]{from, to});
            }
        }

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();
        for (String v : vertices) {
            adj.put(v, new ArrayList<>());
            indegree.put(v, 0);
        }
        for (String[] e : edges) {
            adj.get(e[0]).add(e[1]);
            indegree.put(e[1], indegree.get(e[1]) + 1);
        }

        Deque<String> queue = new ArrayDeque<>();
        for (String v : vertices) {
            if (indegree.get(v) == 0) {
                queue.add(v);
            }
        }

        int processed = 0;
        while (!queue.isEmpty()) {
            String v = queue.poll();
            processed++;
            for (String next : adj.get(v)) {
                indegree.put(next, indegree.get(next) - 1);
                if (indegree.get(next) == 0) {
                    queue.add(next);
                }
            }
        }

        if (processed == vertices.size()) {
            System.out.println("no");
        } else {
            System.out.println("yes");
        }
    }
}
