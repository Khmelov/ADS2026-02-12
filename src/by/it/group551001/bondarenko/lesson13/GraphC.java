package by.it.group551001.bondarenko.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphC {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        Map<String, List<String>> adjList = new HashMap<>();
        Map<String, List<String>> revAdjList = new HashMap<>();
        Set<String> allNodes = new TreeSet<>();

        String[] edges = line.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length == 2) {
                String u = parts[0].trim();
                String v = parts[1].trim();
                allNodes.add(u);
                allNodes.add(v);
                adjList.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
                revAdjList.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
            }
        }

        Set<String> visited = new HashSet<>();
        LinkedList<String> stack = new LinkedList<>();

        for (String node : allNodes) {
            if (!visited.contains(node)) {
                dfs1(node, adjList, visited, stack);
            }
        }

        visited.clear();
        List<Set<String>> sccList = new ArrayList<>();

        while (!stack.isEmpty()) {
            String node = stack.pop();
            if (!visited.contains(node)) {
                Set<String> component = new TreeSet<>();
                dfs2(node, revAdjList, visited, component);
                sccList.add(component);
            }
        }

        Map<String, Integer> nodeToScc = new HashMap<>();
        for (int i = 0; i < sccList.size(); i++) {
            for (String node : sccList.get(i)) {
                nodeToScc.put(node, i);
            }
        }

        int n = sccList.size();
        List<Set<Integer>> sccAdj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            sccAdj.add(new HashSet<>());
        }

        for (String u : adjList.keySet()) {
            int rootU = nodeToScc.get(u);
            for (String v : adjList.get(u)) {
                int rootV = nodeToScc.get(v);
                if (rootU != rootV) {
                    sccAdj.get(rootU).add(rootV);
                }
            }
        }

        List<Set<String>> sortedScc = topologicalSortComponents(sccList, sccAdj);

        for (Set<String> component : sortedScc) {
            StringBuilder sb = new StringBuilder();
            for (String node : component) {
                sb.append(node);
            }
            System.out.println(sb.toString());
        }
    }

    private static void dfs1(String node, Map<String, List<String>> adjList, Set<String> visited, LinkedList<String> stack) {
        visited.add(node);
        List<String> neighbors = adjList.get(node);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    dfs1(neighbor, adjList, visited, stack);
                }
            }
        }
        stack.push(node);
    }

    private static void dfs2(String node, Map<String, List<String>> revAdjList, Set<String> visited, Set<String> component) {
        visited.add(node);
        component.add(node);
        List<String> neighbors = revAdjList.get(node);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    dfs2(neighbor, revAdjList, visited, component);
                }
            }
        }
    }

    private static List<Set<String>> topologicalSortComponents(List<Set<String>> sccList, List<Set<Integer>> sccAdj) {
        int n = sccList.size();
        int[] inDegree = new int[n];
        for (int i = 0; i < n; i++) {
            for (int neighbor : sccAdj.get(i)) {
                inDegree[neighbor]++;
            }
        }

        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> {
            String firstA = sccList.get(a).iterator().next();
            String firstB = sccList.get(b).iterator().next();
            return firstA.compareTo(firstB);
        });

        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                pq.add(i);
            }
        }

        List<Set<String>> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            int curr = pq.poll();
            result.add(sccList.get(curr));

            for (int neighbor : sccAdj.get(curr)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    pq.add(neighbor);
                }
            }
        }

        return result;
    }
}