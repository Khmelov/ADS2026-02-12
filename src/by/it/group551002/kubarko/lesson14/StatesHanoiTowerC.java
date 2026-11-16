package by.it.group551002.kubarko.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    private static class DisjointSet {
        private final int[] parent;
        private final int[] size;
        private final int[] firstForHeight;

        DisjointSet(int moves, int heightCount) {
            parent = new int[moves + 1];
            size = new int[moves + 1];
            firstForHeight = new int[heightCount];
            for (int i = 0; i < heightCount; i++) {
                firstForHeight[i] = -1;
            }
            for (int i = 0; i <= moves; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int item) {
            if (parent[item] != item) {
                parent[item] = find(parent[item]);
            }
            return parent[item];
        }

        void union(int first, int second) {
            int firstRoot = find(first);
            int secondRoot = find(second);
            if (firstRoot == secondRoot) {
                return;
            }
            if (size[firstRoot] < size[secondRoot]) {
                int swap = firstRoot;
                firstRoot = secondRoot;
                secondRoot = swap;
            }
            parent[secondRoot] = firstRoot;
            size[firstRoot] += size[secondRoot];
        }
    }

    private static int moveNumber;
    private static int[] heights;
    private static DisjointSet states;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int disks = scanner.nextInt();
        if (disks < 0 || disks > 30) {
            throw new IllegalArgumentException("The number of disks must be between 0 and 30");
        }
        if (disks == 0) {
            System.out.println();
            return;
        }

        long moveCount = (1L << disks) - 1;
        if (moveCount > Integer.MAX_VALUE - 1) {
            throw new IllegalArgumentException("The number of Hanoi moves is too large");
        }
        heights = new int[]{disks, 0, 0};
        moveNumber = 0;
        states = new DisjointSet((int) moveCount, disks + 1);
        solve(disks, 0, 1, 2);

        int[] sizes = new int[(int) moveCount];
        int groupCount = 0;
        for (int i = 1; i <= moveNumber; i++) {
            if (states.find(i) == i) {
                sizes[groupCount++] = states.size[i];
            }
        }
        for (int i = 1; i < groupCount; i++) {
            int value = sizes[i];
            int j = i - 1;
            while (j >= 0 && sizes[j] > value) {
                sizes[j + 1] = sizes[j];
                j--;
            }
            sizes[j + 1] = value;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < groupCount; i++) {
            if (i > 0) {
                result.append(' ');
            }
            result.append(sizes[i]);
        }
        System.out.println(result);
    }

    private static void solve(int disks, int from, int to, int auxiliary) {
        if (disks == 0) {
            return;
        }
        solve(disks - 1, from, auxiliary, to);
        heights[from]--;
        heights[to]++;
        registerState();
        solve(disks - 1, auxiliary, to, from);
    }

    private static void registerState() {
        int tallest = Math.max(heights[0], Math.max(heights[1], heights[2]));
        int current = ++moveNumber;
        int previous = states.firstForHeight[tallest];
        if (previous == -1) {
            states.firstForHeight[tallest] = current;
        } else {
            states.union(previous, current);
        }
    }
}
