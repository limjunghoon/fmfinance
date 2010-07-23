package com.bban.test;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TableButton extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ((Button)findViewById(R.id.BtnGrid)).setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intend = new Intent(TableButton.this, GridTableButton.class);
				startActivity(intend);
			}
		 });
    }
}