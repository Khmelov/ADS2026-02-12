
package by.it.group551004.sheev.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);

            if (ra != rb) {
                if (size[ra] < size[rb]) {
                    int t = ra;
                    ra = rb;
                    rb = t;
                }

                parent[rb] = ra;
                size[ra] += size[rb];
            }
        }
    }

    static int n;
    static int[] height = new int[3];
    static int[] firstStepOfMaxHeight;
    static DSU dsu;
    static int stepIndex;
    static int totalSteps;
    static StringBuilder out = new StringBuilder();

    static void move(int from, int to) {
        height[from]--;
        height[to]++;

        int maxH = height[0];

        if (height[1] > maxH) {
            maxH = height[1];
        }

        if (height[2] > maxH) {
            maxH = height[2];
        }

        int cur = stepIndex++;

        if (firstStepOfMaxHeight[maxH] == -1) {
            firstStepOfMaxHeight[maxH] = cur;
        } else {
            dsu.union(cur, firstStepOfMaxHeight[maxH]);
        }
    }

    static void hanoi(int k, int from, int to, int aux) {
        if (k > 0) {
            hanoi(k - 1, from, aux, to);
            move(from, to);
            hanoi(k - 1, aux, to, from);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        n = sc.nextInt();

        stepIndex = 0;
        out.setLength(0);

        height[0] = n;
        height[1] = 0;
        height[2] = 0;

        totalSteps = (1 << n) - 1;

        dsu = new DSU(totalSteps);
        firstStepOfMaxHeight = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            firstStepOfMaxHeight[i] = -1;
        }

        hanoi(n, 0, 1, 2);

        int[] sizeCount = new int[totalSteps + 1];
        boolean[] seenRoot = new boolean[totalSteps];

        for (int i = 0; i < totalSteps; i++) {
            int r = dsu.find(i);

            if (!seenRoot[r]) {
                seenRoot[r] = true;
                sizeCount[dsu.size[r]]++;
            }
        }

        boolean first = true;

        for (int s = 1; s <= totalSteps; s++) {
            for (int c = 0; c < sizeCount[s]; c++) {
                if (!first) {
                    out.append(' ');
                }

                out.append(s);
                first = false;
            }
        }

        System.out.println(out);
    }
}
