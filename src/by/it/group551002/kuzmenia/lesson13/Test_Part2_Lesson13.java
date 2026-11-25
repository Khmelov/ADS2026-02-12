package by.it.group551002.kuzmenia.lesson13;

import by.it.HomeWork;
import org.junit.Test;

@SuppressWarnings("NewClassNamingConvention")
public class Test_Part2_Lesson13 extends HomeWork {

    @Test
    public void testGraphA() {
        run("0 -> 1", true).include("0 1");
        run("0 -> 1, 1 -> 2", true).include("0 1 2");
        run("0 -> 2, 1 -> 2, 0 -> 1", true).include("0 1 2");
        run("0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1", true).include("0 1 2 3");
        run("1 -> 3, 2 -> 3, 2 -> 3, 0 -> 1, 0 -> 2", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 2, 0 -> 2, 1 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("A -> B, A -> C, B -> D, C -> D", true).include("A B C D");
        run("A -> B, A -> C, B -> D, C -> D, A -> D", true).include("A B C D");
        // Дополнительные примеры
        run("1 -> 2", true).include("1 2");
        run("B -> A", true).include("B A");
        run("3 -> 1, 2 -> 1", true).include("2 3 1");
        run("0 -> 3, 0 -> 2, 0 -> 1", true).include("0 1 2 3");
        run("D -> A, C -> A, B -> A", true).include("B C D A");
        run("5 -> 4, 4 -> 3, 3 -> 2, 2 -> 1", true).include("5 4 3 2 1");
        run("0 -> 1, 2 -> 3", true).include("0 1 2 3");
        run("E -> F, A -> B, C -> D, B -> C", true).include("A B C D E F");
        run("B -> D, A -> C, C -> D", true).include("A B C D");
        run("1 -> 4, 2 -> 4, 3 -> 4, 1 -> 2", true).include("1 2 3 4");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4, 3 -> 5, 4 -> 6, 5 -> 6", true).include("0 1 2 3 4 5 6");
        run("C -> A, C -> B, A -> D, B -> D, E -> C", true).include("E C A B D");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 20).
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        // Дополнительные примеры
        run("0 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> B", true).include("yes").exclude("no");
        run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");
        run("1 -> 2, 1 -> 2, 2 -> 3", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 1", true).include("yes").exclude("no");
        run("1 -> 2, 3 -> 4, 4 -> 3", true).include("yes").exclude("no");
        run("1 -> 2, 3 -> 4, 5 -> 6", true).include("no").exclude("yes");
        run("C -> A, C -> B, A -> D, B -> D, D -> E, E -> C", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 12).
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        // Дополнительные примеры
        run("A->B", true)
                .include("A\nB");
        run("A->B, B->A", true)
                .include("AB");
        run("A->A", true)
                .include("A");
        run("A->B, B->C, C->D", true)
                .include("A\nB\nC\nD");
        run("C->B, B->A, A->C", true)
                .include("ABC");
        run("A->B, B->C, C->A, C->D, D->E, E->F, F->D, F->G", true)
                .include("ABC\nDEF\nG");
        run("D->C, C->D, B->A, A->B, B->C", true)
                .include("AB\nCD");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 9).
    }

}