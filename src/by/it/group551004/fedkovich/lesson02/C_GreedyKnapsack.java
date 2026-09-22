package by.it.group551004.fedkovich.lesson02;
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

// we are given the input:
// 4 60
// 60 20
// 100 50
// 120 30
// 100 50
// 
// number of items: 4
// maximum weight of backpack: 60
// and pairs of cost and weight
//
//
// cost and weight both seem like attributes we would like to minimize.
// since we are given a total capacity of the backpack, my guess is that
// we have to minimize the weight and maximize the cost (value?).
//
// Необходимо собрать наиболее дорогой вариант рюкзака для этого объема
// Предметы можно резать на кусочки (т.е. алгоритм будет жадным)
//
// We need to create a the most expensive variant of the backpack while
// being below it's capcity. We can divide items into fractions.
//
// So this is fractional knapsack problem. The solution for such a problem is
// to first find the ratios of the weight compared to the cost (cost/weight),
// then we add the largest fraction first, and then when we don't have the space
// to add the last item we take a fractional amount of it.

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
        int n = input.nextInt(); // сколько предметов в файле
        int W = input.nextInt(); // какой вес у рюкзака
        Item[] items = new Item[n]; // получим список предметов

        for (int i = 0; i < n; i++) { // создавая каждый конструктором
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        // покажем предметы
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        int i;
        double fracAmount;
        double result = 0;
        int currentWeight = 0;

        // sort in descending order by cost relative to weight
        Arrays.sort(items, Collections.reverseOrder());

        i = 0;
        while (i < items.length && items[i].weight + currentWeight < W) {
            currentWeight += items[i].weight;
            result += items[i].cost;
            ++i;
        }

        double newWeight = 0;
        if (currentWeight < W && i < items.length) {
            fracAmount = (double) (W - currentWeight) / items[i].weight;
            result += items[i].cost * fracAmount;

            currentWeight = W;
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
            long otherCost, thisCost;

            otherCost = (long) o.cost * this.weight;
            thisCost = (long) this.cost * o.weight;

            return Long.compare(thisCost, otherCost);
        }
    }
}
