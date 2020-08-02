package com.amairovi.goetz_concurrency_in_practice.chapter_8.puzzle;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

public class ConcurrentPuzzleSolver<P, M> {

    private final Puzzle<P, M> puzzle;
    private final ExecutorService executor;
    private final ConcurrentMap<P, Boolean> seen;
    final ValueLatch<Node<P, M>> solution = new ValueLatch<>();

    public ConcurrentPuzzleSolver(Puzzle<P, M> puzzle, ExecutorService executor) {
        this.puzzle = puzzle;
        this.executor = executor;
        seen = new ConcurrentHashMap<>();
    }

    public List<M> solve() throws InterruptedException {
        try {
            P p = puzzle.initialPosition();
            executor.execute(newTask(p, null, null));
            // block until solution found
            Node<P, M> solutionNode = solution.getValue();
            return solutionNode == null ? null : solutionNode.asMoveList();
        } finally {
            executor.shutdown();
        }
    }

    protected Runnable newTask(P p, M m, Node<P, M> n) {
        return new SolverTask(p, m, n);
    }

    class SolverTask extends Node<P, M> implements Runnable {

        public SolverTask(P pos, M move, Node<P, M> prev) {
            super(pos, move, prev);
        }

        @Override
        public void run() {
            if (solution.isSet()
                    || seen.putIfAbsent(pos, true) != null) {
                return; //already solved or seen this position
            }
            if (puzzle.isGoal(pos)) {
                solution.setValue(this);
            } else {
                for (M m : puzzle.legalMoves(pos)) {
                    P newPos = puzzle.move(pos, m);
                    executor.execute(newTask(newPos, m, this));
                }
            }
        }

    }

}
