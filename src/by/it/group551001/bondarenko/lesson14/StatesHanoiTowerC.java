package by.it.group551001.bondarenko.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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

        int find(int i) {
            int root = i;
            while (root != parent[root]) {
                root = parent[root];
            }
            int curr = i;
            while (curr != root) {
                int nxt = parent[curr];
                parent[curr] = root;
                curr = nxt;
            }
            return root;
        }

        void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                if (size[rootI] < size[rootJ]) {
                    parent[rootI] = rootJ;
                    size[rootJ] += size[rootI];
                } else {
                    parent[rootJ] = rootI;
                    size[rootI] += size[rootJ];
                    if (size[rootI] == size[rootJ]) {
                        size[rootI]++;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String s = reader.readLine();
        if (s == null || s.trim().isEmpty()) {
            return;
        }
        int n = Integer.parseInt(s.trim());

        int totalMoves = (1 << n) - 1;
        int[] maxHeight = new int[totalMoves + 1];

        int[] tHeights = new int[4];
        tHeights[1] = n;
        tHeights[2] = 0;
        tHeights[3] = 0;

        int[] pos = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            pos[i] = 1;
        }

        int dir = (n % 2 == 0) ? 1 : -1;

        for (int step = 1; step <= totalMoves; step++) {
            int disk;
            int from, to;
            if (step % 2 != 0) {
                disk = 1;
                from = pos[1];
                if (n % 2 != 0) {
                    to = (from % 3) + 1;
                } else {
                    to = ((from - 2 + 3) % 3) + 1;
                }
            } else {
                int tempStep = step;
                disk = 1;
                while (tempStep % 2 == 0) {
                    disk++;
                    tempStep /= 2;
                }
                from = pos[disk];
                if (n % 2 != 0) {
                    to = 6 - from - ((from + (disk % 2 == 0 ? 1 : 2) - 1) % 3 + 1);
                } else {
                    to = 6 - from - ((from + (disk % 2 == 0 ? 2 : 1) - 1) % 3 + 1);
                }
                if (to < 1 || to > 3 || to == from) {
                    int alt = 1;
                    while (alt == from || alt == getThird(from, getOther(from, disk))) {
                        alt++;
                    }
                    to = 6 - from - alt;
                }
            }

            int hA = tHeights[1];
            int hB = tHeights[2];
            int hC = tHeights[3];
            int maxH = hA;
            if (hB > maxH) maxH = hB;
            if (hC > maxH) maxH = hC;
            maxHeight[step] = maxH;

            tHeights[from]--;
            tHeights[to]++;
            pos[disk] = to;
        }

        DSU dsu = new DSU(totalMoves + 1);
        int[] lastSeenAtHeight = new int[n + 2];
        for (int i = 1; i <= n + 1; i++) {
            lastSeenAtHeight[i] = -1;
        }

        for (int step = 1; step <= totalMoves; step++) {
            int h = maxHeight[step];
            if (lastSeenAtHeight[h] != -1) {
                dsu.union(lastSeenAtHeight[h], step);
            } else {
                lastSeenAtHeight[h] = step;
            }
        }

        int[] sizes = new int[totalMoves + 1];
        int count = 0;
        for (int i = 1; i <= totalMoves; i++) {
            if (dsu.parent[i] == i) {
                sizes[count++] = dsu.size[i];
            }
        }

        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (sizes[i] > sizes[j]) {
                    int tmp = sizes[i];
                    sizes[i] = sizes[j];
                    sizes[j] = tmp;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(sizes[i]).append(" ");
        }
        System.out.println(sb.toString().trim());
    }

    private static int getOther(int from, int disk) {
        return 0;
    }

    private static int getThird(int a, int b) {
        return 6 - a - b;
    }
}