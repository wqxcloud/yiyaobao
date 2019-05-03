package com.yiyaobao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import com.yiyaobao.adapter.SearchAdapter;
import com.yiyaobao.cn.CNPinyin;
import com.yiyaobao.cn.CNPinyinIndex;
import com.yiyaobao.cn.CNPinyinIndexFactory;
import com.yiyaobao.entity.Medicine;
import com.yiyaobao.search.Contact;
import com.yiyaobao.search.TextViewChangedOnSubscribe;

/**
 * Created by you on 2017/9/12.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "SearchActivity";
    private Toolbar toolbar;
    /**
     * activity中的根目录
     */
    private View ll_root;
    /**
     * 窗体控件上一次的高度,用于监听键盘弹起
     */
    private int mLastHeight;

    private EditText et_search;

    private RecyclerView rv_main;
    private SearchAdapter adapter;

    private ArrayList<CNPinyin<Medicine>> contactList;
    private Subscription subscription;


    private ImageView mToolbarBack;


    public static void lanuch(Context context, ArrayList<CNPinyin<Medicine>> contactList) {
        if (contactList == null) return;
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("contactList", contactList);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        contactList = (ArrayList<CNPinyin<Medicine>>) getIntent().getSerializableExtra("contactList");
        if (contactList == null) {
            return;
        }
        ll_root = findViewById(R.id.ll_root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        rv_main = (RecyclerView) findViewById(R.id.rv_main);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_main.setLayoutManager(manager);
        adapter = new SearchAdapter(new ArrayList<CNPinyinIndex<Medicine>>(), this);
        rv_main.setAdapter(adapter);

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mToolbarBack = (ImageView) findViewById(R.id.toolbar_back);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        mToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        ActionBar bar = getSupportActionBar();
//        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        View actionView = getLayoutInflater().inflate(R.layout.actionbar_search, null);
//        actionView.findViewById(R.id.iv_return).setOnClickListener(this);

        et_search = (EditText) this.findViewById(R.id.toolbar_search);

//        ActionBar.LayoutParams actionBarParams = new ActionBar.LayoutParams(
//                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
//        bar.setCustomView(actionView, actionBarParams);
//        ConfigUtils.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary));


        /**
         * 下面是搜索框智能
         */
        TextViewChangedOnSubscribe plateSubscribe = new TextViewChangedOnSubscribe();
        plateSubscribe.addTextViewWatcher(et_search);
        subscription = Observable.create(plateSubscribe).debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .switchMap(new Func1<String, Observable<ArrayList<CNPinyinIndex<Medicine>>>>() {
                    @Override
                    public Observable<ArrayList<CNPinyinIndex<Medicine>>> call(final String s) {
                        return createObservable(s).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                    }
                }).subscribe(new Subscriber<ArrayList<CNPinyinIndex<Medicine>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ArrayList<CNPinyinIndex<Medicine>> cnPinyinIndices) {
                        Log.i(TAG, "onNext: cnPinyinIndices=" + cnPinyinIndices.size());
                        adapter.setNewDatas(cnPinyinIndices);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    /**
     * 搜索订阅
     *
     * @return
     */
    private Observable<ArrayList<CNPinyinIndex<Medicine>>> createObservable(final String keywork) {
        return Observable.create(new Observable.OnSubscribe<ArrayList<CNPinyinIndex<Medicine>>>() {
            @Override
            public void call(Subscriber<? super ArrayList<CNPinyinIndex<Medicine>>> subscriber) {
                Log.i(TAG, "  keywork=" + keywork);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(CNPinyinIndexFactory.indexList(contactList, keywork));
                }
            }
        });
    }

}
