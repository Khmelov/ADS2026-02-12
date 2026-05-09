package by.it.group551002.puteev.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<n, для которых A[i]>A[j].

    (Такая пара элементов называется инверсией массива.
    Количество инверсий в массиве является в некотором смысле
    его мерой неупорядоченности: например, в упорядоченном по неубыванию
    массиве инверсий нет вообще, а в массиве, упорядоченном по убыванию,
    инверсию образуют каждые (т.е. любые) два элемента.
    )

Sample Input:
5
2 3 9 2 9
Sample Output:
2

Головоломка (т.е. не обязательно).
Попробуйте обеспечить скорость лучше, чем O(n log n) за счет многопоточности.
Докажите рост производительности замерами времени.
Большой тестовый массив можно прочитать свой или сгенерировать его программно.
*/


public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int count = 0;

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        a = mergeSort(a,0,n-1);
        return count;
    }

    private int[] mergeSort(int[] a, int left, int right){
        if (left == right) {
            return new int[]{a[left]};
        }
        int mid = left+(right-left)/2;

        int[] leftPart = mergeSort(a,left,mid);
        int[] rightPart = mergeSort(a,mid+1,right);

        return merge(leftPart,rightPart);
    }

    private int[] merge(int[] left, int[] right){
        int[] result = new int[left.length+ right.length];
        int i=0,j=0,k=0;
        while(i < left.length && j < right.length){
            if (left[i] <= right[j]){
                result[k++] = left[i++];
            } else {
                count += (left.length-i);
                result[k++] = right[j++];
            }
        }

        while(i < left.length) result[k++] = left[i++];
        while(j < right.length) result[k++] = right[j++];

        return  result;
    }
}
