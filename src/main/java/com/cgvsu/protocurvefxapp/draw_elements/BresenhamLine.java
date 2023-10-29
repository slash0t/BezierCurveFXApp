package com.cgvsu.protocurvefxapp.draw_elements;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public final class BresenhamLine {
    private static final Color DEFAULT_COLOR = Color.BLACK;

    private BresenhamLine() {

    }

    public static void drawLine(int x1, int y1, int x2, int y2, PixelWriter pw) {
        drawLine(
                x1, y1,
                x2, y2,
                pw, DEFAULT_COLOR
        );
    }

    public static void drawLine(int x1, int y1, int x2, int y2, PixelWriter pw, Color color) {
        int deltaX = Math.abs(x1 - x2);
        int deltaY = Math.abs(y1 - y2);

        if (deltaX > deltaY) {
            drawLineXMain(x1, y1, x2, y2, pw, color);
        } else {
            drawLineYMain(x1, y1, x2, y2, pw, color);
        }
    }

    private static void drawLineXMain(int x1, int y1, int x2, int y2, PixelWriter pw, Color color) {
        if (x1 > x2) {
            int temp = x2;
            x2 = x1;
            x1 = temp;

            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        int deltaX = x2 - x1;
        int deltaY = Math.abs(y2 - y1);
        int y = y1;
        int dirY = 0;

        int error = 0;
        int deltaError = deltaY + 1;

        if (y2 > y1) {
            dirY = 1;
        } else if (y2 < y1) {
            dirY = -1;
        }

        for (int x = x1; x <= x2; x++) {
            pw.setColor(x, y, color);
            error += deltaError;

            if (error >= deltaX + 1) {
                y += dirY;
                error -= deltaX + 1;
            }
        }
    }

    private static void drawLineYMain(int x1, int y1, int x2, int y2, PixelWriter pw, Color color) {
        if (y1 > y2) {
            int temp = y2;
            y2 = y1;
            y1 = temp;

            temp = x1;
            x1 = x2;
            x2 = temp;
        }

        int deltaY = y2 - y1;
        int deltaX = Math.abs(x2 - x1);
        int x = x1;
        int dirX = 0;

        int error = 0;
        int deltaError = deltaX + 1;

        if (x2 > x1) {
            dirX = 1;
        } else if (x2 < x1) {
            dirX = -1;
        }

        for (int y = y1; y <= y2; y++) {
            pw.setColor(x, y, color);
            error += deltaError;

            if (error >= deltaY + 1) {
                x += dirX;
                error -= deltaY + 1;
            }
        }
    }
}
