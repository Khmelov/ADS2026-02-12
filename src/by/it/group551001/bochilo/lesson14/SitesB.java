package by.it.group551001.bochilo.lesson14;

import java.util.*;

public class SitesB {

    private static final List<Integer> parent = new ArrayList<>();
    private static final List<Integer> size = new ArrayList<>();

    // эвристика сокращения пути
    private static int find(int x) {
        int root = x;
        while (parent.get(root) != root) root = parent.get(root);
        while (parent.get(x) != root) {
            int next = parent.get(x);
            parent.set(x, root);
            x = next;
        }
        return root;
    }

    // эвристика по размеру поддерева
    private static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) return;
        if (size.get(a) < size.get(b)) { int t = a; a = b; b = t; }
        parent.set(b, a);
        size.set(a, size.get(a) + size.get(b));
    }

    private static int indexOf(String site, Map<String, Integer> index) {
        Integer id = index.get(site);
        if (id == null) {
            id = parent.size();
            index.put(site, id);
            parent.add(id);
            size.add(1);
        }
        return id;
    }

    public static void main(String[] args) {
        parent.clear();
        size.clear();
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> index = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) break;
            if (line.isEmpty()) continue;
            String[] pair = line.split("\\+");
            if (pair.length != 2) continue;
            union(indexOf(pair[0].trim(), index), indexOf(pair[1].trim(), index));
        }

        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < parent.size(); i++) {
            if (find(i) == i) sizes.add(size.get(i));
        }
        sizes.sort(Comparator.reverseOrder());
        StringJoiner out = new StringJoiner(" ");
        for (int s : sizes) out.add(String.valueOf(s));
        System.out.println(out);
    }
}
