import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    // finds all line segments containing 4 or more points
    private final Point[] points;
    private LineSegment[] lineSegments;
    private Comparator<Point> slopeOrder;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point point : points) {
            if (point == null)
                throw new IllegalArgumentException();
        }
        points = Arrays.copyOf(points, points.length);
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0)
                throw new IllegalArgumentException();
        }
        this.points = points;
        this.lineSegments = segments();
    }

    // the number of line segments
    public int numberOfSegments() {
        if (this.lineSegments == null) segments();
        return this.lineSegments.length;
    }

    private boolean collinear(int i, int j) {
        return slopeOrder.compare(points[i], points[j]) == 0;
    }

    private LineSegment[] getLineSegments() {
        return Arrays.copyOf(this.lineSegments, this.lineSegments.length);
    }

    public LineSegment[] segments() {
        if (this.lineSegments != null) return getLineSegments();

        if (this.points.length < 4) {
            this.lineSegments = new LineSegment[0];
            return getLineSegments();
        }

        ArrayList<LineSegment> segments = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            seenPointAsOrigin(segments, i);
        }
        this.lineSegments = segments.toArray(new LineSegment[0]);
        return getLineSegments();
    }

    private void seenPointAsOrigin(ArrayList<LineSegment> segments, int i) {
        Arrays.sort(points);
        this.slopeOrder = points[i].slopeOrder();
        exch(i, 0);
        Arrays.sort(points, 1, points.length, slopeOrder);
        int begin = 1;
        int end = begin;
        while (begin < points.length) {
            while (end < points.length && collinear(begin, end)) {
                end++;
            }
            if (end - begin >= 3) {
                Arrays.sort(points, begin, end);
                if (points[0].compareTo(points[begin]) < 0) {
                    segments.add(new LineSegment(points[0], points[end - 1]));
                }
            }
            begin = end;
        }
    }

    private void exch(int i, int j) {
        Point tmp = points[i];
        points[i] = points[j];
        points[j] = tmp;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
