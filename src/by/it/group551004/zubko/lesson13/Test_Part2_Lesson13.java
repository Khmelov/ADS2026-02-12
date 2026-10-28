package by.it.group551004.zubko.lesson13;

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
        // Additional tests (total 22 >= 20)
        run("1 -> 2", true).include("1 2");
        run("2 -> 1", true).include("2 1");
        run("B -> A", true).include("B A");
        run("A -> C, B -> C", true).include("A B C");
        run("B -> C, A -> C", true).include("A B C");
        run("C -> B, B -> A", true).include("C B A");
        run("1 -> 4, 2 -> 4, 3 -> 4", true).include("1 2 3 4");
        run("4 -> 1, 4 -> 2, 4 -> 3", true).include("4 1 2 3");
        run("A -> B, B -> C, C -> D, D -> E", true).include("A B C D E");
        run("E -> D, D -> C, C -> B, B -> A", true).include("E D C B A");
        run("A -> D, B -> D, C -> D", true).include("A B C D");
        run("A -> Z, B -> Y, C -> X", true).include("A B C X Y Z");
        run("0 -> 5, 1 -> 5, 2 -> 5, 3 -> 5, 4 -> 5", true).include("0 1 2 3 4 5");
        run("5 -> 0, 5 -> 1, 5 -> 2, 5 -> 3, 5 -> 4", true).include("5 0 1 2 3 4");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        // Additional tests (total 14 >= 12)
        run("1 -> 1", true).include("yes").exclude("no");
        run("A -> B, B -> A", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
        run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D, D -> B", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("1 -> 2, 3 -> 4, 5 -> 6", true).include("no").exclude("yes");
        run("1 -> 2, 3 -> 4, 4 -> 3", true).include("yes").exclude("no");
        run("A -> B, B -> C, A -> C", true).include("no").exclude("yes");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        // Additional tests (total 9 >= 8)
        run("A->B, B->A", true).include("AB");
        run("A->B, B->C, C->A", true).include("ABC");
        run("A->B", true).include("A\nB");
        run("A->B, B->C", true).include("A\nB\nC");
        run("A->B, B->C, C->D, D->C", true).include("A\nB\nCD");
        run("A->B, B->A, C->D, D->C, B->C", true).include("AB\nCD");
        run("1->2, 2->1, 3->4, 4->3", true).include("12\n34");
    }
}