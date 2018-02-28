package com.jituscanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import com.jituscanner.R;
import com.jituscanner.utils.DatabaseHandler;
import com.jituscanner.utils.Details;

import java.util.List;

public class MainActivity extends BaseActivity {
    Button scanbtn;
    TextView result,phone;
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;
    DatabaseHandler db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(com.jituscanner.R.layout.activity_main);

        ViewGroup.inflate(this, R.layout.activity_main, rlBaseMain);


        scanbtn = (Button) findViewById(R.id.scanbtn);
        result = (TextView) findViewById(R.id.result);

        db = new DatabaseHandler(this);

     //   phone = (TextView) findViewById(R.id.phone);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE);*/



                Details details =   new Details("aaa","aaa","aaaa");


                insertData(details);


            }
        });

        scanbtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                getScannerHistory();
                return false;
            }
        });
    }


    private void insertData(Details details)
    {
        try{
            Log.d("Insert: ", "Inserting ..");

            db.addDetails(details);
           // db.addDetails(new Details("Raj","8989","xyz@gm.com"));
            //db.addDetails(new Details("Kiran","89863","kiran@gm.com"));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getScannerHistory()
    {
        try{
            // Reading all contacts
            Log.d("Reading: ", "Reading all Details..");
            List<Details> details = db.getAllDetails();

            for (Details cn : details) {
                String log =
                                "Id: " + cn.getId() +
                                "Name: " + cn.getName() +
                                "Phone: " + cn.getPhone_number()+
                                "email: " + cn.getEmail()
                        ;

                Log.i("Reading : ",log);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){

            if(data != null){
                final Barcode barcode = data.getParcelableExtra("barcode");
                result.post(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(barcode.displayValue
                        +"\n \n Raw values"+ barcode.rawValue);


                        Details details =   new Details(barcode.displayValue,"123456",barcode.rawValue);

                        /*if(barcode.rawValue.contains("VCARD"))
                        {
                            details
                        }
                      */
                        insertData(details);

                        // insert to table
                       // phone.setText(barcode.phone);
                    }
                });
            }
        }
    }
}
