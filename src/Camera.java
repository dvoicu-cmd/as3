package src;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

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
    public Matrix getRayVector(int x, int y){
        float pixel_world_x = left + ((right-left)/resolutionX) * x;
        float pixel_world_y = top + ((bottom-top)/resolutionY) * y;
        float[] normalized = normalize(new float[]{pixel_world_x, pixel_world_y, near, 0}, 0);
        return new Matrix(new float[][]{normalized}).transpose(); // ie column vector
    }

    public float[] get_dir_vec(float[] a, float[] b){
        assert a.length == 3;
        assert b.length == 3;
        return new float[]{
                b[0] - a[0],
                b[1] - a[1],
                b[2] - a[2]};
    }

    public float[] normalize(float[] vec, Integer w){
        float mag = (float) Math.sqrt(Math.pow(vec[0], 2) + Math.pow(vec[1], 2) + Math.pow(vec[2], 2));
        if (mag == 0) return new float[]{0,0,0,0};

        if (w == null){
            return new float[]{vec[0]/mag, vec[1]/mag, vec[2]/mag};
        }

        return new float[]{vec[0]/mag, vec[1]/mag, vec[2]/mag, w};
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
    public float getT(Sphere s, Matrix ray){
        Matrix ray2 = s.matrix.simpleInverse(s).multiply(ray);
        float x = ray2.get(0,0);
        float y = ray2.get(1,0);
        float z = ray2.get(2,0);

        // Derived from sphere(ray) where sphere is (X-dx)^2 + (Y-dy)^2 + (Z-dz)^2
        double a = Math.pow(x,2) + Math.pow(y,2) + Math.pow(z, 2);
        double b = (s.pos[0] * x + s.pos[1] * y + s.pos[2] * z);
        double c = Math.pow(s.pos[0], 2) + Math.pow(s.pos[1], 2) + Math.pow(s.pos[2], 2) -1.0;

        double discriminant = Math.sqrt(Math.pow(b, 2) - a * c);
        double root1 = (-1* b/a + discriminant /a);
        double root2 = (-1* b/a - discriminant /a);

        return -1 * (float)  Math.max(root1, root2);
        //return (float)  Math.min(root1, root2); // This was the expected one but empirically did not work
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
                pixels[position] = rgb_array_to_int(calculate_colour(xpixel, ypixel, 3, false));


            }
        }
    }

    /**
     * The recursive part of the raytrace algorithm. Calculates ambient, diffuse, specular components, and the recursive reflected component
     * @param xpixel the x pixel to be coloured
     * @param ypixel the y pixel to be coloured
     * @param n number of bounces
     * @param is_reflected Used to distinguish between rays that hit the background directly from the camera, and those that hit the background after a reflection. Initially should be False
     * @return a float array of size 3 on the range [0-1] indicating the intensity of red, green, and blue.
     */
    public float[] calculate_colour(int xpixel, int ypixel, int n, boolean is_reflected){
        if (n == 0) return new float[]{0,0,0}; // base case. Limits bounces.

        Matrix ray = getRayVector(xpixel, ypixel);
        for (Sphere s: Raytracer.spheres){
            if (ray_collides(s, ray)){
                Matrix collision_point = ray.multiply(getT(s, ray));

                float[] ambient = get_ambient(s);
                float[] diffuse = get_diffuse(collision_point, s);
                float[] specular = get_specular();
                //float[] reflected = calculate_colour(xpixel, ypixel, n-1, true); //recursive call
                float[] reflected = new float[]{0,0,0};

                float[] intensity = new float[3];
                for (int i = 0; i < 3; i++){
                    intensity[i] = Math.min(1, ambient[i] + diffuse[i] + specular[i] + reflected[i]);
                }

                return intensity;

            }
        }

        // No collision with any sphere.
        if(is_reflected) {
            return new float[]{0,0,0};
        }
        return Raytracer.canvas.background;
    }

    public float[] get_ambient(Sphere s){
        float[] ambient = new float[3];
        for (int i = 0; i < 3; i++){
            ambient[i] = s.ka * Raytracer.canvas.ambient[0] * s.color[i];

        }
        return ambient;
    }

    public float[] get_diffuse(Matrix p, Sphere s){
        // TODO: Shadow rays ;-;
        float[] intensity = new float[]{0,0,0};
        for (Light l : Raytracer.lights){
            // CALCULATE NORMAL
            float nx = p.get(0,0) / s.scale[0];
            float ny = p.get(1,0) / s.scale[1];
            float nz = p.get(2,0) / s.scale[2];
            float[] N = normalize(new float[]{nx, ny, nz}, null);

            // CALCULATE L (point-> light) vector
            float[] L = normalize(get_dir_vec(p.transpose().asArray(), l.pos), null);
            //float dot = Math.max(0, Matrix.dot(N, L));
            float dot = Matrix.dot(N, L);

            for (int i =0; i < 3; i++){
                intensity[i] += s.kd * l.intensity[i] * dot * s.color[i];
            }

        }

        return intensity;
    }

    public float[] get_specular(){
        return new float[]{0,0,0};
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