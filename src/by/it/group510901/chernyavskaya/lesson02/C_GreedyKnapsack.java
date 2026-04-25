package by.it.group510901.chernyavskaya.lesson02;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
        //итогом является максимально воможная стоимость вещей в рюкзаке
        //вещи можно резать на кусочки (непрерывный рюкзак)
        double result = 0;
        //тут реализуйте алгоритм сбора рюкзака
        //будет особенно хорошо, если с собственной сортировкой
        //кроме того, можете описать свой компаратор в классе Item

        Arrays.sort(items, (item1, item2) -> {
            double pricePerKg1 = (double) item1.cost / item1.weight;
            double pricePerKg2 = (double) item2.cost / item2.weight;

            if (pricePerKg2 > pricePerKg1) return 1;
            if (pricePerKg2 < pricePerKg1) return -1;
            return 0;
        });


        int remainingSpace = W;
        for (Item item : items) {
            if (item.weight <= remainingSpace) {
                result += item.cost;
                remainingSpace -= item.weight;
                System.out.println("Берём целиком: " + item +
                        " (осталось места: " + remainingSpace + " кг)");
            } else {
                double pricePerKg = (double) item.cost / item.weight;
                double partCost = pricePerKg * remainingSpace;
                result += partCost;
                System.out.printf("Берём часть: %.2f кг из предмета весом %d кг (стоимость части: %.2f)\n",
                        (double) remainingSpace, item.weight, partCost);
                break;
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    private static class Item {
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

        double getPricePerKg() {
            return (double) cost / weight;
        }
    }
}