package src;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static src.Raytracer.rgb_array_to_int;

/**
 * Class for holding properties of the scene camera.
 */
class Camera {
    private final BufferedImage image;
    private final String file_name;
    private final float near;
    private final float left;
    private final float right;
    private final float top;
    private final float bottom;

    private final int resolutionX;
    private final int resolutionY;
    private final int[] pixels;

    /**
     * Constructor method
     * @param near distance to near plane (positive, despite looking toward -Z)
     * @param left boundary in WCS of lhs
     * @param right ""
     * @param top ""
     * @param bottom ""
     */
    public Camera(float near, float left, float right, float top, float bottom, int resolutionX, int resolutionY, String file_name) {
        this.near = -1*near;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.file_name = file_name;
        this.image = new BufferedImage(resolutionX, resolutionY, BufferedImage.TYPE_INT_ARGB);
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
        this.pixels = new int[resolutionX * resolutionY];
    }

    // Given a pixel coordinate, get a direction vector pointing to it (DOES IT NEED TO BE NORMALIZED?)
    public Matrix getDirectionVector(int x, int y){
        float pixel_world_x = left + ((right-left)/resolutionX) * x;
        float pixel_world_y = top + ((bottom-top)/resolutionY) * y;
        return new Matrix(new float[][]{{pixel_world_x, pixel_world_y, near, (float) 0}}).transpose(); // ie column vector
    }

    // Given a direction vector of a ray, return true if it collides with an object.
    public boolean ray_collides(Sphere s, Matrix ray){
        return getDiscriminant(s, ray) >= 0;
    }

    public boolean ray_collides(double discriminant){
        return discriminant >= 0;
    }

    /**
     * Gets the minimum 't' value that results in a collision for a given ray and sphere.
     * @param s     sphere in question
     * @param ray   ray in question
     * @return      double representation of t.
     */
    public double getT(Sphere s, Matrix ray){
        Matrix ray2 = s.matrix.simpleInverse(s).multiply(ray);
        float x = ray2.get(0,0);
        float y = ray2.get(1,0);
        float z = ray2.get(2,0);

        // Derived from sphere(ray) where sphere is (X-dx)^2 + (Y-dy)^2 + (Z-dz)^2
        double a = Math.pow(x,2) + Math.pow(y,2) + Math.pow(z, 2);
        double b = (s.pos[0] * x + s.pos[1] * y + s.pos[2] * z);
        double c = Math.pow(s.pos[0], 2) + Math.pow(s.pos[1], 2) + Math.pow(s.pos[2], 2) -1.0;

        double plus = (-1 * b/a + Math.pow(b,2) - a*c)/a;
        double minus = (-1 * b/a - Math.pow(b,2) - a*c)/a;

        return Math.min(plus, minus);
    }

    /**
     * Return the discriminant of the quadratic formula for the collision of a ray and sphere
     * This will indicate if a collision occurred, and if so, is part of future calculations.
     * @param s     Sphere you are interested in. Should be called once for each sphere.
     * @param ray   Ray in question.
     * @return      Double value of discriminant.
     */
    public double getDiscriminant(Sphere s, Matrix ray){
        Matrix temp_debug = s.matrix.simpleInverse(s);
        Matrix ray2 = s.matrix.simpleInverse(s).multiply(ray);
        float x = ray2.get(0,0);
        float y = ray2.get(1,0);
        float z = ray2.get(2,0);

        // Derived from sphere(ray) where sphere is (X-dx)^2 + (Y-dy)^2 + (Z-dz)^2
        double a = Math.pow(x,2) + Math.pow(y,2) + Math.pow(z, 2);
        double b = (s.pos[0] * x + s.pos[1] * y + s.pos[2] * z);
        double c = Math.pow(s.pos[0], 2) + Math.pow(s.pos[1], 2) + Math.pow(s.pos[2], 2) -1.0;

        return Math.pow(b,2) - a*c;
    }

    /**
     * Perform raytracing on each pixel, setting the color appropriately based on the lighting model.
     * @param n number of bounces.
     */
    public void raytrace(int n){
        for (int xpixel = 0; xpixel < resolutionX; xpixel++){
            for (int ypixel = 0; ypixel < resolutionX; ypixel++){
                int position = xpixel + ypixel*resolutionX;
                pixels[position] = rgb_array_to_int(calculate_colour(xpixel, ypixel, 3));


            }
        }
    }

    /**
     * The recursive part of the raytrace algorithm. Calculates ambient, diffuse, specular components, and the recursive reflected component
     * @param xpixel the x pixel to be coloured
     * @param ypixel the y pixel to be coloured
     * @param n number of bounces
     * @return a float array of size 3 on the range [0-1] indicating the intensity of red, green, and blue.
     */
    public float[] calculate_colour(int xpixel, int ypixel, int n){
        if (n == 0) return new float[]{0,0,0}; // base case. Limits bounces.

        Matrix dir_vec = getDirectionVector(xpixel, ypixel);
        for (Sphere s: Raytracer.spheres){
            if (ray_collides(s, dir_vec)){
                float[] ambient = new float[3];
                float[] diffuse = new float[3];
                float[] specular = new float[3];
                float[] reflected = new float[3];

                for (int i = 0; i < 3; i++){
                    ambient[i] = s.ka * Raytracer.canvas.ambient[0] * s.color[i];
                    diffuse[i] = 0;  // TODO
                    specular[i] = 0; // TODO
                    reflected[i] = 0; // TODO
                }

                float[] intensity = new float[3];
                float[] bounce = calculate_colour(xpixel, ypixel, n-1);
                for (int i = 0; i < 3; i++){
                    intensity[i] = Math.min(1, ambient[i] + diffuse[i] + specular[i] + reflected[i] + bounce[i]);
                }

                return intensity;

            }
        }
        return Raytracer.canvas.background;
    }

    public void exportImage() {
        image.setRGB(0, 0, resolutionX, resolutionY, pixels, 0, resolutionX);   // Replace image's data with the pixel array

        try {
            File out = new File(file_name);
            ImageIO.write(image, "png", out); //TODO: No PNG hardcode!
            System.out.println(file_name + " exported successfully.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}