package by.it.group551001.bondarenko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Итерационно вычислить алгоритм преобразования двух данных непустых строк
    Вывести через запятую редакционное предписание в формате:
     операция("+" вставка, "-" удаление, "~" замена, "#" копирование)
     символ замены или вставки

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    #,#,

    Sample Input 2:
    short
    ports
    Sample Output 2:
    -s,~p,#,#,#,+s,

    Sample Input 3:
    distance
    editing
    Sample Output 2:
    +e,#,#,-s,#,~i,#,-c,~g,


    P.S. В литературе обычно действия редакционных предписаний обозначаются так:
    - D (англ. delete) — удалить,
    + I (англ. insert) — вставить,
    ~ R (replace) — заменить,
    # M (match) — совпадение.
*/


public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int lenOne = one.length();
        int lenTwo = two.length();
        int[][] D = new int[lenOne + 1][lenTwo + 1];
        for (int i = 0; i <= lenOne; i++) D[i][0] = i;
        for (int j = 0; j <= lenTwo; j++) D[0][j] = j;
        for (int i = 1; i <= lenOne; i++) {
            for (int j = 1; j <= lenTwo; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    D[i][j] = D[i - 1][j - 1];
                } else {
                    int ins = D[i][j - 1];
                    int del = D[i - 1][j];
                    int rep = D[i - 1][j - 1];
                    D[i][j] = 1 + Math.min(Math.min(ins, del), rep);
                }
            }
        }
        List<String> operations = new ArrayList<>();
        int i = lenOne;
        int j = lenTwo;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1)) {
                operations.add("#");
                i--;
                j--;
            } else {
                int repCount = (i > 0 && j > 0) ? D[i - 1][j - 1] + 1 : Integer.MAX_VALUE;
                int insCount = (j > 0) ? D[i][j - 1] + 1 : Integer.MAX_VALUE;
                int delCount = (i > 0) ? D[i - 1][j] + 1 : Integer.MAX_VALUE;
                if (repCount == D[i][j]) {
                    operations.add("~" + two.charAt(j - 1));
                    i--;
                    j--;
                } else if (insCount == D[i][j]) {
                    operations.add("+" + two.charAt(j - 1));
                    j--;
                } else if (delCount == D[i][j]) {
                    operations.add("-" + one.charAt(i - 1));
                    i--;
                }
            }
        }
        String result = "";
        for (int k = operations.size() - 1; k >= 0; k--) {
            result = operations.get(k) + "," + result;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
    }

}