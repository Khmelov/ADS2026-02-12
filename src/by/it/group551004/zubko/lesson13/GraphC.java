package by.it.group551004.zubko.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(line);
            }
        }
        String input = sb.toString();
        if (input.isEmpty()) return;

        Map<String, Set<String>> adj = new HashMap<>();
        Map<String, Set<String>> revAdj = new HashMap<>();
        Set<String> allNodes = new TreeSet<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                allNodes.add(from);
                allNodes.add(to);

                adj.putIfAbsent(from, new HashSet<>());
                adj.putIfAbsent(to, new HashSet<>());
                revAdj.putIfAbsent(from, new HashSet<>());
                revAdj.putIfAbsent(to, new HashSet<>());

                adj.get(from).add(to);
                revAdj.get(to).add(from);
            }
        }

        // Step 1: Kosaraju finish order
        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();
        for (String node : allNodes) {
            if (!visited.contains(node)) {
                dfsOrder(node, adj, visited, order);
            }
        }

        // Step 2: DFS on reverse graph to extract SCCs
        visited.clear();
        List<List<String>> components = new ArrayList<>();
        Map<String, Integer> compOf = new HashMap<>();

        for (int i = order.size() - 1; i >= 0; i--) {
            String node = order.get(i);
            if (!visited.contains(node)) {
                List<String> comp = new ArrayList<>();
                dfsRev(node, revAdj, visited, comp);
                Collections.sort(comp);
                int compIdx = components.size();
                components.add(comp);
                for (String v : comp) {
                    compOf.put(v, compIdx);
                }
            }
        }

        // Step 3: Build condensation DAG
        int k = components.size();
        String[] compNames = new String[k];
        for (int i = 0; i < k; i++) {
            StringBuilder name = new StringBuilder();
            for (String v : components.get(i)) {
                name.append(v);
            }
            compNames[i] = name.toString();
        }

        Map<Integer, Set<Integer>> dagAdj = new HashMap<>();
        int[] dagInDegree = new int[k];
        for (int i = 0; i < k; i++) {
            dagAdj.put(i, new HashSet<>());
        }

        for (String u : allNodes) {
            int cu = compOf.get(u);
            for (String v : adj.getOrDefault(u, Collections.emptySet())) {
                int cv = compOf.get(v);
                if (cu != cv) {
                    if (dagAdj.get(cu).add(cv)) {
                        dagInDegree[cv]++;
                    }
                }
            }
        }

        // Step 4: Topological sort of condensation DAG using Kahn's algorithm
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparing(i -> compNames[i]));
        for (int i = 0; i < k; i++) {
            if (dagInDegree[i] == 0) {
                pq.add(i);
            }
        }

        while (!pq.isEmpty()) {
            int cur = pq.poll();
            System.out.println(compNames[cur]);
            for (int next : dagAdj.get(cur)) {
                dagInDegree[next]--;
                if (dagInDegree[next] == 0) {
                    pq.add(next);
                }
            }
        }
    }

    private static void dfsOrder(String u, Map<String, Set<String>> adj, Set<String> visited, List<String> order) {
        visited.add(u);
        for (String v : adj.getOrDefault(u, Collections.emptySet())) {
            if (!visited.contains(v)) {
                dfsOrder(v, adj, visited, order);
            }
        }
        order.add(u);
    }

    private static void dfsRev(String u, Map<String, Set<String>> revAdj, Set<String> visited, List<String> comp) {
        visited.add(u);
        comp.add(u);
        for (String v : revAdj.getOrDefault(u, Collections.emptySet())) {
            if (!visited.contains(v)) {
                dfsRev(v, revAdj, visited, comp);
            }
        }
    }
}
