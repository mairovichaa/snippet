package com.amairovi.goetz_concurrency_in_practice.chapter_8.puzzle;

import java.util.Set;

public interface Puzzle<P, M> {
    P initialPosition();

    boolean isGoal(P position);

    Set<M> legalMoves(P position);

    P move(P position, M move);
}
