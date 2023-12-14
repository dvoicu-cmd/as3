package src;
// Tester class I made when something was busted.
// Might be good to build on these as we go.
public class Tester {
    static void t_Sphere_getColorInt(){
        Sphere s = new Sphere(new float[]{1,1,1});
        boolean result = s.getColorInt() == 0xFFFFFFFF;
        System.out.println("t_Sphere_getColorInt " + result);
    }

    static void t2_Sphere_getColorInt(){
        Sphere s = new Sphere(new float[]{18,75,150});
        boolean result = s.getColorInt() == 0xFF124b96;
        System.out.println("t2_Sphere_getColorInt " + result);
    }

    static void simple_inverse_tester(){
        String[] argsArr = new String[16];
        for (int i = 0; i < argsArr.length; i++){
            argsArr[i] = "0";
        }
        argsArr[2] = "2"; //x pos
        argsArr[3] = "3"; //y pos
        argsArr[4] = "3"; //z pos

        argsArr[5] = "4"; //x scale
        argsArr[6] = "-2";//y scale
        argsArr[7] = "3"; //z scale

        Sphere s = new Sphere(argsArr);
        Matrix test = s.matrix.simpleInverse(s);
        Matrix identity = test.multiply(s.matrix); //Should be identity
        System.out.println("DEBUG BREAKPOINT HANDLE");

    }

    static void matrix_mul_tester_1(){
        Matrix first = new Matrix(new float[][]{{1,2,3,4},{0,2,4,1},{3,2,-1,1},{4,2,-2,9}});
        Matrix second = new Matrix(new float[][]{{-1,4,-3,6},{2,2,9,9},{2,-2,-10,14},{24,2,-2,9}});
        Matrix result = first.multiply(second);
        System.out.println("DEBUG BREAKPOINT HANDLE");
    }

    static void matrix_mul_tester_2(){
        Matrix first = new Matrix(new float[][]{{1,2,3,4},{0,2,4,1},{3,2,-1,1},{4,2,-2,9}});
        Matrix second = new Matrix(new float[][]{{-1,4,-3,0}}).transpose();
        Matrix result = first.multiply(second);
        result.print();
    }

    public static void main(String[] args){
        t_Sphere_getColorInt();
        t2_Sphere_getColorInt();
        simple_inverse_tester();
        matrix_mul_tester_1();
        matrix_mul_tester_2();
    }
}
