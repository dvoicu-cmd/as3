package src.MV;

class Matrix{

    private float[][] mat;
    private int numRow;
    private int numCol;

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
            for(int j = 0; j<this.numCol; j++){
                this.mat[i][j] = array[i][j];
            }
        }
    }

    public float getValueAt(int row, int col){
        return this.mat[row][col];
    }

    // Access indexes starting from 0
    public void setValueAt(int row, int col, float value){
        if(row >= this.getNumRow() || col >= this.getNumCol()){
            return; //Don't do anything, as provided values are out of bounds.
        }
        else {
            this.mat[row][col] = value;
        }
    }

    public int getNumRow(){
        return this.numRow;
    }

    public int getNumCol(){
        return this.numCol;
    }

    public boolean isEqualSize(Matrix other){
        if(other.getNumCol() != this.getNumCol() || other.getNumRow() != this.getNumCol() ){
            return false;
        }
        else return true;
    }

    public Matrix add(Matrix other){
        if(this.isEqualSize(other)){
            Matrix output = new Matrix(this.getNumCol(), this.getNumRow());
            for(int i = 0; i < this.getNumRow(); i++){
                for(int j = 0; j < this.getNumCol(); j++){
                    float value = this.getValueAt(i,j) + other.getValueAt(i,j);
                    output.setValueAt(i,j,value);
                }
            }
            return output;
        }
        else return null;
    }

    public Matrix subtract(Matrix other){
        if(this.isEqualSize(other)){
            Matrix output = new Matrix(this.getNumCol(), this.getNumRow());
            for(int i = 0; i < this.getNumRow(); i++){
                for(int j = 0; j < this.getNumCol(); j++){
                    float value = this.getValueAt(i,j) - other.getValueAt(i,j);
                    output.setValueAt(i,j,value);
                }
            }
            return output;
        }
        else return null;
    }

    public Matrix multiply(Matrix other){
        int m = this.getNumRow();
        int n = other.getNumCol();
        int p = other.getNumRow();


        if(this.getNumCol() == other.getNumRow()){ //Only multiply if col = row
            Matrix output = new Matrix(m,n);

            for (int i = 0; i < m; i++){
                for (int j = 0; j < n; j++) {
                    float sum = 0;
                    for(int k = 0; k < p; k++){
                        sum += this.getValueAt(i,k) * other.getValueAt(k,j);
                    }
                    output.setValueAt(i,j, sum);
                }
            }
            return output;
        }
        else return null;
    }

    //Uhhhhhhhh.
    public Matrix invert4x4(){
        if(this.getNumCol() != 4 || this.getNumRow() != 4){
            return null;
        }
        else{
            float[][] m = this.mat;
            float SubFactor00 = m[2][2] * m[3][3] - m[3][2] * m[2][3];
            float SubFactor01 = m[2][1] * m[3][3] - m[3][1] * m[2][3];
            float SubFactor02 = m[2][1] * m[3][2] - m[3][1] * m[2][2];
            float SubFactor03 = m[2][0] * m[3][3] - m[3][0] * m[2][3];
            float SubFactor04 = m[2][0] * m[3][2] - m[3][0] * m[2][2];
            float SubFactor05 = m[2][0] * m[3][1] - m[3][0] * m[2][1];
            float SubFactor06 = m[1][2] * m[3][3] - m[3][2] * m[1][3];
            float SubFactor07 = m[1][1] * m[3][3] - m[3][1] * m[1][3];
            float SubFactor08 = m[1][1] * m[3][2] - m[3][1] * m[1][2];
            float SubFactor09 = m[1][0] * m[3][3] - m[3][0] * m[1][3];
            float SubFactor10 = m[1][0] * m[3][2] - m[3][0] * m[1][2];
            float SubFactor11 = m[1][1] * m[3][3] - m[3][1] * m[1][3];
            float SubFactor12 = m[1][0] * m[3][1] - m[3][0] * m[1][1];
            float SubFactor13 = m[1][2] * m[2][3] - m[2][2] * m[1][3];
            float SubFactor14 = m[1][1] * m[2][3] - m[2][1] * m[1][3];
            float SubFactor15 = m[1][1] * m[2][2] - m[2][1] * m[1][2];
            float SubFactor16 = m[1][0] * m[2][3] - m[2][0] * m[1][3];
            float SubFactor17 = m[1][0] * m[2][2] - m[2][0] * m[1][2];
            float SubFactor18 = m[1][0] * m[2][1] - m[2][0] * m[1][1];

            float[][] Inverse = new float[4][4];

            Inverse[0][0] = (m[1][1] * SubFactor00 - m[1][2] * SubFactor01 + m[1][3] * SubFactor02);
            Inverse[0][1] = - (m[1][0] * SubFactor00 - m[1][2] * SubFactor03 + m[1][3] * SubFactor04);
            Inverse[0][2] =  (m[1][0] * SubFactor01 - m[1][1] * SubFactor03 + m[1][3] * SubFactor05);
            Inverse[0][3] =  - (m[1][0] * SubFactor02 - m[1][1] * SubFactor04 + m[1][2] * SubFactor05);

            Inverse[1][0] =  - (m[0][1] * SubFactor00 - m[0][2] * SubFactor01 + m[0][3] * SubFactor02);
            Inverse[1][1] = (m[0][0] * SubFactor00 - m[0][2] * SubFactor03 + m[0][3] * SubFactor04);
            Inverse[1][2] =  - (m[0][0] * SubFactor01 - m[0][1] * SubFactor03 + m[0][3] * SubFactor05);
            Inverse[1][3] =  (m[0][0] * SubFactor02 - m[0][1] * SubFactor04 + m[0][2] * SubFactor05);

            Inverse[2][0] =  (m[0][1] * SubFactor06 - m[0][2] * SubFactor07 + m[0][3] * SubFactor08);
            Inverse[2][1] =  - (m[0][0] * SubFactor06 - m[0][2] * SubFactor09 + m[0][3] * SubFactor10);
            Inverse[2][2] = (m[0][0] * SubFactor11 - m[0][1] * SubFactor09 + m[0][3] * SubFactor12);
            Inverse[2][3] =  - (m[0][0] * SubFactor08 - m[0][1] * SubFactor10 + m[0][2] * SubFactor12);

            Inverse[3][0] =  - (m[0][1] * SubFactor13 - m[0][2] * SubFactor14 + m[0][3] * SubFactor15);
            Inverse[3][1] = (m[0][0] * SubFactor13 - m[0][2] * SubFactor16 + m[0][3] * SubFactor17);
            Inverse[3][2] =  - (m[0][0] * SubFactor14 - m[0][1] * SubFactor16 + m[0][3] * SubFactor18);
            Inverse[3][3] = (m[0][0] * SubFactor15 - m[0][1] * SubFactor17 + m[0][2] * SubFactor18);

            float determinant = m[0][0] * Inverse[0][0] + m[0][1] * Inverse[0][1] + m[0][2] * Inverse[0][2] + m[0][3] * Inverse[0][3];

            float iDeterminant = (float) (1.0/determinant); //Hopefully the casting does not mess up the solution
            for(int i = 0; i<4; i++){
                for(int j = 0; j<4; j++){
                    Inverse[i][j] = Inverse[i][j]*iDeterminant;
                }
            }

            //Now load into matrix object and export address to obj.
            Matrix inverseOutput = new Matrix(Inverse);
            return inverseOutput;
        }
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