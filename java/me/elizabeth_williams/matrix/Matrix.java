package me.elizabeth_williams.matrix;

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
    int[][] matrix2DArray;

    public Matrix(List<Integer> values, int r, int c){
        rows = r;
        cols = c;
        matrix2DArray = new int[rows][cols];

        //TODO populate 2d array with values

    }



    /**
     * Add two matrices.
     * @precondition otherMatrix is of same dimensions as this matrix
     * @param otherMatrix
     * @return result of matrix addition
     */
    public Matrix add(Matrix otherMatrix){
       //TODO
       return otherMatrix;
    }

    /**
     * Multiply two matrices.
     * @precondition column's in this matrix == row's in otherMatrix
     * @param otherMatrix
     * @return
     */
    public Matrix multiply(Matrix otherMatrix){
        //TODO
        return otherMatrix;
    }


    /**
     * Subtract two matrices.
     * @precondition otherMatrix is of same dimensions as this matrix
     * @param otherMatrix
     * @return
     */
    public Matrix subtract(Matrix otherMatrix){
        //TODO
        return otherMatrix;

    }





}