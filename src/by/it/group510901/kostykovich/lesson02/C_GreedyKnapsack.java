package by.it.group510901.kostykovich.lesson02;
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
    static int Quicksort(Item[] items, int left, int right) {

        double pivot = (double) items[(left + right) / 2].cost / items[(left + right) / 2].weight;

        while (left <= right) {

            while ((double) items[left].cost / items[left].weight > pivot) left++;
            while ((double) items[right].cost / items[right].weight < pivot) right--;

            if (left <= right) {
                Item changer = items[left];
                items[left] = items[right];
                items[right] = changer;
                left++;
                right--;
            }
        }
        return left;
    }

    static void SortHoara(Item[] items, int start, int end) {
        if (start >= end) return;
        int mid = Quicksort(items, start, end);
        SortHoara(items, start, mid - 1);
        SortHoara(items, mid, end);
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

        //тут реализуйте алгоритм сбора рюкзака
        //будет особенно хорошо, если с собственной сортировкой
        //кроме того, можете описать свой компаратор в классе Item

        //ваше решение.
        double result = 0;

        SortHoara(items, 0, items.length-1);
        for(int i =0; i < items.length; i++){
            int sortedkeff = items[i].cost/ items[i].weight;
            //System.out.println(sortedkeff);

        }

        int Space = W;
        for(int i = 0; i < items.length; i++){
            if(Space <= 0){
                break;

            }
            if(items[i].weight <= Space){
                result += items[i].cost;
                Space -= items[i].weight;
            }else{
                double divide_object = (double) Space/items[i].weight;
                result += items[i].cost * divide_object;
                Space = 0;
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
            //тут может быть ваш компаратор


            return 0;
        }
    }
}