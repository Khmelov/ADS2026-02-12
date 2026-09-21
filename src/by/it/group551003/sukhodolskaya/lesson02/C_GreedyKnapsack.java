package by.it.group551003.sukhodolskaya.lesson02;
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
Предметы можно резать на кусочки (т.е. алгоритм будет жадным)
 */

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class C_GreedyKnapsack {
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        if (inputStream == null) {
            System.out.println("Файл не найден!");
            return;
        }
        double costFinal = new C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d мс)\n", costFinal, finishTime - startTime);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);

        if (!input.hasNextInt()) return 0;
        int n = input.nextInt();      // количество предметов
        int W = input.nextInt();      // вместимость рюкзака

        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        //от дорогого к дешевому
        Arrays.sort(items);

        double result = 0;
        int currentWeight = 0;

        //берем предметы
        for (Item item : items) {
            if (currentWeight < W) {
                int remainingCapacity = W - currentWeight;

                //если вмещается то берем целиком
                if (item.weight <= remainingCapacity) {
                    result += item.cost;
                    currentWeight += item.weight;
                } else {
                    //режем
                    double fraction = (double) remainingCapacity / item.weight;
                    result += item.cost * fraction;
                    currentWeight = W; //рюкзак забит
                    break;
                }
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
            return String.format("Item{cost=%d, weight=%d, unitPrice=%.2f}",
                    cost, weight, (double) cost / weight);
        }

        @Override
        public int compareTo(Item o) {
            //цена за кг
            double r1 = (double) this.cost / this.weight;
            double r2 = (double) o.cost / o.weight;

            //сортировка от дорогих к дешевым
            return Double.compare(r2, r1);
        }
    }
}