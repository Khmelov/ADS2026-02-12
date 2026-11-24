package by.it.group551003.kravchenkovikk.lesson13;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Set;
import java.util.Arrays;

public class GraphB {
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
        int count = vertex.size();
        String[] nodes = vertex.toArray(new String[0]);
        boolean[][] adj = new boolean[count][count];
        int[] degree = new int[count];
        for (String[] edge : edges) {
            int uIndex = Arrays.binarySearch(nodes, edge[0]);
            int vIndex = Arrays.binarySearch(nodes, edge[1]);
            if (!adj[uIndex][vIndex]) {
                adj[uIndex][vIndex] = true;
                degree[vIndex]++;
            }
        }
        boolean[] isVisited = new boolean[count];
        int resCount = 0;
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
            resCount++;
            for (int j = 0; j < count; j++){
                if (adj[next][j]) {
                    degree[j]--;
                }
            }
        }
        if (resCount == count) {
            System.out.print("no");
        } else {
            System.out.print("yes");
        }
    }
}
