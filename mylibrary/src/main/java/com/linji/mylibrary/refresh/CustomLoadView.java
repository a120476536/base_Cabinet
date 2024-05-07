package com.linji.mylibrary.refresh;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.linji.mylibrary.R;


/**
 * Created by Administrator on 2018/6/12.
 */

public class CustomLoadView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.layout_loadmore_view;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
