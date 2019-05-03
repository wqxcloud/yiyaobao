package com.yiyaobao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.yiyaobao.adapter.MedicineAdapter;
import com.yiyaobao.entity.Medicine;
import com.yiyaobao.search.CharIndexView;
import com.yiyaobao.search.LetterView;
import com.yiyaobao.stickyheader.StickyHeaderDecoration;
import com.yiyaobao.adapter.TestUtils;
import com.yiyaobao.cn.CNPinyin;
import com.yiyaobao.cn.CNPinyinFactory;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private RecyclerView rv_main;
    private MedicineAdapter adapter;

    private CharIndexView iv_main;
    private TextView tv_index;

    private ArrayList<CNPinyin<Medicine>> contactList = new ArrayList<>();
    private Subscription subscription;
    private LetterView mLetterView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        rv_main = (RecyclerView) findViewById(R.id.rv_main);
        iv_main = (CharIndexView) findViewById(R.id.iv_main);
        tv_index = (TextView) findViewById(R.id.tv_index);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_main.setLayoutManager(mLayoutManager);

        iv_main.setOnCharIndexChangedListener(new CharIndexView.OnCharIndexChangedListener() {
            @Override
            public void onCharIndexChanged(char currentIndex) {

                Log.i(TAG, "onCharIndexChanged: currentIndex=" + currentIndex);
                for (int i = 0; i < contactList.size(); i++) {
                    Log.i(TAG, "onCharIndexChanged: contactList.get(i).getFirstChar=" + contactList.get(i).getFirstChar());
                    String character = (currentIndex + "").toUpperCase(Locale.ENGLISH);
                    Log.i(TAG, "70 onCharIndexChanged: character=" + character);
                    if (character.hashCode() < "A".hashCode() || character.hashCode() > "Z".hashCode()) {
                        Log.e(TAG, "onCharIndexChanged: scrool 0");
                        mLayoutManager.scrollToPositionWithOffset(0, 0);
                        return;
                    } else {
                        Log.w(TAG, "else character=" + character);
                        Log.w(TAG, "else currentIndex=" + currentIndex);
                        if (contactList.get(i).getFirstChar() == currentIndex) {
                            mLayoutManager.scrollToPositionWithOffset(i, 0);
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCharIndexSelected(String currentIndex) {
                if (currentIndex == null) {
                    tv_index.setVisibility(View.INVISIBLE);
                } else {
                    tv_index.setVisibility(View.VISIBLE);
                    tv_index.setText(currentIndex);
                }
            }
        });
        adapter = new MedicineAdapter(contactList,this);
        rv_main.setAdapter(adapter);
        rv_main.addItemDecoration(new StickyHeaderDecoration(adapter));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater(); //获取Menu的Inflater
        menuInflater.inflate(R.menu.main_menu, menu); //inflate你的Item布局
        //MenuItem action_search = menu.findItem(R.id.action_search); //获取MenuItem的实例,注意,这里获取的不是SearchView的实例.
        //action_search.setVisible(false); //设置Item是否可见,这里与View的设置不太一样,接受的是boolean值,只有两种状态
        //searchViewInit(menu);//自定义的一个初始化SearchView的方法
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Intent intent = new Intent(this, SettingActivity.class);
//            LogUtils.i(TAG, "startActivity  SettingActivity");
//            startActivity(intent);
//            return true;
//        }
        if (id == R.id.action_search) {
            // startActivity(new Intent(this, FriendAddActivity.class));
            Log.i(TAG, "onOptionsItemSelected: action_search");
            SearchActivity.lanuch(MainActivity.this, contactList);
        }
        if (id == R.id.action_plus) {
            Log.i(TAG, "onOptionsItemSelected: action_plus");
            Intent intent = new Intent(this, AddActivity.class);
            intent.putExtra("contactList", contactList);
            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        Log.i(TAG, "onMenuOpened: ");
        return super.onMenuOpened(featureId, menu);
    }

    private void getPinyinList() {
        subscription = Observable.create(new Observable.OnSubscribe<List<CNPinyin<Medicine>>>() {
            @Override
            public void call(Subscriber<? super List<CNPinyin<Medicine>>> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    List<CNPinyin<Medicine>> contactList = CNPinyinFactory.createCNPinyinList(TestUtils.contactList(MainActivity.this));
                    Collections.sort(contactList);
                    subscriber.onNext(contactList);
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CNPinyin<Medicine>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<CNPinyin<Medicine>> cnPinyins) {
                        contactList.clear();
                        contactList.addAll(cnPinyins);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPinyinList();
    }

    @Override
    protected void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

}
