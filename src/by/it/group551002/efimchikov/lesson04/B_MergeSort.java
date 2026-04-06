package by.it.group551002.efimchikov.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Реализуйте сортировку слиянием для одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо отсортировать полученный массив.

Sample Input:
5
2 3 9 2 9
Sample Output:
2 2 3 9 9
*/
public class B_MergeSort {
    public static void merge(int[] a,int L,int R){
        int C=(L+R)/2;
        int[] res=new int[a.length];
        int i=L,j=C+1,resp=L;
        while((i<=C) && (j<=R)){
            if(a[i]<a[j])
                res[resp++]=a[i++];
            else
                res[resp++]=a[j++];
        }
        while(i<=C)res[resp++]=a[i++];
        while(j<=R)res[resp++]=a[j++];
        System.arraycopy(res,L,a,L,R-L+1);
    }
    public static void mergesort(int[] a,int L,int R){
        if(L>=R)
            return;
        if(R-L==1){
            if(a[L]>a[R]) {
                int temp = a[R];
                a[R] = a[L];
                a[L] = temp;
                return;
            }
        }
        int C=(R+L)/2;
        mergesort(a,L,C);
        mergesort(a,C+1,R);
        merge(a,L,R);
    }
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        //long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием

        mergesort(a,0,a.length-1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
