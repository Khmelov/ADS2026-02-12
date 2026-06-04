package by.it.group551002.potyagov.lesson2;
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
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("by/it/group551002/potyagov/lesson2/greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)", costFinal, finishTime - startTime);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n1 = input.nextInt();      //сколько предметов в файле
        int W2 = input.nextInt();      //какой вес у рюкзака
        Item[] items = new Item[n1];   //получим список предметов
        for (int i = 0; i < n1; i++) { //создавая каждый конструктором
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        //покажем предметы
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n1, W2);

        Arrays.sort(items);

        double result = 0;
        int remainingSpace = W2;

        for (int i = 0; i <= items.length; i++) {

            Item current = items[i];

            if (remainingSpace == 0) break;

            if (current.weight <= remainingSpace) {

                result += current.cost;
                remainingSpace -= current.weight;
            }
            else {
                double partWeight = remainingSpace;
                double partCost = (double) current.cost / current.weight * partWeight;

                result += partCost;
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
            double thisPricePerKg = (double) this.cost / this.weight;
            double otherPricePerKg = (double) o.cost / o.weight;

            return Double.compare(otherPricePerKg, thisPricePerKg);

        }
    }
}