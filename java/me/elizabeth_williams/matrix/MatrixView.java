package me.elizabeth_williams.matrix;

import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Keep view related components of matrix here.
 * Created by a0 on 6/4/16.
 */
public class MatrixView {

    private GridLayout matrixView;
    private ArrayList<TextView> cells;
    private int rows;
    private int cols;

    public MatrixView(GridLayout g, ArrayList<TextView> textviews, int r, int c){
        matrixView = g;
        cells = textviews;
        rows = r;
        cols = c;

    }

    public GridLayout getGridLayout(){
        return matrixView;
    }

    public ArrayList<TextView> getCells(){
        return cells;
    }

    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }

    public void setRows(int r){
        rows = r;
    }

    public void setCols(int c){
        cols = c;
    }

    public void setCells(ArrayList<TextView> c){
        cells = c;
    }



}
