package com.wpy.cycleviewpager.Listener;

import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wpy.cycleviewpager.Adapter.BaseCycleAdapterInterface;

import java.lang.ref.WeakReference;


/**
 * Created by wpy on 15/9/1.
 */
public class BaseCyclePageChangeListener implements ViewPager.OnPageChangeListener {
    public static final int SET_ITEM_DELAY = 160;

    private ViewPager                         mViewPager;
    private ViewPager.OnPageChangeListener    mListener;
    private AdapterDataChangeObserverListener mObserverListener;
    private int                               mItemCount;

    public BaseCyclePageChangeListener(ViewPager viewPager) {
        if (viewPager.getAdapter() == null || !(viewPager.getAdapter() instanceof BaseCycleAdapterInterface)) {
            throw new IllegalStateException("Viewpager does not have adapter instance or adapter does not implements BaseCycleAdapterInterface");
        }
        this.mViewPager = viewPager;
        this.mItemCount = ((BaseCycleAdapterInterface) mViewPager.getAdapter()).getRealCount();
        mViewPager.getAdapter().registerDataSetObserver(mDataSetObserver);
        mViewPager.setCurrentItem(1, false);
        initScroll();
    }

    /**
     * 设置OnPageChangeListener，如果要自己处理的话
     *
     * @param listener
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * 获取当前ViewpagerCurrentItem，去除边界之后的
     *
     * @return
     */
    public int getViewpagerCurrentItem() {
        return mViewPager.getCurrentItem() >= 1 ? mViewPager.getCurrentItem() - 1 : mViewPager.getCurrentItem();
    }

    public void setAdapterDataChangeObserverListener(AdapterDataChangeObserverListener listener) {
        this.mObserverListener = listener;
    }

    public void removeAdapterDataChangeObserverListener() {
        this.mObserverListener = null;
    }

    @Override
    public void onPageSelected(int position) {
        handleSetCurrentItemWithDelay(position);
        callOnPageSelected(position);
    }

    private void handleSetCurrentItemWithDelay(final int position) {
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                handleSetCurrentItem(position);
            }
        }, SET_ITEM_DELAY);
    }

    private void handleSetCurrentItem(final int position) {
        final int lastPosition = mViewPager.getAdapter().getCount() - 1;
        if (position == 0) {
            mViewPager.setCurrentItem(lastPosition == 0 ? 0 : lastPosition - 1, false);
        } else if (position == lastPosition) {
            mViewPager.setCurrentItem(1, false);
        }
    }

    private void callOnPageSelected(final int position) {
        if (mListener != null) {
            if (position == 0 || position == mViewPager.getAdapter().getCount() - 1) {//防止ViewPager.setCurrentItem时调用传出position
                Log.d("callOnPageSelected ", "This position is Useless");
            } else {
                mListener.onPageSelected(position - 1);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        callOnPageScrollStateChanged(state);
    }

    private void callOnPageScrollStateChanged(final int state) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        callOnPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    private void callOnPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        if (mListener != null) {
            if (position == 0) {
                //当position＝0，为了使viewpager向左滑动，OnPageScrolled的position显示为前一个，由于没想到使用场景，这个可能还存在问题！！！
                mListener.onPageScrolled(mViewPager.getAdapter().getCount() == 1 ? 0 : mViewPager.getAdapter().getCount() - 3, positionOffset, positionOffsetPixels);
//                mListener.onPageScrolled(0, positionOffset, positionOffsetPixels);
            } else if (position == mViewPager.getAdapter().getCount() - 1) {//防止ViewPager.setCurrentItem时调用传出position
                Log.d("callOnPageScrolled ", "This position is Useless");
            } else {
                mListener.onPageScrolled(position - 1, positionOffset, positionOffsetPixels);
            }
        }
    }

    /**
     * 监听viewpager adapter的数据变化，用在此类当中主要是为了防止以下这两种情况：
     * 1.当viewpager没有item或只有一个item时，增加了item的数量时，显示的界面为最后一个item并且左右两边有一边是不能滑动的
     * 2.当有多个item，viewpager处在最后一个item时，减少item的数量，显示界面为第一个，同样有一边不能滑动；
     * 原因：当viewpager的item数量变化的时候，viewpager的CurrentItem是不变的，而实现这个无限循环主要是在item前后各加一个item
     * 具体请看实现原理，所以处在边界时，item发生变化，viewpager的显示就会出现问题
     */
    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            PagerAdapter adapter = mViewPager.getAdapter();
            int count;
            if (adapter instanceof BaseCycleAdapterInterface) {
                count = ((BaseCycleAdapterInterface) adapter).getRealCount();
            } else {
                count = adapter.getCount();
            }
            if (mViewPager.getCurrentItem() == 0 && mItemCount >= 0 && mItemCount <= 1 && count > mItemCount) {
                setCurrentItemWhenDataChange(1);
            } else if (mViewPager.getCurrentItem() == mItemCount
                    && count < mItemCount) {
                setCurrentItemWhenDataChange(count);
            }
            mItemCount = count;
            if (mObserverListener != null) {
                mObserverListener.onChange();
            }
            sendScrollMessage();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            if (mObserverListener != null) {
                mObserverListener.onInvalidated();
            }
        }
    };

    private void setCurrentItemWhenDataChange(final int position) {
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(position, false);
            }
        }, 0);
    }

    private final static int AUTO_SCROLL_WHAT = 0;

    private boolean isAutoScroll      = false;
    private long    mDelayTimeInMills = 3000;
    private Handler mHandler;

    private void initScroll() {
        mHandler = new AutoScrollHandler(this);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        stopAutoScroll();
                        break;
                    case MotionEvent.ACTION_UP:
                        startAutoScroll();
                        break;
                }
                return false;
            }
        });
    }

    public void setDelayTimeInMills(long delayTimeInMills) {
        this.mDelayTimeInMills = delayTimeInMills;
    }

    public void startAutoScroll() {
        isAutoScroll = true;
        sendScrollMessage();
    }

    public void stopAutoScroll() {
        isAutoScroll = false;
        mHandler.removeMessages(AUTO_SCROLL_WHAT);
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    private void sendScrollMessage() {
        if (mHandler != null) {
            mHandler.removeMessages(AUTO_SCROLL_WHAT);
            if (isAutoScroll && mViewPager.getAdapter().getCount() > 1) {
                mHandler.sendEmptyMessageDelayed(AUTO_SCROLL_WHAT, mDelayTimeInMills);
            }
        }
    }

    private class AutoScrollHandler extends Handler {

        private final WeakReference<BaseCyclePageChangeListener> mWeakReference;

        public AutoScrollHandler(BaseCyclePageChangeListener listener) {
            this.mWeakReference = new WeakReference<BaseCyclePageChangeListener>(listener);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AUTO_SCROLL_WHAT) {
                BaseCyclePageChangeListener listener = mWeakReference.get();
                int size = listener.getViewPager().getAdapter().getCount();
                int position = (listener.getViewPager().getCurrentItem() + 1) % size;
                listener.getViewPager().setCurrentItem(position, true);
                listener.sendScrollMessage();
            }
        }
    }

    public interface AdapterDataChangeObserverListener {
        void onChange();

        void onInvalidated();
    }
}
