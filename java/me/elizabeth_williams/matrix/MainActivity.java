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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // constants to refer to matrices
    private final int MATRIX_A = 0;
    private final int MATRIX_B = 1;
    private final int MATRIX_C = 2;

    //hashmap linking matrix # to corresponding view
    HashMap<Integer, MatrixView> matrixViews;
    MatrixView matrixA;
    MatrixView matrixB;
    MatrixView matrixC;

    // defaults
    private final int ROW_DEFAULT = 3;
    private final int COL_DEFAULT = 3;

    // matrix A size controls
    private Button setA;
    private EditText rowA;
    private EditText colA;

    // matrix B size controls
    private Button setB;
    private EditText rowB;
    private EditText colB;


    //operation buttons
    private Button add;
    private Button sub;
    private Button mul;
    Button swap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get matrix A controls from resources
        setA = (Button) findViewById(R.id.buttonA);
        rowA = (EditText) findViewById(R.id.editRowsA);
        colA = (EditText) findViewById(R.id.editColsA);
        GridLayout matrixAView = (GridLayout) findViewById(R.id.matrixA);

        //Get matrix B controls from resources
        setB = (Button) findViewById(R.id.buttonB);
        rowB = (EditText) findViewById(R.id.editRowsB);
        colB = (EditText) findViewById(R.id.editColsB);
        GridLayout matrixBView = (GridLayout) findViewById(R.id.matrixB);
        setB = (Button) findViewById(R.id.buttonB);

        GridLayout matrixCView = (GridLayout) findViewById(R.id.matrixC);

        // matrixview objects
        matrixA = new MatrixView(matrixAView, null, ROW_DEFAULT, COL_DEFAULT);
        matrixB = new MatrixView(matrixBView, null, ROW_DEFAULT, COL_DEFAULT);
        matrixC = new MatrixView(matrixCView, null, ROW_DEFAULT, COL_DEFAULT);

        matrixViews = new HashMap<Integer, MatrixView>();

        matrixViews.put(MATRIX_A, matrixA);
        matrixViews.put(MATRIX_B, matrixB);
        matrixViews.put(MATRIX_C, matrixC);


        // listeners for setting matrix size
        setA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setMatrixSize(MATRIX_A, inputToInt(rowA.getText().toString()),
                       inputToInt(colA.getText().toString()));
            }
        });

        setB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMatrixSize(MATRIX_B, Integer.valueOf(rowB.getText().toString()),
                        Integer.valueOf(colB.getText().toString()));
            }
        });

        // get operation buttons
        add = (Button) findViewById(R.id.add);
        sub = (Button) findViewById(R.id.sub);
        mul = (Button) findViewById(R.id.mul);
        swap = (Button) findViewById(R.id.swap);
        // operation listeners

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               ArrayList<Double> cellAVals = userInputToValues(matrixA.getCells());
               ArrayList<Double> cellBVals = userInputToValues(matrixB.getCells());

               // if there are blank cells when user tries to perform an operation, break
               if(cellAVals == null || cellBVals == null){
                   errorToast(getString(R.string.missingCells));
                   return;
               }

               Matrix mA = new Matrix(cellAVals, matrixA.getRows(), matrixA.getCols());
               Matrix mB = new Matrix(cellBVals, matrixB.getRows(), matrixB.getCols());
               Matrix matrixResult = mA.add(mB);

               // if result matrix is null
               if(matrixResult == null){
                   errorToast(getString(R.string.wrongSize));
                   return;
               }

               displayResult(matrixResult);
            }
        });

        sub.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               errorToast("Not implemented yet");
            }
        });

        mul.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                errorToast("Not implemented yet");
            }
        });

        swap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                swap(MATRIX_A, MATRIX_B);
            }
        });



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        setMatrixSize(MATRIX_A, ROW_DEFAULT, COL_DEFAULT);
        setMatrixSize(MATRIX_B, ROW_DEFAULT, COL_DEFAULT);

    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        //TODO
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle){
        //TODO
        super.onRestoreInstanceState(bundle);
    }


    /**
     * Set matrix size. Generate/edit corresponding gridlayout and
     * edit text cells.
     * @param m - matrix (see above constants)
     * @param r - rows
     * @param c - cols
     */
    private void setMatrixSize(int m, int r, int c){
        int rows = r;
        int cols = c;
        ArrayList<TextView> cells = new ArrayList<TextView>();

        MatrixView matrix = matrixViews.get(m);
        GridLayout matrixGrid = matrix.getGridLayout();
        matrix.setCells(cells);
        matrix.setRows(r);
        matrix.setCols(c);

        // check for valid size input
        if(r == -1 || c == -1){
            errorToast(getString(R.string.sizeInvalid));
            return;
        }

        // clear grid elements
        matrixGrid.removeAllViews();

        //proceed to setting matrix sizes
        matrixGrid.setRowCount(rows);
        matrixGrid.setColumnCount(cols);
        int numCells =  rows * cols;

        // width of grid
        int gridWidth = matrixGrid.getWidth();

        // add EditText cells to grid. Note that list of cells will go from left to right,
        // until end of row, then left to right of next row, etc.

        for(int i = 0; i < numCells; i++){
            EditText e = new EditText(this);

            // get number input only
            e.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);

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
            matrixGrid.addView(e); // add to View


        }


    }


    /**
     * Get user input values from TextView fields to separate Matrix from Views.
     * @param input List of TextView fields (so can be used in more situations)
     * @return ArrayList<Double> of values from text fields. Null if there are empty text fields.
     */
    private ArrayList<Double> userInputToValues(ArrayList<TextView> input){
        ArrayList<Double> values = new ArrayList<Double>();
        for(TextView t : input){
            if(!t.getText().toString().isEmpty()){
                values.add(Double.parseDouble(t.getText().toString()));
            }
            else{
                return null; // break if a null text field is encountered
            }
        }
        return values;
    }



    /**
     * Converts  input by user from string to int, also validating input >= 0
     * since used in generating matrix dimensions.
     * @param input String input by user to convert
     * @return int value of string if valid, otherwise -1
     */
    private int inputToInt(String input){
        int result;
        int i = Integer.parseInt(input);
        if(i <= 0){
            result = -1;
        }
        else{
            result = i;
        }
        return result;
    }

    /**
     * method for making toasts
     * @param s
     */
    public void errorToast(String s){
        Toast t = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        t.show();
    }

    /**
     * Display 3rd matrix with results of matrix operation. (Matrix C)
     * @param c Result of operation
     */
    private void displayResult(Matrix c){
        GridLayout matrixCView = matrixC.getGridLayout();
        matrixCView.removeAllViews(); // call in case there's already stuff there
        // set grid layout rows/cols
        matrixCView.setColumnCount(c.getCols());
        matrixCView.setRowCount(c.getRows());

        // assign values to MatrixView object
        matrixC.setRows(c.getRows());
        matrixC.setCols(c.getCols());
        ArrayList<TextView> cellsC = new ArrayList<TextView>();
        matrixC.setCells(cellsC);

        ArrayList<Double> vals = c.matrixToList(); // contents of matrix as a list
        int numCells = c.getCols() * c.getRows(); // how many cells matrix will have

        int width = matrixCView.getWidth();
        width = width / matrixC.getCols();

        for(int i = 0; i < numCells; i++ ){
            String num = vals.get(i).toString();
            EditText e = new EditText(this);
            e.setText(num);
            e.setMinimumWidth(width);
            // e.setInputType(InputType.TYPE_NULL); // disable editing
            matrixCView.addView(e);
            matrixC.getCells().add(e); // stuff for restoring saved instance state
        }

    }

    /**
     * swap fields of matrixA and matrixB (note, could be matrix C too).
     * @param matrixA matrix referred to by constant
     * @param matrixB
     */
    private void swap(int matrixA, int matrixB){
        MatrixView mva = matrixViews.get(matrixA);
        MatrixView mvb = matrixViews.get(matrixB);

        // save B
        int bRow = mvb.getRows();
        int bCols = mvb.getCols();
        ArrayList<Double> valuesB = userInputToValues(mvb.getCells());

        // set B to values of A
        ArrayList<Double> valuesA = userInputToValues(mva.getCells());
        setCells(matrixB, valuesA, mva.getRows(), mva.getCols());

        // now set A to B
        setCells(matrixA, valuesB, bRow, bCols);

    }

    /**
     * Set the cells of a matrix, given cell values in order, number of rows, and number of columns
     * @param matrix which matrix
     * @param cellValues values
     * @param r rows
     * @param c columns
     * @precondition values.size() == r * c
     */
    private void setCells(int matrix, ArrayList<Double> cellValues, int r, int c){
        //TODO
        setMatrixSize(matrix, r, c); // set size
        MatrixView m = matrixViews.get(matrix);
        GridLayout matrixGrid = matrixViews.get(matrix).getGridLayout();
        for(int i = 0; i < cellValues.size(); i++){
            m.getCells().get(i).setText(String.valueOf(cellValues.get(i)));
        }


    }


}
