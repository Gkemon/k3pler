package com.k3.k3pler.frag;


import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.k3.k3pler.R;
import com.k3.k3pler.handler.RequestDialog;
import com.k3.k3pler.handler.SqliteDBHelper;
import com.k3.k3pler.sub.HTTPReq;
import com.k3.k3pler.sub.SQLiteBL;

import java.util.ArrayList;

/** Main page **/
public class MainPageInflater {
    private final Context context;
    private final ViewGroup viewGroup;
    private SqliteDBHelper sqliteDBHelper;
    private final ArrayList httpReqs;
    public interface IMainPage {
        void onRecyclerViewInit(RecyclerView recyclerView);
        void onTextViewInit(TextView textView);
    }
    // ** //
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swpMain;
    private TextView txvMainPageMsg;

    public MainPageInflater(Context context, ViewGroup viewGroup, ArrayList httpReqs){
        this.context = context;
        this.viewGroup = viewGroup;
        this.httpReqs = httpReqs;
    }
    public void init(IMainPage iMainPage){
        txvMainPageMsg = viewGroup.findViewById(R.id.txvMainPageMsg);
        recyclerView = viewGroup.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        iMainPage.onRecyclerViewInit(recyclerView);
        iMainPage.onTextViewInit(txvMainPageMsg);
        swpMain = viewGroup.findViewById(R.id.swpMain);
        swpMain.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            httpReqs.clear();
            recyclerView.setAdapter(null);
            txvMainPageMsg.setVisibility(View.VISIBLE);
            swpMain.setRefreshing(false);
        }, 800));
    }
    public void onDetailDialogItemClick(final HTTPReq item, final BlacklistPageInflater blacklistPageInflater){
        new RequestDialog(context, item).show((btnReqBlackList, dialog, uri) -> btnReqBlackList.setOnClickListener(view -> {
            sqliteDBHelper = new SqliteDBHelper(context,
                    new SQLiteBL(context).getWritableDatabase(),
                    SQLiteBL.BLACKLIST_DATA, SQLiteBL.TABLE_NAME);
            if(!sqliteDBHelper.getAll().contains(uri)) {
                sqliteDBHelper.insert(uri);
            }
            sqliteDBHelper.close();
            dialog.cancel();
            blacklistPageInflater.setBlacklistLstView();
        }));
    }
}
