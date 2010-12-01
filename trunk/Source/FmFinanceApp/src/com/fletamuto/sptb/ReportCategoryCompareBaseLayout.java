package com.fletamuto.sptb;

import java.util.ArrayList;

import com.fletamuto.common.control.fmgraph.LineGraph;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class ReportCategoryCompareBaseLayout extends FmBaseActivity {  	
	private LineGraph lg;
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_categoyr_compare_base, true);
       
     // 꺽은선 그래프 예제
		ArrayList<Long> iv = new ArrayList<Long>();
		iv.add((long)1000);
		iv.add((long)2000);
		iv.add((long)4000);
		iv.add((long)2000);
		iv.add((long)5000);
		iv.add((long)1000);
		iv.add((long)3000);
		iv.add((long)4000);
		iv.add((long)1000);
		iv.add((long)2000);
		iv.add((long)3000);
		iv.add((long)6000);

		ArrayList<Long> iv2 = new ArrayList<Long>();
		iv2.add((long)2000);
		iv2.add((long)3500);
		iv2.add((long)4000);
		iv2.add((long)2500);
		iv2.add((long)5000);
		iv2.add((long)3200);
		iv2.add((long)5500);
		iv2.add((long)500);
		iv2.add((long)2300);
		iv2.add((long)3600);
		iv2.add((long)4500);
		iv2.add((long)1200);

		ArrayList<Long> iv3 = new ArrayList<Long>();
		iv3.add((long)7000);
		iv3.add((long)200);
		iv3.add((long)4000);
		iv3.add((long)250);
		iv3.add((long)3000);
		iv3.add((long)550);
		iv3.add((long)3700);
		iv3.add((long)4500);
		iv3.add((long)1000);


		ArrayList<String> at = new ArrayList<String>();
		at.add("1");
		at.add("2");
		at.add("3");
		at.add("4");
		at.add("5");
		at.add("6");
		at.add("7");
		at.add("8");
		at.add("9");
		at.add("10");
		at.add("11");
		at.add("12");


//		setContentView(R.layout.main);
		lg = (LineGraph) findViewById (R.id.lgraph);
		Log.d("jp test 1", "11");
		
		
		lg.makeUserTypeGraph(iv, iv2, iv3, at);
		
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lg.getLayoutParams();
		
		params.width = (320 > lg.getLineGraphWidth()) ? 320 : lg.getLineGraphWidth();
		params.height = (350 > lg.getLineGraphHeight()) ? 320 : lg.getLineGraphHeight();
		
		lg.setLayoutParams(params);
		
		lg.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    		int sel;
		    		sel = lg.FindTouchItemID((int)event.getX(), (int)event.getY());
		
		    		if (sel == -1) {
		    			return true;
		    		} else {
		    			Toast.makeText(lg.getContext(), "ID = " + sel + " 그래프 터치됨", Toast.LENGTH_SHORT).show();
		    			return true;
		    		}
		    	}
				return false;
			}
		});

    }

	
}