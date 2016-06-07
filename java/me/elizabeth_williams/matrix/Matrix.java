package me.elizabeth_williams.matrix;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Matrix class for performing operations.
 * Created by a0 on 6/3/16.
 */
public class Matrix {

    //number of rows
    private int rows;

    //number of columns
    private int cols;

    //2D array of values in matrix
    double[][] matrix2DArray;

    /**
     * Constructing from activity
     * @param values
     * @param r
     * @param c
     */
    public Matrix(List<Double> values, int r, int c){
        rows = r;
        cols = c;
        matrix2DArray = new double[rows][cols];
        int valueIndex = 0;
        //TODO populate 2d array with values
        for(int i = 0; i < r; i++){//loop through rows
            for(int j = 0; j < c; j++){ //loop through cols
                matrix2DArray[i][j] = values.get(valueIndex);
                valueIndex++;
            }
        }

    }

    /**
     * Alternative constructor (constructing from matrix class)
     * @param values
     * @param r
     * @param c
     */
    public Matrix(double[][] values, int r, int c){
        matrix2DArray = values;
        rows = r;
        cols = c;

    }



    /**
     * Add two matrices.
     * @precondition otherMatrix is of same dimensions as this matrix. If not, returns null
     * @param otherMatrix
     * @return result of matrix addition, null if invalid operation
     */
    public Matrix add(Matrix otherMatrix){
        if(this.rows != otherMatrix.rows || this.cols != otherMatrix.cols){
            return null;
        }

        double[][] resultArr = new double[this.rows][this.cols];

        for(int i = 0; i < rows; i++ ){
            for(int j = 0; j < cols; j++){
                resultArr[i][j] = this.matrix2DArray[i][j] + otherMatrix.matrix2DArray[i][j];
            }
        }

       return new Matrix(resultArr, this.rows, this.cols);
    }

    /**
     * Multiply two matrices.
     * @precondition column's in this matrix == row's in otherMatrix
     * @param otherMatrix
     * @return
     */
    public Matrix multiply(Matrix otherMatrix){
        //TODO
        return null;
    }


    /**
     * Subtract two matrices.
     * @precondition otherMatrix is of same dimensions as this matrix
     * @param otherMatrix
     * @return
     */
    public Matrix subtract(Matrix otherMatrix){
        //TODO
        if(this.rows != otherMatrix.rows || this.cols != otherMatrix.cols){
            return null;
        }

        double[][] resultArr = new double[this.rows][this.cols];

        for(int i = 0; i < rows; i++ ){
            for(int j = 0; j < cols; j++){
                resultArr[i][j] = this.matrix2DArray[i][j] - otherMatrix.matrix2DArray[i][j];
            }
        }

        return new Matrix(resultArr, this.rows, this.cols);

    }

    /**
     * Scalar multiplication
     * @param n scalar
     * @return result of scalar multiplication
     */
    public Matrix scalarMultiply(double n){
        double[][] newArray = new double[this.rows][this.cols];
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                newArray[i][j] = this.matrix2DArray[i][j] * n;
            }
        }
        return new Matrix(newArray, this.rows, this.cols);
    }

    /**
     * Inverse of a matrix
     * @return
     */
    public Matrix inverse(){
        //TODO
        return null;
    }

    /**
     * Rank of a matrix
     * @return
     */
    public Matrix matrixRank(){
        return null;
    }

    /**
     * Transpose the matrix
     * @return transposed matrix
     */
    public Matrix transpose(){
        double[][] transpose = new double[this.cols][this.rows];
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                transpose[j][i] = this.matrix2DArray[i][j];

            }
        }

        for(int i = 0; i < this.cols; i++){
            for(int j = 0; j < this.rows; j++){
                Log.i("new", String.valueOf(transpose[i][j]));
            }
        }

        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                Log.i("old", String.valueOf(this.matrix2DArray[i][j]));
            }
        }

        return new Matrix(transpose, this.cols, this.rows);

    }

    /**
     * Calculate determinant of matrix
     * @return
     */
    public double determinant(){
        //TODO
        return 0;
    }

    /**
     * getter for rows of matrix
     * @return rows
     */
    public int getRows(){
        return rows;
    }

    /**
     * setter for rows of matrix
     * @return columns
     */
    public int getCols(){
        return cols;
    }

    /**
     * Take contents of matrix and represent as a list (0,0), (0,1)... (1,0), (1,1)
     * To be used by the activity class so it doesn't have to do actual matrix calculations
     *
     */
    public ArrayList<Double> matrixToList(){
        ArrayList<Double> values = new ArrayList<Double>();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                values.add(matrix2DArray[i][j]);
            }
        }
        return values;
    }

    /**
     * Change dimensions of matrix to specified rows/cols, preserving cells when possible
     * note: initializes to zero because java
     * @param r
     * @param c
     * @return
     */
    public Matrix changeDimensions(int r, int c){
        double[][] newMatrixArray = new double[r][c];
        int rowBounds;
        int colBounds;

        // avoid array out of bounds if going bigger or smaller
        if(r > this.getRows()){
            rowBounds = this.getRows();
        }
        else{
            rowBounds = r;
        }

        if(c > this.getCols()){
            colBounds = this.getCols();
        }
        else{
            colBounds = c;
        }

        for(int i = 0; i < rowBounds; i++){
            for(int j = 0; j < colBounds; j++){
                newMatrixArray[i][j] = this.matrix2DArray[i][j];
            }
        }
        return new Matrix(newMatrixArray, r, c);

    }




}
