package com.app.project.blooddonorfinder.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.project.blooddonorfinder.AsyncTask.ATRequestBloodDonation;
import com.app.project.blooddonorfinder.DataModels.Donor;
import com.app.project.blooddonorfinder.Modules.HospitalTransactions.DonorsInListView;
import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.Repositories.DonorTransactionRepository;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pranavi on 9/26/2015.
 */
public class DonorsListAdapter extends RecyclerView.Adapter<DonorsListAdapter.DonorsListsDataHolder> {

    Context mContext;
    List<Donor> data=new ArrayList<>();

    public DonorsListAdapter(Context context) {
        mContext = context;
        data= DonorTransactionRepository.getAllDonors(context);
    }

    @Override
    public DonorsListsDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView= LayoutInflater.from(mContext).inflate(R.layout.donors_list_holder,parent,false);
        DonorsListsDataHolder dataHolder = new DonorsListsDataHolder(rootView);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(DonorsListsDataHolder holder, int position) {

        holder.userName.setText(String.valueOf(data.get(position).getUserName()));
        holder.bloodGroup.setText(String.valueOf(data.get(position).getBloodGroup()));
        holder.address.setText(String.valueOf(data.get(position).getAddress()));
        String locationString=String.valueOf(data.get(position).getLastKnownLocation());
        Geocoder geocoder=new Geocoder(mContext);

        String[] latLong=null;
        if(locationString!=null)
        latLong=locationString.split(";");

        List<Address> addressList=new ArrayList<>();
        if(latLong!=null && latLong.length==2)
            try {
                addressList=geocoder.getFromLocation(Double.valueOf(latLong[0]),Double.valueOf(latLong[1]),1);
            } catch (IOException e) {
                e.printStackTrace();
            }

        if(addressList.size()>0)
            holder.currentAddress.setText(String.valueOf(addressList.get(0).getAddressLine(0)));
        else
            holder.currentAddress.setText(String.valueOf("NA"));

    }

    public OnRequestDonationListener onRequestDonationListener=new OnRequestDonationListener() {
        @Override
        public void onRequestSuccessful() {
            Toast.makeText(mContext,"Request Sent Successfully",Toast.LENGTH_LONG).show();

        }

        @Override
        public void onRequestFailure() {
            Toast.makeText(mContext,"Request Not Sent!Please Try Again",Toast.LENGTH_LONG).show();
        }
    };

    public interface OnRequestDonationListener
    {
        public void onRequestSuccessful();
        public void onRequestFailure();

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class DonorsListsDataHolder extends RecyclerView.ViewHolder {
        TextView userName,bloodGroup,address,currentAddress,requestDonation;
        public DonorsListsDataHolder(View rootView) {

            super(rootView);
            userName=(TextView)rootView.findViewById(R.id.user_name);
            bloodGroup=(TextView)rootView.findViewById(R.id.blood_group);
            address=(TextView)rootView.findViewById(R.id.address);
            currentAddress=(TextView)rootView.findViewById(R.id.current_address);
            requestDonation=(TextView)rootView.findViewById(R.id.request_donation);
            requestDonation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences appPreferences = mContext.getSharedPreferences(mContext.getString(R.string.app_preference), mContext.MODE_PRIVATE);
                    long hospitalId=appPreferences.getLong(mContext.getString(R.string.userid), -1);
                    new ATRequestBloodDonation(mContext,onRequestDonationListener,hospitalId,data.get(getAdapterPosition()).getUserId()).execute();
                }
            });
        }
    }
}
