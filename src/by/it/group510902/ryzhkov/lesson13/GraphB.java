package by.it.group510902.ryzhkov.lesson13;

import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;

public class GraphB {
    public static void main(String[] args) {
        String input = new Scanner(System.in).nextLine();
        String[] tokens = input.trim().split("\\s*,\\s*|\\s*->\\s*");

        TreeSet<String> nodes = new TreeSet<>(Arrays.asList(tokens));
        int n = nodes.size();
        boolean[][] adj = new boolean[n][n];
        int[] inDegree = new int[n];

        for (int i = 0; i < tokens.length; i += 2) {
            int u = nodes.headSet(tokens[i]).size();
            int v = nodes.headSet(tokens[i + 1]).size();
            if (!adj[u][v]) {
                adj[u][v] = true;
                inDegree[v]++;
            }
        }

        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (inDegree[j] == 0) {
                    count++;
                    inDegree[j] = -1;
                    for (int k = 0; k < n; k++) {
                        if (adj[j][k])
                            inDegree[k]--;
                    }
                    break;
                }
            }
        }

        System.out.println(count < n ? "yes" : "no");
    }
}
