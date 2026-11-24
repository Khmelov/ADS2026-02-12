package by.it.group551002.yorsh.lesson13;
import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String line = scanner.nextLine();

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, List<String>> transpose = new HashMap<>();
        Set<String> nodes = new TreeSet<>();

        String[] edges = line.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            if (edge.isEmpty()) continue;
            String[] parts = edge.split("->");
            String u = parts[0].trim();
            String v = parts[1].trim();

            nodes.add(u);
            nodes.add(v);

            adj.putIfAbsent(u, new ArrayList<>());
            adj.get(u).add(v);

            transpose.putIfAbsent(v, new ArrayList<>());
            transpose.get(v).add(u);
        }

        // Шаг 1: Заполнение стека по времени выхода (Алгоритм Косарайю)
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        List<String> sortedNodes = new ArrayList<>(nodes);

        for (String node : sortedNodes) {
            if (!visited.contains(node)) {
                fillOrder(node, adj, visited, stack);
            }
        }

        // Шаг 2: Обход транспонированного графа
        visited.clear();
        List<List<String>> sccs = new ArrayList<>();

        while (!stack.isEmpty()) {
            String node = stack.pop();
            if (!visited.contains(node)) {
                List<String> scc = new ArrayList<>();
                dfsTranspose(node, transpose, visited, scc);
                Collections.sort(scc); // Лексикографический порядок внутри компоненты
                sccs.add(scc);
            }
        }

        // Отображение вершины на индекс ее компоненты сильной связности (КСВ)
        Map<String, Integer> nodeToScc = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++) {
            for (String node : sccs.get(i)) {
                nodeToScc.put(node, i);
            }
        }

        // Шаг 3: Построение графа конденсации
        int k = sccs.size();
        List<Set<Integer>> condAdj = new ArrayList<>();
        for (int i = 0; i < k; i++) condAdj.add(new HashSet<>());

        for (String u : adj.keySet()) {
            int sccU = nodeToScc.get(u);
            for (String v : adj.get(u)) {
                int sccV = nodeToScc.get(v);
                if (sccU != sccV) {
                    condAdj.get(sccU).add(sccV);
                }
            }
        }

        // Шаг 4: Топологическая сортировка графа конденсации (от истока к стоку)
        int[] inDegree = new int[k];
        for (int i = 0; i < k; i++) {
            for (int neighbor : condAdj.get(i)) {
                inDegree[neighbor]++;
            }
        }

        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < k; i++) {
            if (inDegree[i] == 0) {
                q.add(i);
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            topoOrder.add(u);
            for (int v : condAdj.get(u)) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    q.add(v);
                }
            }
        }

        // Шаг 5: Вывод результатов (без пробелов)
        for (int idx : topoOrder) {
            StringBuilder sb = new StringBuilder();
            for (String node : sccs.get(idx)) {
                sb.append(node);
            }
            System.out.println(sb.toString());
        }
    }

    private static void fillOrder(String u, Map<String, List<String>> adj, Set<String> visited, Stack<String> stack) {
        visited.add(u);
        if (adj.containsKey(u)) {
            List<String> neighbors = new ArrayList<>(adj.get(u));
            Collections.sort(neighbors);
            for (String v : neighbors) {
                if (!visited.contains(v)) {
                    fillOrder(v, adj, visited, stack);
                }
            }
        }
        stack.push(u);
    }

    private static void dfsTranspose(String u, Map<String, List<String>> transpose, Set<String> visited, List<String> scc) {
        visited.add(u);
        scc.add(u);
        if (transpose.containsKey(u)) {
            List<String> neighbors = new ArrayList<>(transpose.get(u));
            Collections.sort(neighbors);
            for (String v : neighbors) {
                if (!visited.contains(v)) {
                    dfsTranspose(v, transpose, visited, scc);
                }
            }
        }
    }
}