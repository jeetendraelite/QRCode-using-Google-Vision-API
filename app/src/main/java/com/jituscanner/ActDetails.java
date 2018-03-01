package com.jituscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jituscanner.utils.Details;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActDetails extends BaseActivity {

    @BindView(R.id.tv_detail)
    TextView tv_detail;

    Details details = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_act_details);

        ViewGroup.inflate(this, R.layout.activity_act_details, rlBaseMain);
        ButterKnife.bind(this);


        Bundle b = getIntent().getExtras();
      if(b!= null){
          details= (Details) getIntent().getSerializableExtra("ActHistory");
          //tv_detail.setText(details.getName()+"\n"+details.getCell()+"\n"+details.getEmail());

          if(details.getType().equalsIgnoreCase("weblink")){
              tv_detail.setText(details.getDetail());
          }
            if(details.getType().equalsIgnoreCase("contact")){
              tv_detail.setText(details.getName()+"\n"+details.getCell()+"\n"+details.getEmail());
          }

      }










    }






}
