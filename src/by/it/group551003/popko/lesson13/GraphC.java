package by.it.group551003.popko.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphC {
    private static int time = 0;

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        Map<String, List<String>> adj = new HashMap<>();
        Set<String> vertices = new TreeSet<>();

        String[] edges = line.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length != 2) continue;
            String u = parts[0].trim();
            String v = parts[1].trim();

            vertices.add(u);
            vertices.add(v);

            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
        }

        List<String> sortedVertices = new ArrayList<>(vertices);

        Map<String, Integer> disc = new HashMap<>();
        Map<String, Integer> low = new HashMap<>();
        Stack<String> stack = new Stack<>();
        Set<String> inStack = new HashSet<>();
        List<List<String>> sccs = new ArrayList<>();
        time = 0;

        for (String v : sortedVertices) {
            if (!disc.containsKey(v)) {
                tarjanDFS(v, adj, disc, low, stack, inStack, sccs);
            }
        }

        Map<String, Integer> vertexToScc = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++) {
            for (String v : sccs.get(i)) {
                vertexToScc.put(v, i);
            }
        }

        int n = sccs.size();
        Set<Integer>[] condAdj = new HashSet[n];
        for (int i = 0; i < n; i++) {
            condAdj[i] = new HashSet<>();
        }
        int[] inDegree = new int[n];

        for (Map.Entry<String, List<String>> entry : adj.entrySet()) {
            String u = entry.getKey();
            int sccU = vertexToScc.get(u);
            for (String v : entry.getValue()) {
                int sccV = vertexToScc.get(v);
                if (sccU != sccV) {
                    if (condAdj[sccU].add(sccV)) {
                        inDegree[sccV]++;
                    }
                }
            }
        }

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                pq.add(i);
            }
        }

        List<Integer> sortedSccs = new ArrayList<>();
        while (!pq.isEmpty()) {
            int u = pq.poll();
            sortedSccs.add(u);
            for (int v : condAdj[u]) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    pq.add(v);
                }
            }
        }

        for (int idx : sortedSccs) {
            List<String> comp = sccs.get(idx);
            Collections.sort(comp);
            StringBuilder sb = new StringBuilder();
            for (String v : comp) {
                sb.append(v);
            }
            System.out.println(sb.toString());
        }
    }

    private static void tarjanDFS(String u, Map<String, List<String>> adj,
                                  Map<String, Integer> disc, Map<String, Integer> low,
                                  Stack<String> stack, Set<String> inStack,
                                  List<List<String>> sccs) {
        disc.put(u, time);
        low.put(u, time);
        time++;
        stack.push(u);
        inStack.add(u);

        if (adj.containsKey(u)) {
            List<String> neighbors = new ArrayList<>(adj.get(u));
            Collections.sort(neighbors);
            for (String v : neighbors) {
                if (!disc.containsKey(v)) {
                    tarjanDFS(v, adj, disc, low, stack, inStack, sccs);
                    low.put(u, Math.min(low.get(u), low.get(v)));
                } else if (inStack.contains(v)) {
                    low.put(u, Math.min(low.get(u), disc.get(v)));
                }
            }
        }

        if (low.get(u).equals(disc.get(u))) {
            List<String> component = new ArrayList<>();
            while (true) {
                String v = stack.pop();
                inStack.remove(v);
                component.add(v);
                if (v.equals(u)) {
                    break;
                }
            }
            sccs.add(component);
        }
    }
}