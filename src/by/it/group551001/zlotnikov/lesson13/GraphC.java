package by.it.group551001.zlotnikov.lesson13;

import java.util.*;

public class GraphC {
    private static void dfs1(String u, Map<String, List<String>> adj, Set<String> visited, Deque<String> order) {
        visited.add(u);
        for (String v : adj.getOrDefault(u, Collections.emptyList())) {
            if (!visited.contains(v)) {
                dfs1(v, adj, visited, order);
            }
        }
        order.push(u);
    }

    private static void dfs2(String u, Map<String, List<String>> revAdj, Set<String> visited, List<String> component) {
        visited.add(u);
        component.add(u);
        for (String v : revAdj.getOrDefault(u, Collections.emptyList())) {
            if (!visited.contains(v)) {
                dfs2(v, revAdj, visited, component);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            return;
        }

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, List<String>> revAdj = new HashMap<>();
        TreeSet<String> vertices = new TreeSet<>();

        String[] edges = line.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length != 2) {
                continue;
            }
            String u = parts[0].trim();
            String v = parts[1].trim();
            vertices.add(u);
            vertices.add(v);
            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            revAdj.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
        }

        Set<String> visited = new HashSet<>();
        Deque<String> order = new ArrayDeque<>();
        for (String v : vertices) {
            if (!visited.contains(v)) {
                dfs1(v, adj, visited, order);
            }
        }

        visited.clear();
        List<String> components = new ArrayList<>();
        Map<String, Integer> compIndex = new HashMap<>();

        while (!order.isEmpty()) {
            String v = order.pop();
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, revAdj, visited, comp);
                Collections.sort(comp);
                StringBuilder sb = new StringBuilder();
                for (String node : comp) {
                    sb.append(node);
                    compIndex.put(node, components.size());
                }
                components.add(sb.toString());
            }
        }

        int n = components.size();
        List<Set<Integer>> condAdj = new ArrayList<>();
        int[] inDegree = new int[n];
        for (int i = 0; i < n; i++) {
            condAdj.add(new HashSet<>());
        }
        for (Map.Entry<String, List<String>> entry : adj.entrySet()) {
            int cu = compIndex.get(entry.getKey());
            for (String target : entry.getValue()) {
                int cv = compIndex.get(target);
                if (cu != cv && condAdj.get(cu).add(cv)) {
                    inDegree[cv]++;
                }
            }
        }

        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparing(components::get));
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                pq.add(i);
            }
        }

        StringBuilder out = new StringBuilder();
        while (!pq.isEmpty()) {
            int cu = pq.poll();
            out.append(components.get(cu)).append('\n');
            for (int cv : condAdj.get(cu)) {
                if (--inDegree[cv] == 0) {
                    pq.add(cv);
                }
            }
        }

        System.out.print(out);
    }
}
