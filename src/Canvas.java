package src;

/**
 * Class for canvas
 */
class Canvas {
    public final float[] background;
    public final float[] ambient;

    public Canvas(float[] background, float[] ambient) {
        this.background = background;
        this.ambient = ambient;
    }

    int getBackgroundColorInt(){
        return Raytracer.rgb_array_to_int(background);
    }
}