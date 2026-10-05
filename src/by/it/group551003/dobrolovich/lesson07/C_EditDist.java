package by.it.group551003.dobrolovich.lesson07;

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

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = one.length();
        int m = two.length();
        int[][] D = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            D[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            D[0][j] = j;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                D[i][j] = Math.min(D[i - 1][j] + 1, Math.min( D[i][j - 1] + 1, D[i - 1][j - 1] + cost)
                );
            }
        }
        StringBuilder sb = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                if (D[i][j] == D[i - 1][j - 1] + cost) {
                    if (cost == 0) {
                        sb.append("#,");
                    } else {
                        sb.append("~").append(two.charAt(j - 1)).append(",");
                    }
                    i--; j--;
                } else if (D[i][j] == D[i - 1][j] + 1) {
                    sb.append("-").append(one.charAt(i - 1)).append(",");
                    i--;
                } else {
                    sb.append("+").append(two.charAt(j - 1)).append(",");
                    j--;
                }
            } else if (i > 0) {
                sb.append("-").append(one.charAt(i - 1)).append(",");
                i--;
            } else {
                sb.append("+").append(two.charAt(j - 1)).append(",");
                j--;
            }
        }
        String[] parts = sb.toString().split("(?<=,)");
        StringBuilder result = new StringBuilder();
        for (int k = parts.length - 1; k >= 0; k--) {
            result.append(parts[k]);
        }
        return result.toString();
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