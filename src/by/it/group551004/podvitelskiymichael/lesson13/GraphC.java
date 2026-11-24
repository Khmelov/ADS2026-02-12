package by.it.group551004.podvitelskiymichael.lesson13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                if (!sb.isEmpty()) {
                    sb.append(", ");
                }
                sb.append(line);
            }
        }
        String input = sb.toString();
        if (input.isEmpty()) {
            return;
        }

        Map<String, Set<String>> adj = new HashMap<>();
        Map<String, Set<String>> revAdj = new HashMap<>();
        Set<String> allNodes = new TreeSet<>();

        for (String edge : input.split(",")) {
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

        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();
        for (String node : allNodes) {
            if (!visited.contains(node)) {
                dfsOrder(node, adj, visited, order);
            }
        }

        visited.clear();
        List<List<String>> components = new ArrayList<>();
        Map<String, Integer> compOf = new HashMap<>();

        for (int i = order.size() - 1; i >= 0; i--) {
            String node = order.get(i);
            if (!visited.contains(node)) {
                List<String> comp = new ArrayList<>();
                dfsRev(node, revAdj, visited, comp);
                Collections.sort(comp);
                int index = components.size();
                components.add(comp);
                for (String v : comp) {
                    compOf.put(v, index);
                }
            }
        }

        int k = components.size();
        String[] names = new String[k];
        for (int i = 0; i < k; i++) {
            names[i] = String.join("", components.get(i));
        }

        List<Set<Integer>> dag = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            dag.add(new HashSet<>());
        }
        int[] inDegree = new int[k];

        for (String u : allNodes) {
            int cu = compOf.get(u);
            for (String v : adj.get(u)) {
                int cv = compOf.get(v);
                if (cu != cv && dag.get(cu).add(cv)) {
                    inDegree[cv]++;
                }
            }
        }

        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparing(i -> names[i]));
        for (int i = 0; i < k; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int cur = queue.poll();
            System.out.println(names[cur]);
            for (int next : dag.get(cur)) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    queue.add(next);
                }
            }
        }
    }

    private static void dfsOrder(String u, Map<String, Set<String>> adj, Set<String> visited, List<String> order) {
        visited.add(u);
        for (String v : adj.get(u)) {
            if (!visited.contains(v)) {
                dfsOrder(v, adj, visited, order);
            }
        }
        order.add(u);
    }

    private static void dfsRev(String u, Map<String, Set<String>> revAdj, Set<String> visited, List<String> comp) {
        visited.add(u);
        comp.add(u);
        for (String v : revAdj.get(u)) {
            if (!visited.contains(v)) {
                dfsRev(v, revAdj, visited, comp);
            }
        }
    }
}