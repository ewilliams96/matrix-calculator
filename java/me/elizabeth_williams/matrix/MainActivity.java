package me.elizabeth_williams.matrix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // constants to refer to matrices
    private final int MATRIX_A = 0;
    private final int MATRIX_B = 1;

    // matrix A size controls
    private Button setA;
    private EditText rowA;
    private EditText colA;

    // matrix B size controls
    //TODO:

    //matrix layout components
    GridLayout matrixA;
    GridLayout matrixB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setA = (Button) findViewById(R.id.buttonA);
        rowA = (EditText) findViewById(R.id.editRowsA);
        colA = (EditText) findViewById(R.id.editColsA);
        matrixA = (GridLayout) findViewById(R.id.matrixA);
        setA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setMatrixSize(MATRIX_A);
            }
        });


    }

    /**
     * Set matrix size. Generate/edit corresponding gridlayout and
     * edit text cells.
     * @param - matrix (see above constants)
     */
    private void setMatrixSize(int matrix){
        int rows;
        int cols;


        if(matrix == MATRIX_A){
            rows = inputToInt(rowA.getText().toString());
            cols = inputToInt(colA.getText().toString());
        }
        else{
            //TODO: row B stuff
            rows = inputToInt(rowA.getText().toString());
            cols = inputToInt(colA.getText().toString());
        }

        // check for valid size input
        if(rows == -1 || cols == -1){
            matrixSizeInvalid();
            return;
        }

        //proceed to setting matrix sizes



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

    private void matrixSizeInvalid(){
        Toast toast = new Toast(this);
        toast.setText("Invalid matrix dimensions entered.");
        toast.setDuration(Toast.LENGTH_SHORT);

    }




}
