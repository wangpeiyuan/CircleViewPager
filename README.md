# Android Cycle ViewPager
![](https://github.com/cock-tail/CircleViewPager/blob/master/screenshots/cycleviewpager.gif)

* **实现**  
主要通过实现BaseCycleFragmentStatePagerAdapter，使得item的个数比原来的item多两个。比如有A>B>C>D四个item，然后将最后一个(D)复制一份放到首位，将第一个(A)复制一份放到末尾，就成了D>A>B>C>D>A六个item。但需要注意的是，当数据源大于1的时候，必须将ViewPager current item 设为1，因为ViewPager默认显示为0.

  最主要的是边界时滑动的处理，当ViewPager在1（A）的时候，向左滑动，此时ViewPager显示的D,
  position=0,如果不处理的话，那么就没办法继续向左滑动，所以ViewPager.setCurrentItem(4, false);
  同样的当ViewPager在4（D）的时候，向右滑动，将ViewPager.setCurrentItem(1, false);

## Current Problem
* just support FragmentStatePagerAdapter

## Usage
### Step 1
* Gradle
```
coming soon
```

### Step 2

* layout
```
<android.support.v4.view.ViewPager
            android:id="@+id/view_pager"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>
```
use default indicators
```
<com.wpy.cycleviewpager.Indicator.PagerIndicator
            android:id="@+id/pagerIndicator"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentBottom="true"
            android:layout_centerHorizontal="true"
            android:layout_marginBottom="@dimen/dp_40"
            android:gravity="center"
            app:selected_color="#bf1900"
            app:shape="oval"
            app:unselected_color="@android:color/darker_gray"/>
```
* extends BaseCycleFragmentStatePagerAdapter
```
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
```

### Step 3

```
mCycleViewStatePagerAdapter = new CycleViewStatePagerAdapter(this, getSupportFragmentManager());
mViewPager.setAdapter(mCycleViewStatePagerAdapter);

mBaseCyclePageChangeListener = new BaseCyclePageChangeListener(mViewPager);
mBaseCyclePageChangeListener.setOnPageChangeListener(createOnPageChangeListener());
mViewPager.addOnPageChangeListener(mBaseCyclePageChangeListener);
```
Or if you use default indicators

```
mCycleViewStatePagerAdapter = new CycleViewStatePagerAdapter(this, getSupportFragmentManager());
mViewPager.setAdapter(mCycleViewStatePagerAdapter);

mPagerIndicator.setViewPager(mViewPager);
mPagerIndicator.redraw();
```
### Step 4
Auto Scroll
```
mBaseCyclePageChangeListener.startAutoScroll();
```
Or
```
mPagerIndicator.setAutoScroll();
```
## Thanks
[CircularViewPager](https://github.com/TobiasBuchholz/CircularViewPager)

[AndroidImageSlider](https://github.com/daimajia/AndroidImageSlider)

## About
if you have any problems when using this library, you can write to me <wpy0830@gmail.com>
