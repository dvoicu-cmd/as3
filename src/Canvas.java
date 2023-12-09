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

    //Resolution of canvas
    private int resolutionX;
    private int resolutionY;

    //RGB, float [0,1]
    private float red;
    private float green;
    private float blue;

    private Vector<Float> rgb;

    //Scene Ambiance, float [0,1]
    private float intensityRed;
    private float intensityGreen;
    private float intensityBlue;

    private Vector<Float> ambiance;

    private int[] pixels;
    private BufferedImage image;
    private String file_name;

    public Canvas(int resolutionX, int resolutionY, float red, float green, float blue, float intensityRed, float intensityGreen, float intensityBlue, String file_name) {
        rgb = new Vector<Float>(3);
        ambiance = new Vector<Float>(3);

        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.intensityRed = intensityRed;
        this.intensityGreen = intensityGreen;
        this.intensityBlue = intensityBlue;
        this.file_name = file_name;

        // TEMPORARY: SET THE IMAGE TO A HARDCODED VALUE
        this.pixels = new int[resolutionX * resolutionY];
        for (int i =0; i < 400*10; i++){
            pixels[i] = 0xFF1fafa8;
        }

        for (int i =400*10; i < 400*20; i++){
            pixels[i] = 0xFF26c609;
        }
        //===============================================

        this.image = new BufferedImage(resolutionX, resolutionY, BufferedImage.TYPE_INT_ARGB);
    }

    public void exportImage(){
        // Replace image's data with the pixel array
        image.setRGB(0,0,resolutionX, resolutionY, pixels, 0, resolutionX);

        try {
            File out = new File(file_name);
            ImageIO.write(image, "png", out);
            System.out.println(file_name + " exported successfully.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}