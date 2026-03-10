package by.it.group551002.shinkevich.lesson02;

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

        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

		java.util.Arrays.sort(items);
		double result = 0;
		int currentWeight = 0;

		for (Item item : items) {
			if (currentWeight + item.weight <= W) {
				result += item.cost;
				currentWeight += item.weight;
			} else {
				int remainingWeight = W - currentWeight;
				result += (double) item.cost * remainingWeight / item.weight;
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
			double thisRatio = (double) this.cost / this.weight;
			double otherRatio = (double) o.cost / o.weight;

			return Double.compare(otherRatio, thisRatio);
		}
    }
}