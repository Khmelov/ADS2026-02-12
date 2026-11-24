package by.it.group551001.bogush.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class SitesB {

    static int[] parent;
    static int[] size;

    // поиск корня со сжатием пути
    static int find(int x) {
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
        // второй проход: все вершины пути вешаем прямо на корень
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    // объединение по размеру поддерева
    static void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) {
            return;
        }
        if (size[ra] < size[rb]) {
            parent[ra] = rb;
            size[rb] += size[ra];
        } else {
            parent[rb] = ra;
            size[ra] += size[rb];
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // читаем все пары до "end"
        ArrayList<String> lines = new ArrayList<>();
        while (in.hasNextLine()) {
            String line = in.nextLine().trim();
            if (line.equals("end")) {
                break;
            }
            if (line.length() == 0) {
                continue;
            }
            lines.add(line);
        }

        // в каждой строке максимум 2 новых сайта
        int maxSites = lines.size() * 2;
        parent = new int[maxSites];
        size = new int[maxSites];

        HashMap<String, Integer> ids = new HashMap<>();
        int count = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int plus = line.indexOf('+');
            String left = line.substring(0, plus);
            String right = line.substring(plus + 1);

            int a;
            if (ids.containsKey(left)) {
                a = ids.get(left);
            } else {
                a = count;
                ids.put(left, a);
                parent[a] = a;
                size[a] = 1;
                count++;
            }

            int b;
            if (ids.containsKey(right)) {
                b = ids.get(right);
            } else {
                b = count;
                ids.put(right, b);
                parent[b] = b;
                size[b] = 1;
                count++;
            }

            union(a, b);
        }

        // размеры кластеров = размеры корней
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (find(i) == i) {
                result.add(size[i]);
            }
        }

        Collections.sort(result);
        Collections.reverse(result);   // убери эту строку, если нужен строго порядок возрастания

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(result.get(i));
        }
        System.out.println(sb);
    }
}
