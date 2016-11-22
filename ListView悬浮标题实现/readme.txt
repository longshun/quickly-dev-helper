1.http://blog.csdn.net/u010378579/article/details/53035663用法详解

2.自定义适配器实现PinnedSectionListView.PinnedSectionListAdapter，重写
 @Override
    public boolean isItemViewTypePinned(int viewType) {
        return isPined;//返回true时加载的布局将会悬浮在顶部
    }
3.我们可以准备两套布局，根据一定的条件选择加载不同的布局；

4.上拉加载更多设置监听：
pslBillList = (PinnedSectionListView) findViewById(R.id.psl_bill_list);
pslBillList.setLoadMoreListener(this);

加载完成调用下面代码隐藏foot
pslBillList.loadComplete();