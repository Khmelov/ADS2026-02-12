package by.it.group510902.makaruk.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        // Читаем строку из консоли
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine();


        // adj — обычные стрелки
        // revAdj — развернутые стрелки (наоборот: куда -> откуда)
        Map<String, List<String>> adj = new HashMap<>();
        Map<String, List<String>> revAdj = new HashMap<>();
        Set<String> vertices = new TreeSet<>(); // TreeSet = все вершины по алфавиту

        if (line != null && !line.trim().isEmpty()) {
            for (String part : line.split(",")) {
                String[] sides = part.split("->");
                if (sides.length != 2) continue;

                String from = sides[0].trim();          // кто
                String to = sides[1].trim();            // куда

                // В обычный граф: from -> to
                adj.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                // В обратный граф: to -> from (наоборот!)
                revAdj.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
                // Запоминаем обе вершины
                vertices.add(from);
                vertices.add(to);
            }
        }

        // проход по обычным стрелкам
        List<String> order = new ArrayList<>();
        Set<String> visited = new HashSet<>();  // кого уже видели

        for (String v : vertices) {             // перебираем все вершины по алфавиту
            if (!visited.contains(v)) {         // если ещё не были здесь
                dfs1(v, adj, visited, order);   // обходим
            }
        }
        // Теперь order выглядит так: [тупики, ..., истоки]

        // теперь мы начинаем с тех, кто был "тупиком" в первом проходе, переворот списка
        Collections.reverse(order);

        // проход по развернутым стрелкам
        Set<String> assigned = new HashSet<>();          // кого уже положили в кучку
        List<List<String>> components = new ArrayList<>(); // все кучки

        for (String v : order) {
            if (!assigned.contains(v)) {                 // если вершина ещё не в кучке
                List<String> comp = new ArrayList<>();   // новая кучка
                dfs2(v, revAdj, assigned, comp);         // собираем по развернутым стрелкам
                Collections.sort(comp);                  // сортируем внутри кучки по алфавиту
                components.add(comp);                    // сохраняем кучку
            }
        }
        StringBuilder sb = new StringBuilder();
        for (List<String> comp : components) {
            sb.append(String.join("", comp)).append("\n");
        }
        System.out.print(sb);
    }

    // Первый обход: идем вглубь, пока можем. Добавляем вершину в order ТОЛЬКО когда выходим из неё.
    private static void dfs1(String v, Map<String, List<String>> adj,
                             Set<String> visited, List<String> order) {
        visited.add(v);                                // отметили, что зашли
        for (String to : adj.getOrDefault(v, Collections.emptyList())) {
            if (!visited.contains(to)) {               // если сосед новый
                dfs1(to, adj, visited, order);         // идем в него
            }
        }
        order.add(v);                                  // ВЫХОДИМ из вершины — записываем её
    }

    // Второй обход: собираем всех, кто достижим по РАЗВЕРНУТЫМ стрелкам — это одна компонента.
    private static void dfs2(String v, Map<String, List<String>> revAdj,
                             Set<String> assigned, List<String> comp) {
        assigned.add(v);                               // отметили, что вершина в кучке
        comp.add(v);                                   // добавили в кучку
        for (String to : revAdj.getOrDefault(v, Collections.emptyList())) {
            if (!assigned.contains(to)) {              // если сосед ещё не в кучке
                dfs2(to, revAdj, assigned, comp);      // идем в него
            }
        }
    }
}