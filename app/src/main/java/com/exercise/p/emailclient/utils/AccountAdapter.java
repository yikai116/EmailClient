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
import com.exercise.p.emailclient.dto.data.Email;

import java.util.ArrayList;
import java.util.Collection;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {

    private ArrayList<Email> accounts;

    private ArrayList<Boolean> isChoose;
    private Context context;

    public AccountAdapter(ArrayList<Email> data, Context context) {
        accounts = data;
        this.context = context;
        isChoose = new ArrayList<>();
        for (int i = 0; i< data.size();i++){
            isChoose.add(false);
        }
    }

    public void addAccount(Email email){
        this.accounts.add(email);
        this.isChoose.add(false);
    }

    public void removeAll(Collection<?> accounts) {
        for (Object o : accounts) {
            this.notifyItemRemoved(this.accounts.indexOf(o));
            this.isChoose.remove(this.accounts.indexOf(o));
            this.accounts.remove(o);
        }
    }

    public void setChoose(int position,boolean choose){
        isChoose.set(position,choose);
    }

    public void setAllChoose(boolean choose){
        isChoose = new ArrayList<>();
        for (int i = 0; i< accounts.size();i++){
            isChoose.add(choose);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.layout_account_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Log.i(SignActivity.TAG,"bind " + position + " " + isChoose.get(position));
        holder.accountText.setText(accounts.get(position).getAccount());
        holder.itemView.setBackgroundColor(isChoose.get(position)?
                context.getResources().getColor(R.color.colorHint):0);
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
        return accounts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView accountText;
        public MyViewHolder(View itemView) {
            super(itemView);
            accountText = (TextView) itemView.findViewById(R.id.account_item_email);
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