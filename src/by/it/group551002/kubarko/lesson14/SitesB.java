package by.it.group551002.kubarko.lesson14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SitesB {

    private static class DisjointSet {
        private int[] parent = new int[16];
        private int[] size = new int[16];
        private int count;

        int add() {
            if (count == parent.length) {
                parent = Arrays.copyOf(parent, parent.length * 2);
                size = Arrays.copyOf(size, size.length * 2);
            }
            parent[count] = count;
            size[count] = 1;
            return count++;
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String[]> links = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }
            String[] sites = line.split("\\+", -1);
            if (sites.length != 2 || sites[0].trim().isEmpty() || sites[1].trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid site pair: " + line);
            }
            links.add(new String[]{sites[0].trim(), sites[1].trim()});
        }

        Map<String, Integer> siteIds = new HashMap<>();
        DisjointSet clusters = new DisjointSet();
        for (String[] link : links) {
            int first = siteIds.computeIfAbsent(link[0], ignored -> clusters.add());
            int second = siteIds.computeIfAbsent(link[1], ignored -> clusters.add());
            clusters.union(first, second);
        }

        int[] clusterSizes = new int[siteIds.size()];
        int clusterCount = 0;
        for (int i = 0; i < clusters.count; i++) {
            if (clusters.find(i) == i) {
                clusterSizes[clusterCount++] = clusters.size[i];
            }
        }
        Arrays.sort(clusterSizes, 0, clusterCount);

        StringBuilder result = new StringBuilder();
        for (int i = clusterCount - 1; i >= 0; i--) {
            if (i < clusterCount - 1) {
                result.append(' ');
            }
            result.append(clusterSizes[i]);
        }
        System.out.println(result);
    }
}
