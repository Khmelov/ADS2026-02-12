package by.it.group551001.docenko.lesson13;

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
        run("B -> A", true).include("B A");
        run("D -> C, C -> B, B -> A", true).include("D C B A");
        run("C -> A, C -> B", true).include("C A B");
        run("A -> C, B -> C", true).include("A B C");
        run("3 -> 1, 2 -> 1, 1 -> 0", true).include("2 3 1 0");
        run("5 -> 2, 5 -> 0, 4 -> 0, 4 -> 1, 2 -> 3, 3 -> 1", true).include("4 5 0 2 3 1");
        run("A -> B, C -> D", true).include("A B C D");
        run("C -> D, A -> B", true).include("A B C D");
        run("B -> D, D -> A, C -> A", true).include("B C D A");
        run("10 -> 2, 9 -> 2", true).include("10 9 2");
        run("alpha -> beta, beta -> gamma, alpha -> gamma", true).include("alpha beta gamma");
        run("A->B,B->C", true).include("A B C");
        run("A -> D, B -> D, B -> E, C -> E, D -> F, E -> F, F -> G", true).include("A B C D E F G");
        run("H -> G, G -> F, H -> F, E -> F, E -> D", true).include("E D H G F");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("0 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("1 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");
        run("A -> B, A -> C, B -> D, C -> D, A -> D", true).include("no").exclude("yes");
        run("0 -> 1, 0 -> 1", true).include("no").exclude("yes");
        run("A -> B, C -> D, D -> E, E -> C", true).include("yes").exclude("no");
        run("A -> B, C -> D, E -> F", true).include("no").exclude("yes");
        run("a -> b, b -> c, c -> d, d -> e, e -> b", true).include("yes").exclude("no");
        run("5 -> 6, 1 -> 2, 3 -> 4, 4 -> 3, 6 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 2 -> 1, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("A->B", true).include("A\nB");
        run("B->A", true).include("B\nA");
        run("A->A", true).include("A");
        run("A->B, B->A", true).include("AB");
        run("C->B, B->A, A->C", true).include("ABC");
        run("A->B, B->C, C->D", true).include("A\nB\nC\nD");
        run("E->D, D->E, D->C, C->B, B->C, B->A", true).include("DE\nBC\nA");
        run("A->B, B->A, B->C, C->D, D->C, D->E, E->F, F->E, A->F", true).include("AB\nCD\nEF");
    }


}