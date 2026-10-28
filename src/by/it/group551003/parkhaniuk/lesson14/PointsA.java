package by.it.group551003.parkhaniuk.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class PointsA {
    private static int[] parent;
    private static int[] size;

    // Эвристика 2: сокращение пути (Path Compression)
    public static int find(int i) {
        if (parent[i] == i) {
            return i;
        }
        return parent[i] = find(parent[i]);
    }

    // Эвристика 1: объединение по размеру поддерева (Union by Size)
    public static void union(int i, int j) {
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

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) return;

        StringTokenizer tokenizer = new StringTokenizer(line);
        if (!tokenizer.hasMoreTokens()) return;

        double D = Double.parseDouble(tokenizer.nextToken());
        int N = Integer.parseInt(tokenizer.nextToken());

        int[][] points = new int[N][3];
        for (int i = 0; i < N; i++) {
            line = reader.readLine();
            if (line == null) break;
            tokenizer = new StringTokenizer(line);
            points[i][0] = Integer.parseInt(tokenizer.nextToken());
            points[i][1] = Integer.parseInt(tokenizer.nextToken());
            points[i][2] = Integer.parseInt(tokenizer.nextToken());
        }

        // Инициализация DSU
        parent = new int[N];
        size = new int[N];
        for (int i = 0; i < N; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // Проверяем все пары точек и объединяем, если расстояние строго меньше D
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                double dx = points[i][0] - points[j][0];
                double dy = points[i][1] - points[j][1];
                double dz = points[i][2] - points[j][2];
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

                if (dist < D) {
                    union(i, j);
                }
            }
        }

        // Собираем размеры кластеров
        int[] clusterSizes = new int[N];
        int clusterCount = 0;
        for (int i = 0; i < N; i++) {
            if (parent[i] == i) {
                clusterSizes[clusterCount++] = size[i];
            }
        }

        // Сортируем размеры по УБЫВАНИЮ (требуется в юнит-тесте)
        for (int i = 1; i < clusterCount; i++) {
            int key = clusterSizes[i];
            int j = i - 1;
            while (j >= 0 && clusterSizes[j] < key) { // Изменено условие на < для сортировки по убыванию
                clusterSizes[j + 1] = clusterSizes[j];
                j--;
            }
            clusterSizes[j + 1] = key;
        }

        // Выводим результат в строку через пробел
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clusterCount; i++) {
            sb.append(clusterSizes[i]);
            if (i < clusterCount - 1) {
                sb.append(" ");
            }
        }
        System.out.println(sb.toString());
    }
}