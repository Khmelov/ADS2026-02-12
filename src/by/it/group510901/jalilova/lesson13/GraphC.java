package by.it.group510901.jalilova.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String input = scanner.nextLine();

        // 1. Парсинг графа
        Map<String, Set<String>> adj = new HashMap<>();
        Map<String, Set<String>> revAdj = new HashMap<>();
        Set<String> allVertices = new TreeSet<>(); // Использование TreeSet сразу дает лексикографический порядок базового обхода

        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            if (edge.isEmpty()) continue;
            String[] parts = edge.split("->");
            if (parts.length != 2) continue;

            String u = parts[0].trim();
            String v = parts[1].trim();

            allVertices.add(u);
            allVertices.add(v);

            adj.putIfAbsent(u, new HashSet<>());
            adj.get(u).add(v);

            revAdj.putIfAbsent(v, new HashSet<>());
            revAdj.get(v).add(u);
        }

        // Гарантируем наличие всех вершин в мапах смежности, чтобы избежать NullPointerException
        for (String v : allVertices) {
            adj.putIfAbsent(v, new HashSet<>());
            revAdj.putIfAbsent(v, new HashSet<>());
        }

        //Косарайю
        //Поиск в глубину на исходном графе для заполнения стека
        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();

        //Обходим в обратном лексикографическом порядке вершин, чтобы при инвертировании стека
        //или правильном обходе сохранялся приоритет алфавита
        List<String> sortedVertices = new ArrayList<>(allVertices);
        for (String v : sortedVertices) {
            if (!visited.contains(v)) {
                dfs1(v, adj, visited, order);
            }
        }
        Collections.reverse(order); // Получаем порядок завершения (аналог стека)

        //Поиск в глубину на инвертированном графе
        visited.clear();
        List<List<String>> components = new ArrayList<>();

        for (String v : order) {
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, revAdj, visited, comp);
                // Сортируем вершины внутри компоненты по алфавиту
                Collections.sort(comp);
                components.add(comp);
            }
        }

        // Топологическая сортировка компонент (от истока к стоку)

        // узел — это индекс компоненты
        int n = components.size();
        Map<String, Integer> vertexToCompId = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (String vertex : components.get(i)) {
                vertexToCompId.put(vertex, i);
            }
        }

        List<Set<Integer>> metaAdj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            metaAdj.add(new HashSet<>());
        }
        int[] inDegree = new int[n];

        for (String u : allVertices) {
            int compU = vertexToCompId.get(u);
            for (String v : adj.get(u)) {
                int compV = vertexToCompId.get(v);
                if (compU != compV) {
                    if (metaAdj.get(compU).add(compV)) {
                        inDegree[compV]++;
                    }
                }
            }
        }

        // Используем алгоритм Кана с PriorityQueue для лексикографического порядка самих компонент (по их первой вершине)
        PriorityQueue<Integer> pq = new PriorityQueue<>((id1, id2) ->
                components.get(id1).get(0).compareTo(components.get(id2).get(0))
        );

        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                pq.add(i);
            }
        }

        // Вывод компонент в топологическом порядке
        while (!pq.isEmpty()) {
            int compId = pq.poll();

            // Склеиваем вершины компоненты без пробелов
            StringBuilder sb = new StringBuilder();
            for (String vertex : components.get(compId)) {
                sb.append(vertex);
            }
            System.out.println(sb.toString());

            for (int neighborComp : metaAdj.get(compId)) {
                inDegree[neighborComp]--;
                if (inDegree[neighborComp] == 0) {
                    pq.add(neighborComp);
                }
            }
        }
    }

    private static void dfs1(String u, Map<String, Set<String>> adj, Set<String> visited, List<String> order) {
        visited.add(u);
        // Чтобы обойти соседей детерминированно, сортируем их
        List<String> neighbors = new ArrayList<>(adj.get(u));
        Collections.sort(neighbors);
        for (String v : neighbors) {
            if (!visited.contains(v)) {
                dfs1(v, adj, visited, order);
            }
        }
        order.add(u);
    }

    private static void dfs2(String u, Map<String, Set<String>> revAdj, Set<String> visited, List<String> comp) {
        visited.add(u);
        comp.add(u);
        List<String> neighbors = new ArrayList<>(revAdj.get(u));
        Collections.sort(neighbors);
        for (String v : neighbors) {
            if (!visited.contains(v)) {
                dfs2(v, revAdj, visited, comp);
            }
        }
    }
}

