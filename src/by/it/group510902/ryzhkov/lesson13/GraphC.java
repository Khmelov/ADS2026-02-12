package by.it.group510902.ryzhkov.lesson13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

public class GraphC {
    public static void main(String[] args) {
        String input = new Scanner(System.in).nextLine();
        String[] tokens = input.trim().split("\\s*,\\s*|\\s*->\\s*");

        TreeSet<String> nodes = new TreeSet<>(Arrays.asList(tokens));
        int n = nodes.size();
        String[] labels = nodes.toArray(new String[0]);

        boolean[][] adj = new boolean[n][n];
        boolean[][] revAdj = new boolean[n][n];

        for (int i = 0; i < tokens.length; i += 2) {
            int u = nodes.headSet(tokens[i]).size();
            int v = nodes.headSet(tokens[i + 1]).size();
            adj[u][v] = true;
            revAdj[v][u] = true;
        }

        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs1(i, adj, visited, order);
            }
        }

        Arrays.fill(visited, false);
        for (int i = order.size() - 1; i >= 0; i--) {
            int u = order.get(i);
            if (!visited[u]) {
                List<String> component = new ArrayList<>();
                dfs2(u, revAdj, visited, component, labels);

                Collections.sort(component);
                System.out.println(String.join("", component));
            }
        }
    }

    private static void dfs1(int u, boolean[][] adj, boolean[] visited, List<Integer> order) {
        visited[u] = true;
        for (int v = 0; v < adj.length; v++) {
            if (adj[u][v] && !visited[v]) {
                dfs1(v, adj, visited, order);
            }
        }
        order.add(u);
    }

    private static void dfs2(int u, boolean[][] revAdj, boolean[] visited, List<String> comp, String[] labels) {
        visited[u] = true;
        comp.add(labels[u]);
        for (int v = 0; v < revAdj.length; v++) {
            if (revAdj[u][v] && !visited[v]) {
                dfs2(v, revAdj, visited, comp, labels);
            }
        }
    }
}
