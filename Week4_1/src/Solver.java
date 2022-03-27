import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

public class Solver {

    private static class GameTree implements Comparable<GameTree> {
        private final GameTree parent;
        private final Board board;
        private final int moves;
        private final boolean twin;
        private final int distance;
        private final int priority;

        private GameTree(Board board, boolean twin) {
            this.parent = null;
            this.board = board;
            this.moves = 0;
            this.twin = twin;
            this.distance = board.manhattan();
            this.priority = moves + distance;
        }

        private GameTree(Board board, GameTree parent) {
            this.parent = parent;
            this.board = board;
            this.moves = parent.moves + 1;
            this.twin = parent.twin;
            this.distance = board.manhattan();
            this.priority = moves + distance;
        }

        @Override
        public int compareTo(GameTree that) {
            if (this.priority == that.priority) {
                return Integer.compare(this.distance, that.distance);
            } else {
                return Integer.compare(this.priority, that.priority);
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof GameTree)) return false;

            GameTree that = (GameTree) obj;
            if (this == that) return true;

            return board.equals(that.board);
        }
    }

    private final Board initial;
    private boolean solvable;
    private int moves;
    private Deque<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        this.initial = initial;
        solve();
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    private void solve() {
        MinPQ<GameTree> pq = new MinPQ<>();
        pq.insert(new GameTree(initial, false));
        pq.insert(new GameTree(initial.twin(), true));

        GameTree tree = pq.delMin();
        // b或者b的孪生局面，总有一个会到达Goal
        while (!tree.board.isGoal()) {
            for (Board neighbor : tree.board.neighbors()) {
                if (tree.parent == null || !neighbor.equals(tree.parent.board)) {
                    pq.insert(new GameTree(neighbor, tree));
                }
            }
            tree = pq.delMin();
        }

        // if twin被solve，那么不可解，否则可解
        solvable = !tree.twin;
        if (!solvable) {
            moves = -1;
            solution = null;
        } else {
            moves = tree.moves;
            solution = new LinkedList<>();
            while (tree != null) {
                solution.addFirst(tree.board);
                tree = tree.parent;
            }
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
//        In in = new In(args[0]);
        In in = new In(new Scanner(System.in));
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}