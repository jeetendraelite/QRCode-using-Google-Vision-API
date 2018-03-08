package com.jituscanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import com.jituscanner.R;
import com.jituscanner.utils.DatabaseHandler;
import com.jituscanner.utils.Details;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity {
    Button scanbtn, btn_delete;
    TextView result, phone;
    ImageView iv_barcode_image;
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;
    DatabaseHandler db;

    String rawValues = "";
    String displayValues = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(com.jituscanner.R.layout.activity_main);

        ViewGroup.inflate(this, R.layout.activity_main, rlBaseMain);


        scanbtn = (Button) findViewById(R.id.scanbtn);
        // btn_delete = (Button) findViewById(R.id.btn_delete);
        result = (TextView) findViewById(R.id.result);
        iv_barcode_image = (ImageView) findViewById(R.id.iv_barcode_image);

        db = new DatabaseHandler(this);

        //   phone = (TextView) findViewById(R.id.phone);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE);


            }
        });

        scanbtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                getScannerHistory();
                return false;
            }
        });


        // Experiment method




    }



    private void insertData(Details details) {
        try {
            Log.d("Insert: ", "Inserting ..");

            db.addDetails(details);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DeleteData(Details details) {
        try {
            Log.d("Delete: ", "Deleting ..");

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getScannerHistory() {
        try {
            // Reading all contacts
            Log.d("Reading: ", "Reading all Details..");
            List<Details> details = db.getAllDetails();

            for (Details cn : details) {
                String log =
                        "Id: " + cn.getId() +
                                "Name: " + cn.getName() +
                                "Phone: " + cn.getPhone_number() +
                                "email: " + cn.getEmail() +
                                "img: " + cn.getImg() +
                                "detail: " + cn.getDetail() +
                                "time: " + cn.getTime() +
                                "address: " + cn.getAddress() +
                                "organization: " + cn.getOrganization() +
                                "cell: " + cn.getCell() +
                                "URL: " + cn.getURL() +
                                "SMSMESSAGE: " + cn.getSMSMESSAGE() +
                                "SMSPHONENO: " + cn.getSMSPHONENO();

                Log.i("Reading : ", log);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            if (data != null) {
                final Barcode barcode = data.getParcelableExtra("barcode");
                result.post(new Runnable() {
                    @Override
                    public void run() {
                       /* result.setText("Display Value:- " + barcode.displayValue
                                + "\n \n Raw values:-" + barcode.rawValue);
*/
                        if (barcode.rawValue != null) {
                            rawValues = barcode.rawValue;
                        }
                        if (barcode.displayValue != null) {
                            displayValues = barcode.displayValue;
                        }


                        final Details details = new Details("", "", "", "",
                                "", "", "", "",
                                "", "", "", "", "", "");


                        if (rawValues.contains("VCARD")) {
                            details.setType("Contact");
                            details.setDetail(rawValues);
                        } else if (rawValues.contains("http")) {
                            details.setType("Weblink");
                            details.setDetail(barcode.displayValue);
                        } else if (rawValues.contains("SMSTO")) {
                            details.setType("SMS");
                            details.setDetail(rawValues);
                        } else if (rawValues.contains("MATMSG")) {
                            details.setType("EMAIL");
                            details.setDetail(rawValues);
                        } else if (rawValues.contains("tel")) {
                            Log.d("phone number", barcode.displayValue);
                            details.setType("Phone Number");
                            details.setDetail(barcode.displayValue);
                        } else {
                            details.setType("Data");
                            details.setDetail(barcode.rawValue);
                        }

                        //iv_barcode_image.setImageResource();
                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time => " + c.getTime());

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());

                        details.setTime(formattedDate);

                      //  startActivity(new Intent(MainActivity.this,ActDetails.class));


// for the contact detail

                        if (barcode.contactInfo != null) {
                            Barcode.ContactInfo contactInfo = barcode.contactInfo;
                            if (contactInfo != null) {

                                if (contactInfo.name != null) {
                                    details.setName(contactInfo.name.first + " " + contactInfo.name.last);
                                }
                                if (contactInfo.phones != null) {

                                    int UNKNOWN = 0;
                                    int WORK = 1;
                                    int HOME = 2;
                                    int FAX = 3;
                                    int MOBILE = 4;


                                    details.setCell("" + contactInfo.phones[UNKNOWN].number);
                                    details.setPhone_number("" + contactInfo.phones[WORK].number);



                                  /*  if(contactInfo.phones.length > UNKNOWN)
                                        details.setPhone_number(""+contactInfo.phones[UNKNOWN].number);

                                    if(contactInfo.phones.length > WORK)
                                        details.setPhone_number(""+contactInfo.phones[WORK].number);
*/
                                   /* if(contactInfo.phones.length > HOME)
                                        details.setPhone_number(""+contactInfo.phones[HOME].number);*/

                                    /*if(contactInfo.phones.length > MOBILE)
                                        details.setPhone_number(""+contactInfo.phones[MOBILE].number);
*/
                                    if (contactInfo.phones.length > FAX)
                                        details.setFax("" + contactInfo.phones[FAX]);


                                }

                                if (contactInfo.addresses != null) {
                                    int UNKNOWN = 0;
                                    int WORK = 1;
                                    int HOME = 2;

                                    if (contactInfo.addresses.length > UNKNOWN && contactInfo.addresses[UNKNOWN].addressLines.length > 0)
                                        details.setAddress("" + contactInfo.addresses[UNKNOWN].addressLines[0].toString());

                                    if (contactInfo.addresses.length > WORK && contactInfo.addresses[WORK].addressLines.length > 0)
                                        details.setAddress("" + contactInfo.addresses[WORK].addressLines[0].toString());

                                    if (contactInfo.addresses.length > HOME && contactInfo.addresses[HOME].addressLines.length > 0)
                                        details.setAddress("" + contactInfo.addresses[HOME].addressLines[0].toString());
                                }

                                if (contactInfo.emails != null) {
                                    int UNKNOWN = 0;
                                    int WORK = 1;
                                    int HOME = 2;

                                    if (contactInfo.emails.length > UNKNOWN)
                                        details.setEmail("" + contactInfo.emails[UNKNOWN].address.toString());

                                    if (contactInfo.emails.length > WORK)
                                        details.setEmail("" + contactInfo.emails[WORK].address.toString());

                                    if (contactInfo.emails.length > HOME)
                                        details.setEmail("" + contactInfo.emails[HOME].address.toString());
                                }

                                if (contactInfo.organization != null) {
                                    details.setOrganization(contactInfo.organization);
                                }
                                if (contactInfo.urls != null) {
                                    if (contactInfo.urls.length > 0)
                                        details.setURL(contactInfo.urls[0]);

                                   /* if(contactInfo.urls.length > 1)
                                     details.setURL(contactInfo.urls[1]);*/
                                }

                            }
                        }

                        if (barcode.sms != null) {
                            Barcode.Sms sms = barcode.sms;
                            if (barcode.sms != null) {
                                details.setSMSMESSAGE(sms.message);
                                details.setSMSPHONENO(sms.phoneNumber);
                            }
                        }
                        if (barcode.email != null) {
                            Barcode.Email email = barcode.email;
                            details.setEMAIL_TO(email.address);
                            details.setEMAIL_SUB(email.subject);
                            details.setEMAIL_BODY(email.body);

                        }

                        // DeleteData(details);
                        insertData(details);

                        Intent intent = new Intent(MainActivity.this, ActDetails.class);

                        intent.putExtra("ActHistory", details);
                        startActivity(intent);
                        // insert to table
                        // phone.setText(barcode.phone);
                    }
                });
            }
        }
    }
}
