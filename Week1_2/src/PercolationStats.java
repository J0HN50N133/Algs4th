import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // perform independent trials on an n-by-n grid

    private final int trials;
    private final double[] fractions;
    private double mean = 0.0;
    private boolean meanDone = false;
    private double stddev = 0.0;
    private boolean stddevDone = false;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        // numberOfPercolation
        this.trials = trials;
        this.fractions = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                percolation.open(row, col);
            }
            fractions[i] = ((double) percolation.numberOfOpenSites() / (double) (n * n));
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        if (meanDone) return mean;

        meanDone = true;
        mean = StdStats.mean(fractions);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (stddevDone) return stddev;

        stddev = StdStats.stddev(fractions);
        stddevDone = true;
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length < 2) return;

        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);
        StdOut.printf("mean                    = %f\n", ps.mean());
        StdOut.printf("stddev                  = %f\n", ps.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", ps.confidenceLo(), ps.confidenceHi());
    }
}
