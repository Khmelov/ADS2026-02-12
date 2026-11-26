package by.it.group551001.romanovich.lesson14;

import java.util.Locale;
import java.util.Scanner;

public class PointsA {
    static DisjointSetUnion cluster(int[][] points, double distance) {
        if (!Double.isFinite(distance) || distance < 0) {
            throw new IllegalArgumentException("Distance must be finite and nonnegative");
        }
        DisjointSetUnion groups = new DisjointSetUnion(points.length);
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null || points[i].length != 3) {
                throw new IllegalArgumentException("Expected three coordinates per point");
            }
            groups.add();
            for (int j = 0; j < i; j++) {
                double dx = (double) points[i][0] - points[j][0];
                double dy = (double) points[i][1] - points[j][1];
                double dz = (double) points[i][2] - points[j][2];
                if (Math.hypot(Math.hypot(dx, dy), dz) < distance) {
                    groups.union(i, j);
                }
            }
        }
        return groups;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.ROOT);
        double distance = scanner.nextDouble();
        int count = scanner.nextInt();
        if (count < 0) {
            throw new IllegalArgumentException("Negative point count");
        }
        int[][] points = new int[count][3];
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < 3; j++) {
                points[i][j] = scanner.nextInt();
            }
        }
        System.out.println(cluster(points, distance).sizesAsString(true));
    }
}
