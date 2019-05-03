package com.yiyaobao.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Locale;

import com.yiyaobao.AddActivity;
import com.yiyaobao.R;
import com.yiyaobao.cn.CNPinyin;
import com.yiyaobao.entity.Medicine;
import com.yiyaobao.stickyheader.StickyHeaderAdapter;

/**
 * Created by you on 2017/9/11.
 */

public class MedicineAdapter extends RecyclerView.Adapter<MedicineHolder> implements StickyHeaderAdapter<HeaderHolder> {

    private final List<CNPinyin<Medicine>> cnPinyinList;
    private Context mContext;
    public MedicineAdapter(List<CNPinyin<Medicine>> cnPinyinList,Context context) {
        this.cnPinyinList = cnPinyinList;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return cnPinyinList.size();
    }

    @Override
    public MedicineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MedicineHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_item, parent, false));
    }


    @Override
    public void onBindViewHolder(MedicineHolder holder, final int position) {
        final Medicine medicine = cnPinyinList.get(position).data;
        holder.iv_header.setImageResource(R.mipmap.ic_launcher);
        holder.tv_name.setText(medicine.getName());
        holder.tv_changshang.setText(medicine.getChangshang());
        holder.search_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Medicine med = cnPinyinList.get(position).data;
                Intent intent = new Intent(mContext, AddActivity.class);
                intent.putExtra("medicine", med);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public long getHeaderId(int childAdapterPosition) {
        String character = (cnPinyinList.get(childAdapterPosition).getFirstChar() + "").toUpperCase(Locale.ENGLISH);
        if (character.hashCode() >= "A".hashCode() && character.hashCode() <= "Z".hashCode()) {
            return cnPinyinList.get(childAdapterPosition).getFirstChar();
        } else {
            return '#';
        }
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder holder, int childAdapterPosition) {
        String character = (cnPinyinList.get(childAdapterPosition).getFirstChar() + "").toUpperCase(Locale.ENGLISH);
        if (character.hashCode() >= "A".hashCode() && character.hashCode() <= "Z".hashCode()) {
            holder.tv_header.setText(character);
        } else {
            holder.tv_header.setText("#");
        }


    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_header, parent, false));
    }

}
