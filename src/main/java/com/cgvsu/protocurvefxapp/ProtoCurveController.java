package com.cgvsu.protocurvefxapp;

import com.cgvsu.protocurvefxapp.draw_elements.BezierCurve;
import com.cgvsu.protocurvefxapp.draw_elements.PolygonalChain;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ProtoCurveController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    private BezierCurve curve;

    private PolygonalChain polygonalChain;

    private ArrayList<Point2D> points = new ArrayList<>();

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY -> handlePrimaryClick(canvas.getGraphicsContext2D(), event);
            }
        });

        canvas.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            System.out.println(key.getText());
        });

        curve = new BezierCurve(Color.BLUE);
        polygonalChain = new PolygonalChain(Color.RED);
    }

    private void handlePrimaryClick(GraphicsContext graphicsContext, MouseEvent event) {
        final Point2D clickPoint = new Point2D(event.getX(), event.getY());

//        points.add(clickPoint);
        curve.addPoint(clickPoint);
        polygonalChain.addPoint(clickPoint);

        redraw();
    }

    private void redraw() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        curve.draw(graphicsContext.getPixelWriter());
        polygonalChain.draw(graphicsContext);
    }
}