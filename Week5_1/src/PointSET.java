import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private final TreeSet<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkNull(p);
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNull(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }

    private void checkNull(Object o) {
        if (o == null) throw new IllegalArgumentException();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        List<Point2D> result = new ArrayList<>();
        for (Point2D point : points) {
            if (point.y() > rect.ymax()) break;
            if (rect.contains(point)) result.add(point);
        }
        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNull(p);
        // maximum in the given data range
        double minDis = Double.POSITIVE_INFINITY;
        Point2D result = null;
        for (Point2D point : points) {
            double distance = point.distanceSquaredTo(p);
            if (distance < minDis) {
                minDis = distance;
                result = point;
            }
        }
        return result;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}