package by.it.group551003.matskevich.lesson02;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

public class C_GreedyKnapsack {
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = by.it.group551003.matskevich.lesson02.C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new by.it.group551003.matskevich.lesson02.C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)", costFinal, finishTime - startTime);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();      //сколько предметов в файле
        int W = input.nextInt();      //какой вес у рюкзака
        by.it.group551003.matskevich.lesson02.C_GreedyKnapsack.Item[] items = new by.it.group551003.matskevich.lesson02.C_GreedyKnapsack.Item[n];   //получим список предметов
        for (int i = 0; i < n; i++) { //создавая каждый конструктором
            items[i] = new by.it.group551003.matskevich.lesson02.C_GreedyKnapsack.Item(input.nextInt(), input.nextInt());
        }
        //покажем предметы
        for (by.it.group551003.matskevich.lesson02.C_GreedyKnapsack.Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        //тут необходимо реализовать решение задачи
        //итогом является максимально воможная стоимость вещей в рюкзаке
        //вещи можно резать на кусочки (непрерывный рюкзак)
        double remaining;
        double result = 0;
        double currWeight = 0;
        //тут реализуйте алгоритм сбора рюкзака
        //будет особенно хорошо, если с собственной сортировкой
        //кроме того, можете описать свой компаратор в классе Item

        //ваше решение.
        Arrays.sort(items);
        for(int i = 0; i < items.length && currWeight < W; i++) {
            if (currWeight + items[i].weight <= W) {
                result += items[i].cost;
                currWeight += items[i].weight;
            }
            else {
                remaining = W - currWeight;
                result += items[i].cost * (remaining / items[i].weight);
                currWeight = W;
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }
    private static class Item implements Comparable<by.it.group551003.matskevich.lesson02.C_GreedyKnapsack.Item> {
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
        public int compareTo(by.it.group551003.matskevich.lesson02.C_GreedyKnapsack.Item o) {
            //тут может быть ваш компаратор
            long thisObject = (long) this.cost * (long) o.weight;
            long otherObject = (long) o.cost * (long) this.weight;

            if (thisObject > otherObject) return -1;
            if (thisObject < otherObject) return 1;
            if (this.cost > o.cost) return -1;
            if (this.cost < o.cost) return 1;

            return 0;
        }
    }
}
