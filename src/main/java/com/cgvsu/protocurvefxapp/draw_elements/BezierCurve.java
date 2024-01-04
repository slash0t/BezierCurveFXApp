package com.cgvsu.protocurvefxapp.draw_elements;

import com.cgvsu.protocurvefxapp.PixelPrinter;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.*;

public class BezierCurve {
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final double MAX_DISTANCE = 5;

    private final LinkedList<Point2D> points;

    private final Color color;

    private long[] combinations;

    private int curveDegree;

    private BezierCurve(Builder builder) {
        this.curveDegree = builder.curveDegree;
        this.color = builder.color;
        this.points = builder.points;

        if (curveDegree >= 0) {
            recountCombinations();
        }
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

    private Point2D countPoint(double t) {
        double[] baseCoefficient = countBaseCoefficients(t);

        double x = 0, y = 0;
        Iterator<Point2D> iterator = points.iterator();
        for (int j = 0; j <= curveDegree; j++) {
            double combination = combinations[Math.min(curveDegree - j, j)];
            double currBase = baseCoefficient[j] * combination;

            Point2D point = iterator.next();
            x += point.getX() * currBase;
            y += point.getY() * currBase;
        }
        return new Point2D(x, y);
    }

    public void draw(PixelPrinter pixelPrinter) {
        Stack<Double> drawStack = new Stack<>();

        drawStack.add(1.0);
        drawStack.add(0.0);

        Point2D lastPoint = null;
        double lastT = 0;

        HashMap<Double, Point2D> calculatedPoints = new HashMap<>();

        while (drawStack.size() > 0) {
            double currT = drawStack.pop();

            Point2D currPoint;
            if (calculatedPoints.containsKey(currT)) {
                currPoint = calculatedPoints.remove(currT);
            } else {
                currPoint = countPoint(currT);
            }

            if (lastPoint == null) {
                lastPoint = currPoint;
                continue;
            }

            double xDif = currPoint.getX() - lastPoint.getX();
            double yDif = currPoint.getY() - lastPoint.getY();
            double segmentSize = Math.sqrt(xDif * xDif + yDif * yDif);

            if (segmentSize > MAX_DISTANCE) {
                drawStack.add(currT);
                drawStack.add((lastT + currT) / 2);

                calculatedPoints.put(currT, currPoint);
            } else {
                BresenhamLine.drawLine(
                        round(lastPoint.getX()), round(lastPoint.getY()),
                        round(currPoint.getX()), round(currPoint.getY()),
                        pixelPrinter, color
                );

                lastT = currT;
                lastPoint = currPoint;
            }
        }
    }

    public int round(double number) {
        return (int) Math.round(number);
    }

    public static class Builder {
        private int curveDegree;
        private Color color;
        private LinkedList<Point2D> points;

        public Builder() {
            this.curveDegree = -1;
            this.color = DEFAULT_COLOR;
            this.points = new LinkedList<>();
        }

        public Builder withColor(Color color) {
            this.color = color;
            return this;
        }

        public Builder withPoints(Collection<Point2D> points) {
            this.points = new LinkedList<>(points);
            this.curveDegree = points.size() - 1;
            return this;
        }

        public BezierCurve build() {
            return new BezierCurve(this);
        }
    }
}
