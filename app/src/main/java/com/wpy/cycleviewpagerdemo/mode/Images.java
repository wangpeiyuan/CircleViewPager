package com.wpy.cycleviewpagerdemo.mode;

import com.wpy.cycleviewpagerdemo.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wpy on 15/9/2.
 */
public class Images implements Serializable {
    public String mTitle;
    public int    mImageResourceId;

    public Images(String mTitle, int mImageResourceId) {
        this.mTitle = mTitle;
        this.mImageResourceId = mImageResourceId;
    }

    public static List<Images> createSampleImages() {
        final List<Images> images = new ArrayList<Images>();
        images.add(new Images("banner1", R.drawable.banner1));
        images.add(new Images("banner2", R.drawable.banner2));
        images.add(new Images("banner3", R.drawable.banner3));
        images.add(new Images("banner4", R.drawable.banner4));
        return images;
    }
}
