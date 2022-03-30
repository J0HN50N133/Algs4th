import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KdTree {
    private final Node root;

    private static class Node {
        // dimension, if X then is XTree, else is YTre
        private final boolean X;
        private int size;
        private Point2D root;
        private Node less;
        private Node greater;
        private final RectHV rect;

        private Node() {
            this.X = true;
            this.size = 0;
            this.rect = new RectHV(0, 0, 1, 1);
        }

        private Node(boolean X, RectHV rect) {
            this.X = X;
            this.size = 0;
            this.root = null;
            this.rect = rect;
        }

        private boolean isEmpty() {
            return size == 0;
        }

        // number of points in the set
        public int size() {
            return size;
        }

        private boolean inLess(Point2D p) {
            return (X && p.x() < root.x()) || (!X && p.y() < root.y());
        }

        // add the point to the set (if it is not already in the set)
        private static int getSize(Node n) {
            return n == null ? 0 : n.size;
        }

        private void insert(Point2D p) {
            if (root == null) {
                root = p;
                size++;
                return;
            }
            if (!p.equals(root)) {
                if (inLess(p)) {
                    if (less == null) less = new Node(!X, nextLTHV());
                    less.insert(p);
                } else {
                    if (greater == null) greater = new Node(!X, nextGTHV());
                    greater.insert(p);
                }
                size = 1 + getSize(less) + getSize(greater);
            }
        }

        // does the set contain point p?
        private boolean contains(Point2D p) {
            if (root == null) return false;
            if (root.equals(p)) return true;
            if (inLess(p) && less != null) return less.contains(p);
            else if (greater != null) return greater.contains(p);
            return false;
        }

        // draw all points to standard draw
        private void draw() {
            StdDraw.clear();
            draw(this);
        }

        private static void draw(Node node) {
            if (node == null || node.root == null) return;
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(node.root.x(), node.root.y());
            if (node.X) {
                // 竖着画
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.root.x(), node.rect.ymin(), node.root.x(), node.rect.ymax());
            } else {
                // 横着画
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.root.y(), node.rect.xmax(), node.root.y());
            }
            draw(node.less);
            draw(node.greater);
        }

        // all points that are inside the rectangle (or on the boundary)
        private Iterable<Point2D> range(RectHV rect) {
            return range(new ArrayList<>(), rect);
        }

        private RectHV nextLTHV() {
            if (X) {
                return new RectHV(rect.xmin(), rect.ymin(), root.x(), rect.ymax());
            } else {
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), root.y());
            }
        }

        private RectHV nextGTHV() {
            if (X) {
                return new RectHV(root.x(), rect.ymin(), rect.xmax(), rect.ymax());
            } else {
                return new RectHV(rect.xmin(), root.y(), rect.xmax(), rect.ymax());
            }
        }

        private Iterable<Point2D> range(List<Point2D> result, RectHV searchRect) {
            if (root == null) return null;
            if (searchRect.intersects(rect)) {
                if (searchRect.contains(root)) {
                    result.add(root);
                }
                if (less != null)
                    less.range(result, searchRect);
                if (greater != null)
                    greater.range(result, searchRect);
            }
            return result;
        }

        // a nearest neighbor in the set to point p; null if the set is empty
        private Point2D nearest(Point2D p) {
            return nearest(this, p, root);
        }

        // 传入n可以减少很多null判断的麻烦
        private static Point2D updatePoint(Point2D p, Point2D result, Point2D candidate) {
            double d = candidate.distanceSquaredTo(p);
            if (d < result.distanceSquaredTo(p)) {
                return candidate;
            }
            return result;
        }

        private static Point2D nearest(Node n, Point2D p, Point2D result) {
            // root is null if and only if tree is empty
            if (n == null || n.root == null) return result;
            if (n.rect.distanceSquaredTo(p) > result.distanceSquaredTo(p)) {
                return result;
            } else {
                result = updatePoint(p, result, n.root);
                double toLT = n.less == null ? Double.POSITIVE_INFINITY : n.less.rect.distanceTo(p);
                double toGT = n.greater == null ? Double.POSITIVE_INFINITY : n.greater.rect.distanceTo(p);
                if (n.less != null && n.less.rect.contains(p) || toLT < toGT) {
                    result = updatePoint(p, result, nearest(n.less, p, result));
                    result = updatePoint(p, result, nearest(n.greater, p, result));
                } else if (n.greater != null && n.greater.rect.contains(p) || toGT <= toLT) {
                    result = updatePoint(p, result, nearest(n.greater, p, result));
                    result = updatePoint(p, result, nearest(n.less, p, result));
                }
            }
            return result;
        }
    }

    public KdTree() {
        root = new Node();
    }

    private void checkNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
    }

    // is the set empty?
    public boolean isEmpty() {
        return root.isEmpty();
    }

    // number of points in the set
    public int size() {
        return root.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkNull(p);
        root.insert(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNull(p);
        return root.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        root.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        return root.range(rect);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNull(p);
        return root.nearest(p);
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        In in = new In(new Scanner(System.in));
        KdTree kdTree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            kdTree.insert(new Point2D(x, y));
        }
        kdTree.draw();
    }
}