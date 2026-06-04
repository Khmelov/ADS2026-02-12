package by.it.group551003.ogonovsky.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        long[] m = new long[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextLong();
        }

        if (n == 0) {
            System.out.println(0);
            return 0;
        }

        // tails[k]    - значение последнего элемента в "лучшей" невозрастающей
        //               подпоследовательности длины k+1.
        //               Содержимое tails само невозрастает по индексу k.
        // tailsIdx[k] - индекс этого элемента в исходном массиве m.
        long[] tails = new long[n];
        int[] tailsIdx = new int[n];
        int[] prev = new int[n]; // prev[i] - индекс предыдущего элемента в цепочке, либо -1
        int len = 0;

        for (int i = 0; i < n; i++) {
            long v = m[i];

            // ищем первую позицию pos в tails[0..len-1], где tails[pos] < v
            // (туда мы поместим v: всё, что слева, >= v - значит цепочка длины pos
            //  заканчивается элементом >= v и её можно продлить нашим v)
            int lo = 0, hi = len;
            while (lo < hi) {
                int mid = (lo + hi) >>> 1;
                if (tails[mid] >= v) {
                    lo = mid + 1;   // tails[mid] >= v: v нельзя сюда, надо правее
                } else {
                    hi = mid;       // tails[mid] < v: годится, ищем левее
                }
            }
            int pos = lo;

            // предшественник в цепочке - тот, кто сейчас на pos-1 в tails
            prev[i] = (pos > 0) ? tailsIdx[pos - 1] : -1;

            tails[pos] = v;
            tailsIdx[pos] = i;
            if (pos == len) {
                len++;
            }
        }

        // восстанавливаем индексы: идём от хвоста самой длинной цепочки назад
        int[] resultIdx = new int[len];
        int cur = tailsIdx[len - 1];
        for (int k = len - 1; k >= 0; k--) {
            resultIdx[k] = cur;
            cur = prev[cur];
        }

        // печать ответа
        StringBuilder sb = new StringBuilder();
        sb.append(len).append('\n');
        for (int k = 0; k < len; k++) {
            if (k > 0) sb.append(' ');
            sb.append(resultIdx[k] + 1); // индексация с 1
        }
        System.out.println(sb);

        return len;
    }
}