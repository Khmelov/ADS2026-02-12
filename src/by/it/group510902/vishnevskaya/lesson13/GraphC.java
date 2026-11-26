package by.it.group510902.vishnevskaya.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Проверяем, есть ли входная строка
        if (!scanner.hasNextLine()) {
            return;
        }

        // Считываем строку и убираем лишние пробелы
        String input = scanner.nextLine().trim();

        // Если строка пустая, завершаем программу
        if (input.isEmpty()) {
            return;
        }

        // Обычный граф: храним, куда идут стрелки из каждой вершины
        Map<String, Set<String>> graph = new TreeMap<>();

        // Перевёрнутый граф: храним, откуда приходят стрелки в вершину
        Map<String, Set<String>> reversed = new TreeMap<>();

        // Разделяем строку на отдельные связи
        String[] edges = input.split("\\s*,\\s*");

        // Перебираем все связи
        for (String edge : edges) {

            // Пропускаем пустые элементы
            if (edge.isEmpty()) {
                continue;
            }

            // Разделяем связь на начальную и конечную вершины
            String[] parts = edge.split("\\s*->\\s*");

            // Если связь записана неправильно, пропускаем её
            if (parts.length != 2) {
                continue;
            }

            // Получаем названия вершин
            String from = parts[0].trim();
            String to = parts[1].trim();

            // Добавляем связь в обычный граф
            graph.computeIfAbsent(from, k -> new TreeSet<>()).add(to);

            // Добавляем конечную вершину, даже если из неё нет стрелок
            graph.computeIfAbsent(to, k -> new TreeSet<>());

            // Добавляем обратную связь в перевёрнутый граф
            reversed.computeIfAbsent(to, k -> new TreeSet<>()).add(from);

            // Добавляем начальную вершину в перевёрнутый граф
            reversed.computeIfAbsent(from, k -> new TreeSet<>());
        }

        // Множество уже посещённых вершин
        Set<String> visited = new HashSet<>();

        // Список, в котором храним порядок завершения обхода вершин
        List<String> finishOrder = new ArrayList<>();

        // Перебираем все вершины обычного графа
        for (String vertex : graph.keySet()) {

            // Если вершина ещё не посещена, запускаем обход в глубину
            if (!visited.contains(vertex)) {
                dfs1(vertex, graph, visited, finishOrder);
            }
        }

        // Очищаем посещённые вершины перед вторым обходом
        visited.clear();

        // Список для хранения всех найденных компонент связности
        List<List<String>> components = new ArrayList<>();

        // Проходим по вершинам в обратном порядке завершения обхода
        for (int i = finishOrder.size() - 1; i >= 0; i--) {

            // Берём очередную вершину
            String vertex = finishOrder.get(i);

            // Если вершина ещё не посещена
            if (!visited.contains(vertex)) {

                // Создаём новый список для компоненты
                List<String> component = new ArrayList<>();

                // Запускаем второй обход по перевёрнутому графу
                dfs2(vertex, reversed, visited, component);

                // Сортируем вершины внутри компоненты по алфавиту
                Collections.sort(component);

                // Добавляем найденную компоненту в общий список
                components.add(component);
            }
        }

        // Создаём строку для вывода результата
        StringBuilder sb = new StringBuilder();

        // Перебираем все найденные компоненты
        for (List<String> comp : components) {

            // Записываем вершины одной компоненты подряд
            for (String v : comp) {
                sb.append(v);
            }

            // Переходим на новую строку после каждой компоненты
            sb.append('\n');
        }

        // Выводим результат
        System.out.print(sb);
    }

    // Первый поиск в глубину
    private static void dfs1(String vertex, Map<String, Set<String>> graph,
                             Set<String> visited, List<String> finishOrder) {

        // Отмечаем вершину как посещённую
        visited.add(vertex);

        // Перебираем все вершины, в которые ведут стрелки
        for (String next : graph.getOrDefault(vertex, Collections.emptySet())) {

            // Если следующая вершина ещё не посещена
            if (!visited.contains(next)) {

                // Продолжаем поиск из неё
                dfs1(next, graph, visited, finishOrder);
            }
        }

        // Добавляем вершину в список после завершения её обхода
        finishOrder.add(vertex);
    }

    // Второй поиск в глубину по перевёрнутому графу
    private static void dfs2(String vertex, Map<String, Set<String>> reversed,
                             Set<String> visited, List<String> component) {

        // Отмечаем вершину как посещённую
        visited.add(vertex);

        // Добавляем вершину в текущую компоненту
        component.add(vertex);

        // Перебираем вершины, из которых приходят стрелки
        for (String next : reversed.getOrDefault(vertex, Collections.emptySet())) {

            // Если следующая вершина ещё не посещена
            if (!visited.contains(next)) {

                // Продолжаем поиск по перевёрнутому графу
                dfs2(next, reversed, visited, component);
            }
        }
    }
}