package com.wpy.cycleviewpagerdemo.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.wpy.cycleviewpager.Adapter.BaseCycleFragmentPagerAdapter;
import com.wpy.cycleviewpagerdemo.fragment.ViewPagerItemFragment;
import com.wpy.cycleviewpagerdemo.mode.Images;

import java.util.List;

/**
 * Created by wpy on 15/9/29.
 */
public class CycleViewPagerAdapter extends BaseCycleFragmentPagerAdapter<Images> {
    private final Context mContext;

    public CycleViewPagerAdapter(final Context context, FragmentManager fm, List<Images> images) {
        super(fm, images);
        this.mContext = context;
    }

    @Override
    protected Fragment getItemFragment(Images images, int position) {
        return ViewPagerItemFragment.instantiateWithArgs(mContext, images);
    }
}
