package com.jituscanner;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jituscanner.utils.Details;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.cketti.mailto.EmailIntentBuilder;

public class ActDetails extends BaseActivity {

    @BindView(R.id.tv_detail)
    TextView tv_detail;

    @BindView(R.id.btn_addContacts)
    ImageView btn_addContacts;

    @BindView(R.id.btn_sendsms)
    ImageView btn_sendsms;

    @BindView(R.id.iv_type)
    ImageView iv_type;

    @BindView(R.id.dial)
    ImageView dial;

    @BindView(R.id.btn_sendemail)
    ImageView btn_sendemail;

    @BindView(R.id.btn_showmap)
    ImageView btn_showmap;

    @BindView(R.id.tv_current_time)
    TextView tv_current_time;

    @BindView(R.id.llContactScanAction)
    LinearLayout llContactScanAction;

    @BindView(R.id.llContactSMSAction)
    LinearLayout llContactSMSAction;


    @BindView(R.id.llShareContact)
    LinearLayout llShareContact;


    @BindView(R.id.llShareEmail)
    LinearLayout llShareEmail;


    @BindView(R.id.llShareSms)
    LinearLayout llShareSms;


    @BindView(R.id.llContactEmailAction)
    LinearLayout llContactEmailAction;


    @BindView(R.id.tv_type_detail)
    TextView tv_type_detail;

    Details details = null;


    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_act_details);

        ViewGroup.inflate(this, R.layout.activity_act_details, rlBaseMain);
        ButterKnife.bind(this);
        llContactEmailAction.setVisibility(View.GONE);
        llContactSMSAction.setVisibility(View.GONE);
        llContactScanAction.setVisibility(View.GONE);

        // dial.setVisibility(View.GONE);
       /* btn_addContacts.setVisibility(View.GONE);
        btn_sendsms.setVisibility(View.GONE);
        btn_sendemail.setVisibility(View.GONE);

*/

        Bundle b = getIntent().getExtras();
        if (b != null) {
            details = (Details) getIntent().getSerializableExtra("ActHistory");
            //tv_detail.setText(details.getName()+"\n"+details.getCell()+"\n"+details.getEmail());
            tv_current_time.setText(details.getTime());

            btn_showmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(details.getAddress()));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });

            if (details.getType().equalsIgnoreCase("weblink")) {
                tv_detail.setText(details.getDetail());
                tv_type_detail.setText("Weblink");
                iv_type.setImageResource(R.drawable.ic_weblink_black_24dp);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(details.getDetail()));
                startActivity(intent);

            }
            if (details.getType().equalsIgnoreCase("contact")) {
                tv_detail.setText(details.getName() + "\n" + details.getCell() + "\n" + details.getPhone_number() + "\n" + details.getEmail() + "\n"
                        + details.getOrganization() + "\n" + details.getAddress());
                tv_type_detail.setText("Contact");

                llContactScanAction.setVisibility(View.VISIBLE);

                llShareContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String shareBody = tv_detail.getText().toString();
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        ;
                        // sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    }
                });

            }
            if (details.getType().equalsIgnoreCase("sms")) {

                tv_type_detail.setText("SMS");
                // tv_detail.setText(details.getDetail());
                //  btn_sendsms.setVisibility(View.VISIBLE);
                llContactSMSAction.setVisibility(View.VISIBLE);
                iv_type.setImageResource(R.drawable.ic_sms_black_24dp);
                tv_detail.setText( details.getSMSPHONENO() + "\n" + details.getSMSMESSAGE());

                llShareSms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String shareBody = tv_detail.getText().toString();
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        ;
                        // sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    }
                });
            }
            if (details.getType().equalsIgnoreCase("email")) {
                // tv_detail.setText(details.getDetail());
                tv_type_detail.setText("Email");
                llContactEmailAction.setVisibility(View.VISIBLE);
                tv_detail.setText(details.getEMAIL_TO() + "\n" + details.getEMAIL_SUB() +
                        "\n" + details.getEMAIL_BODY());
                iv_type.setImageResource(R.drawable.ic_email_black_24dp);

                llShareEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String shareBody = tv_detail.getText().toString();
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        ;
                        // sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    }
                });
            }
            if (details.getType().equalsIgnoreCase("Phone Number")) {
                tv_detail.setText(details.getDetail());
                iv_type.setImageResource(R.drawable.ic_phone_black_24dp);
                tv_type_detail.setText("Phone Number");
                //  dial.setVisibility(View.VISIBLE);
                //  btn_addContacts.setVisibility(View.VISIBLE);
            }
            if (details.getType().equalsIgnoreCase("data")) {
                tv_detail.setText(details.getDetail());
                tv_type_detail.setText("Data");
                iv_type.setImageResource(R.drawable.ic_datausage_usage_black_24dp);
            }

        }
        btn_sendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = details.getSMSMESSAGE();
                String phoneNo = details.getSMSPHONENO();

                if (!TextUtils.isEmpty(message) && !TextUtils.isEmpty(phoneNo)) {
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNo));
                    smsIntent.putExtra("sms_body", message);
                    startActivity(smsIntent);
                }


            }
        });
        btn_addContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddContact();
            }
        });
        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                //String callaNumber=details.getCell().trim();
                String callaNumber = "";


                if (details.getCell() != null && details.getCell().length() > 1) {
                    callaNumber = details.getCell();
                    Log.d("cell Number", details.getCell());
                } else if (details.getPhone_number() != null && details.getPhone_number().length() > 1) {
                    callaNumber = details.getPhone_number();
                    Log.d("phone Number", details.getPhone_number());
                }
                callIntent.setData(Uri.parse("tel:" + callaNumber));
                // callIntent.setData(Uri.parse("tel:91-000-000-0000")); // this is working
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }

        });

        btn_sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TO = details.getEMAIL_TO();
                String Sub = details.getEMAIL_SUB();
                String Message_Body = details.getEMAIL_BODY();

                String[] addresses = {TO};

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                intent.putExtra(Intent.EXTRA_SUBJECT, Sub);
                intent.putExtra(Intent.EXTRA_STREAM, Message_Body);
                intent.putExtra(Intent.EXTRA_TEXT, Message_Body);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });


    }

    public void AddContact() {
        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (details.getName() != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            details.getName()).build());
        }

        //------------------------------------------------------ Mobile Number
        if (details.getCell() != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, details.getCell())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        //------------------------------------------------------ Home Numbers
        if (details.getPhone_number() != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, details.getPhone_number())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }


        //------------------------------------------------------ Email
        if (details.getEmail() != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, details.getEmail())
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }


        // Asking the Contact provider to create a new contact
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(ActDetails.this, "Contact Added Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            Log.d("TAG", e.getMessage());
        }

        ActivityCompat.requestPermissions(ActDetails.this,
                new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE},
                1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ActDetails.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}



