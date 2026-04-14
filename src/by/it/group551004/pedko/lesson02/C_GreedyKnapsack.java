package by.it.group551004.pedko.lesson02;
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

        //тут необходимо реализовать решение задачи
        //итогом является максимально возможная стоимость вещей в рюкзаке
        //вещи можно резать на кусочки (непрерывный рюкзак)
        double result = 0;
        //тут реализуйте алгоритм сбора рюкзака
        //будет особенно хорошо, если с собственной сортировкой
        //кроме того, можете описать свой компаратор в классе Item

        //ваше решение.

        double cost[];
        int i;
        int j;
        double rational1;
        double rational2;
        double remainingW;
        Item temp;

        rational1 = 0;
        rational2 = 0;

        cost = new double[items.length];

//        for (i = 0; i < items.length; ++i) {
//            cost[i] = items[i].cost / items[i].weight;
//            System.out.println(cost[i]);
//        }
//        System.out.println();


        // Sort
        for (i = 0; i < items.length; i++) {
            for (j = 0; j < items.length - i - 1; j++) {
                rational1 = (double) items[j].cost / items[j].weight;
                rational2 = (double) items[j + 1].cost / items[j + 1].weight;
                if (rational1 < rational2) {
                    temp = items[j];
                    items[j] = items[j+1];
                    items[j+1] = temp;
                    System.out.println(temp.cost + "  " + temp.weight);
                }
            }

        }

        for (i = 0; i < items.length; ++i) {
            cost[i] = items[i].cost / items[i].weight;
            System.out.println(cost[i]);
        }

        remainingW = W;

        for (Item item : items) {
            if (remainingW != 0) {
                if (item.weight <= remainingW) {
                    result = result + item.cost;
                    remainingW = remainingW - item.weight;
                } else {
                    result = result + item.cost * (remainingW / item.weight);
                    remainingW = 0;
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