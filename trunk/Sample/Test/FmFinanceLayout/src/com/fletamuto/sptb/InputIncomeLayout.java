package com.fletamuto.sptb;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class InputIncomeLayout extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_income);
        
        Button inputDate = (Button)findViewById(R.id.BtnIncomeDate);
        
        Date createDate = new Date();
        SimpleDateFormat createDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        inputDate.setText(createDateFormat.format(createDate));
    }
}
