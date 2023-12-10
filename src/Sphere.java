package src;

/**
 * Class for determining attributes of sphere objects.
 */
class Sphere {
    String name;
    float[] pos;
    float[] scale;
    int[] color;
    float ka;
    float kd;
    float ks;
    float kr;
    int spec_exp;

    public Sphere(String[] args){
        this.name = args[1];
        this.pos = new float[]{Float.parseFloat(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4])};
        this.scale = new float[]{Float.parseFloat(args[5]), Float.parseFloat(args[6]), Float.parseFloat(args[7])};
        this.color = Raytracer.color_to_255(Float.parseFloat(args[8]),Float.parseFloat(args[9]),Float.parseFloat(args[10]));
        this.ka = Float.parseFloat(args[11]);
        this.kd = Float.parseFloat(args[12]);
        this.ks = Float.parseFloat(args[13]);
        this.kr = Float.parseFloat(args[14]);
        this.spec_exp = Integer.parseInt(args[15]);
    }

    public Sphere(int[] c){
        this.color = c;
    }

    int getColorInt(){
        return Raytracer.rgb_array_to_int(color);
    }
}