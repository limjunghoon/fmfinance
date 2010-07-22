package com.fletamuto.sptb;


import android.app.ListActivity;
import android.os.Bundle;

public class ReportBaseListLayout extends ListActivity {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
/*    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            event.startTracking();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
	            && !event.isCanceled()) {
	    	Intent intent = new Intent(ReportBaseListLayout.this, FmFinanceLayout.class);
			startActivity(intent);
	        return true;
	    }
	    return super.onKeyUp(keyCode, event);
    }
  */  
}