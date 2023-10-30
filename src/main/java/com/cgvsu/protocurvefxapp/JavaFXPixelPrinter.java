package com.cgvsu.protocurvefxapp;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class JavaFXPixelPrinter extends PixelPrinter {

    private final PixelWriter pixelWriter;

    public JavaFXPixelPrinter(PixelWriter pixelWriter) {
        this.pixelWriter = pixelWriter;
    }

    @Override
    public void putPixel(int x, int y) {
        pixelWriter.setColor(x, y, Color.BLACK);
    }

    @Override
    public void putPixel(int x, int y, Color color) {
        pixelWriter.setColor(x, y, color);
    }
}
