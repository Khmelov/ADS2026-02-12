package by.it.group510901.gulchenko.lesson02;
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

        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        //тут необходимо реализовать решение задачи
        //итогом является максимально воможная стоимость вещей в рюкзаке
        //вещи можно резать на кусочки (непрерывный рюкзак)
        double result = 0;
        //тут реализуйте алгоритм сбора рюкзака
        //будет особенно хорошо, если с собственной сортировкой
        //кроме того, можете описать свой компаратор в классе Item

        //ваше решение.

        sort(items, 0, items.length - 1);

        //покажем предметы
        for (Item item : items) {
            System.out.println(item);
        }

        int weigth = W;
        for (Item item : items) {
            if (item.weight > weigth) {
                result += weigth * specificWeigh(item);
                break;
            }

            result += item.cost;
            weigth -= item.weight;
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    private static int quickSort(Item[] items, int leftBorder, int rightBorder) {
        Item pivot = items[(leftBorder + rightBorder) / 2];
        int left = leftBorder;
        int rigth = rightBorder;

        while (true) {
            while (pivot.compareTo(items[left]) > 0) {
                left++;
            }
            while (pivot.compareTo(items[rigth]) < 0) {
                rigth--;
            }

            if (left >= rigth) {
                return rigth;
            }

            if (left < rigth) {
                Item tempItem = items[left];
                items[left] = items[rigth];
                items[rigth] = tempItem;
                left++;
                rigth--;
            }
        }
    }

    private static void sort(Item[] items, int leftBorder, int rightBorder) {
        int pivot = quickSort(items, leftBorder, rightBorder);
        if (leftBorder >= rightBorder) {
            return;
        }

        sort(items, leftBorder, pivot);
        sort(items, pivot + 1, rightBorder);
    }

    private static double specificWeigh(Item item) {
        double weight = item.weight;
        double cost = item.cost;
        // удельный вес
        return cost / weight;
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
            double thisValue = specificWeigh(this);
            double valueO = specificWeigh(o);

            return (thisValue == valueO) ? 0 : (thisValue > valueO ? -1 : 1);
        }
    }
}