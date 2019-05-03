package com.yiyaobao.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.yiyaobao.AddActivity;
import com.yiyaobao.R;
import com.yiyaobao.SearchActivity;
import com.yiyaobao.cn.CNPinyinIndex;
import com.yiyaobao.entity.Medicine;
import com.yiyaobao.search.Contact;

/**
 * Created by you on 2017/9/12.
 */

public class SearchAdapter extends RecyclerView.Adapter<MedicineHolder> {
    private static String TAG = "SearchAdapter";
    private final List<CNPinyinIndex<Medicine>> medicineIndexList;
    private Context mContext;

    public SearchAdapter(List<CNPinyinIndex<Medicine>> contactList,Context context) {
        this.medicineIndexList = contactList;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return medicineIndexList.size();
    }

    @Override
    public MedicineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MedicineHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MedicineHolder holder, int position) {
        CNPinyinIndex<Medicine> index = medicineIndexList.get(position);
        final Medicine medicine = index.cnPinyin.data;
        holder.iv_header.setImageResource(R.mipmap.ic_launcher);
        String name = medicine.getName();
        String changShang = medicine.getChangshang();
        int name_length = name.length();
        int changShang_length = changShang.length();

        Log.i(TAG, "onBindViewHolder: name_length=" + name_length);
        if (index.end < name_length) {//名称搜索到了
            SpannableStringBuilder ssb = new SpannableStringBuilder(medicine.getName());
            ForegroundColorSpan span = new ForegroundColorSpan(Color.GREEN);
            ssb.setSpan(span, index.start, index.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_name.setText(ssb);
            holder.tv_changshang.setText(medicine.getChangshang());
        } else {
            if (index.start >= name_length) {//厂商搜索到了
                SpannableStringBuilder ssb = new SpannableStringBuilder(medicine.getChangshang());
                ForegroundColorSpan span = new ForegroundColorSpan(Color.GREEN);
                ssb.setSpan(span, index.start - name_length, index.end - name_length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tv_changshang.setText(ssb);
                holder.tv_name.setText(medicine.getName());
            } else {//有可能名称和厂商都搜索到了，这里只取名称：如搜索  感冒灵  ，名字xx感冒，厂商  灵感
                SpannableStringBuilder ssb = new SpannableStringBuilder(medicine.getName());
                ForegroundColorSpan span = new ForegroundColorSpan(Color.GREEN);
                ssb.setSpan(span, index.start, name_length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tv_name.setText(ssb);
                holder.tv_changshang.setText(medicine.getChangshang());
            }
        }
        holder.search_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddActivity.class);
                intent.putExtra("medicine", medicine);
                mContext.startActivity(intent);
            }
        });


//        SpannableStringBuilder ssb = new SpannableStringBuilder(medicine.chinese());
//        ForegroundColorSpan span = new ForegroundColorSpan(Color.BLUE);
//        ssb.setSpan(span, index.start, index.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        holder.tv_name.setText(ssb);
        //holder.tv_changshang.setText(ssb);
    }

    public void setNewDatas(List<CNPinyinIndex<Medicine>> newDatas) {
        this.medicineIndexList.clear();
        if (newDatas != null && !newDatas.isEmpty()) {
            this.medicineIndexList.addAll(newDatas);
        }
        notifyDataSetChanged();
    }

}
