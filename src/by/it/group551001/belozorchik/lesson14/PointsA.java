package by.it.group551001.belozorchik.lesson14;

import java.util.Scanner;

public class PointsA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1ая строка - допустимое расстояние D и число точек N
        String firstLine = scanner.nextLine().trim();
        String[] first = firstLine.split(" +");
        double D = Double.parseDouble(first[0]);
        int N = Integer.parseInt(first[1]);

        // координаты точек
        double[] x = new double[N];
        double[] y = new double[N];
        double[] z = new double[N];

        for (int i = 0; i < N; i++) {
            String[] parts = scanner.nextLine().trim().split(" +");
            x[i] = Double.parseDouble(parts[0]);
            y[i] = Double.parseDouble(parts[1]);
            z[i] = Double.parseDouble(parts[2]);
        }

        // parent[i] == i, то i - представитель своего мн-ва
        int[] parent = new int[N];
        // size[i] - размер множества с корнем i
        int[] size = new int[N];
        for (int i = 0; i < N; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // объединение близких точечек
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                double dx = x[i] - x[j];
                double dy = y[i] - y[j];
                double dz = z[i] - z[j];
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (dist < D) {
                    union(i, j, parent, size);
                }
            }
        }

        // сколько точек оказалось в каждом кластере
        int[] clusterSize = new int[N];
        for (int i = 0; i < N; i++) {
            int root = find(i, parent);
            clusterSize[root]++;
        }

        // ненулевые размеры в отдельный М
        int[] sizes = new int[N];
        int k = 0;
        for (int i = 0; i < N; i++) {
            if (clusterSize[i] > 0) {
                sizes[k] = clusterSize[i];
                k++;
            }
        }

        // сортировка - по убыванию
        for (int i = 1; i < k; i++) {
            int current = sizes[i];
            int j = i - 1;
            while (j >= 0 && sizes[j] < current) {
                sizes[j + 1] = sizes[j];
                j--;
            }
            sizes[j + 1] = current;
        }
        // печать
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < k; i++) {
            if (i > 0) {
                out.append(' ');
            }
            out.append(sizes[i]);
        }
        System.out.println(out);
    }

    // поиск представителя мн-ва, кот принад v
    // и сжатие пути
    static int find(int v, int[] parent) {
        int root = v;
        while (parent[root] != root) {
            root = parent[root];
        }
        int current = v;
        while (parent[current] != root) {
            int next = parent[current];
            parent[current] = root;
            current = next;
        }
        return root;
    }

    // объединение двух мн-в
    static void union(int a, int b, int[] parent, int[] size) {
        int rootA = find(a, parent);
        int rootB = find(b, parent);
        if (rootA == rootB) {
            return; // уже в одном множестве
        }
        if (size[rootA] < size[rootB]) {
            int tmp = rootA;
            rootA = rootB;
            rootB = tmp;
        }
        parent[rootB] = rootA;
        size[rootA] += size[rootB];
    }
}
