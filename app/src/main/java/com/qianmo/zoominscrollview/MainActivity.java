package com.qianmo.zoominscrollview;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Integer> list;
    ZoomHeaderScrollView zoomHeaderScrollView;
    RefreshImageView refreshImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView= (RecyclerView) findViewById(R.id.anniversary_list);
        refreshImageView= (RefreshImageView) findViewById(R.id.refresh_view);
        zoomHeaderScrollView = (ZoomHeaderScrollView) findViewById(R.id.zommLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TestAdapter testAdapter = new TestAdapter(this);
        recyclerView.setAdapter(testAdapter);
        list=new ArrayList<>();

        for (int i=0;i<20;i++){
            list.add(i);
        }


        testAdapter.notifyDataSetChanged();

        zoomHeaderScrollView.setOnPullZoomListener(new ZoomHeaderScrollView.OnPullZoomListener() {
            @Override
            public void onPullZooming(float newScrollValue) {
                refreshImageView.onDrag(newScrollValue);
            }

            @Override
            public void onPullZoomEnd(float distan) {
                if (distan>200) {
                    refreshImageView.onRefresh();
                    zoomHeaderScrollView.setZoomEnable(false);
                }else {
                    refreshImageView.setVisibility(View.GONE);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshImageView.onRefreshEnd();
                        zoomHeaderScrollView.setZoomEnable(true);
                    }
                },1000);
            }
        });

    }


    private class TestAdapter extends RecyclerView.Adapter {
        Context context;
        public TestAdapter(Context context) {
            this.context=context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new TestHolder(LayoutInflater.from(context).inflate(R.layout.test_item,viewGroup,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TestHolder testHolder= (TestHolder) viewHolder;
            testHolder.textView.setText(list.get(i)+"item");
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        private class TestHolder extends RecyclerView.ViewHolder {
            TextView textView;
            public TestHolder(View view) {
                super(view);
                textView= (TextView) view.findViewById(R.id.tv);
            }
        }
    }
}
