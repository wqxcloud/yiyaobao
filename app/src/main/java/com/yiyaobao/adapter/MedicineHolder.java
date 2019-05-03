package com.yiyaobao.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyaobao.R;

/**
 * Created by you on 2017/9/11.
 */

public class MedicineHolder extends RecyclerView.ViewHolder {

    public final View search_item;
    public final ImageView iv_header;

    public final TextView tv_name;
    public final TextView tv_changshang;
    public MedicineHolder(View itemView) {
        super(itemView);
        search_item = (View) itemView.findViewById(R.id.search_item);
        iv_header = (ImageView) itemView.findViewById(R.id.iv_header);
        tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        tv_changshang = (TextView) itemView.findViewById(R.id.tv_changshang);
    }
}
