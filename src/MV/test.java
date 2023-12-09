package src.MV;

class Test{
    public static void main(String[] args) {
        Matrix mat1 = new Matrix(2,3);
        Matrix mat2 = new Matrix(3,2);

        mat1.setValueAt(0,0, 1);
        mat1.setValueAt(0,1, 2);
        mat1.setValueAt(0,2,3);
        mat1.setValueAt(1,0,4);
        mat1.setValueAt(1,1,5);
        mat1.setValueAt(1,2,6);

        mat2.setValueAt(0,0,7);
        mat2.setValueAt(0,1,10);
        mat2.setValueAt(1,0,8);
        mat2.setValueAt(1,1,11);
        mat2.setValueAt(2,0,9);
        mat2.setValueAt(2,1,12);




        Matrix mat3 = mat1.multiply(mat2);

        System.out.println("Mat1");
        mat1.print();
        System.out.println("Mat2");
        mat2.print();
        System.out.println("Mat3");
        mat3.print();
    }
}