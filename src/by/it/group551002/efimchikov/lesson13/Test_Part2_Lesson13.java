package by.it.group551002.efimchikov.lesson13;

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
        run("0 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("3 -> 1, 3 -> 2, 1 -> 0, 2 -> 0", true).include("3 1 2 0");
        run("A -> C, B -> C, C -> D, B -> D", true).include("A B C D");
        run("B -> C, A -> C", true).include("A B C");
        run("10 -> 2, 2 -> 3, 1 -> 3", true).include("1 10 2 3");
        run("2 -> 3, 1 -> 3, 10 -> 3", true).include("1 10 2 3");
        run("A -> B, A -> C, B -> C, C -> D, B -> D, A -> D", true).include("A B C D");
        run("x -> y, y -> z, a -> z, a -> y", true).include("a x y z");
        run("0 -> 4, 1 -> 4, 2 -> 4, 3 -> 4, 0 -> 2, 1 -> 3", true).include("0 1 2 3 4");
        run("5 -> 6, 4 -> 6, 3 -> 5, 3 -> 4, 2 -> 3, 1 -> 2", true).include("1 2 3 4 5 6");
        run("A -> D, B -> D, C -> D, A -> B, A -> C", true).include("A B C D");
        run("M -> N, N -> O, O -> P, M -> O, N -> P, M -> P", true).include("M N O P");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> E", true).include("no").exclude("yes");
        run("0 -> 1, 0 -> 2, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("A -> B, C -> D, B -> C, D -> A", true).include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("0 -> 1", true).include("0\n1");
        run("0 -> 1, 1 -> 2", true).include("0\n1\n2");
        run("A -> B, B -> C, C -> D", true).include("A\nB\nC\nD");
        run("1 -> 2, 2 -> 3, 3 -> 1", true).include("123");
        run("1 -> 2, 2 -> 1, 2 -> 3, 3 -> 4, 4 -> 3", true).include("12\n34");
        run("A -> B, B -> A, B -> C, C -> D, D -> C, D -> E", true).include("AB\nCD\nE");
    }


}