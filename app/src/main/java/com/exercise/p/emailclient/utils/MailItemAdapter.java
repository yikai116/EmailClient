package com.exercise.p.emailclient.utils;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.exercise.p.emailclient.GlobalInfo;
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
        for (int i = 0; i < data.size(); i++) {
            isChoose.add(false);
        }
    }

    public void addAccount(MailPreviewResponse email) {
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

    public void setChoose(int position, boolean choose) {
        isChoose.set(position, choose);
    }

    public void setAllChoose(boolean choose) {
        isChoose = new ArrayList<>();
        for (int i = 0; i < mails.size(); i++) {
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
        Log.i(SignActivity.TAG, "bind " + position + " " + isChoose.get(position));
        if (isChoose.get(position)) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorSelected));
            float scale = context.getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (8 * scale + 0.5f);
            holder.avatar.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
            holder.avatar.setImageResource(R.drawable.icon_avatar_selected);
        } else {
            holder.itemView.setBackgroundColor(0);
            holder.avatar.setPadding(0, 0, 0, 0);
            holder.avatar.setImageResource(R.drawable.icon_side_avatar);
        }
        ArrayList<SimpleAccount> fromList = SimpleAccount.toList(mail.getFrom());
        StringBuilder fromStr = new StringBuilder("");
        for (int i = 0; i < fromList.size(); i++) {
            fromStr.append(fromList.get(i).getName() == null
                    ? fromList.get(i).getEmailAddr() : fromList.get(i).getName());
            if (i != fromList.size() - 1)
                fromStr.append(",");
        }
        String data = new SimpleDateFormat("MM月dd日", Locale.getDefault()).format(mail.getSendDate());
        if (mail.isSeen()) {
            holder.subjectText.setTextColor(context.getResources().getColor(R.color.colorTextGrey));
            holder.dateText.setTextColor(context.getResources().getColor(R.color.colorTextGrey));
            holder.fromText.setText(fromStr);
            holder.subjectText.setText(mail.getSubject());
            holder.dateText.setText(new SimpleDateFormat("MM月dd日", Locale.getDefault()).format(mail.getSendDate()));
        } else {
            holder.subjectText.setTextColor(context.getResources().getColor(R.color.colorTextBlack));
            holder.dateText.setTextColor(context.getResources().getColor(R.color.colorTextBlue));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.fromText.setText(Html.fromHtml(
                        "<b>" + fromStr + "</b>", Html.FROM_HTML_MODE_COMPACT));
                holder.subjectText.setText(Html.fromHtml(
                        "<b>" + mail.getSubject() + "</b>", Html.FROM_HTML_MODE_COMPACT));
                holder.dateText.setText(Html.fromHtml(
                        "<b>" + data + "</b>", Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.fromText.setText(Html.fromHtml(
                        "<b>" + fromStr + "</b>"));
                holder.subjectText.setText(Html.fromHtml(
                        "<b>" + mail.getSubject() + "</b>"));
                holder.dateText.setText(Html.fromHtml(
                        "<b>" + data + "</b>"));
            }
        }
        holder.textText.setText(mail.getTextBody());

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
        ImageView avatar;

        public MyViewHolder(View itemView) {
            super(itemView);
            fromText = (TextView) itemView.findViewById(R.id.mail_item_from);
            subjectText = (TextView) itemView.findViewById(R.id.mail_item_subject);
            textText = (TextView) itemView.findViewById(R.id.mail_item_text);
            dateText = (TextView) itemView.findViewById(R.id.mail_item_date);
            avatar = itemView.findViewById(R.id.mail_item_avatar);
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