package by.it.group510902.kudan.lesson14;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SitesB {

    //dsu на массивах, сайту сопоставляем номер и работаем уже с номерами
    private static int[] parent;
    private static int[] size;
    private static int n;

    //номер сайта, если сайт новый то он становится отдельным кластером из одного элемента
    private static int id(Map<String, Integer> map, String site) {
        Integer x = map.get(site);
        if (x != null) {
            return x;
        }

        //массивы закончились, увеличиваем их вдвое
        if (n == parent.length) {
            parent = Arrays.copyOf(parent, n * 2);
            size = Arrays.copyOf(size, n * 2);
        }
        parent[n] = n;
        size[n] = 1;
        map.put(site, n);
        return n++;
    }

    //поиск корня с сокращением пути, все вершины по пути сразу подвешиваем к корню
    private static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    //объединение по размеру, меньшее поддерево подвешиваем к большему
    //вместе с сокращением пути получается почти константа, n log*(n) на все операции
    private static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (size[a] < size[b]) {
            int t = a;
            a = b;
            b = t;
        }
        parent[b] = a;
        size[a] += size[b];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> map = new HashMap<>();

        //начальные массивы небольшие, дальше они сами растут
        parent = new int[16];
        size = new int[16];
        n = 0;

        //читаем пары до строки end, плюс в регулярке надо экранировать
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) {
                break;
            }
            String[] parts = line.split("\\+");
            //сайт связанный сам с собой тоже попадет в dsu, union просто ничего не сделает
            union(id(map, parts[0]), id(map, parts[1]));
        }

        //собираем размеры кластеров из корней
        int[] res = new int[n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (parent[i] == i) {
                res[count++] = size[i];
            }
        }

        //выводим от большего к меньшему как в примере
        Arrays.sort(res, 0, count);
        StringBuilder sb = new StringBuilder();
        for (int i = count - 1; i >= 0; i--) {
            sb.append(res[i]).append(" ");
        }
        System.out.println(sb.toString().trim());
    }
}