package by.it.group551001.docenko.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {

    private final int[] parent;
    private final int[] size;
    private final int[] firstStepByHeight;
    private final int[] heights = new int[3];
    private int step;

    private StatesHanoiTowerC(int diskCount) {
        int stepCount = (1 << diskCount) - 1;
        parent = new int[stepCount];
        size = new int[stepCount];
        for (int i = 0; i < stepCount; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        firstStepByHeight = new int[diskCount + 1];
        Arrays.fill(firstStepByHeight, -1);
        heights[0] = diskCount;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int diskCount = scanner.nextInt();
        StatesHanoiTowerC tower = new StatesHanoiTowerC(diskCount);
        tower.moveDisks(diskCount, 0, 1, 2);
        System.out.println(tower.sortedGroupSizes());
    }

    private void moveDisks(int disks, int from, int to, int spare) {
        if (disks == 0) {
            return;
        }
        moveDisks(disks - 1, from, spare, to);
        heights[from]--;
        heights[to]++;
        registerStep();
        moveDisks(disks - 1, spare, to, from);
    }

    private void registerStep() {
        int maxHeight = Math.max(heights[0], Math.max(heights[1], heights[2]));
        if (firstStepByHeight[maxHeight] < 0) {
            firstStepByHeight[maxHeight] = step;
        } else {
            union(firstStepByHeight[maxHeight], step);
        }
        step++;
    }

    private int find(int element) {
        if (parent[element] != element) {
            parent[element] = find(parent[element]);
        }
        return parent[element];
    }

    private void union(int first, int second) {
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

    private String sortedGroupSizes() {
        int[] sizes = new int[firstStepByHeight.length];
        int groupCount = 0;
        for (int firstStep : firstStepByHeight) {
            if (firstStep >= 0) {
                sizes[groupCount++] = size[find(firstStep)];
            }
        }
        Arrays.sort(sizes, 0, groupCount);

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < groupCount; i++) {
            if (i > 0) {
                out.append(' ');
            }
            out.append(sizes[i]);
        }
        return out.toString();
    }
}
