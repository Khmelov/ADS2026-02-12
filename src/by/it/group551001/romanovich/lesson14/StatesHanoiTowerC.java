package by.it.group551001.romanovich.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {
    private final int[] heights;
    private final int[] representative;
    private final DisjointSetUnion states;

    private StatesHanoiTowerC(int disks) {
        if (disks < 0 || disks > 30) {
            throw new IllegalArgumentException("The array-based DSU supports 0 to 30 disks");
        }
        heights = new int[]{disks, 0, 0};
        representative = new int[disks + 1];
        Arrays.fill(representative, -1);
        states = new DisjointSetUnion((int) ((1L << disks) - 1));
    }

    private void move(int disks, int from, int to, int spare) {
        if (disks == 0) {
            return;
        }
        move(disks - 1, from, spare, to);
        heights[from]--;
        heights[to]++;
        int height = Math.max(heights[0], Math.max(heights[1], heights[2]));
        int state = states.add();
        if (representative[height] == -1) {
            representative[height] = state;
        } else {
            states.union(representative[height], state);
        }
        move(disks - 1, spare, to, from);
    }

    static int[] clusterSizes(int disks) {
        StatesHanoiTowerC tower = new StatesHanoiTowerC(disks);
        tower.move(disks, 0, 1, 2);
        return tower.states.componentSizes();
    }

    public static void main(String[] args) {
        int disks = new Scanner(System.in).nextInt();
        int[] sizes = clusterSizes(disks);
        StringBuilder output = new StringBuilder();
        for (int size : sizes) {
            if (!output.isEmpty()) {
                output.append(' ');
            }
            output.append(size);
        }
        System.out.println(output);
    }
}
