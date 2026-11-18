package by.it.group551003.kravchenkovikk.lesson13;

import java.util.Scanner;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;

public class GraphA {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();

        List<String[]> edges = new ArrayList<>();
        Set<String> vertex = new TreeSet<>();
        String[] rawEdges = input.split(",");
        for (String edge: rawEdges) {
            String[] parts = edge.trim().split("->");
            if (parts.length == 2) {
                String u = parts[0].trim();
                String v = parts[1].trim();
                edges.add(new String[] {u, v});
                vertex.add(u);
                vertex.add(v);
            }
        }
        int count = vertex.size();
        String[] nodes = vertex.toArray(new String[0]);
        boolean[][] adj = new boolean[count][count];
        int[] degree = new int[count];

        boolean[] isVisited = new boolean[count];
        List<String> res = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int next = -1;
            for (int j = 0; j < count; j++) {
                if (!isVisited[j] && degree[j] == 0) {
                    next = j;
                    break;
                }
            }
            if (next == -1) {
                break;
            }
            isVisited[next] = true;
            res.add(nodes[next]);
            for (int j = 0; j < count; j++){
                if (adj[next][j]) {
                    degree[j]--;
                }
            }
        }
        System.out.println(String.join(" ", res));
    }
}
