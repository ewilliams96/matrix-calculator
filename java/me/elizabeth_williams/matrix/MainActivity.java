package me.elizabeth_williams.matrix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
    private Button sub2;
    private Button mul;
    private Button mul2;
    Button swap1;
    Button swap2;
    Button swap3;

    ToggleButton tog;
    Button transpose;
    Button scale;
    EditText scaleNum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get matrix A controls from resources
        setA = (Button) findViewById(R.id.buttonA);
        rowA = (EditText) findViewById(R.id.editRowsA);
        colA = (EditText) findViewById(R.id.editColsA);
        final GridLayout matrixAView = (GridLayout) findViewById(R.id.matrixA);

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

        // set listeners for buttons
        setOnClickListeners();

        setMatrixSize(matrixA, ROW_DEFAULT, COL_DEFAULT);
        setMatrixSize(matrixB, ROW_DEFAULT, COL_DEFAULT);
        setMatrixSize(matrixC, ROW_DEFAULT, COL_DEFAULT);
    }

    /**
     * Initialize blank matrices of default size when starting app
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus){

        super.onWindowFocusChanged(hasFocus);

        for(Integer currentKey : matrixViews.keySet()){
            MatrixView m = matrixViews.get(currentKey);
            int col = m.getCols();
            int screenWidth = m.getGridLayout().getWidth();
            for(TextView t : m.getCells()){
                t.setWidth(screenWidth/col);
            }
        }

    }


    /**
     * Helper method to reduce length of onCreate method
     */
    private void setOnClickListeners(){
        // listeners for setting matrix size
        setA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix old = null;
                boolean notEmpty = false;
                if(!hasEmptyCell(matrixA)){
                    old = new Matrix(userInputToValues(matrixA.getCells()), matrixA.getRows(), matrixA.getCols());
                    notEmpty = true;
                }
                setMatrixSize(matrixA, inputToInt(rowA.getText().toString()),
                        inputToInt(colA.getText().toString()));
                if(notEmpty){
                    //int oldr = old.getRows();
                    //int oldc = old.getCols();
                    Matrix sizeAdjust = old.changeDimensions(inputToInt(rowA.getText().toString()), inputToInt(colA.getText().toString()));
                    displayResult(sizeAdjust, matrixA);
                    //clearRows(matrixA, oldr, sizeAdjust.getRows() );
                }

            }
        });

        setB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix old = null;
                boolean notEmpty = false;
                if(!hasEmptyCell(matrixB)){
                    old = new Matrix(userInputToValues(matrixB.getCells()), matrixB.getRows(), matrixB.getCols());
                    notEmpty = true;
                }
                setMatrixSize(matrixB, inputToInt(rowB.getText().toString()),
                        inputToInt(colB.getText().toString()));
                if(notEmpty){
                    //int oldr = old.getRows();
                    //int oldc = old.getCols();
                    Matrix sizeAdjust = old.changeDimensions(inputToInt(rowB.getText().toString()), inputToInt(colB.getText().toString()));
                    displayResult(sizeAdjust, matrixB);
                    //clearRows(matrixA, oldr, sizeAdjust.getRows() );
                }
            }
        });

        // get operation buttons
        add = (Button) findViewById(R.id.add);
        sub = (Button) findViewById(R.id.sub);
        sub2 = (Button) findViewById(R.id.sub2);
        mul = (Button) findViewById(R.id.mul);
        mul2 = (Button) findViewById(R.id.mul2);
        swap1 = (Button) findViewById(R.id.swap1);
        swap2 = (Button) findViewById(R.id.swap2);
        swap3 = (Button) findViewById(R.id.swap3);
        scale = (Button) findViewById(R.id.scalar);
        scaleNum = (EditText) findViewById(R.id.scaleNum);


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

                displayResult(matrixResult, matrixC);
            }
        });

        sub.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ArrayList<Double> cellAVals = userInputToValues(matrixA.getCells());
                ArrayList<Double> cellBVals = userInputToValues(matrixB.getCells());

                // if there are blank cells when user tries to perform an operation, break
                if(cellAVals == null || cellBVals == null){
                    errorToast(getString(R.string.missingCells));
                    return;
                }

                Matrix mA = new Matrix(cellAVals, matrixA.getRows(), matrixA.getCols());
                Matrix mB = new Matrix(cellBVals, matrixB.getRows(), matrixB.getCols());
                Matrix matrixResult = mA.subtract(mB);

                // if result matrix is null
                if(matrixResult == null){
                    errorToast(getString(R.string.wrongSize));
                    return;
                }

                displayResult(matrixResult, matrixC);
            }
        });

        sub2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ArrayList<Double> cellAVals = userInputToValues(matrixA.getCells());
                ArrayList<Double> cellBVals = userInputToValues(matrixB.getCells());

                // if there are blank cells when user tries to perform an operation, break
                if(cellAVals == null || cellBVals == null){
                    errorToast(getString(R.string.missingCells));
                    return;
                }

                Matrix mA = new Matrix(cellAVals, matrixA.getRows(), matrixA.getCols());
                Matrix mB = new Matrix(cellBVals, matrixB.getRows(), matrixB.getCols());
                Matrix matrixResult = mB.subtract(mA);

                // if result matrix is null
                if(matrixResult == null){
                    errorToast(getString(R.string.wrongSize));
                    return;
                }

                displayResult(matrixResult, matrixC);
            }
        });

        mul.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                errorToast("Not implemented yet");
            }
        });

        mul2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                errorToast("Not implemented yet");
            }
        });

        swap1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(hasEmptyCell(matrixA) || hasEmptyCell(matrixB)){
                    errorToast(getString(R.string.missingCells));
                    return;
                }
                swap(matrixA, matrixB);
            }
        });

        swap2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(hasEmptyCell(matrixB) || hasEmptyCell(matrixC)){
                    errorToast(getString(R.string.missingCells));
                    return;
                }
                swap(matrixB, matrixC);
            }
        });

        swap3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(hasEmptyCell(matrixA) || hasEmptyCell(matrixC)){
                    errorToast(getString(R.string.missingCells));
                    return;
                }
                swap(matrixA, matrixC);
            }
        });

        transpose = (Button) findViewById(R.id.transpose);
        tog = (ToggleButton) findViewById(R.id.toggleButton);
        transpose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MatrixView m;
                MatrixView mView;
                EditText setRow;
                EditText setCol;
                switch(tog.getText().toString()){
                    case "A":
                        m = matrixA;
                        mView = matrixA;
                        setRow = rowA;
                        setCol = colA;
                        break;
                    case "B":
                        m = matrixB;
                        mView = matrixB;
                        setRow = rowB;
                        setCol = colB;
                        break;
                    default:
                        m = null; // shouldn't happen
                        mView = null;
                        setRow = null;
                        setCol = null;
                        return;
                }

                if(hasEmptyCell(m)){
                    errorToast(getString(R.string.missingCells));
                    return;
                }
                else{
                    MatrixView matrixv = m;

                    Matrix mat = new Matrix(userInputToValues(matrixv.getCells()), matrixv.getRows(), matrixv.getCols());
                    Matrix result = mat.transpose();
                    displayResult(result, mView);
                    int oldRows = mat.getRows();
                    int oldCols = mat.getCols();
                    setRow.setText(String.valueOf(oldCols));
                    setCol.setText(String.valueOf(oldRows));

                }


            }
        });
        scale.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        scale.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                double scale;

                if(scaleNum.getText().toString().isEmpty()){
                    errorToast(getString(R.string.missingCells));
                    return;
                }
                scale = Double.valueOf(scaleNum.getText().toString());
                MatrixView mView;
                switch(tog.getText().toString()){
                    case "A":
                        mView = matrixA;
                        break;
                    case "B":
                        mView = matrixB;
                        break;
                    default:
                        mView = null;
                }

                if(hasEmptyCell(mView)){
                    errorToast(getString(R.string.missingCells));
                    return;
                }

                Matrix operand = new Matrix(userInputToValues(mView.getCells()), mView.getRows(), mView.getCols());
                displayResult(operand.scalarMultiply(scale), mView);


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
    private void setMatrixSize(MatrixView m, int r, int c){
        int rows = r;
        int cols = c;
        ArrayList<TextView> cells = new ArrayList<TextView>();

        MatrixView matrix = m;
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
            e.setId(View.generateViewId()); // generate ID to save contents
            e.setText("0"); //init values to 0
            // get number input only
            e.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);

            int colWidth = gridWidth/cols; // set min width of columns
            e.setMinimumWidth(colWidth);

            // hide keyboard since the focus change doesn't work as I'd like right now
            /*e.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            });*/


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
     * Check if a matrix has empty cells
     * @param m
     * @return
     */
    private boolean hasEmptyCell(MatrixView m){

        for(TextView t : m.getCells() ){
            if(t.getText().toString().isEmpty()){
                return true;
            }
        }

        return false;
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
    private void displayResult(Matrix c, MatrixView n){
        GridLayout matrixCView = n.getGridLayout();
        matrixCView.removeAllViews(); // call in case there's already stuff there
        // set grid layout rows/cols
        matrixCView.setColumnCount(c.getCols());
        matrixCView.setRowCount(c.getRows());

        // assign values to MatrixView object
        n.setRows(c.getRows());
        n.setCols(c.getCols());
        ArrayList<TextView> cellsC = new ArrayList<TextView>();
        n.setCells(cellsC);

        ArrayList<Double> vals = c.matrixToList(); // contents of matrix as a list
        int numCells = c.getCols() * c.getRows(); // how many cells matrix will have

        int width = matrixCView.getWidth();
        width = width / n.getCols();

        for(int i = 0; i < numCells; i++ ){
            String num = vals.get(i).toString();
            num = num.indexOf(".") < 0 ? num : num.replaceAll("0*$", "").replaceAll("\\.$", "");
            EditText e = new EditText(this);
            e.setId(View.generateViewId()); //set id to save contents
            e.setText(num);
            e.setMinimumWidth(width);
            // e.setInputType(InputType.TYPE_NULL); // disable editing
            e.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);
            matrixCView.addView(e);
            n.getCells().add(e); // stuff for restoring saved instance state
        }

    }

    /**
     * swap fields of matrixA and matrixB (note, could be matrix C too).
     * @param a matrix
     * @param b
     */
    private void swap(MatrixView a, MatrixView b){


        // save B
        int bRow = b.getRows();
        int bCols = b.getCols();
        ArrayList<Double> valuesB = userInputToValues(b.getCells());

        // set B to values of A
        ArrayList<Double> valuesA = userInputToValues(a.getCells());
        displayResult(new Matrix(valuesA, a.getRows(), a.getCols()), b);


        // now set A to B
        //setCells(matrixA, valuesB, bRow, bCols);
        displayResult(new Matrix(valuesB, bRow, bCols), a);

    }

    /**
     * Set the cells of a matrix, given cell values in order, number of rows, and number of columns
     * @param m which matrix
     * @param cellValues values
     * @param r rows
     * @param c columns
     * @precondition values.size() == r * c
     */
    private void setCells(MatrixView m, ArrayList<Double> cellValues, int r, int c){
        //TODO
        setMatrixSize(m, r, c); // set size
        GridLayout matrixGrid = m.getGridLayout();
        for(int i = 0; i < cellValues.size(); i++){
            String num = String.valueOf(cellValues.get(i));
            num = num.indexOf(".") < 0 ? num : num.replaceAll("0*$", "").replaceAll("\\.$", "");
            Log.i("oldCell", m.getCells().get(i).getText().toString());
            m.getCells().get(i).setText(String.valueOf(cellValues.get(i)));
            Log.i("cell", String.valueOf(num));
        }


    }



    /**
     * Clear range of rows.
     * @param m MatrixView to be cleared
     * @param rstart start row index. inclusive
     * @param rfinish end row index. exclusive
     */
    private void clearRows(MatrixView m, int rstart, int rfinish){
        GridLayout gl = m.getGridLayout();
        int c = gl.getColumnCount();
        int r = gl.getRowCount();
        ArrayList<TextView[]> rows = new ArrayList<TextView[]>();
        for(int i = 0; i < r; i ++){
            TextView[] tRow = new TextView[c];
            for(int j = 0; j < c; j++){
                tRow[j] = m.getCells().get(i*j + j);

            }
            rows.add(tRow);
        }

        for(int i = rstart; i < rfinish; i++){
            TextView[] currentRow = rows.get(i);
            for(int j = 0; j < currentRow.length; j++){
                currentRow[j].setText("");
            }
        }

    }


}
