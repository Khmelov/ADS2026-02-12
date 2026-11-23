package by.it.group551002.sevkovich.lesson14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StatesHanoiTowerC {

    private static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n + 1];
            size = new int[n + 1];
            for (int i = 1; i <= n; i++) {
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
            if (ra == rb) return;
            if (size[ra] < size[rb]) {
                int t = ra;
                ra = rb;
                rb = t;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
        }
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        int n = fs.nextInt();
        if (n <= 0) {
            System.out.println();
            return;
        }

        int total = (1 << n) - 1;
        DSU dsu = new DSU(total);

        int[] last = new int[n + 1];
        for (int i = 0; i <= n; i++) last[i] = -1;

        int[] h = new int[3];
        h[0] = n;
        h[1] = 0;
        h[2] = 0;

        int[] step = new int[1];
        step[0] = 0;
        solve(n, 0, 1, 2, h, dsu, last, step);

        int count = 0;
        for (int i = 1; i <= total; i++) {
            if (dsu.find(i) == i) {
                count++;
            }
        }
        int[] sizes = new int[count];
        int idx = 0;
        for (int i = 1; i <= total; i++) {
            if (dsu.find(i) == i) {
                sizes[idx++] = dsu.size[i];
            }
        }
        for (int i = 1; i < sizes.length; i++) {
            int key = sizes[i];
            int j = i - 1;
            while (j >= 0 && sizes[j] > key) {
                sizes[j + 1] = sizes[j];
                j--;
            }
            sizes[j + 1] = key;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes[i]);
        }
        System.out.println(sb.toString());
    }

    private static void solve(int k, int from, int to, int aux, int[] h, DSU dsu, int[] last, int[] step) {
        if (k == 0) return;
        solve(k - 1, from, aux, to, h, dsu, last, step);

        h[from]--;
        h[to]++;
        step[0]++;
        int s = step[0];
        int m = h[0];
        if (h[1] > m) m = h[1];
        if (h[2] > m) m = h[2];
        if (last[m] != -1) {
            dsu.union(last[m], s);
        } else {
            last[m] = s;
        }

        solve(k - 1, aux, to, from, h, dsu, last, step);
    }

    private static class FastScanner {
        private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        private StringBuilder sb = new StringBuilder();
        private int idx = 0;

        int nextInt() throws IOException {
            while (true) {
                while (idx < sb.length() && Character.isWhitespace(sb.charAt(idx))) idx++;
                if (idx < sb.length()) {
                    int sign = 1;
                    if (sb.charAt(idx) == '-') {
                        sign = -1;
                        idx++;
                    } else if (sb.charAt(idx) == '+') {
                        idx++;
                    }
                    int val = 0;
                    int start = idx;
                    while (idx < sb.length() && Character.isDigit(sb.charAt(idx))) {
                        val = val * 10 + (sb.charAt(idx) - '0');
                        idx++;
                    }
                    if (idx > start) return sign * val;
                }
                String line = br.readLine();
                if (line == null) return 0;
                sb = new StringBuilder(line);
                idx = 0;
            }
        }
    }
}