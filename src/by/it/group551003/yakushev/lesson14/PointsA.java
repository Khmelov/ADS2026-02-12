package by.it.group551003.yakushev.lesson14;

import java.util.*;

public class PointsA {
    private static int[] parent, rankArr;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double d = sc.nextDouble();
        int n = sc.nextInt();

        int[][] pts = new int[n][3];
        for (int i = 0; i < n; i++) {
            pts[i][0] = sc.nextInt();
            pts[i][1] = sc.nextInt();
            pts[i][2] = sc.nextInt();
        }

        parent = new int[n];
        rankArr = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        double dSq = d * d;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = pts[i][0] - pts[j][0];
                long dy = pts[i][1] - pts[j][1];
                long dz = pts[i][2] - pts[j][2];
                double distSq = dx * dx + dy * dy + dz * dz;
                if (distSq < dSq) {
                    union(i, j);
                }
            }
        }

        Map<Integer, Integer> sizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            sizes.merge(find(i), 1, Integer::sum);
        }

        List<Integer> res = new ArrayList<>(sizes.values());
        res.sort(Collections.reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (int v : res) sb.append(v).append(' ');
        System.out.println(sb.toString().trim());
    }

    private static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    private static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) return;
        if (rankArr[a] < rankArr[b]) parent[a] = b;
        else if (rankArr[a] > rankArr[b]) parent[b] = a;
        else {
            parent[b] = a;
            rankArr[a]++;
        }
    }
}