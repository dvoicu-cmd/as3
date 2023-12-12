package src;

/**
 * Class for determining attributes of sphere objects.
 */
public class Sphere {
    String name;
    float[] pos;
    float[] scale;
    float[] color;
    float ka;
    float kd;
    float ks;
    float kr;
    int spec_exp;
    Matrix matrix;

    public Sphere(String[] args){
        this.name = args[1];
        this.pos = new float[]{Float.parseFloat(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4])};
        this.scale = new float[]{Float.parseFloat(args[5]), Float.parseFloat(args[6]), Float.parseFloat(args[7])};
        this.color = new float[]{Float.parseFloat(args[8]),Float.parseFloat(args[9]),Float.parseFloat(args[10])};
        //this.color = Raytracer.color_to_255(Float.parseFloat(args[8]),Float.parseFloat(args[9]),Float.parseFloat(args[10]));
        this.ka = Float.parseFloat(args[11]);
        this.kd = Float.parseFloat(args[12]);
        this.ks = Float.parseFloat(args[13]);
        this.kr = Float.parseFloat(args[14]);
        this.spec_exp = Integer.parseInt(args[15]);

        // MATRIX REPRESENTATION
        float zero = 0;
        float[][] mtx = new float[4][4];
        mtx[0] = new float[] {scale[0], zero, zero, pos[0]};
        mtx[1] = new float[] {zero, scale[1], zero, pos[1]};
        mtx[2] = new float[] {zero, zero, scale[2], pos[2]};
        mtx[3] = new float[] {zero, zero, zero, (float) 1};
        this.matrix = new Matrix(mtx);
    }

    // JUST FOR TESTING PURPOSES
    public Sphere(float[] c){
        this.color = c;
    }


    int getColorInt(){
        return Raytracer.rgb_array_to_int(color);
    }
}