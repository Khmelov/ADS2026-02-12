package by.it.group551001.Barusevich.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {
    private static int[] parent;
    private static int[] componentSize;
    private static int[] firstByHeight;
    private static int[] heights = new int[3];
    private static int step;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        int disks = scanner.nextInt();
        if (disks <= 0) {
            System.out.println();
            return;
        }
        if (disks >= 31) throw new IllegalArgumentException("N is too large");
        int moves = (1 << disks) - 1;
        parent = new int[moves];
        componentSize = new int[moves];
        firstByHeight = new int[disks + 1];
        Arrays.fill(firstByHeight, -1);
        Arrays.fill(heights, 0);
        heights[0] = disks;
        step = 0;
        move(disks, 0, 1, 2);
        int clusters = 0;
        for (int i = 0; i < moves; i++) if (find(i) == i) clusters++;
        int[] result = new int[clusters];
        int index = 0;
        for (int i = 0; i < moves; i++) if (find(i) == i) result[index++] = componentSize[i];
        Arrays.sort(result);
        StringBuilder output = new StringBuilder();
        for (int value : result) {
            if (output.length() > 0) output.append(' ');
            output.append(value);
        }
        System.out.println(output);
    }

    private static void move(int count, int from, int to, int auxiliary) {
        if (count == 0) return;
        move(count - 1, from, auxiliary, to);
        heights[from]--;
        heights[to]++;
        registerStep();
        move(count - 1, auxiliary, to, from);
    }

    private static void registerStep() {
        int maximum = Math.max(heights[0], Math.max(heights[1], heights[2]));
        parent[step] = step;
        componentSize[step] = 1;
        int previous = firstByHeight[maximum];
        if (previous < 0) firstByHeight[maximum] = step;
        else union(step, previous);
        step++;
    }

    private static int find(int value) {
        if (parent[value] != value) parent[value] = find(parent[value]);
        return parent[value];
    }

    private static void union(int first, int second) {
        int a = find(first);
        int b = find(second);
        if (a == b) return;
        if (componentSize[a] < componentSize[b]) {
            int temp = a;
            a = b;
            b = temp;
        }
        parent[b] = a;
        componentSize[a] += componentSize[b];
    }
}
