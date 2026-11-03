package by.it.group510902.klimantovich.lesson01.lesson02;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Даны
1) объем рюкзака 4
2) число возможных предметов 60
3) сам набор предметов
    100 50
    120 30
    100 50
Все это указано в файле (by/it/a_khmelev/lesson02/greedyKnapsack.txt)

Необходимо собрать наиболее дорогой вариант рюкзака для этого объема
Предметы можно резать на кусочки (т.е.  алгоритм будет жадным)
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
        int n = input.nextInt();//сколько предметов в файле
        int W = input.nextInt();//какой вес у рюкзака
        Item[] items = new Item[n];//получим список предметов
        for (int i = 0; i < n; i++) {//создавая каждый конструктором
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        //покажем предметы
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);
        // Решение задачи о непрерывном рюкзаке
        double result = 0;
        int remainingWeight = W;
        // Сортируем предметы по убыванию удельной стоимости (cost/weight)
        Arrays.sort(items);
        // Жадный алгоритм: берем предметы с наибольшей удельной стоимостью
        for (Item item : items) {
            if (remainingWeight <= 0) break;
            if (item.weight <= remainingWeight) {
                // Берем предмет целиком
                result += item.cost;
                remainingWeight -= item.weight;
                System.out.printf("Берем целиком %s\n", item);
            } else {
                // Берем часть предмета
                double fraction = (double) remainingWeight / item.weight;
                result += item.cost * fraction;
                System.out.printf("Берем %.2f от %s (вес %d, стоимость %.2f)\n",
                        fraction, item, remainingWeight, item.cost * fraction);
                remainingWeight = 0;
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
                    ", valuePerKg=" + String.format("%.2f", (double)cost/weight) +
                    '}';
        }
        @Override
        public int compareTo(Item o) {
            // Сортировка по убыванию удельной стоимости (cost/weight)
            double thisValuePerKg = (double) this.cost / this.weight;
            double otherValuePerKg = (double) o.cost / o.weight;
            // Для сортировки по убыванию
            if (thisValuePerKg > otherValuePerKg) {
                return -1;
            } else if (thisValuePerKg < otherValuePerKg) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}