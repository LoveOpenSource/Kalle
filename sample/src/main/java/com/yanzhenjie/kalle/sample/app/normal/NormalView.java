/*
 * Copyright 2018 Zhenjie Yan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.kalle.sample.app.normal;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.yanzhenjie.kalle.sample.R;
import com.yanzhenjie.kalle.sample.app.main.MainAdapter;
import com.yanzhenjie.kalle.sample.entity.News;
import com.yanzhenjie.kalle.sample.entity.Page;
import com.yanzhenjie.kalle.sample.util.DisplayUtils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Zhenjie Yan on 2018/3/28.
 */
public class NormalView extends Contract.NormalView {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recycler_view)
    SwipeRecyclerView mRecyclerView;

    MainAdapter mAdapter;

    public NormalView(Activity activity, Contract.NormalPresenter presenter) {
        super(activity, presenter);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().refresh();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        int color = ContextCompat.getColor(getContext(), R.color.line_color);
        int size = DisplayUtils.dip2px(1);
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(color, 0, size));
        mRecyclerView.useDefaultLoadMore();
        mRecyclerView.setLoadMoreListener(new SwipeRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                getPresenter().loadMore();
            }
        });

        mAdapter = new MainAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setRefresh(boolean refresh) {
        mRefreshLayout.setRefreshing(refresh);
    }

    @Override
    public void setDataList(List<News> dataList, Page page) {
        mAdapter.setDataList(dataList);
        mAdapter.notifyDataSetChanged();

        boolean isEmpty = dataList == null || dataList.isEmpty();
        mRecyclerView.loadMoreFinish(isEmpty, page.getPageNum() < page.getPageCount());
    }

    @Override
    public void addDataList(Page page) {
        mAdapter.notifyDataSetChanged();
        mRecyclerView.loadMoreFinish(false, page.getPageNum() < page.getPageCount());
    }
}