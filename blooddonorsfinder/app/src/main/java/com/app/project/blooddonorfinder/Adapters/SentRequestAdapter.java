package com.app.project.blooddonorfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.project.blooddonorfinder.Constants.BloodDonationStatus;
import com.app.project.blooddonorfinder.DataModels.Donor;
import com.app.project.blooddonorfinder.DataModels.Transaction;
import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.Repositories.DonorTransactionRepository;
import com.app.project.blooddonorfinder.Repositories.TransactionRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pranavi on 9/26/2015.
 */
public class SentRequestAdapter extends RecyclerView.Adapter<SentRequestAdapter.SentRequestDataHolder> {

    Context mContext;
    List<Transaction> transactionsList=new ArrayList<>();


    public SentRequestAdapter(Context context) {

        mContext = context;
        transactionsList= TransactionRepository.getTransactionByStatus(mContext, BloodDonationStatus.REQUEST_SENT);

    }

    @Override
    public SentRequestDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView= LayoutInflater.from(mContext).inflate(R.layout.sent_request_holder,parent,false);
        SentRequestDataHolder dataHolder = new SentRequestDataHolder(rootView);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(SentRequestDataHolder holder, int position) {

        long donorId=transactionsList.get(position).getUserId();
        Donor donor=DonorTransactionRepository.getDonorByUserId(mContext, donorId);
        if(donor!=null)
        {
            holder.userName.setText(String.valueOf(donor.getUserName()));
            holder.contactNumber.setText(String.valueOf(donor.getPhoneNumber()));
            holder.bloodGroup.setText(String.valueOf(donor.getBloodGroup()));
            holder.address.setText(String.valueOf(donor.getAddress()));
        }
        else {

        }

    }

    @Override
    public int getItemCount() {
        return transactionsList!=null?transactionsList.size():0;
    }

    public class SentRequestDataHolder extends RecyclerView.ViewHolder {
        TextView userName,contactNumber,address,bloodGroup;
        public SentRequestDataHolder(View rootView)
        {
            super(rootView);
            userName=(TextView)rootView.findViewById(R.id.user_name);
            contactNumber=(TextView)rootView.findViewById(R.id.user_number);
            address=(TextView)rootView.findViewById(R.id.user_address);
            bloodGroup=(TextView)rootView.findViewById(R.id.user_blood_group);

        }
    }
}
