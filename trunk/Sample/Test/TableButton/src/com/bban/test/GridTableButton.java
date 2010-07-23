package com.bban.test;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class GridTableButton extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.grid_view);
    	GridView grid = (GridView)findViewById(R.id.GridTableButton);
    	ButtonAdpter adapter = new ButtonAdpter();
   	grid.setAdapter(adapter);
    }
    
    class ButtonAdpter extends BaseAdapter {
  //  	private Context context;
    	Button btnWord = new Button(GridTableButton.this);
    //	Button button = new Button();

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 100;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return btnWord;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return btnWord.getId();
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return new Button(GridTableButton.this);
		}
    	
    	
    }

}