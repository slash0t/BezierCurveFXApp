package com.cgvsu.protocurvefxapp.draw_elements;

import com.cgvsu.protocurvefxapp.PixelPrinter;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Iterator;
import java.util.LinkedList;

public class PolygonalChain {
    private static final Color DEFAULT_COLOR = Color.BLACK;

    private final LinkedList<Point2D> points;

    private final Color vertexColor;
    private final Color segmentColor;

    private boolean isVertexVisible;
    private boolean areSegmentsVisible;

    public PolygonalChain() {
        this.points = new LinkedList<>();
        this.vertexColor = DEFAULT_COLOR;
        this.segmentColor = DEFAULT_COLOR;

        this.isVertexVisible = true;
        this.areSegmentsVisible = true;
    }

    public PolygonalChain(Color color) {
        this.points = new LinkedList<>();
        this.vertexColor = color;
        this.segmentColor = color;

        this.isVertexVisible = true;
        this.areSegmentsVisible = true;
    }

    public PolygonalChain(Color vertexColor, Color segmentColor) {
        this.points = new LinkedList<>();
        this.vertexColor = vertexColor;
        this.segmentColor = segmentColor;

        this.isVertexVisible = true;
        this.areSegmentsVisible = true;
    }

    public void changeVertexVisibility() {
        isVertexVisible = !isVertexVisible;
    }

    public void changeSegmentsVisibility() {
        areSegmentsVisible = !areSegmentsVisible;
    }

    public void addPoint(Point2D point) {
        points.add(point);
    }

    public void clear() {
        points.clear();
    }

    public void draw(GraphicsContext graphicsContext, PixelPrinter pixelPrinter) {
        if (points.size() < 1) {
            return;
        }

        PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        if (areSegmentsVisible) {
            Iterator<Point2D> iterator = points.iterator();
            Point2D lastPoint = iterator.next();

            for (int i = 1; i < points.size(); i++) {
                Point2D point = iterator.next();

                BresenhamLine.drawLine(
                        (int) lastPoint.getX(), (int) lastPoint.getY(),
                        (int) point.getX(), (int) point.getY(),
                        pixelPrinter, segmentColor
                );

                lastPoint = point;
            }
        }

        if (isVertexVisible) {
            Paint old = graphicsContext.getFill();
            graphicsContext.setFill(vertexColor);

            final int POINT_RADIUS = 3;
            for (Point2D point : points) {
                graphicsContext.fillOval(
                        point.getX() - POINT_RADIUS, point.getY() - POINT_RADIUS,
                        2 * POINT_RADIUS, 2 * POINT_RADIUS
                );
            }

            graphicsContext.setFill(old);
        }
    }
}
