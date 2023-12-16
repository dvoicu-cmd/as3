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
        //float[] normalized = normalize(new float[]{pixel_world_x, pixel_world_y, near, 0}, 0);
        return new Matrix(new float[][]{new float[]{pixel_world_x, pixel_world_y, near, 0}}).transpose(); // ie column vector
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
        //return getDiscriminant(s, ray) >= 0;
        return getT(s, ray) != null;
        //return true;
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
    public Matrix getT(Sphere s, Matrix ray){
        Matrix ray2 = s.matrix.simpleInverse(s).multiply(ray);

        Matrix origin2 = s.matrix.simpleInverse(s).multiply(new Matrix(new float[][]{ new float[]{0,0,0,1}}).transpose());


        Matrix pos = s.matrix.simpleInverse(s).multiply(new Matrix (new float[][]{{s.pos[0],s.pos[1],s.pos[2],0}}).transpose());
        float x = ray2.get(0,0);
        float y = ray2.get(1,0);
        float z = ray2.get(2,0);

        float ox = origin2.get(0,0);
        float oy = origin2.get(1,0);
        float oz = origin2.get(2,0);

        float a = x*x + y*y + z*z;
        float b = 2*(x*ox + y*oy + z*oz);
        float c = ox*ox + oy*oy + oz*oz -1;
        /*
       OLD

        // Derived from sphere(ray) where sphere is (X-dx)^2 + (Y-dy)^2 + (Z-dz)^2
        double a = Math.pow(x,2) + Math.pow(y,2) + Math.pow(z, 2);
        //double b = 0;
        //double c = -1;
        double b = 2 * (s.pos[0] * x + s.pos[1] * y + s.pos[2] * z);
        double c = Math.pow(s.pos[0], 2) + Math.pow(s.pos[1], 2) + Math.pow(s.pos[2], 2) -1.0;
        */

        double discriminant = Math.sqrt(Math.pow(b, 2) - 4* a * c);
        double root1 = (-1* b/(2*a) + discriminant /(2*a));
        double root2 = (-1* b/(2*a) - discriminant /(2*a));
        if (Double.isNaN(discriminant)) return null;

        // Select the smallest value, unless it is negative. Answer may be negative if both are negative. In this case, no solution.
        if (Math.max(root1, root2) < 0){
            return null;
        }

        float t;
        if (root1 > 0 && root2 > 0){
            t = (float) Math.min(root1, root2);
        }
        else {
            t = (float) Math.max(root1, root2);
        }

        Matrix ret = ray2.multiply(t).add(origin2);
        return ret;


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
        //double b = (s.pos[0] * x + s.pos[1] * y + s.pos[2] * z);
        double b = 0;
        double c = -1;
        //double c = Math.pow(s.pos[0], 2) + Math.pow(s.pos[1], 2) + Math.pow(s.pos[2], 2) -1.0;

        return Math.pow(b,2) - a*c;
    }

    /**
     * Perform raytracing on each pixel, setting the color appropriately based on the lighting model.
     * @param n number of bounces.
     */
    public void raytrace(int n){
        for (int xpixel = 0; xpixel < resolutionX; xpixel++){
            for (int ypixel = 0; ypixel < resolutionY; ypixel++){
                if (xpixel == 401 & ypixel == 214){
                    System.out.println("REMOVE ME");
                }
                int position = xpixel + ypixel*resolutionX;
                pixels[position] = rgb_array_to_int(calculate_colour(getRayVector(xpixel, ypixel), 3, false));


            }
        }
    }

    /**
     * The recursive part of the raytrace algorithm. Calculates ambient, diffuse, specular components, and the recursive reflected component
     * @param n number of bounces
     * @param is_reflected Used to distinguish between rays that hit the background directly from the camera, and those that hit the background after a reflection. Initially should be False
     * @return a float array of size 3 on the range [0-1] indicating the intensity of red, green, and blue.
     */
    public float[] calculate_colour(Matrix ray, int n, boolean is_reflected){
        if (n == 0) return new float[]{0,0,0}; // base case. Limits bounces.

        //Matrix ray_norm = new Matrix(new float[][]{normalize(ray.transpose().asArray(), 0)}).transpose();


        for (Sphere s: Raytracer.spheres){
            if (ray_collides(s, ray)){
                Matrix collision_point = getT(s, ray);
                //Matrix global_collision = s.matrix.simpleInverse(s).transpose().multiply(collision_point);
                Matrix global_collision = s.matrix.multiply(collision_point);
                Matrix N = new Matrix(new float[][]{get_normal(global_collision,s)});

                float[] ambient = get_ambient(s);
                float[] diffuse = get_diffuse(global_collision, s);
                float[] specular = get_specular(global_collision, s, N);


                float[] ray_bounced_arr = bounce(N, new Matrix(new float[][]{ray.transpose().asArray()}));
                Matrix ray_bounced = new Matrix(new float[][]{ray_bounced_arr}).transpose();
                float[] reflected =  calculate_colour(ray_bounced, n-1, true); //recursive call

                //float[] reflected = new float[]{0,0,0};

                float[] intensity = new float[3];
                for (int i = 0; i < 3; i++){
                    intensity[i] = Math.min(1, ambient[i] + diffuse[i] + specular[i] + s.kr * reflected[i]);
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
            ambient[i] = s.ka * Raytracer.canvas.ambient[i] * s.color[i];

        }
        return ambient;
    }

    public float[] get_diffuse(Matrix p, Sphere s){
        float[] intensity = new float[]{0,0,0};
        for (Light l : Raytracer.lights){
            float[] N = get_normal(p, s);

            // CALCULATE L (point-> light) vector
            float[] L = normalize(get_dir_vec(p.transpose().asArray(), l.pos), null);
            float dot = Math.max(0, Matrix.dot(N, L));
            //float dot = Matrix.dot(N, L);
            //float dot = 1; // TODO remove this

            for (int i =0; i < 3; i++){
                intensity[i] += s.kd * l.intensity[i] * dot * s.color[i];
            }

        }

        return intensity;
    }

    public float[] get_normal(Matrix p, Sphere s){
        //float nx = p.get(0,0) / s.scale[0];
        //float ny = p.get(1,0) / s.scale[1];
        //float nz = p.get(2,0) / s.scale[2];

        float nx = (p.get(0,0) - s.pos[0]) / s.scale[0] / s.scale[0];
        float ny = (p.get(1,0) - s.pos[1]) / s.scale[1] / s.scale[1];
        float nz = (p.get(2,0) - s.pos[2]) / s.scale[2] / s.scale[2];
        return normalize(new float[]{nx, ny, nz}, null);
    }

    public float[] get_specular(Matrix p, Sphere s, Matrix N){
        float[] intensity = new float[]{0,0,0};

        for (Light l : Raytracer.lights) {
            float[] L_vec = normalize(get_dir_vec(p.transpose().asArray(), l.pos), null);
            Matrix L = new Matrix(new float[][]{L_vec});

            float[] r = normalize(bounce(N, L), 0);
            float[] v = normalize(get_dir_vec(p.transpose().asArray(), new float[]{0,0,0}), 0);

            for (int i =0; i<3; i++){
                intensity[i] += (float) (s.ks * l.intensity[i] * Math.pow(Math.max(0, Matrix.dot(r,v)), s.spec_exp));
            }
        }

        return intensity;
    }

    public float[] bounce(Matrix N, Matrix L){
        float scalar = (float) 2.0 * Matrix.dot(N.asArray(), L.asArray());
        return normalize(N.multiply(scalar).subtract(L).asArray(), 0);
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