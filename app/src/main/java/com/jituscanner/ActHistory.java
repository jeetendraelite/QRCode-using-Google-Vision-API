package com.jituscanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.android.gms.vision.barcode.Barcode;
import com.jituscanner.utils.DatabaseHandler;
import com.jituscanner.utils.Details;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActHistory extends BaseActivity {
    DatabaseHandler db;

    List<Details> listDetails = new ArrayList<>();


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.materialRefreshLayout)
    MaterialRefreshLayout materialRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(com.jituscanner.R.layout.activity_main);


        try {


            ViewGroup.inflate(this, R.layout.activity_history, rlBaseMain);

            ButterKnife.bind(this);

            db = new DatabaseHandler(this);

            // for the base
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ActHistory.this, MainActivity.class);
                    startActivity(intent);


                }
            });

            getScannerHistory();

            //setadapter

            iniatialize();

            //  setClickEvents();

            setAdapterData();
            // for the listing

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAdapterData() {
        try {

            //arrayListAllDoctorListModel = StaticDataList.getDoctorList();
            // List<Details> listDetails = new ArrayList<>();
            ListAdapter listAdapter = new ListAdapter(this, listDetails);

            recyclerView.setAdapter(listAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.VersionViewHolder> {
        List<Details> listDetails;
        Context mContext;


        public ListAdapter(Context context, List<Details> arrayListFollowers) {
            listDetails = arrayListFollowers;
            mContext = context;
        }

        @Override
        public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_history_list, viewGroup, false);
            VersionViewHolder viewHolder = new VersionViewHolder(view);
            return viewHolder;
        }


        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(final VersionViewHolder versionViewHolder, final int i) {
            try {
                final Details mDetail = listDetails.get(i);

                versionViewHolder.tvtype.setText(mDetail.getType());
               // versionViewHolder.tvdetail.setText(mDetail.getDetail());


                if (mDetail.getType().equalsIgnoreCase("Weblink")) {
                    versionViewHolder.tvdetail.setText(mDetail.getDetail());
                }

                if (mDetail.getType().equalsIgnoreCase("Contact")) {
                    versionViewHolder.tvdetail.setText(mDetail.getName() + "\n" + mDetail.getCell() + "\n" + mDetail.getEmail());
                }


               // versionViewHolder.tvdetail.setText(mDetail.getName() + "\n" + mDetail.getCell() + "\n" + mDetail.getEmail());


                if (mDetail.getType().equalsIgnoreCase("contact")) {
                    versionViewHolder.img.setImageResource(R.drawable.ic_perm_contact_calendar_black_24dp);
                }

                versionViewHolder.rlMainLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ActHistory.this, ActDetails.class);

                        intent.putExtra("ActHistory", listDetails.get(i));
                        startActivity(intent);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return listDetails.size();
        }


        class VersionViewHolder extends RecyclerView.ViewHolder {

            TextView tvtype, tvdetail;

            RelativeLayout rlMainLay;
            ImageView img;


            public VersionViewHolder(View itemView) {
                super(itemView);

                rlMainLay = (RelativeLayout) itemView.findViewById(R.id.rlMainLay);


                img = (ImageView) itemView.findViewById(R.id.img);
                tvtype = (TextView) itemView.findViewById(R.id.tvtype);
                tvdetail = (TextView) itemView.findViewById(R.id.tvdetail);


            }

        }
    }

    private void iniatialize() {
        try {
            materialRefreshLayout.setIsOverLay(true);
            materialRefreshLayout.setWaveShow(true);
            materialRefreshLayout.setWaveColor(0x55ffffff);

            materialRefreshLayout.setLoadMore(false); // enable if pagination

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void getScannerHistory() {
        try {
            // Reading all contacts
            Log.d("Reading: ", "Reading all Details..");
            listDetails = db.getAllDetails();

            for (Details cn : listDetails) {
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
                                "URL: " + cn.getURL();

                Log.i("Reading : ", log);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
