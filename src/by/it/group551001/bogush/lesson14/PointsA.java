package by.it.group551001.bogush.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class PointsA {

    static int[] parent;
    static int[] size;

    static int find(int x) {
        while (parent[x] != x) {
            x = parent[x];
        }
        return x;
    }

    // объединение по размеру поддерева
    static void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) {
            return;
        }
        if (size[ra] < size[rb]) {
            parent[ra] = rb;
            size[rb] += size[ra];
        } else {
            parent[rb] = ra;
            size[ra] += size[rb];
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int d = in.nextInt();
        int n = in.nextInt();

        int[] x = new int[n];
        int[] y = new int[n];
        int[] z = new int[n];
        parent = new int[n];
        size = new int[n];

        for (int i = 0; i < n; i++) {
            x[i] = in.nextInt();
            y[i] = in.nextInt();
            z[i] = in.nextInt();
            parent[i] = i;
            size[i] = 1;
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = Math.hypot(Math.hypot(x[i] - x[j], y[i] - y[j]), z[i] - z[j]);
                if (dist < d) {
                    union(i, j);
                }
            }
        }

        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (find(i) == i) {
                result.add(size[i]);
            }
        }

        Collections.sort(result);
        Collections.reverse(result);   // тест ждёт по убыванию

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(result.get(i));
        }
        System.out.println(sb);
    }
}
