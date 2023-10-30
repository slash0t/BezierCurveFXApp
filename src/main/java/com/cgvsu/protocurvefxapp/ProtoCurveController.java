package com.cgvsu.protocurvefxapp;

import com.cgvsu.protocurvefxapp.draw_elements.BezierCurve;
import com.cgvsu.protocurvefxapp.draw_elements.PolygonalChain;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ProtoCurveController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    private PixelPrinter pixelPrinter;

    private BezierCurve curve;

    private PolygonalChain polygonalChain;

    private ArrayList<Point2D> points = new ArrayList<>();

    @FXML
    private void initialize() {
        canvas.setFocusTraversable(true);

        pixelPrinter = new JavaFXPixelPrinter(canvas.getGraphicsContext2D().getPixelWriter());

        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY -> handlePrimaryClick(event);
            }
        });

        canvas.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case Z:
                    polygonalChain.changeSegmentsVisibility();
                    redraw();
                    break;
                case X:
                    polygonalChain.changeVertexVisibility();
                    redraw();
                    break;
                case SPACE:
                    polygonalChain.clear();
                    curve.clear();
                    points.clear();
                    redraw();
                    break;
            }
        });

        curve = new BezierCurve(Color.BLUE);
        polygonalChain = new PolygonalChain(Color.RED);
    }

    private void handlePrimaryClick(MouseEvent event) {
        final Point2D clickPoint = new Point2D(event.getX(), event.getY());

//        points.add(clickPoint);
        curve.addPoint(clickPoint);
        polygonalChain.addPoint(clickPoint);

        redraw();
    }

    private void redraw() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        curve.draw(pixelPrinter);
        polygonalChain.draw(graphicsContext, pixelPrinter);
    }
}