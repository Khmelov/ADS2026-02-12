package by.it.group551002.litovchenko.lesson13;

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
        run("1 -> 0", true).include("1 0");
        run("D -> C, C -> B, B -> A", true).include("D C B A");
        run("D -> C, D -> B, C -> A", true).include("D B C A");
        run("2 -> 0, 3 -> 1", true).include("2 0 3 1");
        run("B -> A, D -> C, B -> A", true).include("B A D C");
        run("C->A, B->A, D->C, D->B", true).include("D B C A");
        run("E -> D, E -> A, E -> C, E -> B", true).include("E A B C D");
        run("3 -> 0, 1 -> 0, 4 -> 0, 2 -> 0", true).include("1 2 3 4 0");
        run("5 -> 2, 5 -> 0, 4 -> 0, 4 -> 1, 2 -> 3, 3 -> 1", true).include("4 5 0 2 3 1");
        run("G -> E, G -> F, F -> D, E -> D, D -> B, D -> C, B -> A, C -> A, F -> D", true)
                .include("G E F D B C A");
        run("5 -> 3, 5 -> 4, 3 -> 1, 4 -> 1, 3 -> 2, 4 -> 2, 1 -> 0, 2 -> 0", true)
                .include("5 3 4 1 2 0");
        run("C -> A, F -> C, F -> D, D -> B, A -> E, B -> E, G -> F, H -> D, E -> I", true)
                .include("G F C A H D B E I");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 8).
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("1 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> A", true).include("yes").exclude("no");
        run("0 -> 1, 2 -> 3, 3 -> 2", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("A -> B, A -> C, B -> D, C -> D, D -> E, B -> E, A -> E", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2, 4 -> 5", true).include("yes").exclude("no");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 5 -> 6, 6 -> 7, 5 -> 7", true).include("no").exclude("yes");
        run("A->B, B->C, C->D, D->E, E->B, A->F, F->G", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("D->B, B->A, A->C, C->D, A->D", true)
                .include("ABCD");
        run("C->B, B->A", true)
                .include("C\nB\nA");
        run("0->0, 0->1, 1->2, 2->1, 2->3", true)
                .include("0\n12\n3");
        run("B->A, A->C, C->B, C->E, E->D, D->E, A->D", true)
                .include("ABC\nDE");
        run("S->A, A->B, B->C, C->A, C->D, D->C, B->E, E->F, F->E, F->Z", true)
                .include("S\nABCD\nEF\nZ");
        run("X -> Y, Y -> X, Y -> M, X -> M, M -> N, N -> P, P -> M, P -> K, N -> K, K -> L, L -> K, L -> Q", true)
                .include("XY\nMNP\nKL\nQ");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).
    }


}