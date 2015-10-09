package com.wpy.circleviewpager.Adapter;


import java.util.List;

/**
 * Created by wpy on 15/9/29.
 */
public interface BaseCycleAdapterInterface<Item> {
    /**
     * 获取实际的数量
     */
    int getRealCount();

    /**
     * 设置刷新数据源
     */
    void setItems(final List<Item> items);

    /**
     * 根据指定的位置，返回一个Item
     */
    public Item getItemObject(int position);

}
