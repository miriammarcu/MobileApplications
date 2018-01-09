package miriam.lab5_tot;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Daniel on 1/5/2018.
 */

public class ListDataActivity extends AppCompatActivity {
    private static final String TAG="ListDataActivity";

    DatabaseHelper mDatabaseHelper;
    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        mListView=(ListView) findViewById(R.id.listView);
        mDatabaseHelper=new DatabaseHelper(this);

        populateListView();
    }

    private void populateListView() {
        Log.d(TAG,"populateListView: Display data in the ListView");

        //gets the data & appends it to the list
        Cursor data=mDatabaseHelper.getData();

        ArrayList<String> listData=new ArrayList<>();
        //iterates through each element from the rows
        while (data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the arraylist
            listData.add(data.getString(1));
        }

        //create the list adapter and set the adapter
        ListAdapter adapter =new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listData);
        mListView.setAdapter(adapter);

        //create an onclick event so that we can navigate through the screen
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,View view, int i, long l){
                //grab the name from the listviwe and log the name
                //name which is he text fron out listview->evenimente
                //getitematposition will return an obj,but we can convert
                //it into a string
                String name=adapterView.getItemAtPosition(i).toString();
                //log for debug,used for debug purpose
                //print msg so i can log the exact flow of the program
                Log.d(TAG,"onItemClick: You clicked on "+ name);
                //gets the id associated with the name
                Cursor data=mDatabaseHelper.getItemID(name);
                 //gets the data using movetonext
                //we do like this to make sure that we return smth
                //that exists
                 int itemId=-1;
                while (data.moveToNext()){
                    itemId=data.getInt(0);
                }
                //error handling
                if (itemId>-1){
                    Log.d(TAG,"onItemClick: The id is: "+ itemId);
                    //if there exista an id associated with the name
                    //we want to navigate to the next scree=>use INTENT
                    Intent editScreedIntent=new Intent(ListDataActivity.this, miriam.lab5_tot.EditDataActivity.class);
                    editScreedIntent.putExtra("id",itemId);
                    editScreedIntent.putExtra("name",name);
                    startActivity(editScreedIntent);
                }else{
                    toastMessage("The id is not associated with the name!");
                }
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
