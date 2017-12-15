package com.exercise.p.emailclient.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exercise.p.emailclient.R;
import com.exercise.p.emailclient.activity.SignActivity;
import com.exercise.p.emailclient.dto.data.MailPreviewResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class MailItemAdapter extends RecyclerView.Adapter<MailItemAdapter.MyViewHolder> {

    private ArrayList<MailPreviewResponse> mails;

    private ArrayList<Boolean> isChoose;
    private Context context;

    public MailItemAdapter(ArrayList<MailPreviewResponse> data, Context context) {
        mails = data;
        this.context = context;
        isChoose = new ArrayList<>();
        for (int i = 0; i< data.size();i++){
            isChoose.add(false);
        }
    }

    public void addAccount(MailPreviewResponse email){
        this.mails.add(email);
        this.isChoose.add(false);
    }

    public void removeAll(Collection<?> accounts) {
        for (Object o : accounts) {
            this.notifyItemRemoved(this.mails.indexOf(o));
            this.isChoose.remove(this.mails.indexOf(o));
            this.mails.remove(o);
        }
    }

    public void setChoose(int position,boolean choose){
        isChoose.set(position,choose);
    }

    public void setAllChoose(boolean choose){
        isChoose = new ArrayList<>();
        for (int i = 0; i< mails.size(); i++){
            isChoose.add(choose);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.layout_email_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MailPreviewResponse mail = mails.get(position);
        Log.i(SignActivity.TAG,"bind " + position + " " + isChoose.get(position));
        holder.fromText.setText(mail.getFrom());
        holder.subjectText.setText(mail.getSubject());
        holder.textText.setText(mail.getTextBody());
        holder.dateText.setText(new SimpleDateFormat("MM月dd日",Locale.getDefault()).format(mail.getSendDate()));

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mails.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fromText;
        TextView subjectText;
        TextView textText;
        TextView dateText;
        public MyViewHolder(View itemView) {
            super(itemView);
            fromText = (TextView) itemView.findViewById(R.id.mail_item_from);
            subjectText = (TextView) itemView.findViewById(R.id.mail_item_subject);
            textText = (TextView) itemView.findViewById(R.id.mail_item_text);
           dateText = (TextView) itemView.findViewById(R.id.mail_item_date);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}