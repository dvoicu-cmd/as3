package src;

/**
 * Class for determining light source
 */
class Light {
    String name;
    float pos[];
    float intensity[];

    public Light(String[] args){
        this.name = args[1];
        this.pos = new float[]{Float.parseFloat(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4])};
        this.intensity = new float[]{Float.parseFloat(args[5]), Float.parseFloat(args[6]), Float.parseFloat(args[7])};
    }

}

