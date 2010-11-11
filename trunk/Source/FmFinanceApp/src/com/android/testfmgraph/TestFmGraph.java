package com.android.testfmgraph;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.widget.*;
//import android.util.*;
//import android.view.*;
//
//import com.android.fmgraph.*;
//
//public class TestFmGraph extends Activity {
//    /** Called when the activity is first created. */
//	private PieGraph pg;
//	private BarGraph bg;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        
//        long[] iv = new long[] {1000, -2000, 4000, 2000, 5000, 1000, 3000, 4000, 1000, 2000, 6000, 3500, 4200, 3300, 1100, 2200};
//        int[] ap = new int[] {0,1,2,3,4,5};
//        String[] at = new String[] {"1", "2", "3", "4", "5", "6"};
//
//        setContentView(R.layout.main2);
//        
//        bg = (BarGraph) findViewById (R.id.bgraph);
// 
//        bg.makeUserTypeGraph(ap, Constants.BAR_AXIS_X_BOTTOM, iv, 1, at);
//
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bg.getLayoutParams();
//
//        params.width = (320 > bg.getBarGraphWidth()) ? 320 : bg.getBarGraphWidth();
//        params.height = (350 > bg.getBarGraphHeight()) ? 320 : bg.getBarGraphHeight();
//       
//        bg.setLayoutParams(params);
//        
///*
//        pg = (PieGraph) findViewById (R.id.pgraph);
//        pg.setItemValues(iv);
//        pg.setOnTouchListener(new View.OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//		    		int sel;
//		    		sel = pg.FindTouchItemID((int)event.getX(), (int)event.getY());
//		    		Log.d("jptest1", "onTouchEvent = " + sel);
//		    		Log.d("jptest1", "getRawX" + (int)event.getXPrecision());
//		    		Log.d("jptest1", "getRawY" + (int)event.getYPrecision());
//		    		if (sel == -1) {
//		    			return true;
//		    		} else {
//		    			Toast.makeText(pg.getContext(), "ID = " + sel + " Value = " + pg.getItemValue(sel) + " Color = " + pg.getItemColor(sel), Toast.LENGTH_SHORT).show();
//		    			return true;
//		    		}
//		    	}
//				return false;
//			}
//		});
//*/
//    }
//}