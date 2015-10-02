package com.wpy.cycleviewpager.Indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wpy.cycleviewpager.Adapter.BaseCycleAdapterInterface;
import com.wpy.cycleviewpager.Listener.BaseCyclePageChangeListener;
import com.wpy.cycleviewpager.R;

import java.util.ArrayList;

/**
 * Created by wpy on 15/9/30.
 * <p>
 * Thanks:https://github.com/daimajia/AndroidImageSlider/
 */
public class PagerIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {

    private Context   mContext;
    private ViewPager mViewPager;
    /**
     * Variable to remember the previous selected indicator.
     */
    private ImageView mPreviousSelectedIndicator;

    /**
     * Previous selected indicator position.
     */
    private int mPreviousSelectedPosition;

    /**
     * Custom selected indicator style resource id.
     */
    private int mUserSetUnSelectedIndicatorResId;


    /**
     * Custom unselected indicator style resource id.
     */
    private int mUserSetSelectedIndicatorResId;

    private Drawable mSelectedDrawable;
    private Drawable mUnselectedDrawable;

    /**
     * This value is from {@link BaseCycleAdapterInterface} getRealCount() represent
     * <p>
     * the indicator count that we should draw.
     */
    private int mItemCount = 0;

    private Shape mIndicatorShape = Shape.Oval;

    public enum Shape {
        Oval, Rectangle
    }

    private IndicatorVisibility mVisibility = IndicatorVisibility.Visible;

    public enum IndicatorVisibility {
        Visible,
        Invisible
    }

    private int mDefaultSelectedColor;
    private int mDefaultUnSelectedColor;

    private float mDefaultSelectedWidth;
    private float mDefaultSelectedHeight;

    private float mDefaultUnSelectedWidth;
    private float mDefaultUnSelectedHeight;

    private GradientDrawable mUnSelectedGradientDrawable;
    private GradientDrawable mSelectedGradientDrawable;

    private LayerDrawable mSelectedLayerDrawable;
    private LayerDrawable mUnSelectedLayerDrawable;

    private float mPadding_left;
    private float mPadding_right;
    private float mPadding_top;
    private float mPadding_bottom;

    private float mSelectedPadding_Left;
    private float mSelectedPadding_Right;
    private float mSelectedPadding_Top;
    private float mSelectedPadding_Bottom;

    private float mUnSelectedPadding_Left;
    private float mUnSelectedPadding_Right;
    private float mUnSelectedPadding_Top;
    private float mUnSelectedPadding_Bottom;

    /**
     * Put all the indicators into a ArrayList, so we can remove them easily.
     */
    private ArrayList<ImageView> mIndicators = new ArrayList<ImageView>();

    public enum Unit {
        DP, Px
    }

    private BaseCyclePageChangeListener mBaseCyclePageChangeListener;

    public PagerIndicator(Context context) {
        this(context, null);
    }


    public PagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;

        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PagerIndicator, 0, 0);

        int visibility = attributes.getInt(R.styleable.PagerIndicator_visibility, IndicatorVisibility.Visible.ordinal());

        for (IndicatorVisibility v : IndicatorVisibility.values()) {
            if (v.ordinal() == visibility) {
                mVisibility = v;
                break;
            }
        }

        int shape = attributes.getInt(R.styleable.PagerIndicator_shape, Shape.Oval.ordinal());
        for (Shape s : Shape.values()) {
            if (s.ordinal() == shape) {
                mIndicatorShape = s;
                break;
            }
        }

        mUserSetSelectedIndicatorResId = attributes.getResourceId(R.styleable.PagerIndicator_selected_drawable,
                0);
        mUserSetUnSelectedIndicatorResId = attributes.getResourceId(R.styleable.PagerIndicator_unselected_drawable,
                0);

        mDefaultSelectedColor = attributes.getColor(R.styleable.PagerIndicator_selected_color, Color.rgb(255, 255, 255));
        mDefaultUnSelectedColor = attributes.getColor(R.styleable.PagerIndicator_unselected_color, Color.argb(33, 255, 255, 255));

        mDefaultSelectedWidth = attributes.getDimension(R.styleable.PagerIndicator_selected_width, (int) pxFromDp(6));
        mDefaultSelectedHeight = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_selected_height, (int) pxFromDp(6));

        mDefaultUnSelectedWidth = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_unselected_width, (int) pxFromDp(6));
        mDefaultUnSelectedHeight = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_unselected_height, (int) pxFromDp(6));

        mSelectedGradientDrawable = new GradientDrawable();
        mUnSelectedGradientDrawable = new GradientDrawable();

        mPadding_left = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_padding_left, (int) pxFromDp(3));
        mPadding_right = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_padding_right, (int) pxFromDp(3));
        mPadding_top = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_padding_top, (int) pxFromDp(0));
        mPadding_bottom = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_padding_bottom, (int) pxFromDp(0));

        mSelectedPadding_Left = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_selected_padding_left, (int) mPadding_left);
        mSelectedPadding_Right = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_selected_padding_right, (int) mPadding_right);
        mSelectedPadding_Top = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_selected_padding_top, (int) mPadding_top);
        mSelectedPadding_Bottom = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_selected_padding_bottom, (int) mPadding_bottom);

        mUnSelectedPadding_Left = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_unselected_padding_left, (int) mPadding_left);
        mUnSelectedPadding_Right = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_unselected_padding_right, (int) mPadding_right);
        mUnSelectedPadding_Top = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_unselected_padding_top, (int) mPadding_top);
        mUnSelectedPadding_Bottom = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_unselected_padding_bottom, (int) mPadding_bottom);

        mSelectedLayerDrawable = new LayerDrawable(new Drawable[]{mSelectedGradientDrawable});
        mUnSelectedLayerDrawable = new LayerDrawable(new Drawable[]{mUnSelectedGradientDrawable});

        setIndicatorStyleResource(mUserSetSelectedIndicatorResId, mUserSetUnSelectedIndicatorResId);
        setDefaultIndicatorShape(mIndicatorShape);
        setDefaultSelectedIndicatorSize(mDefaultSelectedWidth, mDefaultSelectedHeight, Unit.Px);
        setDefaultUnselectedIndicatorSize(mDefaultUnSelectedWidth, mDefaultUnSelectedHeight, Unit.Px);
        setDefaultIndicatorColor(mDefaultSelectedColor, mDefaultUnSelectedColor);
        setIndicatorVisibility(mVisibility);
        attributes.recycle();
    }

    /**
     * Set Indicator style.
     *
     * @param selected   page selected drawable
     * @param unselected page unselected drawable
     */
    public void setIndicatorStyleResource(int selected, int unselected) {
        mUserSetSelectedIndicatorResId = selected;
        mUserSetUnSelectedIndicatorResId = unselected;
        if (selected == 0) {
            mSelectedDrawable = mSelectedLayerDrawable;
        } else {
            mSelectedDrawable = mContext.getResources().getDrawable(mUserSetSelectedIndicatorResId);
        }
        if (unselected == 0) {
            mUnselectedDrawable = mUnSelectedLayerDrawable;
        } else {
            mUnselectedDrawable = mContext.getResources().getDrawable(mUserSetUnSelectedIndicatorResId);
        }

        resetDrawable();
    }

    /**
     * if you are using the default indicator, this method will help you to set the shape of
     * indicator, there are two kind of shapes you  can set, oval and rect.
     *
     * @param shape
     */
    public void setDefaultIndicatorShape(Shape shape) {
        if (mUserSetSelectedIndicatorResId == 0) {
            if (shape == Shape.Oval) {
                mSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
            } else {
                mSelectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
            }
        }
        if (mUserSetUnSelectedIndicatorResId == 0) {
            if (shape == Shape.Oval) {
                mUnSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
            } else {
                mUnSelectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
            }
        }
        resetDrawable();
    }

    public void setDefaultSelectedIndicatorSize(float width, float height, Unit unit) {
        if (mUserSetSelectedIndicatorResId == 0) {
            float w = width;
            float h = height;
            if (unit == Unit.DP) {
                w = pxFromDp(width);
                h = pxFromDp(height);
            }
            mSelectedGradientDrawable.setSize((int) w, (int) h);
            resetDrawable();
        }
    }

    public void setDefaultUnselectedIndicatorSize(float width, float height, Unit unit) {
        if (mUserSetUnSelectedIndicatorResId == 0) {
            float w = width;
            float h = height;
            if (unit == Unit.DP) {
                w = pxFromDp(width);
                h = pxFromDp(height);
            }
            mUnSelectedGradientDrawable.setSize((int) w, (int) h);
            resetDrawable();
        }
    }

    /**
     * if you are using the default indicator , this method will help you to set the selected status and
     * the unselected status color.
     *
     * @param selectedColor
     * @param unselectedColor
     */
    public void setDefaultIndicatorColor(int selectedColor, int unselectedColor) {
        if (mUserSetSelectedIndicatorResId == 0) {
            mSelectedGradientDrawable.setColor(selectedColor);
        }
        if (mUserSetUnSelectedIndicatorResId == 0) {
            mUnSelectedGradientDrawable.setColor(unselectedColor);
        }
        resetDrawable();
    }

    /**
     * set the visibility of indicator.
     *
     * @param visibility
     */
    public void setIndicatorVisibility(IndicatorVisibility visibility) {
        if (visibility == IndicatorVisibility.Visible) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.INVISIBLE);
        }
        resetDrawable();
    }

    private void resetDrawable() {
        for (View i : mIndicators) {
            if (mPreviousSelectedIndicator != null && mPreviousSelectedIndicator.equals(i)) {
                ((ImageView) i).setImageDrawable(mSelectedDrawable);
            } else {
                ((ImageView) i).setImageDrawable(mUnselectedDrawable);
            }
        }
    }

    public void setDefaultIndicatorSize(float width, float height, Unit unit) {
        setDefaultSelectedIndicatorSize(width, height, unit);
        setDefaultUnselectedIndicatorSize(width, height, unit);
    }

    public void setAutoScroll() {
        if (mBaseCyclePageChangeListener == null) {
            throw new IllegalStateException("BaseCyclePageChangeListener does not instance");
        }
        mBaseCyclePageChangeListener.startAutoScroll();
    }

    /**
     * bind indicator with viewpager.
     *
     * @param pager
     */
    public void setViewPager(ViewPager pager) {
        if (pager.getAdapter() == null || !(pager.getAdapter() instanceof BaseCycleAdapterInterface)) {
            throw new IllegalStateException("Viewpager does not have adapter instance or adapter does not implements BaseCycleAdapterInterface");
        }
        this.mViewPager = pager;
        mBaseCyclePageChangeListener = new BaseCyclePageChangeListener(mViewPager);
        mBaseCyclePageChangeListener.setOnPageChangeListener(this);
        mViewPager.addOnPageChangeListener(mBaseCyclePageChangeListener);
        mBaseCyclePageChangeListener.setAdapterDataChangeObserverListener(mObserverListener);
    }

    /**
     * redraw the indicators.
     */
    public void redraw() {
        mItemCount = getShouldDrawCount();
        mPreviousSelectedIndicator = null;
        for (View i : mIndicators) {
            removeView(i);
        }
        if (mItemCount > 1) {
            for (int i = 0; i < mItemCount; i++) {
                addIndicatorView();
            }
            setItemAsSelected(mPreviousSelectedPosition);
        }
    }

    private void addIndicatorView() {
        ImageView indicator = new ImageView(mContext);
        indicator.setImageDrawable(mUnselectedDrawable);
        indicator.setPadding((int) mUnSelectedPadding_Left,
                (int) mUnSelectedPadding_Top,
                (int) mUnSelectedPadding_Right,
                (int) mUnSelectedPadding_Bottom);
        addView(indicator);
        mIndicators.add(indicator);
    }

    private int getShouldDrawCount() {
        if (mViewPager.getAdapter() instanceof BaseCycleAdapterInterface) {
            return ((BaseCycleAdapterInterface) mViewPager.getAdapter()).getRealCount();
        } else {
            return mViewPager.getAdapter().getCount();
        }
    }

    private void setItemAsSelected(int position) {
        if (mPreviousSelectedIndicator != null) {
            mPreviousSelectedIndicator.setImageDrawable(mUnselectedDrawable);
            mPreviousSelectedIndicator.setPadding(
                    (int) mUnSelectedPadding_Left,
                    (int) mUnSelectedPadding_Top,
                    (int) mUnSelectedPadding_Right,
                    (int) mUnSelectedPadding_Bottom
            );
        }
        ImageView currentSelected = (ImageView) getChildAt(position);
        if (currentSelected != null) {
            currentSelected.setImageDrawable(mSelectedDrawable);
            currentSelected.setPadding(
                    (int) mSelectedPadding_Left,
                    (int) mSelectedPadding_Top,
                    (int) mSelectedPadding_Right,
                    (int) mSelectedPadding_Bottom
            );
            mPreviousSelectedIndicator = currentSelected;
        }
        mPreviousSelectedPosition = position;
    }

    /**
     * clear self means unregister the dataset observer and remove all the child views(indicators).
     */
    public void destroySelf() {
        if (mViewPager == null || mViewPager.getAdapter() == null) {
            return;
        }
        mBaseCyclePageChangeListener.removeAdapterDataChangeObserverListener();
        removeAllViews();
    }

    private BaseCyclePageChangeListener.AdapterDataChangeObserverListener mObserverListener = new BaseCyclePageChangeListener.AdapterDataChangeObserverListener() {
        @Override
        public void onChange() {
            PagerAdapter adapter = mViewPager.getAdapter();
            int count;
            if (adapter instanceof BaseCycleAdapterInterface) {
                count = ((BaseCycleAdapterInterface) adapter).getRealCount();
            } else {
                count = adapter.getCount();
            }
            if (count == 1 || count == 0) {
                removeAllViews();
                mIndicators.clear();
            } else if (count > mItemCount) {
                for (int i = 0; i < count - mItemCount; i++) {
                    addIndicatorView();
                }
                if (mItemCount == 1) {
                    addIndicatorView();
                }
            } else if (count < mItemCount) {
                for (int i = 0; i < mItemCount - count; i++) {
                    removeView(mIndicators.get(0));
                    mIndicators.remove(0);
                }
            }
            mItemCount = count;
            setItemAsSelected(mBaseCyclePageChangeListener.getViewpagerCurrentItem());
        }

        @Override
        public void onInvalidated() {
            redraw();
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setItemAsSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private float dpFromPx(float px) {
        return px / this.getContext().getResources().getDisplayMetrics().density;
    }

    private float pxFromDp(float dp) {
        return dp * this.getContext().getResources().getDisplayMetrics().density;
    }
}
