
package by.it.group551002.kurzhalov.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {
    static int[] parent;
    static int[] size;
    static int[] representative;

    static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    static void union(int a, int b) {
        a = find(a);
        b = find(b);

        if (a == b) {
            return;
        }

        if (size[a] < size[b]) {
            int temp = a;
            a = b;
            b = temp;
        }

        parent[b] = a;
        size[a] += size[b];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        if (n < 1 || n > 21) {
            return;
        }

        int totalMoves = (1 << n) - 1;

        parent = new int[totalMoves];
        size = new int[totalMoves];
        representative = new int[n + 1];

        Arrays.fill(representative, -1);

        int[] heights = {n, 0, 0};
        int[] rods = new int[n];

        for (int i = 0; i < n; i++) {
            rods[i] = 0;
        }

        for (int step = 1; step <= totalMoves; step++) {
            int disk = Integer.numberOfTrailingZeros(step);
            int from = rods[disk];
            int to;

            int direction;
            if ((n - disk) % 2 == 1) {
                direction = 1;
            } else {
                direction = -1;
            }

            to = (from + direction + 3) % 3;

            heights[from]--;
            heights[to]++;
            rods[disk] = to;

            int maximum = Math.max(
                    heights[0],
                    Math.max(heights[1], heights[2])
            );

            int current = step - 1;
            parent[current] = current;
            size[current] = 1;

            if (representative[maximum] == -1) {
                representative[maximum] = current;
            } else {
                union(current, representative[maximum]);
            }
        }

        int[] result = new int[totalMoves];
        int count = 0;

        for (int i = 0; i < totalMoves; i++) {
            if (find(i) == i) {
                result[count++] = size[i];
            }
        }

        Arrays.sort(result, 0, count);

        StringBuilder output = new StringBuilder();

        for (int i = 0; i < count; i++) {
            if (i > 0) {
                output.append(' ');
            }
            output.append(result[i]);
        }

        System.out.println(output);
    }
}
