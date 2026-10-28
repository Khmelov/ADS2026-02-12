package by.it.group510902.klimantovich.lesson01.lesson13;
import java.util.*;
public class GraphA {
    public static void main(String[] args){
        //считываем ввод с консоли
        Scanner scanner = new Scanner(System.in);
        if(!scanner.hasNextLine()) return;
        String input = scanner.nextLine();
        //строим граф, treemap и treeset, чтобы вершины автоматически сортировались по алфавиту
        // еще счетчик входящих ребер
        Map<String, Set<String>> graph = new TreeMap<>();
        Map<String, Integer> inDegree = new TreeMap<>();
        //разделяем строку по запятым, получаем отдельные ребра
        String[] edges = input.split(",");
        for (String edge :edges) {
            //разделяем каждое ребро по стрелке
            String[] parts = edge.split("->");
            if(parts.length != 2) continue;
            // Убираем лишение пробелы вокруг вершин
            String from = parts[0].trim();
            String to = parts[1].trim();
            //добавляем обе вершины даже если 0 входящих ребер
            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);
            // Добавляем ребра в граф
            graph.computeIfAbsent(from, k-> new TreeSet<>()).add(to);
            //инициализируем счетчики входящих степеней
            inDegree.putIfAbsent(to, inDegree.get(to)+1);
        }
        //алгоритм кана, для топологической сортировки, очередь с приоритетом гаранитирует что всегда берется вершина с наименьшим именем
        PriorityQueue<String> queue = new PriorityQueue<>();
        // находим все вершины, в которые не входят ни одно ребро
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()){
            if(entry.getValue()== 0){
                queue.add(entry.getKey());
            }
        }
        List<String> result = new ArrayList<>();
        //пока есть вершины без входяших ребер, извлекаем их
        while (!queue.isEmpty()){
            String current = queue.poll();// берем вершину с наибольшим приоритетом
            result.add(current);
            // Удаляем эту вершину из графа, уменьшая степени входа у ее соседей
            if (graph.containsKey(current)){
                for(String neighbor : graph.get(current)){
                    inDegree.put(neighbor, inDegree.get(neighbor)-1);
                    // если у соседа больше нет входящих ребер, добавляем его в очередь
                    if(inDegree.get(neighbor) == 0){
                        queue.add(neighbor);
                    }
                }
            }
        }
        //выводим результат через пробел
        System.out.println(String.join(" ", result));
    }
}
