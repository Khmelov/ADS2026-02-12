package by.it.group551003.popko.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class PointsA {
    static class DSU {
        int[] parent;
        int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]);
        }

        public void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                if (size[rootI] < size[rootJ]) {
                    int temp = rootI;
                    rootI = rootJ;
                    rootJ = temp;
                }
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) return;

        StringTokenizer st = new StringTokenizer(line);
        double d = Double.parseDouble(st.nextToken());
        int n = Integer.parseInt(st.nextToken());

        double[][] points = new double[n][3];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(reader.readLine());
            points[i][0] = Double.parseDouble(st.nextToken());
            points[i][1] = Double.parseDouble(st.nextToken());
            points[i][2] = Double.parseDouble(st.nextToken());
        }

        DSU dsu = new DSU(n);
        double dSq = d * d;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = points[i][0] - points[j][0];
                double dy = points[i][1] - points[j][1];
                double dz = points[i][2] - points[j][2];
                if (dx * dx + dy * dy + dz * dz < dSq) {
                    dsu.union(i, j);
                }
            }
        }

        int[] clusterSizes = new int[n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (dsu.parent[i] == i) {
                clusterSizes[count++] = dsu.size[i];
            }
        }

        int[] result = Arrays.copyOf(clusterSizes, count);
        Arrays.sort(result); // Сначала сортируем по возрастанию

        StringBuilder sb = new StringBuilder();
        // Выводим в обратном порядке (по убыванию)
        for (int i = result.length - 1; i >= 0; i--) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(result[i]);
        }
        System.out.println(sb);
    }
}