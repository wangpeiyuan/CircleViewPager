package com.wpy.cycleviewpagerdemo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wpy.cycleviewpager.Indicator.PagerIndicator;
import com.wpy.cycleviewpager.Listener.BaseCyclePageChangeListener;
import com.wpy.cycleviewpagerdemo.R;
import com.wpy.cycleviewpagerdemo.adapter.CycleViewStatePagerAdapter;
import com.wpy.cycleviewpagerdemo.mode.Images;

import java.util.List;

/**
 * Created by wpy on 15/9/2.
 */
public class CycleViewPagerActivity extends FragmentActivity {

    private final String TAG = "CycleViewPagerActivity";

    private ViewPager      mViewPager;
    private Button         setItemBtn;
    private Button         addItemBtn;
    private Button         removeItemBtn;
    private PagerIndicator mPagerIndicator;
    private List<Images>   mImages;

    private CycleViewStatePagerAdapter  mCycleViewStatePagerAdapter;
    private BaseCyclePageChangeListener mBaseCyclePageChangeListener;
    private int                         tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cylce_viewpager_demo);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerIndicator = (PagerIndicator) findViewById(R.id.pagerIndicator);
        setItemBtn = (Button) findViewById(R.id.set_item);
        addItemBtn = (Button) findViewById(R.id.add_item);
        removeItemBtn = (Button) findViewById(R.id.remove_item);

        setListener();

//        mImages = Images.createSampleImages();
//        tmp = mImages.size();
//        mCycleViewStatePagerAdapter = new CycleViewStatePagerAdapter(this,getSupportFragmentManager(),mImages);

        mCycleViewStatePagerAdapter = new CycleViewStatePagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mCycleViewStatePagerAdapter);


//        mBaseCyclePageChangeListener = new BaseCyclePageChangeListener(mViewPager);
//        mBaseCyclePageChangeListener.setOnPageChangeListener(createOnPageChangeListener());
//        mViewPager.addOnPageChangeListener(mBaseCyclePageChangeListener);


        mPagerIndicator.setViewPager(mViewPager);
        mPagerIndicator.redraw();
        mPagerIndicator.setAutoScroll();
    }

    private void setListener() {
        setItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImages = Images.createSampleImages();
                tmp = mImages.size();
                mCycleViewStatePagerAdapter.setItems(mImages);
            }
        });
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmp++;
                mImages.add(new Images("banner" + tmp, R.drawable.banner2));
                mCycleViewStatePagerAdapter.setItems(mImages);
            }
        });
        removeItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tmp - 1 >= 0) {
                    tmp--;
                    mImages.remove(tmp);
                    mCycleViewStatePagerAdapter.setItems(mImages);
                }
            }
        });
    }

    private ViewPager.OnPageChangeListener createOnPageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
//                setDLog(" onPageScrolled position >> ", position + "");
            }

            @Override
            public void onPageSelected(final int position) {
                setDLog(" onPageSelected position >> ", position + "");
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
//                setDLog(" onPageScrollStateChanged state >> ", state + "");
            }
        };
    }


    private void setDLog(String tag, String msg) {
        Log.d(TAG + tag, msg);
    }

}
