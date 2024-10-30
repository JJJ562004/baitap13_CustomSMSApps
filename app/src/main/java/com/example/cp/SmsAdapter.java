package com.example.cp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsViewHolder> {

    private List<SMS> smsList;

    public SmsAdapter(List<SMS> smsList) {
        this.smsList = smsList;
    }

    @NonNull
    @Override
    public SmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms, parent, false);
        return new SmsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmsViewHolder holder, int position) {
        SMS sms = smsList.get(position);
        holder.addressTextView.setText("Từ: " + sms.getAddress());
        holder.bodyTextView.setText(sms.getBody());
        holder.dateTextView.setText(sms.getDate());
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public static class SmsViewHolder extends RecyclerView.ViewHolder {
        TextView addressTextView, bodyTextView, dateTextView;

        public SmsViewHolder(View itemView) {
            super(itemView);
            addressTextView = itemView.findViewById(R.id.sms_address);
            bodyTextView = itemView.findViewById(R.id.sms_body);
            dateTextView = itemView.findViewById(R.id.sms_date);
        }
    }
}
