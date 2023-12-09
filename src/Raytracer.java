package src;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;


/**
 * Main class that handles file io and execution.
 */
class Raytracer {
    static ArrayList<Sphere> spheres = new ArrayList<>();
    static Camera camera;
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

            float near = Float.parseFloat(reader.readLine().split(" ")[1]);
            float left = Float.parseFloat(reader.readLine().split(" ")[1]);
            float right = Float.parseFloat(reader.readLine().split(" ")[1]);
            float bottom = Float.parseFloat(reader.readLine().split(" ")[1]);
            float top = Float.parseFloat(reader.readLine().split(" ")[1]);
            camera = new Camera(near, left, right, top, bottom);

            //Read each line into the arraylist until the end of file
            String line = reader.readLine();
            String[] line_args;
            while(line != null){
                line_args = line.split(" ");
                if (line_args[0].compareTo("SPHERE") == 0){
                    spheres.add(new Sphere(line_args));
                }

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

    // takes in a color array on range 0-1 and restates it as 0-255
    static int[] color_to_255(float r, float g, float b){
        return new int[]{Math.round(r * 255), Math.round(g * 255), Math.round(b * 255)};
    }





} //End of Raytracer class