package by.it.group510901.chernyavskaya.lesson14;
//нужно объединить в группы точки, находящиеся ближе заданного расстояния,
// и вывести размеры групп по убыванию.
import java.util.*;

public class PointsA {
    static int[] parent;
    static int[] size;

    static int find(int i) {
        if (parent[i] != i) {
            parent[i] = find(parent[i]);
        }
        return parent[i];
    }

    static void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if (rootA != rootB) {
            if (size[rootA] < size[rootB]) {
                parent[rootA] = rootB;
                size[rootB] += size[rootA];
            } else {
                parent[rootB] = rootA;
                size[rootA] += size[rootB];
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double d = sc.nextDouble();
        int n = sc.nextInt();

        int[][] pts = new int[n][3];
        parent = new int[n];
        size = new int[n];

        for (int i = 0; i < n; i++) {
            pts[i][0] = sc.nextInt();
            pts[i][1] = sc.nextInt();
            pts[i][2] = sc.nextInt();
            parent[i] = i;
            size[i] = 1;
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = pts[i][0] - pts[j][0];
                double dy = pts[i][1] - pts[j][1];
                double dz = pts[i][2] - pts[j][2];
                if (Math.hypot(Math.hypot(dx, dy), dz) < d) {
                    union(i, j);
                }
            }
        }

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (parent[i] == i) {
                res.add(size[i]);
            }
        }

        res.sort(Collections.reverseOrder());
        for (int i = 0; i < res.size(); i++) {
            System.out.print(res.get(i) + (i == res.size() - 1 ? "" : " "));
        }
        System.out.println();
    }
}