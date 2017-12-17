package com.exercise.p.emailclient.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exercise.p.emailclient.R;
import com.exercise.p.emailclient.activity.SignActivity;
import com.exercise.p.emailclient.dto.data.MailBoxResponse;

import java.util.ArrayList;
import java.util.Collection;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {

    private ArrayList<MailBoxResponse> boxes;

    private ArrayList<Boolean> isChoose;
    private Context context;

    public AccountAdapter(ArrayList<MailBoxResponse> data, Context context) {
        boxes = data;
        this.context = context;
        isChoose = new ArrayList<>();
        for (int i = 0; i< data.size();i++){
            isChoose.add(false);
        }
    }

    public void addBox(MailBoxResponse mailBoxResponse){
        this.boxes.add(mailBoxResponse);
        this.isChoose.add(false);
    }

    public void removeAll(Collection<?> boxes) {
        for (Object o : boxes) {
            this.notifyItemRemoved(this.boxes.indexOf(o));
            this.isChoose.remove(this.boxes.indexOf(o));
            this.boxes.remove(o);
        }
    }

    public void setChoose(int position,boolean choose){
        isChoose.set(position,choose);
    }

    public void setAllChoose(boolean choose){
        isChoose = new ArrayList<>();
        for (int i = 0; i< boxes.size(); i++){
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
        holder.accountText.setText(boxes.get(position).getAccount());
        holder.itemView.setBackgroundColor(isChoose.get(position)?
                context.getResources().getColor(R.color.colorSelected): Color.WHITE);
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
        return boxes.size();
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