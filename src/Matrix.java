package src;

/**
 * Basic Matrix operations.
 */
public class Matrix{

    public final float[][] mat;
    private final int numRow;
    private final int numCol;

    public Matrix(int numRow, int numCol){
        this.mat = new float[numRow][numCol];
        this.numRow = numRow;
        this.numCol = numCol;
    }

    //Deep copy constructor
    public Matrix(float[][] array){
        this.numRow = array.length;
        this.numCol = array[0].length;
        this.mat = new float[numRow][numCol];

        for(int i = 0; i<this.numRow; i++){
            System.arraycopy(array[i], 0, this.mat[i], 0, this.numCol);
        }
    }

    public Matrix(int numRow, int numCol, Template t){
        this.mat = new float[numRow][numCol];
        this.numRow = numRow;
        this.numCol = numCol;


        for (int i = 0; i < numRow; i++){
            for (int j = 0; j < numCol; j++){
                if (t == Template.ZERO || t== Template.HOMOGENOUS){
                    mat[i][j] = 0;
                }
                else if (t == Template.IDENTITY && i==j){
                    mat[i][j] = 1;
                }
            }
        }

        if (t == Template.HOMOGENOUS){
            mat[numRow-1][numCol-1] = 1;
        }
    }


    public float get(int row, int col){
        return this.mat[row][col];
    }

    // Access indexes starting from 0
    public void setValueAt(int row, int col, float value){
        assert row < this.getNumRow() && col < this.getNumCol();
        this.mat[row][col] = value;
    }

    public int getNumRow(){
        return this.numRow;
    }

    public float[] asArray(){
        assert this.getNumRow() == 1;
        float[][] mtx = this.mat;

        return new float[]{mtx[0][0], mtx[0][1], mtx[0][2]};

    }

    static float dot(float[] a, float[] b){
        assert a.length == b.length;

        float sum=0;
        for (int i =0; i < a.length; i++){
            sum += a[i] * b[i];
        }
        return sum;
    }
    public int getNumCol(){
        return this.numCol;
    }

    public Matrix add(Matrix other){
        //assert this.isEqualSize(other);
            Matrix output = new Matrix(this.getNumRow(), this.getNumCol());
            for(int i = 0; i < this.getNumRow(); i++){
                for(int j = 0; j < this.getNumCol(); j++){
                    float value = this.get(i,j) + other.get(i,j);
                    output.setValueAt(i,j,value);
                }
            }
            return output;
    }

    public Matrix subtract(Matrix other){
        Matrix output = new Matrix(this.getNumRow(), this.getNumCol());
        for(int i = 0; i < this.getNumRow(); i++){
            for(int j = 0; j < this.getNumCol(); j++){
                float value = this.get(i,j) - other.get(i,j);
                output.setValueAt(i,j,value);
            }
        }
        return output;
    }

    public Matrix multiply(float scalar){
        Matrix ret = new Matrix(this.mat);
        for (int i = 0; i < this.getNumRow(); i++){
            for (int j = 0; j < this.getNumCol(); j++){
                ret.setValueAt(i,j, ret.get(i,j) * scalar);
            }
        }
        return ret;
    }
    public Matrix multiply(Matrix other){
        //assert this.getNumCol() == other.getNumRow();
        int m = this.getNumRow();
        int n = other.getNumCol();
        int p = other.getNumRow();

        Matrix output = new Matrix(m,n);
        for (int i = 0; i < m; i++){
            for (int j = 0; j < n; j++) {
                float sum = 0;
                for(int k = 0; k < p; k++){
                    sum += this.get(i,k) * other.get(k,j);
                }
                output.setValueAt(i,j, sum);
            }
        }
        return output;

    }

    public Matrix transpose(){
        //Rows on mat --> Cols on transposed, Cols on mat --> Rows on transposed
        float[][] transposed = new float[this.numCol][this.numRow];
        for (int i = 0; i<this.numRow; i++){
            for (int j = 0; j < this.numCol; j++){
                transposed[j][i] = mat[i][j];
            }
        }
        //Output reference to transposed operation as obj ref.
        return new Matrix(transposed);
    }

    // Inverses the transforms of a given sphere given that no rotations occurred.
    public Matrix simpleInverse(Sphere s){
        Matrix ret = new Matrix(this.mat);

        ret.setValueAt(0,0, 1/s.scale[0]);
        ret.setValueAt(1,1, 1/s.scale[1]);
        ret.setValueAt(2,2, 1/s.scale[2]);

       //ret.setValueAt(0,3, -1 * s.pos[0]/s.scale[0]);
       //ret.setValueAt(1,3, -1 * s.pos[1]/s.scale[1]);
       //ret.setValueAt(2,3, -1 * s.pos[2]/s.scale[2]);

        ret.setValueAt(0,3, -1 * s.pos[0]/s.scale[0]);
        ret.setValueAt(1,3, -1 * s.pos[1]/s.scale[1]);
        ret.setValueAt(2,3, -1 * s.pos[2]/s.scale[2]);

        return ret;
    }

    public Matrix translate(float x, float y, float z) {
        assert this.getNumRow() == 4 && this.getNumCol() == 4;

        Matrix transl_mtx = new Matrix(4,4,Template.IDENTITY);
        transl_mtx.setValueAt(0,3, x);
        transl_mtx.setValueAt(1,3, y);
        transl_mtx.setValueAt(2,3, z);
        return this.multiply(transl_mtx);
    }

    public Matrix scale(float x, float y, float z) {
        assert this.getNumRow() == 4 && this.getNumCol() == 4;
            Matrix scaling_mtx = new Matrix(4,4, Template.IDENTITY);
            scaling_mtx.setValueAt(0,0, x);
            scaling_mtx.setValueAt(1,1, y);
            scaling_mtx.setValueAt(2,2, z);
            return this.multiply(scaling_mtx);
    }

    public void print(){
        for(float[] row: this.mat){
            for(float element: row) {
                System.out.print(element+ ", ");
            }
            System.out.println();
        }
    }

}