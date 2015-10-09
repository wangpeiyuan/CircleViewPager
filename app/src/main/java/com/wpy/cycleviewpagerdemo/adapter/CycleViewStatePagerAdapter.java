package com.wpy.cycleviewpagerdemo.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.wpy.circleviewpager.Adapter.BaseCycleFragmentStatePagerAdapter;
import com.wpy.cycleviewpagerdemo.fragment.ViewPagerItemFragment;
import com.wpy.cycleviewpagerdemo.mode.Images;

import java.util.List;

/**
 * Created by wpy on 15/9/2.
 */
public class CycleViewStatePagerAdapter extends BaseCycleFragmentStatePagerAdapter<Images> {

    private final Context mContext;

    public CycleViewStatePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    public CycleViewStatePagerAdapter(final Context context, FragmentManager fragmentManager, List<Images> images) {
        super(fragmentManager, images);
        this.mContext = context;
    }

    @Override
    protected Fragment getItemFragment(Images images, int position) {
        return ViewPagerItemFragment.instantiateWithArgs(mContext, images);
    }
}
