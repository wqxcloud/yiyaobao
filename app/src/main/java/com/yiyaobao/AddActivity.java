package com.yiyaobao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yiyaobao.cn.CNPinyin;
import com.yiyaobao.entity.Medicine;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "AddActivity";

    private TextView tv_index;
    private Button button;
    private AutoCompleteTextView et_guoyao_number;
    private AutoCompleteTextView et_guoyao_name;
    private AutoCompleteTextView et_guoyao_changshang;
    private AutoCompleteTextView et_guoyao_guige;
    private AutoCompleteTextView et_guoyao_type;
    private AutoCompleteTextView et_guoyao_jixing;
    private AutoCompleteTextView et_guoyao_inprice;
    private AutoCompleteTextView et_guoyao_outprice;

    private TextView mToolbarLeftTitle;
    private ImageView mToolbarBack;
    private Button mTtoolbarConfirm;

    private Medicine mMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        initToolBar();
    }

    private void initView() {
        et_guoyao_number = (AutoCompleteTextView) findViewById(R.id.et_guoyao_number);
        et_guoyao_name = (AutoCompleteTextView) findViewById(R.id.et_guoyao_name);
        et_guoyao_changshang = (AutoCompleteTextView) findViewById(R.id.et_guoyao_changshang);
        et_guoyao_guige = (AutoCompleteTextView) findViewById(R.id.et_guoyao_guige);
        et_guoyao_type = (AutoCompleteTextView) findViewById(R.id.et_guoyao_type);
        et_guoyao_jixing = (AutoCompleteTextView) findViewById(R.id.et_guoyao_jixing);
        et_guoyao_inprice = (AutoCompleteTextView) findViewById(R.id.et_guoyao_inprice);
        et_guoyao_outprice = (AutoCompleteTextView) findViewById(R.id.et_guoyao_outprice);
        mMedicine = (Medicine) getIntent().getSerializableExtra("medicine");
        if (mMedicine != null) {
            et_guoyao_number.setText(mMedicine.getNumber());
            et_guoyao_name.setText(mMedicine.getName());
            et_guoyao_changshang.setText(mMedicine.getChangshang());
            et_guoyao_guige.setText(mMedicine.getGuiGe());
            et_guoyao_type.setText(mMedicine.getType());
            et_guoyao_jixing.setText(mMedicine.getJiXing());
            et_guoyao_inprice.setText(mMedicine.getInPrice()+"");
            et_guoyao_outprice.setText(mMedicine.getOutPrice()+"");
        }
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mToolbarLeftTitle = (TextView) findViewById(R.id.toolbar_title_left);
        mToolbarLeftTitle.setVisibility(View.VISIBLE);
        mToolbarBack = (ImageView) findViewById(R.id.toolbar_back);
        mTtoolbarConfirm = (Button) findViewById(R.id.toolbar_confirm);
        mTtoolbarConfirm.setOnClickListener(this);
        mTtoolbarConfirm.setTextColor(this.getResources().getColor(R.color.white));
        mToolbarLeftTitle.setText(toolbar.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        mToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(mMedicine != null){
            mToolbarLeftTitle.setText("更新");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_confirm:
                String name = et_guoyao_name.getText().toString().trim();
                String number = et_guoyao_number.getText().toString().trim();
                String changshang = et_guoyao_changshang.getText().toString().trim();
                String guige = et_guoyao_guige.getText().toString().trim();
                String type = et_guoyao_type.getText().toString().trim();
                String jixing = et_guoyao_jixing.getText().toString().trim();
                String inPri = et_guoyao_inprice.getText().toString().trim();
                String outPri = et_guoyao_outprice.getText().toString().trim();
                float inPrice = 0l;
                if(!TextUtils.isEmpty(inPri)){
                    inPrice = Float.valueOf(inPri);
                }
                float outPrice = 0l;
                if(!TextUtils.isEmpty(outPri)){
                    outPrice = Float.valueOf(outPri);
                }

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(this, "药品名称不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(changshang)) {
                    Toast.makeText(this, "药品厂商不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                DaoUtils daoUtils = new DaoUtils(this);
                boolean result;
                if (mMedicine != null) {
                    Medicine medicine = daoUtils.queryMedicineById(mMedicine.getId());
                    Log.i(TAG, "onClick: medicine="+medicine);
                    if(medicine != null){
                        //result = daoUtils.deleteMedicine(mMedicine);
                        mMedicine.setName(name);
                        mMedicine.setNumber(number);
                        mMedicine.setChangshang(changshang);
                        mMedicine.setGuiGe(guige);
                        mMedicine.setType(type);
                        mMedicine.setJiXing(jixing);
                        mMedicine.setInPrice(inPrice);
                        mMedicine.setOutPrice(outPrice);
                        result = daoUtils.updateMedicine(mMedicine);
                        Log.i(TAG, "onClick: mMedicine.getName()="+mMedicine.getName());
                        if (result) {
                            Toast.makeText(this, "修改完成", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    result = daoUtils.insertMedicine(new Medicine(null, name, number, changshang, guige, type, jixing,inPrice,outPrice,0));
                    if (result) {
                        Toast.makeText(this, "添加完成", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

}
