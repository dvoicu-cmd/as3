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

    // Given a pixel coordinate, get a direction vector pointing to it
    public Matrix getRayVector(int x, int y){
        float pixel_world_x = left + ((right-left)/resolutionX) * x;
        float pixel_world_y = top + ((bottom-top)/resolutionY) * y;
        //float[] normalized = normalize(new float[]{pixel_world_x, pixel_world_y, near, 0}, 0);
        return new Matrix(new float[][]{new float[]{pixel_world_x, pixel_world_y, near, 0}}).transpose(); // ie column vector
    }

    /**
     * Gets the direction vector between two points a->b
     * @param a starting point
     * @param b ending point
     * @return vector
     */
    public float[] get_dir_vec(float[] a, float[] b){
        assert a.length == 3;
        assert b.length == 3;
        return new float[]{
                b[0] - a[0],
                b[1] - a[1],
                b[2] - a[2]};
    }

    /**
     * Normalize a vector, scaling its magnitude to 1
     * @param vec vector to be normalized
     * @param w homogenized coordinate parameter. 0,1 or null to not append any values.
     * @return normalized vector
     */
    public float[] normalize(float[] vec, Integer w){
        float mag = (float) Math.sqrt(Math.pow(vec[0], 2) + Math.pow(vec[1], 2) + Math.pow(vec[2], 2));
        if (mag == 0) return new float[]{0,0,0,0};

        if (w == null){
            return new float[]{vec[0]/mag, vec[1]/mag, vec[2]/mag};
        }

        return new float[]{vec[0]/mag, vec[1]/mag, vec[2]/mag, w};
    }

    // Given a direction vector of a ray, return true if it collides with an object.
    public boolean ray_collides(Sphere s, Matrix ray, Matrix origin){
        return getT(s, ray, origin) != null;
    }

    /**
     * Gets the minimum 't' value that results in a collision for a given ray and sphere.
     * @param s     sphere in question
     * @param ray   ray in question
     * @return      double representation of t.
     */
    public Matrix getT(Sphere s, Matrix ray, Matrix origin){
        Matrix ray2 = s.matrix.simpleInverse(s).multiply(ray); // Transformed ray into sphere coordinate system
        Matrix origin2 = s.matrix.simpleInverse(s).multiply(origin.transpose()); // origin in sphere coordinate system.

        float x = ray2.get(0,0);
        float y = ray2.get(1,0);
        float z = ray2.get(2,0);

        float ox = origin2.get(0,0);
        float oy = origin2.get(1,0);
        float oz = origin2.get(2,0);

        // QUADRATIC FORMULA
        float a = x*x + y*y + z*z;
        float b = 2*(x*ox + y*oy + z*oz);
        float c = ox*ox + oy*oy + oz*oz -1;

        double discriminant = Math.sqrt(Math.pow(b, 2) - 4* a * c);
        double root1 = (-1* b/(2*a) + discriminant /(2*a));
        double root2 = (-1* b/(2*a) - discriminant /(2*a));
        if (Double.isNaN(discriminant)) return null;

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

        return ray2.multiply(t).add(origin2);
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

        for (Sphere s: Raytracer.spheres){
            if (ray_collides(s, ray, new Matrix(new float[][]{new float[]{0,0,0,1}}))){
                Matrix collision_point = getT(s, ray, new Matrix(new float[][]{ new float[]{0,0,0,1}}));
                Matrix global_collision = s.matrix.multiply(collision_point);
                Matrix N = new Matrix(new float[][]{get_normal(global_collision,s)});

                // ADSR
                float[] ambient = get_ambient(s);
                float[] diffuse = get_diffuse(global_collision, s);
                float[] specular = get_specular(global_collision, s, N);

                // REFLECTION
                float[] ray_bounced_arr = bounce(N, new Matrix(new float[][]{ray.transpose().asArray()}));
                Matrix ray_bounced = new Matrix(new float[][]{ray_bounced_arr}).transpose();
                float[] reflected =  calculate_colour(ray_bounced, n-1, true); //recursive call


                // SUM ALL COMPONENTS
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

    /**
     * Gets ambient lighting for a given pixel, given the sphere it belongs to.
     * @param s Sphere in question
     * @return array describing ambient lighting values
     */
    public float[] get_ambient(Sphere s){
        float[] ambient = new float[3];
        for (int i = 0; i < 3; i++){
            ambient[i] = s.ka * Raytracer.canvas.ambient[i] * s.color[i];

        }
        return ambient;
    }

    /**
     * Gets diffuse lighting for a given pixel, given the sphere it belongs to.
     * @param p Point of collision in WCS
     * @param s Sphere in question
     * @return intensity of diffuse lighting as an array.
     */
    public float[] get_diffuse(Matrix p, Sphere s){
        float[] intensity = new float[]{0,0,0};

        outerLoop:
        for (Light l : Raytracer.lights){


            float[] N = get_normal(p, s);

            // CALCULATE L (point-> light) vector
            float[] L = normalize(get_dir_vec(p.transpose().asArray(), l.pos), null);


            /*
            // CHECK FOR SHADOW RAYS --> Doesn't work 100%
            Matrix L_mtx = new Matrix(new float[][]{L});
            for (Sphere shadow_candidate: spheres){
                if (shadow_candidate == s) continue;
                if (ray_collides(shadow_candidate, L_mtx, p)){
                    continue outerLoop;
                }

            }
             */


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