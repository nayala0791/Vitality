package com.app.project.blooddonorfinder.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.project.blooddonorfinder.AsyncTask.ATResponseToRequest;
import com.app.project.blooddonorfinder.DataModels.Transaction;
import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.Repositories.TransactionRepository;

import java.util.List;

/**
 * Created by Pranavi on 9/26/2015.
 */
public class IncomingRequestAdapter extends RecyclerView.Adapter<IncomingRequestAdapter.IncomingRequestDataHolder> {

    Context mContext;
    List<Transaction> transactionList;


    public IncomingRequestAdapter(Context context) {

        mContext = context;
        transactionList = TransactionRepository.getAllTransaction(mContext);

    }

    @Override
    public IncomingRequestAdapter.IncomingRequestDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.incoming_request_holder, parent, false);
        IncomingRequestDataHolder dataHolder = new IncomingRequestDataHolder(rootView);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(IncomingRequestAdapter.IncomingRequestDataHolder holder, int position) {
        holder.hospitalName.setText(String.valueOf(transactionList.get(position).getHospitalName()));
        holder.hospitalAddress.setText(String.valueOf(transactionList.get(position).getHospitalAddress()));
        holder.hospitalContactNumber.setText(String.valueOf(transactionList.get(position).getPhoneNumber()));

        if (transactionList.get(position).getStatus() != null) {
            if (transactionList.get(position).getStatus() == 2) {
                holder.actionAccept.setVisibility(View.GONE);
                holder.actionReject.setVisibility(View.GONE);
                holder.status.setVisibility(View.VISIBLE);
                holder.status.setText("You Have Accepted this request");
                holder.status.setTextColor(Color.GREEN);
            } else if (transactionList.get(position).getStatus() == 3) {
                holder.actionAccept.setVisibility(View.GONE);
                holder.actionReject.setVisibility(View.GONE);
                holder.status.setVisibility(View.VISIBLE);
                holder.status.setText("You Have Rejected this request");
                holder.status.setTextColor(Color.RED);
            } else {
                holder.actionAccept.setVisibility(View.VISIBLE);
                holder.actionReject.setVisibility(View.VISIBLE);
                holder.status.setVisibility(View.GONE);

            }
        }

    }

    public OnRequestUpdateCompleteListener onRequestUpdateCompleteListener = new OnRequestUpdateCompleteListener() {
        @Override
        public void onRefresh() {
            transactionList.clear();
            transactionList = TransactionRepository.getAllTransaction(mContext);
            notifyDataSetChanged();
        }
    };

    public interface OnRequestUpdateCompleteListener {
        public void onRefresh();
    }

    @Override
    public int getItemCount() {
        return transactionList != null ? transactionList.size() : 0;
    }

    public class IncomingRequestDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView hospitalName, hospitalContactNumber, hospitalAddress, actionAccept, actionReject, status;

        public IncomingRequestDataHolder(View rootView) {

            super(rootView);
            hospitalName = (TextView) rootView.findViewById(R.id.hospital_name);
            hospitalContactNumber = (TextView) rootView.findViewById(R.id.contact_number);
            hospitalAddress = (TextView) rootView.findViewById(R.id.address);
            actionAccept = (TextView) rootView.findViewById(R.id.action_accept);
            actionReject = (TextView) rootView.findViewById(R.id.action_reject);
            status = (TextView) rootView.findViewById(R.id.request_status);

            actionAccept.setOnClickListener(this);
            actionReject.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.action_accept:
                    new ATResponseToRequest(mContext, transactionList.get(getAdapterPosition()), 1, onRequestUpdateCompleteListener).execute();
                    break;
                case R.id.action_reject:
                    new ATResponseToRequest(mContext, transactionList.get(getAdapterPosition()), 0, onRequestUpdateCompleteListener).execute();
                    break;

            }


        }
    }
}
