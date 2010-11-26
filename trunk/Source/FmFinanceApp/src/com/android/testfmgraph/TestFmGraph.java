package com.android.testfmgraph;

//import java.util.ArrayList;

//import jpworld.android.linegraphtest.LineGraph;
//import jpworld.android.linegraphtest.R;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.Toast;



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
//  private LineGraph lg;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        


// Bar 그래프 예제
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
//


// Pie 그래프 예제
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
//


// 꺽은선 그래프 예제
//				ArrayList<Long> iv = new ArrayList<Long>();
//				iv.add((long)1000);
//				iv.add((long)2000);
//				iv.add((long)4000);
//				iv.add((long)2000);
//				iv.add((long)5000);
//				iv.add((long)1000);
//				iv.add((long)3000);
//				iv.add((long)4000);
//				iv.add((long)1000);
//				iv.add((long)2000);
//				iv.add((long)3000);
//				iv.add((long)6000);
//
//				ArrayList<Long> iv2 = new ArrayList<Long>();
//				iv2.add((long)2000);
//				iv2.add((long)3500);
//				iv2.add((long)4000);
//				iv2.add((long)2500);
//				iv2.add((long)5000);
//				iv2.add((long)3200);
//				iv2.add((long)5500);
//				iv2.add((long)500);
//				iv2.add((long)2300);
//				iv2.add((long)3600);
//				iv2.add((long)4500);
//				iv2.add((long)1200);
//
//				ArrayList<Long> iv3 = new ArrayList<Long>();
//				iv3.add((long)7000);
//				iv3.add((long)200);
//				iv3.add((long)4000);
//				iv3.add((long)250);
//				iv3.add((long)3000);
//				iv3.add((long)550);
//				iv3.add((long)3700);
//				iv3.add((long)4500);
//				iv3.add((long)1000);
//
//
//				ArrayList<String> at = new ArrayList<String>();
//				at.add("1");
//				at.add("2");
//				at.add("3");
//				at.add("4");
//				at.add("5");
//				at.add("6");
//				at.add("7");
//				at.add("8");
//				at.add("9");
//				at.add("10");
//				at.add("11");
//				at.add("12");
//
//
//				setContentView(R.layout.main);
//				lg = (LineGraph) findViewById (R.id.lgraph);
//				Log.d("jp test 1", "11");
//				
//				
//				lg.makeUserTypeGraph(iv, iv2, iv3, at);
//				
//				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lg.getLayoutParams();
//				
//				params.width = (320 > lg.getLineGraphWidth()) ? 320 : lg.getLineGraphWidth();
//				params.height = (350 > lg.getLineGraphHeight()) ? 320 : lg.getLineGraphHeight();
//				
//				lg.setLayoutParams(params);
//				
//				lg.setOnTouchListener(new View.OnTouchListener() {
//					
//					public boolean onTouch(View v, MotionEvent event) {
//						if (event.getAction() == MotionEvent.ACTION_DOWN) {
//				    		int sel;
//				    		sel = lg.FindTouchItemID((int)event.getX(), (int)event.getY());
//				
//				    		if (sel == -1) {
//				    			return true;
//				    		} else {
//				    			Toast.makeText(lg.getContext(), "ID = " + sel + " 그래프 터치됨", Toast.LENGTH_SHORT).show();
//				    			return true;
//				    		}
//				    	}
//						return false;
//					}
//				});
//    }
//}




// 꺽은선 그래프 XML 예제
//<?xml version="1.0" encoding="utf-8"?>
//<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
//    android:orientation="vertical"
//    android:layout_width="fill_parent"
//    android:layout_height="fill_parent"
//    >
//	<TextView  
//    android:layout_width="fill_parent" 
//    android:layout_height="wrap_content" 
//    android:text="This is Line Graph Test Start"
//    />
//    <HorizontalScrollView
//       	android:id="@+id/scrollview"
//       	android:layout_width="wrap_content"
//       	android:layout_height="wrap_content"
//       	android:scrollbarStyle="outsideOverlay">
//   		<LinearLayout 
//   			android:id="@+id/hsv"
//   			android:orientation="vertical"
//   			android:layout_width="wrap_content"
//   			android:layout_height="wrap_content">
//       		<jpworld.android.linegraphtest.LineGraph
//   				android:id="@+id/lgraph"
//   				android:layout_width="320dip"
//   				android:layout_height="330dip"/>
//		</LinearLayout>
//   </HorizontalScrollView>    
//	<TextView  
//    android:layout_width="fill_parent" 
//    android:layout_height="wrap_content" 
//    android:text="This is Line Graph Test End"
//    />
//</LinearLayout>