package com.cgvsu.protocurvefxapp.draw_elements;

import com.cgvsu.protocurvefxapp.PixelPrinter;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
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

    private PolygonalChain(Builder builder) {
        this.points = new LinkedList<>();
        this.vertexColor = builder.vertexColor;
        this.segmentColor = builder.segmentColor;

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

        if (areSegmentsVisible) {
            Iterator<Point2D> iterator = points.iterator();
            Point2D lastPoint = iterator.next();

            for (int i = 1; i < points.size(); i++) {
                Point2D point = iterator.next();

                BresenhamLine.drawLine(
                        round(lastPoint.getX()), round(lastPoint.getY()),
                        round(point.getX()), round(point.getY()),
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

    public int round(double number) {
        return (int) Math.round(number);
    }

    public static class Builder {
        private Color vertexColor;
        private Color segmentColor;

        public Builder() {
            this.vertexColor = DEFAULT_COLOR;
            this.segmentColor = DEFAULT_COLOR;
        }

        public Builder withColor(Color color) {
            this.vertexColor = color;
            this.segmentColor = color;
            return this;
        }

        public Builder withVertexColor(Color color) {
            this.vertexColor = color;
            return this;
        }

        public Builder withSegmentColor(Color color) {
            this.segmentColor = color;
            return this;
        }

        public PolygonalChain build() {
            return new PolygonalChain(this);
        }
    }
}
