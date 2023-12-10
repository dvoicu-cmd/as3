package src;
// Tester class I made when something was busted.
// Might be good to build on these as we go.
public class Tester {
    static void t_Sphere_getColorInt(){
        Sphere s = new Sphere(new int[]{255,255,255});
        boolean result = s.getColorInt() == 0xFFFFFFFF;
        System.out.println("t_Sphere_getColorInt " + result);
    }

    static void t2_Sphere_getColorInt(){
        Sphere s = new Sphere(new int[]{18,75,150});
        boolean result = s.getColorInt() == 0xFF124b96;
        System.out.println("t2_Sphere_getColorInt " + result);
    }

    public static void main(String[] args){
        t_Sphere_getColorInt();
        t2_Sphere_getColorInt();
    }
}
