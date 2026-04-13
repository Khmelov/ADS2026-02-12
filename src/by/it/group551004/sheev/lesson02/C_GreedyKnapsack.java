package by.it.group551004.sheev.lesson02;
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
        Double valuePerWeight1;
        Double valuePerWeight2;

        for (int i = 0; i < n; i++) { //создавая каждый конструктором
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        //покажем предметы
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        for (int i = 0; i < items.length; i++){
            for (int j = 0; j < items.length - 1 - i; j++){
                valuePerWeight1 = (double) items[j].cost / items[j].weight;
                valuePerWeight2 = (double) items[j + 1].cost / items[j + 1].weight;
                if (valuePerWeight1 < valuePerWeight2){
                    Item temp = items[j];
                    items[j] = items[j + 1];
                    items[j + 1] = temp;
                }
            }
        }
        double result = 0;
        int remainingWeight = W;
        double fraction;

        for (Item item : items) {
            if (remainingWeight <= 0) {
                break;
            }
            if (item.weight <= remainingWeight) {
                result += item.cost;
                remainingWeight -= item.weight;
            }
            else {
                fraction = (double) remainingWeight / item.weight;
                result += item.cost * fraction;
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
                   '}';
        }

        @Override
        public int compareTo(Item o) {
            double thisValue;
            double otherValue;

            thisValue = (double) this.cost / this.weight;
            otherValue = (double) o.cost / o.weight;

            if (thisValue > otherValue) {
                return -1;
            } else
                if (thisValue < otherValue) {
                    return 1;
                }
                else
                    return 0;
        }
    }
}