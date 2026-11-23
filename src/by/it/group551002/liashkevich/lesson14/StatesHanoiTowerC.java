package by.it.group551002.liashkevich.lesson14;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class StatesHanoiTowerC {

    private final Random random = new Random();
    private final int[] parent;

    public StatesHanoiTowerC(int N) {
        parent = new int[N];
        for (int i = 0; i < N; i++)
            parent[i] = i;
    }

    public void makeSet(int x) {
        parent[x] = x;
    }

    public int find(int x) {
        int root = x;
        while (parent[root] != root)
            root = parent[root];
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
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

    public static void hanoi(int n, int from, int to, int aux, int[] heights, int[] maxHeight, int[] step) {
        if (n == 0)
            return;
        hanoi(n-1, from, aux, to, heights, maxHeight, step);
        heights[from]--;
        heights[to]++;
        maxHeight[step[0]++] = Math.max(Math.max(heights[0], heights[1]), heights[2]);
        hanoi(n-1, aux, to, from, heights, maxHeight, step);
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int numberOfDisks = input.nextInt();
        int numberOfOps = (1 << numberOfDisks) - 1;
        int[] heights = {numberOfDisks, 0, 0};
        int[] maxHeight = new int[numberOfOps];
        int[] step = {0};
        hanoi(numberOfDisks, 0, 1, 2, heights, maxHeight, step);
        StatesHanoiTowerC dsu = new StatesHanoiTowerC(numberOfOps);
        int[] first = new int[numberOfDisks + 1];
        Arrays.fill(first, -1);
        for (int i = 0; i < numberOfOps; i++) {
            int h = maxHeight[i];
            if (first[h] == -1)
                first[h] = i;
            else
                dsu.unite(i, first[h]);
        }
        int[] count = new int[numberOfOps];
        for (int i = 0 ; i < numberOfOps; i++)
            ++count[dsu.find(i)];
        Arrays.sort(count);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfOps; i++) {
            if (count[i] != 0)
                sb.append(count[i]).append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb);
    }

}
