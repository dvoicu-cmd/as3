package src;

import java.io.*;
import java.util.ArrayList;


/**
 * Main class that handles file io and execution.
 */
class Raytracer {
    static ArrayList<Sphere> spheres = new ArrayList<>();
    static ArrayList<Light> lights = new ArrayList<>();
    static Camera camera;
    static BufferedReader reader;
    static Canvas canvas;

    /** Main Method */
    public static void main(String[] args){
        // 1) Read cli args.
        // grab the first argument. Maybe make it handle multiple files later on
        File inFile = new File(args[0]);

        //Attempt to read file


        // credit https://www.digitalocean.com/community/tutorials/java-read-file-line-by-line
        try {
            //Read the file path from args
            reader = new BufferedReader(new FileReader(inFile));

            // Camera Parameters
            float near = Float.parseFloat(next()[1]);
            float left = Float.parseFloat(next()[1]);
            float right = Float.parseFloat(next()[1]);
            float bottom = Float.parseFloat(next()[1]);
            float top = Float.parseFloat(next()[1]);
            String[] l = next();
            int resolutionX = Integer.parseInt(l[1]);
            int resolutionY = Integer.parseInt(l[2]);

            String[] line_args = next();
            while(line_args[0].compareTo("SPHERE") == 0){
                spheres.add(new Sphere(line_args));
                line_args = next();
            }

            while(line_args[0].compareTo("LIGHT") == 0){
                lights.add(new Light(line_args));
                line_args = next();
            }

            float[] back = new float[]{Float.parseFloat(line_args[1]), Float.parseFloat(line_args[2]), Float.parseFloat(line_args[3])};
            line_args = next();
            float[] ambient = new float[] {Float.parseFloat(line_args[1]), Float.parseFloat(line_args[2]), Float.parseFloat(line_args[3])};
            canvas = new Canvas(back, ambient);

            String file_name = next()[1];
            camera = new Camera(near, left, right, top, bottom, resolutionX, resolutionY, file_name);


            reader.close();
        }




        catch(IOException e) {
            e.printStackTrace();
        }



        // 2) Parse the arguments into usable data.

        // 3) Create the associated objects with the parsed arguments.

        // 4) Perform ray tracing algorithm and store results into an array.

        // 5) output bit map file produced from array.
        camera.raytrace(3);
        camera.exportImage();

    }

    /**
     * Shorthand for reading in a new line and splitting it on spaces.
     * @return String array
     */
    private static String[] next() throws IOException {
        return reader.readLine().split(" ");
    }

    // takes in a color array on range 0-1 and restates it as 0-255
    static int[] color_to_255(float r, float g, float b){
        return new int[]{Math.round(r * 255), Math.round(g * 255), Math.round(b * 255)};
    }

    static int[] color_to_255(float[] rgb){
        return color_to_255(rgb[0], rgb[1], rgb[2]);
    }

    static int rgb_array_to_int(float[] color){
        int[] rgb = color_to_255(color);
        int ret = 0;
        ret += rgb[0];
        ret = (ret <<8) + rgb[1];
        ret = (ret <<8) + rgb[2];
        return ret + 0xFF000000; //alpha channel maxed out
    }



} //End of Raytracer class