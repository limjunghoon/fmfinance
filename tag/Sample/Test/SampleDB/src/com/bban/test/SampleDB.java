package com.bban.test;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class SampleDB extends Activity {
	ExpenseDBHelper expendDB;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        expendDB = new ExpenseDBHelper(this);
        
        findViewById(R.id.BtnInsert).setOnClickListener(clickListener);
        findViewById(R.id.BtnDelete).setOnClickListener(clickListener);
    }
    
    Button.OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SQLiteDatabase db;
			ContentValues row;
			
			if (v.getId() == R.id.BtnInsert) {
				db = expendDB.getWritableDatabase();
				
				row = new ContentValues();
				row.put("year", 2010);
				row.put("month", 10);
				row.put("day", 20);
				row.put("amount", 1000);
				row.put("memo", "bbbbbb");
				
				db.insert("expense", null, row);
				db.close();
			}
			
			else if (v.getId() == R.id.BtnDelete) {
				db = expendDB.getWritableDatabase();
				
				String deleteQuery = "DELETE FROM expense WHERE _id=2";
				 db.execSQL(deleteQuery);
				 db.close();
			}
		}
	};
    
}