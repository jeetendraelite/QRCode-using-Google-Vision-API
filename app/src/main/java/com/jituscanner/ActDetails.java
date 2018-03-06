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
    Button btn_addContacts;

    @BindView(R.id.btn_sendsms)
    Button btn_sendsms;

    @BindView(R.id.dial)
    Button dial;
    @BindView(R.id.btn_sendemail)
    Button btn_sendemail;

    Details details = null;

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_act_details);

        ViewGroup.inflate(this, R.layout.activity_act_details, rlBaseMain);
        ButterKnife.bind(this);
        dial.setVisibility(View.GONE);
        btn_addContacts.setVisibility(View.GONE);
        btn_sendsms.setVisibility(View.GONE);
        btn_sendemail.setVisibility(View.GONE);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            details = (Details) getIntent().getSerializableExtra("ActHistory");
            //tv_detail.setText(details.getName()+"\n"+details.getCell()+"\n"+details.getEmail());

            if (details.getType().equalsIgnoreCase("weblink")) {
                tv_detail.setText(details.getDetail());
            }
            if (details.getType().equalsIgnoreCase("contact")) {
                tv_detail.setText(details.getName() + "\n" + details.getCell() + "\n" + details.getPhone_number() + "\n" + details.getEmail() + "\n"
                        + details.getOrganization());
                dial.setVisibility(View.VISIBLE);
                btn_addContacts.setVisibility(View.VISIBLE);
            }
            if (details.getType().equalsIgnoreCase("sms")) {

                tv_detail.setText(details.getDetail());
                btn_sendsms.setVisibility(View.VISIBLE);
            }
            if (details.getType().equalsIgnoreCase("email")) {
                tv_detail.setText(details.getDetail());
                btn_sendemail.setVisibility(View.VISIBLE);
            }
            if (details.getType().equalsIgnoreCase("Phone Number")) {
                tv_detail.setText(details.getDetail());
                dial.setVisibility(View.VISIBLE);
                btn_addContacts.setVisibility(View.VISIBLE);
            }
            if (details.getType().equalsIgnoreCase("data")) {
                tv_detail.setText(details.getDetail());
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

                if (details.getPhone_number() != null && details.getPhone_number().length() > 1) {
                    callaNumber = details.getPhone_number();
                    Log.d("phone Number", details.getPhone_number());
                }
                if (details.getCell() != null && details.getCell().length() > 1) {
                    callaNumber = details.getCell();
                    Log.d("cell Number", details.getCell());
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

                String[] addresses={TO};

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

    public  void AddContact(){
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
        if (details.getPhone_number() != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, details.getPhone_number())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        //------------------------------------------------------ Home Numbers
        if (details.getCell() != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, details.getCell())
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

    }
    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
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
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
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
