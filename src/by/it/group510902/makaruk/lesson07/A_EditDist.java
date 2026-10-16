package by.it.group510902.makaruk.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    // Рекурсивный метод вычисления расстояния Левенштейна
    int getDistanceEdinting(String one, String two) {
        // Базовые случаи
        if (one.length() == 0) {
            return two.length();  // нужно вставить все символы
        }
        if (two.length() == 0) {
            return one.length();  // нужно удалить все символы
        }

        // Если последние символы одинаковые
        if (one.charAt(one.length() - 1) == two.charAt(two.length() - 1)) {
            return getDistanceEdinting(
                    one.substring(0, one.length() - 1),
                    two.substring(0, two.length() - 1)
            );
        }

        // Если последние символы разные - пробуем три операции
        // 1. Вставка
        int insert = getDistanceEdinting(
                one,
                two.substring(0, two.length() - 1)
        ) + 1;

        // 2. Удаление
        int delete = getDistanceEdinting(
                one.substring(0, one.length() - 1),
                two
        ) + 1;

        // 3. Замена
        int replace = getDistanceEdinting(
                one.substring(0, one.length() - 1),
                two.substring(0, two.length() - 1)
        ) + 1;

        // Возвращаем минимальную стоимость
        return Math.min(Math.min(insert, delete), replace);
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}