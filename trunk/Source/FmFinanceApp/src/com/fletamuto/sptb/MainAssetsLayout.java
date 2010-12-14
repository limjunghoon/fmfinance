package com.fletamuto.sptb;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fletamuto.common.control.fmgraph.PieGraph;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

public class MainAssetsLayout extends FmBaseActivity {
	private long monthTotalAssets = 0L;
	private long monthTotalLiability = 0L;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.main_asserts, true);
    	setButtonClickListener();
    }
    
	private void setButtonClickListener() {
		
		Button btnExpense = (Button)findViewById(R.id.BtnTotalAssets);
		btnExpense.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
			//	Intent intent = new Intent(MainAssetsLayout.this, ReportCompareAssetsLayout.class);
			//	Intent intent = new Intent(MainAssetsLayout.this, SelectCategoryAssetsLayout.class);
				Intent intent = new Intent(MainAssetsLayout.this, ReportAssetsLayout.class);
				startActivity(intent);
			}
		});
		
		Button btnIncome = (Button)findViewById(R.id.BtnTotalLiability);
		btnIncome.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MainAssetsLayout.this, ReportCompareLiabilityLayout.class);
				startActivity(intent);
			}
		});
	}

	private void getData() {
		monthTotalAssets = DBMgr.getTotalAmount(AssetsItem.TYPE);
		monthTotalLiability = DBMgr.getTotalAmount(LiabilityItem.TYPE);
	}

	@Override
	protected void setTitleBtn() {
    	setTitle("자산 현황");

		super.setTitleBtn();
	}

	private void updateChildView() {
		updateBarGraph();
		
		TextView btnProfit = (TextView)findViewById(R.id.TVTotalProperty);
		btnProfit.setText(String.format("총 자산 : %,d원", monthTotalAssets - monthTotalLiability));
		
		Button btnExpense = (Button)findViewById(R.id.BtnTotalAssets);
		btnExpense.setText(String.format("자산                               %,d원", monthTotalAssets));
		
		Button btnIncome = (Button)findViewById(R.id.BtnTotalLiability);
		btnIncome.setText(String.format("부채                              %,d원", monthTotalLiability));
	}

	private void updateBarGraph() {
		final PieGraph pieGraph;
		ArrayList<Long> pieGraphValues = new ArrayList<Long>();
		
		pieGraphValues.add(monthTotalAssets);
		pieGraphValues.add(monthTotalLiability);
       
		pieGraph = (PieGraph) findViewById (R.id.pgraph);
		pieGraph.setItemValues(pieGraphValues);
		pieGraph.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					int sel;
		    		sel = pieGraph.FindTouchItemID((int)event.getX(), (int)event.getY());
		    		if (sel == -1) {
		    			return true;
		    		} else {
		    			Toast.makeText(pieGraph.getContext(), "ID = " + sel + " 그래프 터치됨", Toast.LENGTH_SHORT).show();
		    			return true;
		    		}
		    	}
				return false;
			}
		});
	}
	
	@Override
	protected void onResume() {
		getData();
    	updateChildView();
		super.onResume();
	}
}