package miriam.lab5_tot;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";
    miriam.lab5_tot.DatabaseHelper mDatabaseHelper;
    private Button btnAdd, btnViewData;
    private EditText editText;

    TextView tv;
    Calendar mCurrentDate;
    int day,month,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv=(TextView) findViewById(R.id.textView);
        mCurrentDate=Calendar.getInstance();
        //used day of month because every month has dates on diffrent daya
        day=mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month=mCurrentDate.get(Calendar.MONTH);
        year=mCurrentDate.get(Calendar.YEAR);
        month=month+1;

        tv.setText(day+"/"+month+"/"+year);
        tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthofYear,int dayofMonth){
                        monthofYear=monthofYear+1;
                        tv.setText(dayofMonth+"/"+monthofYear+"/"+year);

                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });


        editText=(EditText) findViewById(R.id.editText);
        btnAdd=(Button) findViewById(R.id.btnAdd);
        btnViewData=(Button) findViewById(R.id.btnViewData);
        mDatabaseHelper=new miriam.lab5_tot.DatabaseHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String newEntry=editText.getText().toString();
                if (editText.length()!=0){
                    AddData(newEntry);
                    editText.setText("");
                }
                    else{
                    toastMessage("Put something into the text field!");
                }
            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, miriam.lab5_tot.ListDataActivity.class);
                startActivity(intent);
            }

        });
    }

    public void AddData(String newEntry){
        boolean insertData=mDatabaseHelper.addData(newEntry);
        //if insertData is true it was inserted correctly;
        //else it was not, we use addData from DatabaseHelper class
        if (insertData){
            toastMessage("Data was successfully inserted!");
        }else{
            toastMessage("Data was not inserted!");
        }
    }

    /*
    * cutomizable toast
    *
    * */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
