package src;

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

    public Canvas(int resolutionX, int resolutionY, float red, float green, float blue, float intensityRed, float intensityGreen, float intensityBlue) {
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
    }


}