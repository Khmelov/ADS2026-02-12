package by.it.group551001.maxim_belyak.lesson13;


import java.util.*;

public class GraphC {

    static Map<String, List<String>> graph = new TreeMap<>();
    static Map<String, List<String>> reverseGraph = new TreeMap<>();

    static Set<String> visited = new HashSet<>();
    static List<String> order = new ArrayList<>();


    static void dfs1(String vertex) {
        visited.add(vertex);

        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfs1(neighbor);
            }
        }

        order.add(vertex);
    }


    static void dfs2(String vertex, List<String> component) {
        visited.add(vertex);
        component.add(vertex);

        for (String neighbor : reverseGraph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfs2(neighbor, component);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();

        String[] edges = input.split(",");

        // Строим исходный и обратный графы
        for (String edge : edges) {
            String[] vertices = edge.trim().split("\\s*->\\s*");

            String from = vertices[0];
            String to = vertices[1];

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            reverseGraph.putIfAbsent(from, new ArrayList<>());
            reverseGraph.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);
            reverseGraph.get(to).add(from);
        }

        // Первый проход DFS
        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfs1(vertex);
            }
        }

        // Подготавливаем второй проход
        visited.clear();

        List<List<String>> components = new ArrayList<>();
        Map<String, Integer> componentOf = new HashMap<>();

        // Второй проход DFS в обратном графе
        Collections.reverse(order);

        for (String vertex : order) {
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();

                dfs2(vertex, component);

                // Сортируем вершины компоненты по алфавиту
                Collections.sort(component);

                int id = components.size();

                for (String v : component) {
                    componentOf.put(v, id);
                }

                components.add(component);
            }
        }

        // Строим конденсацию графа
        List<Set<Integer>> condensed = new ArrayList<>();
        int[] inDegree = new int[components.size()];

        for (int i = 0; i < components.size(); i++) {
            condensed.add(new HashSet<>());
        }

        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                int a = componentOf.get(from);
                int b = componentOf.get(to);

                if (a != b && condensed.get(a).add(b)) {
                    inDegree[b]++;
                }
            }
        }

        // Топологическая сортировка компонент
        PriorityQueue<Integer> queue = new PriorityQueue<>(
                Comparator.comparing(i -> components.get(i).get(0))
        );

        for (int i = 0; i < components.size(); i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int id = queue.poll();

            // Выводим компоненту без пробелов
            System.out.println(String.join("", components.get(id)));

            for (int neighbor : condensed.get(id)) {
                inDegree[neighbor]--;

                if (inDegree[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }
    }
}

