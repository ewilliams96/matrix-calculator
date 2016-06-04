package me.elizabeth_williams.matrix;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // constants to refer to matrices
    private final int MATRIX_A = 0;
    private final int MATRIX_B = 1;
    private final int MATRIX_C = 2;

    // matrix A size controls
    private Button setA;
    private EditText rowA;
    private EditText colA;

    // matrix B size controls
    private Button setB;
    private EditText rowB;
    private EditText colB;

    //matrix layout components
    GridLayout matrixAView;
    GridLayout matrixBView;
    GridLayout matrixCView;
    ArrayList<TextView> cellsA;
    ArrayList<TextView> cellsB;
    ArrayList<TextView> cellsC;

    //matrix dimensions
    int rowAVal;
    int colAVal;
    int rowBVal;
    int colBVal;
    int rowCVal;
    int colCVal;

    //operation buttons
    Button add;
    Button sub;
    Button mul;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get matrix A controls from resources
        setA = (Button) findViewById(R.id.buttonA);
        rowA = (EditText) findViewById(R.id.editRowsA);
        colA = (EditText) findViewById(R.id.editColsA);
        matrixAView = (GridLayout) findViewById(R.id.matrixA);

        //Get matrix B controls from resources
        setB = (Button) findViewById(R.id.buttonB);
        rowB = (EditText) findViewById(R.id.editRowsB);
        colB = (EditText) findViewById(R.id.editColsB);
        matrixBView = (GridLayout) findViewById(R.id.matrixB);
        setB = (Button) findViewById(R.id.buttonB);

        matrixCView = (GridLayout) findViewById(R.id.matrixC);


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
        // operation listeners

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               ArrayList<Double> cellAVals = userInputToValues(cellsA);
               ArrayList<Double> cellBVals = userInputToValues(cellsB);

               // if there are blank cells when user tries to perform an operation, break
               if(cellAVals == null || cellBVals == null){
                   Log.i("tag", "null cell");
                   errorToast(getString(R.string.missingCells));
                   return;
               }


               Matrix mA = new Matrix(cellAVals, rowAVal, colAVal);
               Matrix mB = new Matrix(cellBVals, rowBVal, colBVal);
               Matrix matrixResult = mA.add(mB);

               // if result matrix is null
               if(matrixResult == null){
                   errorToast(getString(R.string.wrongSize));
               }

               errorToast("Not implemented yet");
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

        //TODO: restore bundle

    }


    @Override
    protected void onPause(){
        super.onPause();

        //TODO: save current displays/dimensions to bundle
        // save cellsA, cellsB, cellsC, rows/cols for A, B, C


    }

    /**
     * Helper method used to restore saved instance state.
     * @param cells list of textviews/edittexts to extract contents from
     * @param r rows
     * @param c cols
     * @param matrix which matrix (see constants)
     */
    private void restoreMatrixView(ArrayList<TextView> cells, int r, int c, int matrix){
        GridLayout m;

        // switch case to figure out which GridLayout we should be modifying
        // idea for future: use a hashmap ?
        switch(matrix){
            case 0:
                m = matrixAView;
                break;
            case 1:
                m = matrixBView;
                break;
            case 2:
                m = matrixCView;
                break;
            default:
                errorToast("what did you do here");
                m = null;
                break;
        }

        m.removeAllViews(); // in case there are some views on m;
        m.setRowCount(r); // set row count
        m.setColumnCount(c); // set col count

        // re add Views to GridLayout
        for(TextView cell : cells){
            m.addView(cell);
        }

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
        ArrayList<TextView> cells;

        rows = r;
        cols = c;

        if(m == MATRIX_A){

            matrix = matrixAView;
            cellsA = new ArrayList<TextView>();
            cells = cellsA;
            rowAVal = rows;
            colAVal = cols;
        }
        else{

            matrix = matrixBView;
            cellsB = new ArrayList<TextView>();
            cells = cellsB;
            rowBVal = rows;
            colBVal = cols;

        }

        // check for valid size input
        if(rows == -1 || cols == -1){
            errorToast(getString(R.string.sizeInvalid));
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

        // add EditText cells to grid. Note that list of cells will go from left to right,
        // until end of row, then left to right of next row, etc.

        for(int i = 0; i < numCells; i++){
            EditText e = new EditText(this);

            // get number input only
            e.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

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
            matrix.addView(e); // add to View

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
            values.add(Double.parseDouble(t.getText().toString()));
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
        matrixCView.removeAllViews(); // call in case there's already stuff there
        // set grid layout rows/cols
        matrixCView.setColumnCount(c.getCols());
        matrixCView.setRowCount(c.getRows());

        //stuff for restoring saved instance state
        rowCVal = c.getRows();
        colCVal = c.getCols();
        ArrayList<TextView> cellsC = new ArrayList<TextView>();

        ArrayList<Double> vals = c.matrixToList(); // contents of matrix as a list
        int numCells = c.getCols() * c.getRows(); // how many cells matrix will have

        for(int i = 0; i < numCells; i++ ){
            String num = vals.toString();
            TextView t = new TextView(this);
            t.setText(num);
            matrixCView.addView(t);
            cellsC.add(t); // stuff for restoring saved instance state
        }

    }









}
