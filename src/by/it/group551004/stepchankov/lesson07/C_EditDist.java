package by.it.group551004.stepchankov.lesson07;

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
        int n = one.length(), m = two.length();
        int[][] dp = new int[n + 2][m + 2];
        for(int i = 0; i <= n + 1; i++){
            for(int j = 0; j <= m + 1; j++){
                dp[i][j] = (int)2e9;
            }
        }
        dp[n][m] = 0;
        for(int i = n; i >= 0; i--){
            for(int j = m; j >= 0; j--) {
                if(n == i && m == j){
                    continue;
                }
                if(n == i){
                    dp[i][j] = dp[i][j + 1] + 1;
                    continue;
                }
                if(m == j){
                    dp[i][j] = dp[i + 1][j] + 1;
                    continue;
                }
                dp[i][j] = (dp[i + 1][j] < dp[i][j + 1] ? dp[i + 1][j] : dp[i][j + 1]) + 1;
                int x = dp[i + 1][j + 1] + (one.charAt(i) != two.charAt(j) ? 1 : 0);
                dp[i][j] = (dp[i][j] < x ? dp[i][j] : x);
            }
        }
        StringBuilder result = new StringBuilder();
        int x = 0, y = 0;
        while(x < n || y < m){
            if(x < n && y < m && one.charAt(x) == two.charAt(y)){
                result.append("#,");
                x++;
                y++;
            } else if(x < n && y < m && dp[x][y] == dp[x + 1][y + 1] + 1){
                result.append("~").append(two.charAt(y)).append(",");
                x++;
                y++;
            } else if(y < m && dp[x][y] == dp[x][y + 1] + 1){
                result.append("+").append(two.charAt(y)).append(",");
                y++;
            } else if(x < n && dp[x][y] == dp[x + 1][y] + 1){
                result.append("-").append(one.charAt(x)).append(",");
                x++;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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