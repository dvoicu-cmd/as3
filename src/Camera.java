package src;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class for holding properties of the scene camera.
 */
class Camera {
    private BufferedImage image;
    private String file_name;
    private float near;
    private float left;
    private float right;
    private float top;
    private float bottom;

    private int resolutionX;
    private int resolutionY;
    private int[] pixels;

    /**
     * Constructor method
     * @param near
     * @param left
     * @param right
     * @param top
     * @param bottom
     */
    public Camera(float near, float left, float right, float top, float bottom, int resolutionX, int resolutionY, String file_name) {
        this.near = near;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.file_name = file_name;
        this.image = new BufferedImage(resolutionX, resolutionY, BufferedImage.TYPE_INT_ARGB);
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;

        // TEMPORARY: SET THE IMAGE TO A HARDCODED VALUE
        this.pixels = new int[resolutionX * resolutionY];
        for (int i = 0; i < 600 * 10; i++) {
            pixels[i] = 0xFF1fafa8;
        }

        for (int i = 600 * 10; i < 600 * 20; i++) {
            pixels[i] = 0xFF26c609;
        }
    }

    public void exportImage() {
        image.setRGB(0, 0, resolutionX, resolutionY, pixels, 0, resolutionX);   // Replace image's data with the pixel array

        try {
            File out = new File(file_name);
            ImageIO.write(image, "png", out);
            System.out.println(file_name + " exported successfully.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}