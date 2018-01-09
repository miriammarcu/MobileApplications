package miriam.lab5_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Daniel on 1/5/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private final String TAG="DatabaseHelper";

    private static final String TABLE_NAME="events_table";
    private static final String COL1="ID";
    private static final String COL2="name_event";

    public DatabaseHelper(Context context){
        super(context,TABLE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable="CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean addData(String item){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL2,item);

        Log.d(TAG,"addData: Adding "+ item + " to " + TABLE_NAME);

        long result=db.insert(TABLE_NAME,null,contentValues);
        //if date is inserted incorrectly, it will show -1

        if (result ==-1){
            return false;
        }else{
            return true;
        }
    }

    /*
    * returns the all data from the database
    * */
    public Cursor getData(){
        SQLiteDatabase db=this.getWritableDatabase();
        String query="SELECT * FROM " + TABLE_NAME;
        Cursor data=db.rawQuery(query,null);
        return data;
    }

    /*
    * returns only the id that matches the name passed in
    * @param name
    * */
    public Cursor getItemID(String name){
        //create the sqlite object
        //then the query
        SQLiteDatabase db=this.getWritableDatabase();
                //select the id where the name equals what i said;
        String query="SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " +COL2 + " = '" + name + "'";
        //rawQuery:
        //Runs the provided SQL and returns a Cursor over the result set.
        Cursor data=db.rawQuery(query,null);
        return data;
    }

    //update an item that exists
    public void updateName(String newName,int id,String oldName){
        SQLiteDatabase db=this.getWritableDatabase();
        String query="UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" +newName+ "' WHERE "+ COL1 + " ='" + id+ "'"+
                " AND "+ COL2 +" = '" + oldName + "'";
        Log.d(TAG,"updateName: query "+ query);
        Log.d(TAG,"updateName: setting name to: "+ newName);
        db.execSQL(query);
    }

    //delete from database
    public void deleteName(int id,String name){
        SQLiteDatabase db= this.getWritableDatabase();
        String query="DELETE FROM " +TABLE_NAME+" WHERE " +
                COL1+" = '" + id+ "'"+ " AND " +
                COL2+ " = '"+name+ "'";
        Log.d(TAG,"deleteName:  query: "+ query);
        Log.d(TAG,"deleteName: Deleting "+name+" from database.");
        db.execSQL(query);
    }
}
