package by.it.group551001.romanovich.lesson13;

import by.it.HomeWork;
import org.junit.Test;

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
        run("A->B, C->D", true).include("A B C D");
        run("A->D, B->D, C->D", true).include("A B C D");
        run("A->B, A->C, A->D, A->E", true).include("A B C D E");
        run("D->C, C->B, B->A", true).include("D C B A");
        run("Z->A, B->C", true).include("B C Z A");
        run("A->C, B->C, C->D, D->E", true).include("A B C D E");
        run("1->3, 2->3, 3->4, 3->5, 4->6, 5->6", true).include("1 2 3 4 5 6");
        run("A->B, A->B, A->B", true).include("A B");
        run("a -> b, b -> c, a -> c", true).include("a b c");
        run("10->30, 2->30", true).include("10 2 30");
        run("Start->Left, Start->Right, Left->End, Right->End", true).include("Start Left Right End");
        run("A->F, B->F, C->G, D->G, F->H, G->H, E->H", true).include("A B C D E F G H");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("A->A", true).include("yes").exclude("no");
        run("A->B, B->A", true).include("yes").exclude("no");
        run("A->B, C->D, D->C", true).include("yes").exclude("no");
        run("A->B, B->C, C->D, D->E, E->B", true).include("yes").exclude("no");
        run("A->B, A->C, B->D, C->D", true).include("no").exclude("yes");
        run("A->B, A->B, C->D", true).include("no").exclude("yes");
        run("Z->Y, Y->X, X->W", true).include("no").exclude("yes");
        run("1->2, 2->3, 3->4, 4->5, 5->1, 6->7", true).include("yes").exclude("no");
        run("A->B, C->D, E->F, G->H", true).include("no").exclude("yes");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true).include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("A->A", true).include("A");
        run("A->B, B->C, C->A", true).include("ABC");
        run("A->B, B->C, C->D", true).include("A\nB\nC\nD");
        run("A->B, B->A, B->C, C->D, D->C, D->E", true).include("AB\nCD\nE");
        run("A->B, B->A, C->D, D->C", true).include("AB\nCD");
        run("A->B, B->A, A->B, B->C, C->C, C->D, D->E, E->D", true).include("AB\nC\nDE");
    }
}
