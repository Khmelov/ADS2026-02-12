package by.it.group551004.zubko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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
    String findDist(String s1, String s2) {
        int[][] tmpMas = new int[s1.length() + 1][s2.length() + 1];

        int finalDist = 0;

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0 && j == 0) tmpMas[i][j] = 0;
                else if (i == 0) tmpMas[i][j] = j;
                else if (j == 0) tmpMas[i][j] = i;
                else {
                    int res1 = tmpMas[i][j - 1] + 1; // insert
                    int res2 = tmpMas[i - 1][j] + 1; // delete
                    int res3 = tmpMas[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1); // replace
                    finalDist = Math.min(Math.min(res1, res2), res3);
                    tmpMas[i][j] = finalDist;
                }
            }
        }

        StringBuilder result = new StringBuilder();
        int i = s1.length();
        int j = s2.length();
        while (i > 0 && j > 0) {
            if (tmpMas[i][j] == tmpMas[i][j - 1] + 1) {
                result.insert(0, ",");
                result.insert(0, s2.charAt(i - 1));
                result.insert(0, "+");
                j--;
            } else if (tmpMas[i][j] == tmpMas[i - 1][j] + 1) {
                result.insert(0, ",");
                result.insert(0, s1.charAt(i - 1));
                result.insert(0, "-");
                i--;
            } else {
                if ((s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1) == 0) {
                    result.insert(0, ",");
                    result.insert(0, "#");
                } else {
                    result.insert(0, ",");
                    result.insert(0, s2.charAt(j - 1));
                    result.insert(0, "~");
                }
                i--;
                j--;
            }
        }
        if (i > 0) {
            result.insert(0, ",");
            result.insert(0, s1.charAt(i - 1));
            result.insert(0, "-");
        } else if (j > 0) {
            result.insert(0, ",");
            result.insert(0, s2.charAt(j - 1));
            result.insert(0, "+");
        }

        return result.toString();
    }

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return findDist(one, two);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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