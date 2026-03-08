package by.it.group551001.docenko.lesson02;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class C_GreedyKnapsack {
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)", costFinal, finishTime - startTime);
    }

    private void sortItems(Item[] items) {
        if (items.length < 2) {
            return;
        }

        int n = 0;
        int m = items.length / 2;
        Item mid = items[m];

        for (Item cur : items) {
            if (cur.compareTo(mid) < 0)
                n += 1;
        }

        Item[] arr1 = new Item[n];
        Item[] arr2 = new Item[items.length-n-1];

        int i;
        int j = 0;
        int k = 0;
        for (i = 0; i < items.length; i++) {
            Item cur = items[i];
            if (cur.compareTo(mid) < 0) {
                arr1[j] = cur;
                j += 1;
            } else if (i != m) {
                arr2[k] = cur;
                k += 1;
            }
        }

        this.sortItems(arr1);
        this.sortItems(arr2);

        for (i = 0; i < n; i++) {
            items[i] = arr1[i];
        }
        items[n] = mid;
        for (i = n+1; i < items.length; i++) {
            items[i] = arr2[i-n-1];
        }
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


        double result = 0;
        int i = n-1;

        this.sortItems(items);

        for (Item item : items) {
            System.out.println(item);
        }

        while (W > 0 && i >= 0) {
            Item cur = items[i];

            if (cur.weight > W) {
                result += ((double) cur.cost / cur.weight) * W;
                W = 0;
            } else {
                result += cur.cost;
                W -= cur.weight;
            }

            i -= 1;
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
            return Double.compare( (double) this.cost / this.weight, (double) o.cost / o.weight);
        }
    }
}