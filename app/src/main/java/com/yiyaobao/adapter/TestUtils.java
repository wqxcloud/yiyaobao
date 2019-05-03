package com.yiyaobao.adapter;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.yiyaobao.DaoUtils;
import com.yiyaobao.R;
import com.yiyaobao.entity.Medicine;

/**
 * Created by you on 2017/9/11.
 */

public class TestUtils {

    private static String TAG = "TestUtils";
    public static List<Medicine> contactList(Context context) {
        List<Medicine> contactList = new ArrayList<>();
        Random random = new Random(System.currentTimeMillis());

        DaoUtils daoUtils = new DaoUtils(context);
        List<Medicine> medicineList = daoUtils.queryAllMedicine();
        for(Medicine medicine:medicineList){
            Log.i(TAG, "  medicine.getId()="+medicine.getId());
            Log.i(TAG, "  medicine.getName()="+medicine.getName());
            int urlIndex = random.nextInt(URLS.length - 1);
            int url = URLS[urlIndex];
            contactList.add(medicine);
        }
//        String[] names = context.getResources().getStringArray(R.array.names);
//        for (int i = 0; i < names.length; i++) {
//            int urlIndex = random.nextInt(URLS.length - 1);
//            int url = URLS[urlIndex];
//            contactList.add(new Contact(names[i], url));
//        }
        return contactList;
    }

    static int[] URLS = {R.drawable.header0, R.drawable.header1, R.drawable.header2, R.drawable.header3};

}
