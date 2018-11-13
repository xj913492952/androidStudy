package example.home;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.xiajun.xxrefreshview.header.RefreshCircleAProgressBar;

/**
 * 自定义简洁下拉刷新头
 * Created by xiajun on 2018/11/13.
 */

public class SimpleRefreshHeader extends RelativeLayout implements RefreshHeader {
    protected final String TAG = getClass().getSimpleName();
    private TextView mTextView;
    private RefreshCircleAProgressBar progressBar;
    private int state;
    //假设旋转一度需要下拉的距离，单位：px
    int distanceOfAngle = 1;
    private int mStartAngle;

    public SimpleRefreshHeader(Context context) {
        super(context);
        initView(context);
    }

    public SimpleRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SimpleRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SimpleRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        RelativeLayout popView = (RelativeLayout) LayoutInflater.from(context).inflate(com.xiajun.xxrefreshview.R.layout.xxrefresh_default_header, null);
        progressBar = popView.findViewById(com.xiajun.xxrefreshview.R.id.xxrefresh_default_progress_bar);
        mTextView = popView.findViewById(com.xiajun.xxrefreshview.R.id.xxrefresh_default_header_tv);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 240);
        addView(popView, params);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {
        Log.e(TAG, "onInitialized height--" + height + "  extendHeight--" + extendHeight);
    }

    @Override
    public void onPulling(float percent, int offset, int height, int extendHeight) {
        Log.e(TAG, "onPulling percent--" + percent + "  offset--" + offset);
        progressBar.setmIndeterminate(false);
        mStartAngle = -offset / distanceOfAngle % 360;
        progressBar.setStartAngle(mStartAngle);
    }

    @Override
    public void onReleasing(float percent, int offset, int height, int extendHeight) {
        Log.e(TAG, "onReleasing percent--" + height + "  offset--" + offset);

    }

    @Override
    public void onReleased(RefreshLayout refreshLayout, int height, int extendHeight) {
        Log.e(TAG, "onReleased");

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int extendHeight) {
        Log.e(TAG, "onStartAnimator");

    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return 500;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None: // 无状态
                break;
            case PullDownToRefresh: // 可以下拉状态
                mTextView.setText("下拉即可刷新");
                break;
            case Refreshing: // 刷新中状态
                mTextView.setText("正在刷新...");
                progressBar.setmIndeterminate(true);
                break;
            case ReleaseToRefresh:  // 释放就开始刷新状态
                mTextView.setText("松开立即刷新");
                break;
        }
    }
}
