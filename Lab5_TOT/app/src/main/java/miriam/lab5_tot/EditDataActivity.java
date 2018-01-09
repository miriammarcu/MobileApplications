package miriam.lab5_tot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Daniel on 1/5/2018.
 */

public class EditDataActivity extends AppCompatActivity {

    private static final String TAG="EditDataActivity";
    private Button btnSave,btnDelete;
    private EditText editItem;

    DatabaseHelper mDatabaseHelper;
    private String selectedName;
    private int selectedId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout);
        btnSave=(Button) findViewById(R.id.btnSave);
        btnDelete=(Button) findViewById(R.id.btnDelete);
        editItem=(EditText) findViewById(R.id.editItem);
        mDatabaseHelper=new DatabaseHelper(this);

        //get the intent extra for the listdataactivity
        Intent receivedIntent=getIntent();

        //-1 is the default value
        selectedId=receivedIntent.getIntExtra("id",-1);

        //get the extras
        selectedName=receivedIntent.getStringExtra("name");
        editItem.setText(selectedName);

        //now we manage the buttons
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String item=editItem.getText().toString();
                if (!item.equals("")){
                    //if it is something, we want to update
                    //the item=>ctreate update function in the
                    //databasehelper class
                    mDatabaseHelper.updateName(item,selectedId,selectedName);
                }else{
                    toastMessage("You must enter a name!");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mDatabaseHelper.deleteName(selectedId,selectedName);
                editItem.setText("");
                toastMessage("removed from database");

            }
        });
    }

    /*
    * cutomizable toast
    *
    * */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
