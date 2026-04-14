package by.it.group551004.kapusta.lesson02;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Даны:
1) объем рюкзака 4
2) число возможных предметов 60
3) сам набор предметов (cost weight)
Предметы можно резать на кусочки — задача дробного рюкзака (жадный алгоритм).
*/

public class C_GreedyKnapsack {

    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)", costFinal, finishTime - startTime);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);

        int n = input.nextInt();      // количество предметов
        int W = input.nextInt();      // вместимость рюкзака

        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        // Покажем предметы
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        // Жадный алгоритм: сортируем по удельной стоимости (cost/weight)
        Arrays.sort(items);

        double result = 0;
        int remaining = W;

        for (Item item : items) {
            if (remaining == 0) break;

            if (item.weight <= remaining) {
                // Берём весь предмет
                result += item.cost;
                remaining -= item.weight;
            } else {
                // Берём часть предмета
                double fraction = (double) remaining / item.weight;
                result += item.cost * fraction;
                remaining = 0;
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    private static class Item implements Comparable<Item> {
        int cost;
        int weight;

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
        }

        // Удельная стоимость
        double value() {
            return (double) cost / weight;
        }

        @Override
        public int compareTo(Item o) {
            // Сортировка по убыванию удельной стоимости
            return Double.compare(o.value(), this.value());
        }

        @Override
        public String toString() {
            return "Item{" +
                    "cost=" + cost +
                    ", weight=" + weight +
                    ", value=" + value() +
                    '}';
        }
    }
}
