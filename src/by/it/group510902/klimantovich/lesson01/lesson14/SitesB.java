package by.it.group510902.klimantovich.lesson01.lesson14;
import java.util.*;
public class SitesB {
    //Обход в глубину с эвристиками по рангу и сокращению пути
    static class DSU {
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> rank = new HashMap<>();
        Map<String, Integer> size = new HashMap<>();
        // добавить новый элемент
        void makeSet(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
                size.put(x, 1);
            }
        }
        //найти корень с сокращением пути
        String find(String x) {
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x))); // Path compression
            }
            return parent.get(x);
        }
        // объединить два множества (эвристика по рангу)
        void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);
            if (!rootX.equals(rootY)) {
                // присоединяем дерево с меньшим рангом к большему
                if (rank.get(rootX) < rank.get(rootY)) {
                    parent.put(rootX, rootY);
                    size.put(rootY, size.get(rootY) + size.get(rootX));
                } else if (rank.get(rootX) > rank.get(rootY)) {
                    parent.put(rootY, rootX);
                    size.put(rootX, size.get(rootX) + size.get(rootY));
                } else {
                    parent.put(rootY, rootX);
                    size.put(rootX, size.get(rootX) + size.get(rootY));
                    rank.put(rootX, rank.get(rootX) + 1);
                }
            }
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU();
        //читаем строки, пока не встретим "end"
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) break;
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\+");
            if (parts.length != 2) continue;
            String site1 = parts[0].trim();
            String site2 = parts[1].trim();
            // создаем множества для сайтов, если их еще нет
            dsu.makeSet(site1);
            dsu.makeSet(site2);
            //объединяем
            dsu.union(site1, site2);
        }
        //собираем размеры кластеров
        // нам нужны только корни те, у кого родитель == сам элемент
        List<Integer> sizes = new ArrayList<>();
        for (String site : dsu.parent.keySet()) {
            if (site.equals(dsu.find(site))) {
                sizes.add(dsu.size.get(site));
            }
        }
        //сортируем по убыванию
        sizes.sort(Collections.reverseOrder());
        // выводим
        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            if (i < sizes.size() - 1) System.out.print(" ");
        }
        System.out.println();
    }
}