package by.it.group551002.kurzhalov.lesson01;

import java.util.ArrayList;
import java.util.List;
/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации

        // Период Пиано
        List<Long> list = new ArrayList<>();
        // Добавляем 0 и 1, буква L - означает тип данных long
        list.add(0L);
        list.add(1L);

        for (int i = 2; i <= m * 6; i++) {
            list.add((list.get(i - 1) + list.get(i - 2)) % m);
            // Период всегда начинается с 0 и 1
            if (list.get(i) == 1 && list.get(i - 1) == 0) {
                // Когда еще раз встретили 0 и 1, значит начался новый период, 0 и 1 удаляем и делаем break
                list.remove(list.size() - 1);
                // удаляем под тем же индексом, потому что размер листа уменьшился на 1
                list.remove(list.size() - 1);
                break;
            }
        }

        // Индекс внутри периода (индекс остатка от деления n-го числа на m)
        int periodSize = list.size();
        int index = (int) (n % periodSize);

        // возвращаем остаток по чуть выше найденному индексу
        return list.get(index);
    }


}

