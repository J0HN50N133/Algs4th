import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int n;
    private int hole_x;
    private int hole_y;
    private final int[][] board;
    private int hamming;
    private int manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = tiles[i][j];
                if (board[i][j] == 0){
                    hole_x = i;
                    hole_y = j;
                }
            }
        }
        hamming = -1;
        manhattan = -1;
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n).append('\n');
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(' ').append(board[i][j]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        if (hamming != -1) return hamming;
        hamming = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0) {
                    int x = x(board[i][j] - 1);
                    int y = y(board[i][j] - 1);
                    if (x != i || y != j) hamming++;
                }
            }
        }
        return hamming;
    }

    private int x(int i) {
        return i / n;
    }

    private int y(int i) {
        return i % n;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattan != -1) return manhattan;
        manhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0) {
                    int x = x(board[i][j] - 1);
                    int y = y(board[i][j] - 1);
                    manhattan += Math.abs(i - x) + Math.abs(j - y);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (!(y instanceof Board)) {
            return false;
        }
        Board that = (Board) y;
        if (this == that) {
            return true;
        }

        if (n != that.n) {
            return false;
        }
        // 卧槽，这里一开始实现出错了，导致equals判断出问题，A*搜索很慢，没想到软件中即使是非常小的一个环节，对最终整体效果可能产生巨大的影响
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != that.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int x, int y){
        return 0 <= x && x < n && 0 <= y && y < n;
    }
    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();

        // up
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] direction : directions) {
            int x = hole_x + direction[0];
            int y = hole_y + direction[1];
            if (isValid(x, y)) {
                exch(hole_x, hole_y, x, y);
                neighbors.add(new Board(board));
                exch(hole_x, hole_y, x, y);
            }
        }
        return neighbors;
    }

    private void exch(int x1, int y1, int x2, int y2) {
        int tmp = board[x1][y1];
        board[x1][y1] = board[x2][y2];
        board[x2][y2] = tmp;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int x1;
        int y1;
        int x2;
        int y2;
        do{
            x1 = StdRandom.uniform(n);
            y1 = StdRandom.uniform(n);
            x2 = StdRandom.uniform(n);
            y2 = StdRandom.uniform(n);
        }while ((x1 == x2 && y1 == y2) || board[x1][y1] == 0 || board[x2][y2] == 0);
        exch(x1, y1, x2, y2);
        Board board = new Board(this.board);
        exch(x1, y1, x2, y2);
        return board;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] distanceTest = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board distance = new Board(distanceTest);
        System.out.println(distance.hamming());
        System.out.println(distance.manhattan());
        Board neighbor = new Board(distanceTest);
        for (Board board : neighbor.neighbors()) {
            System.out.println(board);
        }
        Board board1 = new Board(distanceTest);
        Object board2 = new Board(distanceTest);
        Object board3 = new Board(distanceTest);
        System.out.println(board1.equals(board2));
        System.out.println(board2.equals(board1));
        System.out.println(board2.equals(board3));
    }
}
