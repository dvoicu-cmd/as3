package src;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class for holding properties of the scene camera.
 */
class Camera {
    private BufferedImage image;
    private final String file_name;
    private final float near;
    private final float left;
    private final float right;
    private final float top;
    private final float bottom;

    private final int resolutionX;
    private final int resolutionY;
    private final int[] pixels;

    double lowest_discriminant = 99999; // TEMP DEBUG

    /**
     * Constructor method
     * @param near
     * @param left
     * @param right
     * @param top
     * @param bottom
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
        return new Matrix(new float[][]{{pixel_world_x, pixel_world_y, near, (float) 0}}).transpose();
    }

    // Given a direction vector of a ray, return true if it collides with an object.
    // **CURRENTLY WRITTEN ASSUMING NO ELLIPSOIDS!!!**
    public boolean ray_collides(Sphere s, Matrix vec){
        Matrix vec2 = s.matrix.invert4x4().multiply(vec); // Transform the ray into the coordinate system of the sphere.
        float x = vec2.get(0,0);
        float y = vec2.get(1,0);
        float z = vec2.get(2,0);

        // Derived from sphere(ray) where sphere is (X-dx)^2 + (Y-dy)^2 + (Z-dz)^2
        double a = Math.pow(x,2) + Math.pow(y,2) + Math.pow(z, 2);
        double b = (s.pos[0] * x + s.pos[1] * y + s.pos[2] * z);
        double c = Math.pow(s.pos[0], 2) + Math.pow(s.pos[1], 2) + Math.pow(s.pos[2], 2) -1.0;

        double discriminant = Math.pow(b,2) - a*c;
        if (discriminant < lowest_discriminant) {
            lowest_discriminant = discriminant;
        }

        return discriminant >= 0;
    }


    /**
    public double get_discriminant(Sphere s, float[] vec){
        // Derived from sphere(ray) where sphere is (X-dx)^2 + (Y-dy)^2 + (Z-dz)^2
        double a = Math.pow(vec[0],2) + Math.pow(vec[1],2) + Math.pow(vec[2], 2);
        double b = -2 * (s.pos[0] * vec[0] + s.pos[1] * vec[1] + s.pos[2] * vec[2]);
        double c = Math.pow(s.pos[0], 2) + Math.pow(s.pos[1], 2) + Math.pow(s.pos[2], 2) -1;

        double discriminant = Math.pow(b,2) - a*c;
        return discriminant;
    }

    public int discriminant_visualization(double discriminant){
        if (discriminant > 0) return 0xFFFFFFFF;
        if(discriminant == 0) return 0xFFFF0000;

        int white = (int) Math.floor(discriminant / -729.0) * 256;
        return Raytracer.rgb_array_to_int(new int[] {white, white, white});
    }
*/

    public void apply_background_color(){
        for (int x = 0; x < resolutionX; x++){
            for (int y =0; y < resolutionY; y++){
                pixels[x + y*resolutionX] = Raytracer.canvas.getBackgroundColorInt();
            }
        }
    }

    // This will be the algorithm itself.
    // Currently it does no ray bouncing at all, consider it for hit detection testing right now.
    public void raytrace(){
        for (int xpixel = 0; xpixel < resolutionX; xpixel++){
            for (int ypixel = 0; ypixel < resolutionX; ypixel++){
                int position = xpixel + ypixel*resolutionX;

                Matrix dir_vec = getDirectionVector(xpixel, ypixel);
                for (Sphere s: Raytracer.spheres){
                    if (ray_collides(s, dir_vec)){
                        pixels[position] = s.getColorInt();
                    }
                }
            }
        }
        System.out.println("Lowest discrim: " + lowest_discriminant);
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