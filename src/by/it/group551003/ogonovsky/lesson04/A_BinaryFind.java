package by.it.group551003.ogonovsky.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_BinaryFind {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_BinaryFind.class.getResourceAsStream("dataA.txt");
        A_BinaryFind instance = new A_BinaryFind();
        int[] result = instance.findIndex(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    public int[] findIndex(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 1; i <= n; i++) {
            a[i - 1] = scanner.nextInt();
        }

        int k = scanner.nextInt();
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt();

            // бинарный поиск: ищем индекс j (1-индексация), где a[j-1] == value,
            // иначе -1
            int lo = 0;
            int hi = n - 1;
            int found = -1;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1; // безопасно от переполнения
                if (a[mid] == value) {
                    found = mid + 1; // сдвиг к 1-индексации по условию
                    break;
                } else if (a[mid] < value) {
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }

            result[i] = found;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}