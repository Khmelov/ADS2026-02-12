package by.it.group551003.tarasau.lesson02;
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
        //тут реализуйте алгоритм сбора рюкзака
        //будет особенно хорошо, если с собственной сортировкой
        //кроме того, можете описать свой компаратор в классе Item

        //ваше решение.
        ArrayList<Item> itemList = new ArrayList<>(Arrays.asList(items));

        stillAQuickSortAttempt(itemList, 0, itemList.size()-1);

        int weightLeft = W;
        for (int i = 0; i < itemList.size(); i++) {

            if (itemList.get(i).weight == weightLeft) {
                weightLeft = weightLeft - itemList.get(i).weight;
                result += itemList.get(i).cost;
                break;
            }

            if (itemList.get(i).weight > weightLeft) {
                result += itemList.get(i).getPartedCost(weightLeft);
                weightLeft = 0;
                break;
            }

            if (itemList.get(i).weight < weightLeft) {
                weightLeft = weightLeft - itemList.get(i).weight;
                result += itemList.get(i).cost;
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    void stillAQuickSortAttempt(ArrayList<Item> items, int begin, int end) {
        if (begin < end) {
            int p = partition(items, begin, end);
            stillAQuickSortAttempt(items, begin, p-1);
            stillAQuickSortAttempt(items , p+1, end);
        }
    }

    int partition(ArrayList<Item> items, int begin, int end) {
        int i = begin;
        int j = end+1;
        Item pivot = items.get(begin);

        while (true) {
            while (items.get(++i).cwRatio > pivot.cwRatio) {
                if (i == end) break;
            }

            while (items.get(--j).cwRatio < pivot.cwRatio) {
                if (j == begin) break;
            }

            if (i >= j) break;

            swap(items, i, j);
        }

        swap(items, begin, j);

        return j;
    }

    void swap(ArrayList<Item> items, int i, int j) {
        Item temp = items.get(i);
        items.set(i, items.get(j));
        items.set(j, temp);
    }

    private static class Item implements Comparable<Item> {
        int cost;
        int weight;
        double cwRatio;  // cost-weight ratio

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
            this.cwRatio = (double)cost / (double)weight;
        }

        public double getPartedCost(int weight) {
            return ((double)weight / this.weight) * this.cost;
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