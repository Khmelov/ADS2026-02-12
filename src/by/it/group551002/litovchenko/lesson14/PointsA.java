package by.it.group551002.litovchenko.lesson14;

import java.util.*;

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextDouble() && !scanner.hasNextInt()) {
            return;
        }

        double distance = scanner.nextDouble();
        int point_num = scanner.nextInt();

        int[][] points = new int[point_num][3];
        for (int i = 0; i < point_num; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }

        DSU dsu = new DSU(point_num);

        for (int i = 0; i < point_num; i++) {
            for (int j = i + 1; j < point_num; j++) {
                int dx = points[i][0] - points[j][0];
                int dy = points[i][1] - points[j][1];
                int dz = points[i][2] - points[j][2];
                double dist = Math.hypot(Math.hypot(dx, dy), dz);
                if (dist < distance) {
                    dsu.union(i, j);
                }
            }
        }

        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < point_num; i++) {
            if (dsu.isRoot(i)) {
                clusterSizes.add(dsu.getSize(i));
            }
        }

        clusterSizes.sort((a, b) -> b - a);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(clusterSizes.get(i));
        }
        System.out.println(sb);
    }
}
