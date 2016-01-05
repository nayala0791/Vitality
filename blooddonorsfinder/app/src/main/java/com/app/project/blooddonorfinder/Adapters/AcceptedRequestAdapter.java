package com.app.project.blooddonorfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.project.blooddonorfinder.AsyncTask.ATRequestMarkComplete;
import com.app.project.blooddonorfinder.Constants.BloodDonationStatus;
import com.app.project.blooddonorfinder.DataModels.Donor;
import com.app.project.blooddonorfinder.DataModels.Transaction;
import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.Repositories.DonorTransactionRepository;
import com.app.project.blooddonorfinder.Repositories.TransactionRepository;

import java.util.List;

/**
 *  * Created by Pranavi on 9/26/2015.
 */
public class AcceptedRequestAdapter extends RecyclerView.Adapter<AcceptedRequestAdapter.AcceptedRequestDataHolder> {

    Context mContext;
    List<Transaction> transactionList;


    public AcceptedRequestAdapter(Context context) {

        mContext = context;
        transactionList= TransactionRepository.getTransactionByStatus(mContext, BloodDonationStatus.REQUEST_ACCEPTED);
    }

    @Override
    public AcceptedRequestDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView= LayoutInflater.from(mContext).inflate(R.layout.accepted_request_holder,parent,false);
        AcceptedRequestDataHolder dataHolder = new AcceptedRequestDataHolder(rootView);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(AcceptedRequestDataHolder holder, int position) {
        long userId=transactionList.get(position).getUserId();
        Donor donor= DonorTransactionRepository.getDonorById(mContext,userId);
        if(donor!=null) {
            holder.mDonorName.setText(String.valueOf(donor.getUserName()));
            holder.mDonorContact.setText(String.valueOf(donor.getPhoneNumber()));
            holder.mDonorAddress.setText(String.valueOf(donor.getAddress()));
            holder.mDonorBloodGroup.setText(String.valueOf(donor.getBloodGroup()));

            if(transactionList.get(position).getStatus()==BloodDonationStatus.REQUEST_COMPLETED)
            {
                holder.mActionMarkComplete.setClickable(false);
                holder.mActionMarkComplete.setText("Completed");
            }else{
                holder.mActionMarkComplete.setClickable(true);
                holder.mActionMarkComplete.setText("Mark Complete");
            }
        }

    }

    public OnMarkCompletListener onMarkCompletListener=new OnMarkCompletListener() {
        @Override
        public void onMarkCompleteSuccessful() {
            transactionList.clear();
            transactionList=TransactionRepository.getTransactionByStatus(mContext,BloodDonationStatus.REQUEST_ACCEPTED);
            notifyDataSetChanged();
        }
    };


    public interface OnMarkCompletListener{
        public void onMarkCompleteSuccessful();
    }
    @Override
    public int getItemCount() {
        return transactionList!=null?transactionList.size():0;
    }

    public class AcceptedRequestDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mDonorName,mDonorContact,mDonorAddress,mDonorBloodGroup,mActionMarkComplete;
        public AcceptedRequestDataHolder(View rootView) {
           super(rootView);
            mDonorName=(TextView)rootView.findViewById(R.id.donor_name);
            mDonorContact=(TextView)rootView.findViewById(R.id.donor_contact);
            mDonorAddress=(TextView)rootView.findViewById(R.id.donor_address);
            mDonorBloodGroup=(TextView)rootView.findViewById(R.id.donor_blood_group_req_don);
            mActionMarkComplete=(TextView)rootView.findViewById(R.id.action_mark_complete);
            mActionMarkComplete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            new ATRequestMarkComplete(mContext,transactionList.get(getAdapterPosition()),onMarkCompletListener).execute();
        }
    }
}
