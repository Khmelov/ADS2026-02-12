package by.it.group551003.kravchenkovikk.lesson14;

import java.util.Scanner;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class PointsA {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        double d = scan.nextDouble();
        int n = scan.nextInt();
        Point[] arrayOfPoints = new Point[n];
        for (int i = 0; i < n; i++) {
            double x = scan.nextDouble();
            double y = scan.nextDouble();
            double z = scan.nextDouble();
            arrayOfPoints[i] = new Point(x, y, z);
        }
        DSU dsu = new DSU(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = arrayOfPoints[i].x - arrayOfPoints[j].x;
                double dy = arrayOfPoints[i].y - arrayOfPoints[j].y;
                double dz = arrayOfPoints[i].z - arrayOfPoints[j].z;
                double distInSq = dx * dx + dy * dy + dz * dz;
                double dist = Math.sqrt(distInSq);
                if (dist < d) {
                    dsu.union(i, j);
                }
            }
        }

        List<Integer> resultList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (dsu.parent[i] == i) {
                resultList.add(dsu.size[i]);
            }
        }
        Collections.sort(resultList);
        Collections.reverse(resultList);
        for (int i = 0; i < resultList.size(); i++) {
            System.out.print(resultList.get(i));
            if (i != resultList.size() - 1) {
                System.out.print(" ");
            }
        }
        scan.close();
    }


    static class Point {
        double x, y, z;

        Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

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
            parent[i] = find(parent[i]);
            return parent[i];
        }

        void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA == rootB) {
                return;
            }
            if (size[rootA] > size[rootB]) {
                parent[rootB] = rootA;
                size[rootA] += size[rootB];
            } else {
                parent[rootA] = rootB;
                size[rootB] += size[rootA];
            }
        }
    }
}