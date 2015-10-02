package com.wpy.cycleviewpagerdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.wpy.cycleviewpagerdemo.R;
import com.wpy.cycleviewpagerdemo.mode.Images;

/**
 * Created by wpy on 15/9/2.
 */
public class ViewPagerItemFragment extends Fragment {

    private static final String BUNDLE_KEY = "bundle_key";
    private Images mImages;

    public static ViewPagerItemFragment instantiateWithArgs(final Context context, final Images images) {
        final ViewPagerItemFragment fragment = (ViewPagerItemFragment) instantiate(context, ViewPagerItemFragment.class.getName());
        final Bundle args = new Bundle();
        args.putSerializable(BUNDLE_KEY, images);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArguments();
    }

    private void initArguments() {
        final Bundle arguments = getArguments();
        if (arguments != null) {
            mImages = (Images) arguments.getSerializable(BUNDLE_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.viewpager_item_fragment, container, false);
        initViews(view);
        return view;
    }

    private void initViews(final View view) {
        final ImageView backgroundImage = (ImageView) view.findViewById(R.id.item_fragment_image);
        backgroundImage.setImageResource(mImages.mImageResourceId);
        backgroundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), mImages.mTitle, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
