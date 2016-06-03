package me.elizabeth_williams.matrix;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // constants to refer to matrices
    private final int MATRIX_A = 0;
    private final int MATRIX_B = 1;

    // matrix A size controls
    private Button setA;
    private EditText rowA;
    private EditText colA;

    // matrix B size controls
    private Button setB;
    private EditText rowB;
    private EditText colB;

    //matrix layout components
    GridLayout matrixA;
    GridLayout matrixB;
    ArrayList<EditText> cellsA;
    ArrayList<EditText> cellsB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get matrix A controls from resources
        setA = (Button) findViewById(R.id.buttonA);
        rowA = (EditText) findViewById(R.id.editRowsA);
        colA = (EditText) findViewById(R.id.editColsA);
        matrixA = (GridLayout) findViewById(R.id.matrixA);

        //Get matrix B controls from resources
        setB = (Button) findViewById(R.id.buttonB);
        rowB = (EditText) findViewById(R.id.editRowsB);
        colB = (EditText) findViewById(R.id.editColsB);
        matrixB = (GridLayout) findViewById(R.id.matrixB);
        setB = (Button) findViewById(R.id.buttonB);


        // listeners for setting matrix size
        setA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setMatrixSize(MATRIX_A, Integer.valueOf(rowA.getText().toString()),
                       Integer.valueOf(rowA.getText().toString()));
            }
        });

        setB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMatrixSize(MATRIX_B, Integer.valueOf(rowB.getText().toString()),
                        Integer.valueOf(colB.getText().toString()));
            }
        });


    }

    /**
     * Set matrix size. Generate/edit corresponding gridlayout and
     * edit text cells.
     * @param m - matrix (see above constants)
     * @param r - rows
     * @param c - cols
     */
    private void setMatrixSize(int m, int r, int c){
        int rows;
        int cols;
        GridLayout matrix;
        ArrayList<EditText> cells;

        rows = r;
        cols = c;

        if(m == MATRIX_A){

            matrix = matrixA;
            cellsA = new ArrayList<EditText>();
            cells = cellsA;
        }
        else{

            matrix = matrixB;
            cellsB = new ArrayList<EditText>();
            cells = cellsB;

        }

        // check for valid size input
        if(rows == -1 || cols == -1){
            errorToast(R.string.sizeInvalid);
            return;
        }

        // clear grid elements
        matrix.removeAllViews();

        //proceed to setting matrix sizes
        matrix.setRowCount(rows);
        matrix.setColumnCount(cols);
        int numCells =  rows * cols;

        // width of grid
        int gridWidth = matrix.getWidth();

        for(int i = 0; i < numCells; i++){
            EditText e = new EditText(this);

            // get number input only
            e.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            int colWidth = gridWidth/cols; // set min width of columns
            e.setMinimumWidth(colWidth);

            // hide keyboard since the focus change doesn't work as I'd like right now
            e.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            });


            cells.add(e); // store reference to each cell in the array
            matrix.addView(e);

        }

    }



    /**
     *
     * @param input String input by user to convert
     * @return int value of string if valid, otherwise -1
     */
    private int inputToInt(String input){
        int result;
        try{
            result = Integer.parseInt(input);
        } catch(NumberFormatException e){
            result = -1;
        }
        return result;
    }

    private void errorToast(String s){
        Toast toast = new Toast(this);
        toast.setText(s);
        toast.setDuration(Toast.LENGTH_SHORT);

    }






}
