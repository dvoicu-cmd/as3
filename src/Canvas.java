package src;

/**
 * Class for canvas
 */
class Canvas {
    private final int[] background;
    private final float[] ambient;

    public Canvas(int[] background, float[] ambient) {
        this.background = background;
        this.ambient = ambient;
    }

    int getBackgroundColorInt(){
        return Raytracer.rgb_array_to_int(background);
    }
}