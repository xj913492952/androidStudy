package example.gesture;

import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.framework.R;
import com.style.framework.databinding.ActivityTestGestureBinding;

/**
 * Created by xiajun on 2016/10/8.
 */
public class TestGestureActivity extends BaseActivity {

    ActivityTestGestureBinding bd;
    private float mDownRawX;
    private float mDownRawY;
    private boolean isShouldFinish;

    protected boolean isGeneralTitleBar() {
        return false;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.activity_test_gesture;
    }
    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }

    @Override
    public void initData() {
        bd = getBinding();

        bd.iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("v2", "onTouchEvent--ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e("v2", "onTouchEvent--ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("v2", "onTouchEvent--ACTION_UP");
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("v1", "dispatchTouchEvent--ACTION_DOWN");
                mDownRawX = ev.getRawX();
                mDownRawY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("v1", "dispatchTouchEvent--ACTION_MOVE");
                //mDownRawX = ev.getRawX();
                float translationY = (ev.getRawY() - mDownRawY);
                bd.getRoot().setTranslationY(translationY);
                if (translationY < -300) {
                    isShouldFinish = true;
                } else {
                    isShouldFinish = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e("v1", "dispatchTouchEvent--ACTION_UP");
                if (isShouldFinish) {

                    finish();
                } else {
                    ValueAnimator va = ValueAnimator.ofFloat(bd.getRoot().getTranslationY(), 0f);
                    va.setDuration(300);
                    //插值器，表示值变化的规律，默认均匀变化
                    va.setInterpolator(new DecelerateInterpolator());
                    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float v = (float) animation.getAnimatedValue();
                            bd.getRoot().setTranslationY(v);
                            Log.d(TAG, "translationY:" + v);
                        }
                    });
                    va.start();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}