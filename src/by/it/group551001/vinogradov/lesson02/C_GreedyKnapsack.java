package by.it.group551001.vinogradov.lesson02;
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

        mergeSort(items, 0, n-1);
        double result = 0;
        for (Item item : items) {
            if (item.weight <= W) {
                W -= item.weight;
                result += item.cost;
            }
            else {
                result += (double) W / item.weight * item.cost;
                break;
            }
        }


        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    private void mergeSort(Item[] items, int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;
            mergeSort(items, l, m);
            mergeSort(items, m + 1, r);
            merge(items, l, m, r);
        }
    }

    private void merge(Item[] items, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        Item[] left = new Item[n1];
        Item[] right = new Item[n2];

        for (int i = 0; i < n1; i++) {
            left[i] = items[l + i];
        }
        for (int j = 0; j < n2; j++) {
            right[j] = items[m + 1 + j];
        }

        int i = 0, j = 0;
        int k = l;

        while (i < n1 && j < n2) {
            if ((double) left[i].cost / left[i].weight >= (double) right[j].cost / right[j].weight) {
                items[k] = left[i];
                i++;
            } else {
                items[k] = right[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            items[k] = left[i];
            i++;
            k++;
        }

        while (j < n2) {
            items[k] = right[j];
            j++;
            k++;
        }
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
            //тут может быть ваш компаратор


            return 0;
        }
    }
}