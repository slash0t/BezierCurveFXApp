package com.cgvsu.protocurvefxapp;

import javafx.scene.paint.Color;

public abstract class PixelPrinter {
    public abstract void putPixel(int x, int y);

    public abstract void putPixel(int x, int y, Color color);
}
