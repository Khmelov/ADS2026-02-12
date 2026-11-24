package by.it.group551001.docenko.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;

public class SitesB {

    private static final class Dsu {
        private final int[] parent;
        private final int[] size;

        Dsu(int count) {
            parent = new int[count];
            size = new int[count];
            for (int i = 0; i < count; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int element) {
            if (parent[element] != element) {
                parent[element] = find(parent[element]);
            }
            return parent[element];
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

        List<Integer> clusterSizes() {
            List<Integer> sizes = new ArrayList<>();
            for (int i = 0; i < parent.length; i++) {
                if (parent[i] == i) {
                    sizes.add(size[i]);
                }
            }
            return sizes;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> siteIds = new HashMap<>();
        List<int[]> links = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) {
                break;
            }
            String[] sites = line.split("\\+");
            links.add(new int[]{idOf(sites[0], siteIds), idOf(sites[1], siteIds)});
        }

        Dsu dsu = new Dsu(siteIds.size());
        for (int[] link : links) {
            dsu.union(link[0], link[1]);
        }

        List<Integer> sizes = dsu.clusterSizes();
        sizes.sort(Collections.reverseOrder());
        StringJoiner out = new StringJoiner(" ");
        for (int size : sizes) {
            out.add(String.valueOf(size));
        }
        System.out.println(out);
    }

    private static int idOf(String site, Map<String, Integer> siteIds) {
        Integer id = siteIds.get(site);
        if (id == null) {
            id = siteIds.size();
            siteIds.put(site, id);
        }
        return id;
    }
}
