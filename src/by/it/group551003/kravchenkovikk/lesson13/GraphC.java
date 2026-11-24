package by.it.group551003.kravchenkovikk.lesson13;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Set;
import java.util.Collections;
import java.util.Arrays;

public class GraphC {

    private static void dfs1(int u, List<List<Integer>> adj, boolean[] visited, List<Integer> order) {
        visited[u] = true;
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                dfs1(v, adj, visited, order);
            }
        }
        order.add(u);
    }

    private static void dfs2(int u, List<List<Integer>> transposeAdj, boolean[] visited, List<Integer> component) {
        visited[u] = true;
        component.add(u);
        for (int v : transposeAdj.get(u)) {
            if (!visited[v]) {
                dfs2(v, transposeAdj, visited, component);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        List<String[]> edges = new ArrayList<>();
        Set<String> vertex = new TreeSet<>();
        String[] subStr = input.split(",");
        for (String edge: subStr) {
            String[] parts = edge.trim().split("->");
            if (parts.length == 2) {
                String u = parts[0].trim();
                String v = parts[1].trim();
                edges.add(new String[] {u, v});
                vertex.add(u);
                vertex.add(v);
            }
        }
        int n = vertex.size();
        String[] nodes = vertex.toArray(new String[0]);
        List<List<Integer>> adj = new ArrayList<>();
        List<List<Integer>> transposeAdj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
            transposeAdj.add(new ArrayList<>());
        }

        for (String[] edge : edges) {
            int u = Arrays.binarySearch(nodes, edge[0]);
            int v = Arrays.binarySearch(nodes, edge[1]);
            if (!adj.get(u).contains(v)) {
                adj.get(u).add(v);
            }
            if (!transposeAdj.get(v).contains(u)) {
                transposeAdj.get(v).add(u);
            }
        }

        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs1(i, adj, visited, order);
            }
        }

        Arrays.fill(visited, false);
        List<List<String>> sccs = new ArrayList<>();

        for (int i = order.size() - 1; i >= 0; i--) {
            int u = order.get(i);
            if (!visited[u]) {
                List<Integer> compIndices = new ArrayList<>();
                dfs2(u, transposeAdj, visited, compIndices);

                List<String> compStrings = new ArrayList<>();
                for (int idx : compIndices) {
                    compStrings.add(nodes[idx]);
                }
                Collections.sort(compStrings);
                sccs.add(compStrings);
            }
        }
        for (List<String> comp : sccs) {
            StringBuilder sb = new StringBuilder();
            for (String s : comp) {
                sb.append(s);
            }
            System.out.println(sb.toString());
        }
    }
}
