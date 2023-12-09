package src;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Class for canvas
 */
class Canvas {
    private int[] background;
    private float[] ambient;

    public Canvas(int[] background, float[] ambient) {
        this.background = background;
        this.ambient = ambient;
    }
}