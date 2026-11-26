package by.it.group551001.bochilo.lesson14;

import java.util.*;

public class PointsA {

    private static int[] parent;
    private static int[] size;

    private static int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    private static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) return;
        if (size[a] < size[b]) { int t = a; a = b; b = t; }
        parent[b] = a;
        size[a] += size[b];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int distance = scanner.nextInt();
        int n = scanner.nextInt();
        int[][] points = new int[n][3];
        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }

        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double d = Math.hypot(Math.hypot(points[i][0] - points[j][0], points[i][1] - points[j][1]),
                        points[i][2] - points[j][2]);
                if (d < distance) union(i, j);
            }
        }

        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (find(i) == i) sizes.add(size[i]);
        }
        sizes.sort(Comparator.reverseOrder());
        StringJoiner out = new StringJoiner(" ");
        for (int s : sizes) out.add(String.valueOf(s));
        System.out.println(out);
    }
}
