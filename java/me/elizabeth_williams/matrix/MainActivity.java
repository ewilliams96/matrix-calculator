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
    ArrayList<EditText> cellsA;
    ArrayList<EditText> cellsB;

    //matrix dimensions
    int rowAVal;
    int colAVal;
    int rowBVal;
    int colBVal;

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
               setMatrixSize(MATRIX_A, Integer.valueOf(rowA.getText().toString()),
                       Integer.valueOf(colA.getText().toString()));
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
               ArrayList<Integer> cellAVals = userInputToValues(cellsA);
               ArrayList<Integer> cellBVals = userInputToValues(cellsB);

               // if there are blank cells when user tries to perform an operation, break
               if(cellAVals == null || cellBVals == null){
                   Log.i("tag", "null cell");
                   errorToast(getString(R.string.missingCells));
                   return;
               }

                if(rowAVal != rowBVal || colAVal != colBVal){
                    errorToast(getString(R.string.wrongSize));
                    return;
                }
               Matrix mA = new Matrix(cellAVals, rowAVal, colAVal);
               Matrix mB = new Matrix(cellBVals, rowBVal, colBVal);
               Matrix matrixResult = mA.add(mB);

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

        //TODO: save current displays/dimensions.


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

            matrix = matrixAView;
            cellsA = new ArrayList<EditText>();
            cells = cellsA;
            rowAVal = rows;
            colAVal = cols;
        }
        else{

            matrix = matrixBView;
            cellsB = new ArrayList<EditText>();
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
            matrix.addView(e); // add to View

        }

    }


    /**
     * Get user input values from EditText fields to separate Matrix from Views.
     * @param input List of EditText fields
     * @return ArrayList<Double> of values from text fields. Null if there are empty text fields.
     */
    private ArrayList<Integer> userInputToValues(ArrayList<EditText> input){
        //TODO
        return null;

    }



    /**
     *
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

    public void errorToast(String s){
        Toast t = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        t.show();
    }

    /**
     * Display 3rd matrix with results of matrix operation.
     * @param c Result of operation
     */
    private void displayResult(Matrix c){
        matrixCView.removeAllViews(); // call in case there's already stuff there
        // set grid layout rows/cols
        matrixCView.setColumnCount(c.getCols());
        matrixCView.setRowCount(c.getRows());

        ArrayList<Double> vals = c.matrixToList(); // contents of matrix as a list

        int numCells = c.getCols() * c.getRows(); // how many cells matrix will have

        for(int i = 0; i < numCells; i++ ){
            String num = vals.toString();
            TextView t = new TextView(this);
            t.setText(num);
            matrixCView.addView(t);
        }

    }








}
