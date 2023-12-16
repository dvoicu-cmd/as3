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
        this.matrix = new Matrix(4,4, Template.IDENTITY);
        this.matrix = this.matrix.translate(pos[0], pos[1], pos[2]);
        this.matrix = this.matrix.scale(scale[0], scale[1], scale[2]);
    }

    // JUST FOR TESTING PURPOSES
    public Sphere(float[] c){
        this.color = c;
    }


    int getColorInt(){
        return Raytracer.rgb_array_to_int(color);
    }
}