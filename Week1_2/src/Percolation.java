import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class Percolation {
    // true for open, false for block
    private final boolean[] grid;
    private final int n;
    private final WeightedQuickUnionUF uf;
    // 不使用back会导致isFull出错
    private final WeightedQuickUnionUF back;
    private final int top;
    private final int down;
    private int cntOpen = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.grid = new boolean[n * n];
        this.n = n;
        // 添加一个top虚拟节点
        this.uf = new WeightedQuickUnionUF(n * n + 1);
        // 添加top和down两个虚拟节点
        this.back = new WeightedQuickUnionUF(n * n + 2);
        this.top = n * n;
        this.down = n * n + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) return;
        grid[rank(row, col)] = true;
        cntOpen++;
        connectUp(row, col);
        connectDown(row, col);
        connectLeft(row, col);
        connectRight(row, col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return grid[rank(row, col)];
    }

    private int rank(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
        return (row - 1) * n + col - 1;
    }

    private void union(int p, int q) {
        uf.union(p, q);
        back.union(p, q);
    }

    private void connectUp(int row, int col) {
        if (row == 1) {
            // 向上连接的时候, uf也要连, 为了后面能正确判断connectedToTop
            union(rank(row, col), top);
        } else if (isOpen(row - 1, col)) {
            union(rank(row, col), rank(row - 1, col));
        }
    }

    private void connectDown(int row, int col) {
        if (row == n) {
            back.union(rank(row, col), down);
        } else if (isOpen(row + 1, col)) {
            union(rank(row, col), rank(row + 1, col));
        }
    }

    private void connectLeft(int row, int col) {
        if (col > 1 && isOpen(row, col - 1)) {
            union(rank(row, col), rank(row, col - 1));
        }
    }

    private void connectRight(int row, int col) {
        if (col < n && isOpen(row, col + 1)) {
            union(rank(row, col), rank(row, col + 1));
        }
    }

    private boolean connectedToTop(int row, int col) {
        return uf.find(rank(row, col)) == uf.find(top);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && connectedToTop(row, col);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return cntOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return back.find(top) == back.find(down);
    }
}
/*
    private void debug() {
        StdOut.println("The system is " + (percolates() ? "" : "not ") + "percolation.");
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                StdOut.print(isOpen(i, j) ? "\u001B[31m$\u001B[0m":' ');
            }
            StdOut.print('\n');
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation percolation = new Percolation(n);
        while (!StdIn.isEmpty()) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            percolation.open(row, col);
        }
        percolation.debug();
    }
}
 */