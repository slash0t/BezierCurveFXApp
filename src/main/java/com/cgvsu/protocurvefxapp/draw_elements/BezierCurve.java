package com.cgvsu.protocurvefxapp.draw_elements;

import javafx.geometry.Point2D;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BezierCurve {
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final int STEPS = 50;

    private final LinkedList<Point2D> points;

    private double[] combinations;

    private int curveDegree;

    private Color color;

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

        recountCombinations();
    }

    private void recountCombinations() {
        double combination = 1;
        int countCombinations = curveDegree / 2 + 1;

        combinations = new double[countCombinations];
        for (int i = 0; i < countCombinations; i++) {
            combinations[i] = combination;
            combination *= (curveDegree - i) / (i + 1.0);
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

    public void drawLine(PixelWriter pixelWriter) {
        Point2D lastPoint = null;

        for (double i = 0; i < STEPS; i++) {
            double[] baseCoefficient = countBaseCoefficients(i / (STEPS - 1));

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
                BresenhamLine.drawLine(
                        (int) lastPoint.getX(), (int) lastPoint.getY(),
                        (int) x, (int) y,
                        pixelWriter, color
                );
            }
            lastPoint = new Point2D(x, y);
        }
    }
}
