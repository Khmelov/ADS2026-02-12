package by.it.group551002.liashkevich.lesson14;

import java.util.*;

public class PointsA {

    private final Random random = new Random();
    private final int[] parent;

    public PointsA(int N) {
        parent = new int[N];
        for (int i = 0; i < N; i++)
            parent[i] = i;
    }

    public void makeSet(int x) {
        parent[x] = x;
    }

    public int find(int x) {
        if (parent[x] == x)
            return x;
        return parent[x] = find(parent[x]);
    }

    public void unite(int x, int y) {
        x = find(x);
        y = find(y);
        if (x == y)
            return;
        if (random.nextBoolean()) {
            int temp = x;
            x = y;
            y = temp;
        }
        parent[x] = y;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String line = input.nextLine();
        String[] tokens = line.split(" ");
        long distanceDoubled = Integer.parseInt(tokens[0]);
        distanceDoubled *= distanceDoubled;
        int numberOfPoints = Integer.parseInt(tokens[1]);
        PointsA dsu = new PointsA(numberOfPoints);
        int[][] points = new int[numberOfPoints][3];
        int[] count = new int[numberOfPoints];
        for (int i = 0; i < numberOfPoints; i++) {
            points[i][0] = input.nextInt();
            points[i][1] = input.nextInt();
            points[i][2] = input.nextInt();
        }
        for (int i = 0; i < numberOfPoints; i++) {
            long dx;
            long dy;
            long dz;
            for (int j = i + 1; j < numberOfPoints; j++) {
                dx = (long) (points[j][0] - points[i][0]) * (points[j][0] - points[i][0]);
                dy = (long) (points[j][1] - points[i][1]) * (points[j][1] - points[i][1]);
                dz = (long) (points[j][2] - points[i][2]) * (points[j][2] - points[i][2]);
                if (distanceDoubled > (dx + dy + dz))
                    dsu.unite(i, j);
            }
        }
        for (int i = 0 ; i < numberOfPoints; i++)
            ++count[dsu.find(i)];
        Arrays.sort(count);
        for (int i = 0; i < numberOfPoints / 2; i++) {
            int temp = count[i];
            count[i] = count[numberOfPoints - 1 - i];
            count[numberOfPoints - 1 - i] = temp;
        }
        for (int i = 0; i < numberOfPoints; i++) {
            if (count[i] != 0)
                System.out.print(count[i] + " ");
        }
    }

}
