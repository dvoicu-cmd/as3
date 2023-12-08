package src;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;


/**
 * Main class that handles file io and execution.
 */
class Raytracer {

    /** Number of command line arguments.
    private int numArgs;

    private Canvas canvas;

    /** The camera object of the scene */
    //private Camera camera;

    /** List of spheres in the scene */
    //private ArrayList<Sphere> spheres;

    /** List of light sources in the scene */
    //private ArrayList<Light> lights;

    /** Main Method */
    public static void main(String[] args) throws IOException {
        // 1) Read cli args.
        // grab the first argument. Maybe make it handle multiple files later on
        File inFile = new File(args[0]);

        //Attempt to read file
        BufferedReader reader;

        //Store the lines in a list of strings
        ArrayList<String> lineArgs = new ArrayList<String>();

        // credit https://www.digitalocean.com/community/tutorials/java-read-file-line-by-line
        try {
            //Read the file path from args
            reader = new BufferedReader(new FileReader(inFile));
            //Line variable
            String line = reader.readLine();

            //Read each line into the arraylist until the end of file
            while(line != null){
                lineArgs.add(line);
                line = reader.readLine();
            }
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }



        // 2) Parse the arguments into usable data.

        // 3) Create the associated objects with the parsed arguments.

        // 4) Perform ray tracing algorithm and store results into an array.

        // 5) output bit map file produced from array.

        Canvas c = new Canvas(400, 400, 0,0,0,0,0,0, "ayo.png");
        c.exportImage();

    }

    /**
     * Splits a string argument by it's spaces into an array of strings.
     * @param str string argument
     * @return list of strings
     */
    private String[] splitSpace(String str){
        String[] split = str.split(" ");
        return split;
    }





} //End of Raytracer class