package by.it.group551001.bondarenko.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class PointsA {

    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]);
        }

        void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                if (size[rootI] < size[rootJ]) {
                    parent[rootI] = rootJ;
                    size[rootJ] += size[rootI];
                } else {
                    parent[rootJ] = rootI;
                    size[rootI] += size[rootJ];
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        StringTokenizer st = new StringTokenizer(line);
        double maxDist = Double.parseDouble(st.nextToken());
        int n = Integer.parseInt(st.nextToken());

        int[][] points = new int[n][3];
        for (int i = 0; i < n; i++) {
            String ptLine = reader.readLine();
            if (ptLine == null || ptLine.trim().isEmpty()) {
                break;
            }
            StringTokenizer ptSt = new StringTokenizer(ptLine);
            points[i][0] = Integer.parseInt(ptSt.nextToken());
            points[i][1] = Integer.parseInt(ptSt.nextToken());
            points[i][2] = Integer.parseInt(ptSt.nextToken());
        }

        DSU dsu = new DSU(n);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = Math.hypot(Math.hypot(points[i][0] - points[j][0], points[i][1] - points[j][1]), points[i][2] - points[j][2]);
                if (dist < maxDist) {
                    dsu.union(i, j);
                }
            }
        }

        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (dsu.parent[i] == i) {
                clusterSizes.add(dsu.size[i]);
            }
        }

        clusterSizes.sort(Collections.reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (int size : clusterSizes) {
            sb.append(size).append(" ");
        }
        System.out.println(sb.toString().trim());
    }
}