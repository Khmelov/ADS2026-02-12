package by.it.group551002.efimchikov.lesson02;
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

        double result = 0;
        double curWeight=0;
        Qsort(0,n-1,items);

        int ptr=0;
        while((curWeight<W)&&(ptr<n)){
            if(curWeight+items[ptr].weight<=W){
                curWeight+=items[ptr].weight;
                result+=items[ptr++].cost;
            }else{
                result+=(double)((W-curWeight)/items[ptr].weight)*(double)(items[ptr].cost);
                curWeight=W;
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    static void Qsort(int L,int R,Item[] items){
        int i=L,j=R;
        Item pivot=items[(R+L)/2];
        do{
            while(items[i].compareTo(pivot)<0)i++;
            while(items[j].compareTo(pivot)>0)j--;
            if(i<=j){
                Item temp=items[i];
                items[i]=items[j];
                items[j]=temp;
                i++;j--;
            }
        }while(i<=j);
        if(L<j)Qsort(L,j,items);
        if(i<R)Qsort(i,R,items);
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
        public int compareTo(Item o){
            if ((double)(this.cost/this.weight) < (double)(o.cost/o.weight)) {
                return 1;
            } else if ((double)(this.cost/this.weight)>(double)(o.cost/o.weight)) {
                return -1;
            } else {
                return 0;
        }
    }
}
}