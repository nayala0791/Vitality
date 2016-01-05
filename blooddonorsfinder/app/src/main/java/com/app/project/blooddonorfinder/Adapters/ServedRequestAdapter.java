package com.app.project.blooddonorfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.project.blooddonorfinder.Constants.BloodDonationStatus;
import com.app.project.blooddonorfinder.DataModels.Transaction;
import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.Repositories.TransactionRepository;

import java.util.List;

/**
 * Created by Pranavi on 9/26/2015.
 */
public class ServedRequestAdapter extends RecyclerView.Adapter<ServedRequestAdapter.ServedRequestDataHolder> {

    Context mContext;
    List<Transaction> servedTransaction;


    public ServedRequestAdapter(Context context) {
        mContext = context;
        servedTransaction=TransactionRepository.getTransactionByStatus(mContext, BloodDonationStatus.REQUEST_COMPLETED);
    }

    @Override
    public ServedRequestDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView= LayoutInflater.from(mContext).inflate(R.layout.served_request_holder,parent,false);
        ServedRequestDataHolder dataHolder = new ServedRequestDataHolder(rootView);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(ServedRequestDataHolder holder, int position) {
    holder.hospitalName.setText(String.valueOf(servedTransaction.get(position).getHospitalName()));
    holder.contactNumber.setText(String.valueOf(servedTransaction.get(position).getPhoneNumber()));
    holder.address.setText(String.valueOf(servedTransaction.get(position).getHospitalAddress()));
    }

    @Override
    public int getItemCount() {
        return servedTransaction!=null?servedTransaction.size():0;
    }

    public class ServedRequestDataHolder extends RecyclerView.ViewHolder {
        TextView hospitalName,contactNumber,address;
        public ServedRequestDataHolder(View rootView) {
            super(rootView);
            hospitalName=(TextView) rootView.findViewById(R.id.hospital_name);
            contactNumber=(TextView) rootView.findViewById(R.id.contact_number);
            address=(TextView) rootView.findViewById(R.id.address);
        }
    }
}
