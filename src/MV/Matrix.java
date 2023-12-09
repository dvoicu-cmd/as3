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
        return null;
    }

    public Matrix invert3x3(){
        return null;
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