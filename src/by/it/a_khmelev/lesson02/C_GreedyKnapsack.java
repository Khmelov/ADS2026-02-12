package by.it.a_khmelev.lesson02;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

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
        int n = input.nextInt();      //сколько предметов в файле
        int W = input.nextInt();      //какой вес у рюкзака
        Item[] items = new Item[n];   //получим список предметов
        for (int i = 0; i < n; i++) { //создавая каждый конструктором
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        //покажем предметы
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        //тут необходимо реализовать решение задачи
        //итогом является максимально возможная стоимость вещей в рюкзаке
        //вещи можно резать на кусочки (непрерывный рюкзак)
        double result = 0;

        // Жадный алгоритм для непрерывного рюкзака
        // Сортируем предметы по убыванию удельной стоимости (cost/weight)
        Arrays.sort(items);

        int remaining = W; // оставшийся вес рюкзака
        for (Item item : items) {
            if (remaining == 0) break; // рюкзак заполнен

            if (item.weight <= remaining) {
                // предмет помещается целиком
                result += item.cost;
                remaining -= item.weight;
            } else {
                // берём часть предмета
                result += (double) item.cost * remaining / item.weight;
                remaining = 0;
                break;
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

        @Override
        public String toString() {
            return "Item{" +
                    "cost=" + cost +
                    ", weight=" + weight +
                    '}';
        }
        @Override
        public int compareTo(Item o) {
            // Сортировка по убыванию удельной стоимости (cost/weight)
            double thisRatio = (double) this.cost / this.weight;
            double otherRatio = (double) o.cost / o.weight;
            return Double.compare(otherRatio, thisRatio); // большее значение thisRatio даёт отрицательный результат
        }
    }
}