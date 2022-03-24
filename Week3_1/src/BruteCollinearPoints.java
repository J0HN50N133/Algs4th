import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    // finds all line segments containing 4 points
    private final Point[] points;

    private LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        points = Arrays.copyOf(points, points.length);
        for (Point point : points) {
            if (point == null)
                throw new IllegalArgumentException();
        }
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0)
                throw new IllegalArgumentException();
        }
        this.points = points;
        this.lineSegments = segments();
    }

    private LineSegment[] getLineSegments() {
        return Arrays.copyOf(lineSegments, lineSegments.length);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments().length;
    }

    // the line segments
    public LineSegment[] segments() {
        if (this.lineSegments != null) return getLineSegments();

        ArrayList<LineSegment> segments = new ArrayList<>();
        for (int p = 0; p < points.length; p++) {
            for (int q = 0; q < p; q++) {
                for (int r = 0; r < q; r++) {
                    for (int s = 0; s < r; s++) {
                        if (collinear(p, q, r, s)) {
                            segments.add(new LineSegment(points[p], points[s]));
                        }
                    }
                }
            }
        }
        this.lineSegments = segments.toArray(new LineSegment[0]);
        return getLineSegments();
    }

    private boolean collinear(int p, int q, int r, int s) {
        double pq = points[p].slopeTo(points[q]);
        double pr = points[p].slopeTo(points[r]);
        double ps = points[p].slopeTo(points[s]);
        return pq == pr && pr == ps;
    }

    public static void main(String[] args) {
        // read the n points from a file
        int n = StdIn.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = StdIn.readInt();
            int y = StdIn.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
