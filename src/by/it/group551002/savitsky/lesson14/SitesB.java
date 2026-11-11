package by.it.group551002.savitsky.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        int[] parent;
        int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int i) {
            if (parent[i] == i) {
                return i;
            }

            return parent[i] = find(parent[i]);
        }

        public void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI != rootJ) {
                if (size[rootI] < size[rootJ]) {
                    parent[rootI] = rootJ;
                    size[rootJ] += size[rootI];
                } else {
                    parent[rootJ] = rootI;
                    size[rootI] += size[rootJ];
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, Integer> sites = new HashMap<>();
        List<int[]> connections = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.equals("end")) {
                break;
            }

            String[] pair = line.split("\\+");

            String site1 = pair[0].trim();
            String site2 = pair[1].trim();

            if (!sites.containsKey(site1)) {
                sites.put(site1, sites.size());
            }

            if (!sites.containsKey(site2)) {
                sites.put(site2, sites.size());
            }

            int index1 = sites.get(site1);
            int index2 = sites.get(site2);

            connections.add(new int[]{index1, index2});
        }

        int N = sites.size();
        DSU dsu = new DSU(N);

        for (int[] connection : connections){
            dsu.union(connection[0], connection[1]);
        }

        List<Integer> clusterSizes = new ArrayList<>();

        for (int i = 0; i < N; i++){
            if (dsu.find(i) == i){
                clusterSizes.add(dsu.size[i]);
            }
        }
        clusterSizes.sort(Collections.reverseOrder());

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < clusterSizes.size(); i++) {
            result.append(clusterSizes.get(i));

            if (i < clusterSizes.size() - 1) {
                result.append(" ");
            }
        }

        System.out.println(result);
    }
}
