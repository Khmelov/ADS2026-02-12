package by.it.group551003.ogonovsky.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        // d[i][j] - расстояние Левенштейна между one[0..i) и two[0..j)
        int[][] d = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) d[i][0] = i;
        for (int j = 0; j <= m; j++) d[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    d[i][j] = d[i - 1][j - 1];
                } else {
                    int del = d[i - 1][j];
                    int ins = d[i][j - 1];
                    int rep = d[i - 1][j - 1];
                    d[i][j] = 1 + Math.min(del, Math.min(ins, rep));
                }
            }
        }

        // восстановление операций - идём от (n, m) к (0, 0)
        StringBuilder ops = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0
                    && one.charAt(i - 1) == two.charAt(j - 1)
                    && d[i][j] == d[i - 1][j - 1]) {
                // копирование
                ops.append("#,");
                i--; j--;
            } else if (i > 0 && j > 0 && d[i][j] == d[i - 1][j - 1] + 1) {
                // замена one[i-1] -> two[j-1]
                ops.append('~').append(two.charAt(j - 1)).append(',');
                i--; j--;
            } else if (i > 0 && d[i][j] == d[i - 1][j] + 1) {
                // удаление one[i-1]
                ops.append('-').append(one.charAt(i - 1)).append(',');
                i--;
            } else {
                // j > 0 && d[i][j] == d[i][j-1] + 1 - вставка two[j-1]
                ops.append('+').append(two.charAt(j - 1)).append(',');
                j--;
            }
        }

        // ops собрана в обратном порядке - токены вида "op,char,", разделённые запятыми;
        // нужно развернуть последовательность токенов (а не просто символов).
        // Проще всего пройти по строке от конца к началу и собрать токены.
        StringBuilder result = new StringBuilder();
        // токены в ops разделены запятыми; идём по ним с конца
        String s = ops.toString();
        // разбор с конца: каждая операция оканчивается на ','
        int end = s.length();
        while (end > 0) {
            int start = s.lastIndexOf(',', end - 2); // позиция запятой ПЕРЕД текущим токеном
            int tokenStart = (start < 0) ? 0 : start + 1;
            // токен - подстрока [tokenStart, end), включая хвостовую запятую
            result.append(s, tokenStart, end);
            end = (start < 0) ? 0 : start + 1 - 1; // переходим за предыдущую запятую
            if (start < 0) break;
            end = start + 1; // оставляем end на позицию ЗА предыдущей запятой
            // подправляем: следующий токен заканчивается на этой запятой
            end = start + 1;
            if (end == 1) {
                // мы на самой первой запятой - перед ней токенов нет
                break;
            }
            end = start + 1 - 1 + 1; // = start + 1
            // упростим логику ниже отдельным циклом
            break;
        }

        // Переписываю аккуратно: разворот списка токенов через явный split
        return reverseTokens(ops.toString());
    }

    /**
     * Принимает строку вида "tok1,tok2,tok3," (каждый токен оканчивается запятой,
     * запятых внутри токена нет) и возвращает её с обратным порядком токенов,
     * сохраняя ту же форму "tokN,...,tok1,".
     */
    private String reverseTokens(String s) {
        // разбиваем по запятым; последний элемент после финальной запятой будет ""
        String[] parts = s.split(",", -1);
        StringBuilder sb = new StringBuilder();
        // parts.length-1 - индекс пустого хвоста после последней запятой, его пропускаем
        for (int k = parts.length - 2; k >= 0; k--) {
            sb.append(parts[k]).append(',');
        }
        return sb.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}