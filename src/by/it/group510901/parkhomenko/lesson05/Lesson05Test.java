package by.it.group510901.parkhomenko.lesson05;

import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

// Тесты для всех трех задач пятого урока.
public class Lesson05Test {
    @Test
    public void checkA() throws Exception {
        // Задача A проверяет количество отрезков, содержащих каждую точку.
        InputStream inputStream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(inputStream);
        boolean ok = Arrays.equals(result, new int[]{1, 0, 0});
        assertTrue("A failed", ok);
    }


    @Test
    public void checkB() throws Exception {
        // Задача B проверяет сортировку подсчетом для чисел из маленького диапазона.
        InputStream inputStream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(inputStream);
        boolean ok = Arrays.equals(result, new int[]{2, 2, 3, 9, 9});
        assertTrue("B failed", ok);
    }


    @Test
    public void checkC() throws Exception {
        // Задача C проверяет тот же ответ, что и A, но для оптимизированной реализации.
        InputStream inputStream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(inputStream);
        boolean ok = Arrays.equals(result, new int[]{1, 0, 0});
        assertTrue("C failed", ok);
    }

}
