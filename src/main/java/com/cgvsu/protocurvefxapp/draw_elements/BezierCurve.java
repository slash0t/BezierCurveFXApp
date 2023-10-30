package com.cgvsu.protocurvefxapp.draw_elements;

import com.cgvsu.protocurvefxapp.PixelPrinter;
import javafx.geometry.Point2D;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class BezierCurve {
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final int STEPS = 50;
    private static final double MAX_DISTANCE = 5;

    private final LinkedList<Point2D> points;

    private final Color color;

    private long[] combinations;

    private int curveDegree;

    public BezierCurve() {
        this.curveDegree = -1;
        this.points = new LinkedList<>();
        this.color = DEFAULT_COLOR;
    }

    public BezierCurve(Color color) {
        this.curveDegree = -1;
        this.points = new LinkedList<>();
        this.color = color;
    }

    public BezierCurve(List<Point2D> points) {
        this.curveDegree = points.size() - 1;

        this.points = new LinkedList<>();
        this.points.addAll(points);

        this.color = DEFAULT_COLOR;

        recountCombinations();
    }

    private void recountCombinations() {
        long combination = 1;
        int countCombinations = curveDegree / 2 + 1;

        combinations = new long[countCombinations];
        for (int i = 0; i < countCombinations; i++) {
            combinations[i] = combination;
            combination = combination * (curveDegree - i) / (i + 1);
        }
    }

    private double[] countBaseCoefficients(double t) {
        double[] coefficientT = new double[curveDegree + 1];
        double[] coefficientT1 = new double[curveDegree + 1];

        double t1 = 1 - t;

        double tNow = 1;
        double t1Now = 1;
        for (int i = 0; i <= curveDegree; i++) {
            coefficientT[i] = tNow;
            coefficientT1[curveDegree - i] = t1Now;

            tNow *= t;
            t1Now *= t1;
        }

        double[] baseCoefficient = new double[curveDegree + 1];
        for (int i = 0; i <= curveDegree; i++) {
            baseCoefficient[i] = coefficientT[i] * coefficientT1[i];
        }

        return baseCoefficient;
    }

    public void addPoint(Point2D point) {
        points.add(point);
        curveDegree++;

        recountCombinations();
    }

    public void clear() {
        points.clear();
        curveDegree = -1;

        recountCombinations();
    }

    public void draw(PixelPrinter pixelPrinter) {
        Stack<Double> drawStack = new Stack<>();

        for (int i = STEPS - 1; i >= 0; i--) {
            drawStack.add((double) i);
        }

        Point2D lastPoint = null;
        double lastT = 0;

        while (drawStack.size() > 0) {
            double currT = drawStack.pop();

            double[] baseCoefficient = countBaseCoefficients(currT / (STEPS - 1));

            double x = 0, y = 0;
            Iterator<Point2D> iterator = points.iterator();
            for (int j = 0; j <= curveDegree; j++) {
                double combination;
                if (j > curveDegree - j) {
                    combination = combinations[curveDegree - j];
                } else {
                    combination = combinations[j];
                }

                double currBase = baseCoefficient[j] * combination;

                Point2D point = iterator.next();
                x += point.getX() * currBase;
                y += point.getY() * currBase;
            }

            if (lastPoint != null) {
                double xDif = x - lastPoint.getX();
                double yDif = y - lastPoint.getY();
                double segmentSize = Math.sqrt(xDif * xDif + yDif * yDif);

                if (segmentSize > MAX_DISTANCE) {
                    drawStack.add(currT);
                    drawStack.add((lastT + currT) / 2);
                } else {
                    BresenhamLine.drawLine(
                            (int) lastPoint.getX(), (int) lastPoint.getY(),
                            (int) x, (int) y,
                            pixelPrinter, color
                    );

                    lastT = currT;
                }
            }
            if (drawStack.size() > 0 && drawStack.peek() > currT) {
                lastPoint = new Point2D(x, y);
            }
        }
    }
}
