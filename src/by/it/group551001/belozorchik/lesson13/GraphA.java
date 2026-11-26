package by.it.group551001.belozorchik.lesson13;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        String line = scanner.nextLine().trim();

        // для каждой вершины - список её соседей по исходящим рёбрам
        Map<String, ArrayList<String>> graph = new HashMap<>();
        // для каждой вершины храним количество входящих в неё рёбер
        Map<String, Integer> incoming = new HashMap<>();

        if (!line.isEmpty()) {
            for (String edge : line.split(",")) {
                String[] vertices = edge.split("->");
                String from = vertices[0].trim();
                String to = vertices[1].trim();

                addVertex(graph, incoming, from);
                addVertex(graph, incoming, to);
                //достаёт список соседей т добавляет в него значение
                graph.get(from).add(to);
                incoming.put(to, incoming.get(to) + 1);
            }
        }

        // вершины, кот м вывести сейчас
        // poll() берёт наименьшую
        PriorityQueue<String> ready = new PriorityQueue<>();
        for (String vertex : graph.keySet()) {
            if (incoming.get(vertex) == 0) {
                ready.add(vertex);
            }
        }

        ArrayList<String> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            String vertex = ready.poll();
            order.add(vertex);

            for (String neighbor : graph.get(vertex)) {
                int remaining = incoming.get(neighbor) - 1;
                incoming.put(neighbor, remaining);
                if (remaining == 0) {
                    ready.add(neighbor);
                }
            }
        }

        // при наличии цикла полной топологической сортировки нет
        // усл не задаёт специальный вывод для этого случая
        if (order.size() == graph.size()) {
            System.out.println(String.join(" ", order));
        }
    }

    private static void addVertex(Map<String, ArrayList<String>> graph,
                                  Map<String, Integer> incoming, String vertex) {
        if (!graph.containsKey(vertex)) {
            //созд пустой список для соседей
            graph.put(vertex, new ArrayList<>());
            incoming.put(vertex, 0);
        }
    }
}
