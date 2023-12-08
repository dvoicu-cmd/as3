package src;

/**
 * Class for holding properties of the scene camera.
 */
class Camera {

    private float near;

    private float left;

    private float right;

    private float top;

    private float bottom;

    /**
     * Constructor method
     * @param near
     * @param left
     * @param right
     * @param top
     * @param bottom
     */
    public Camera(float near, float left, float right, float top, float bottom) {
        this.near = near;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

}