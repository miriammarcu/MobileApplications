package miriam.lab5_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static Button btnAlert;
    //piechart
    private float[] yData={25.3f,10.6f,66.7f,44.5f,46.5f,16.4f,23.4f};
    private String[] xData={"Concert HFG","Concert ABC","stand up A","concert c","conferinta adf","conferinta ff","stand up"};
    PieChart pieChart;

    private static final String TAG="MainActivity";
    DatabaseHelper mDatabaseHelper;
    private Button btnAdd, btnViewData;
    private EditText editText;

    //FOR ALERT DIALOG
    public void onButtonClickListener(){
        btnAlert=(Button) findViewById(R.id.btnAlert);
        btnAlert.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v){
                        AlertDialog.Builder a_builder=new AlertDialog.Builder(MainActivity.this);
                        a_builder.setMessage("Do you want to close this App?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //closes the app
                                        finish();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //cancel the dialog box
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert=a_builder.create();
                        alert.setTitle("Alert!");
                        alert.show();
                    }
                }        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onButtonClickListener();
        Log.d(TAG,"onCreate: starting to create chart");

        //piechart
        pieChart=(PieChart) findViewById(R.id.idPieChart);
        pieChart.setDescription("Popular events this month");
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("chart");
        pieChart.setCenterTextSize(10);
        //pieChart.setDrawEntryLabels(true);
        addDataSet();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG,"onValueSelected: Value selected from chart");
                Log.d(TAG,"onValueSelected: "+e.toString());
                Log.d(TAG,"onValueSelected: "+h.toString());

                int pos1=e.toString().indexOf("(sum): ");
                String hmany=e.toString().substring(pos1+7);
                for(int i=0; i<yData.length; i++){
                    if (yData[i]==Float.parseFloat(hmany)){
                        pos1=i;
                        break;
                    }
                }
                String event=xData[pos1+1];
                Toast.makeText(MainActivity.this,"Even t"+event+"\n"+"How many people "+hmany,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected() {

            }
        });

        editText=(EditText) findViewById(R.id.editText);
        btnAdd=(Button) findViewById(R.id.btnAdd);
        btnViewData=(Button) findViewById(R.id.btnViewData);
        mDatabaseHelper=new DatabaseHelper(this);

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
                Intent intent=new Intent(MainActivity.this,ListDataActivity.class);
                startActivity(intent);
            }

        });
    }

    private void addDataSet() {
        Log.d(TAG,"addDataSet started");
        ArrayList<PieEntry> yEntrys=new ArrayList<>();
        ArrayList<String> xEntrys=new ArrayList<>();

        for(int i=0; i<yData.length; i++){
            yEntrys.add(new PieEntry(yData[i],i));
        }

        for(int i=1; i<xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet=new PieDataSet(yEntrys,"People going to events");
        //distanta dintre slices din pie chart
        pieDataSet.setSliceSpace(2);
        //cat de mari sunt numerele de pe slices
        pieDataSet.setValueTextSize(12);

        //add colors
        ArrayList<Integer> colors=new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend=pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

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
