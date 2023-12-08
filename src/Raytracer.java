package src;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Vector;


/**
 * Main class that handles file io and execution.
 */
class Raytracer {

    /** Number of cli input arguments */
    private int numArgs;

    /** Array for current working image */
    private int[] image;

    /** Main Method */
    public static void main(String[] args) {
        // 1) first from cli args, grab the first argument
       //File inFile = new File(args[0]);

        // 2) Parse the arguments into usable data.

        // 3) Create the associated objects with the parsed arguments.

        // 4) Perform ray tracing algorithm and store results into an array.

        // 5) output bit map file produced from array.

        Canvas c = new Canvas(400, 400, 0,0,0,0,0,0, "ayo.png");
        c.exportImage();

    }

    /**
     * Private method for parsing input
     */
    private void parseInput(String[] args){

    }



} //End of Raytracer class