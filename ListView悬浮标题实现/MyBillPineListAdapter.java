package com.onetoo.www.onetoo.abapter.my;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onetoo.www.onetoo.R;
import com.onetoo.www.onetoo.bean.my.TransactionRecord;
import com.onetoo.www.onetoo.ui.PinnedSectionListView;

import java.util.List;

/**
 * Created by longShun on 2016/11/3.
 * desc账单adapter
 */
public class MyBillPineListAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private Context context;
    private List<TransactionRecord.DataEntity> recordList;
    private static final int NO_PINE_VIEW = 0;
    private static final int PINE_VIEW = 1;
    private boolean isPined = false;

    public MyBillPineListAdapter(Context context, List<TransactionRecord.DataEntity> recordList) {
        this.context = context;
        this.recordList = recordList;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        TransactionRecord.DataEntity entity = recordList.get(position);
        isPined = position == getFirstPosByMonth(entity.getMonth());
        if (isPined){
            return PINE_VIEW;
        }else {
            return NO_PINE_VIEW;
        }
    }

    @Override
    public int getCount() {
        return recordList == null ? 0 : recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        /*TransactionRecord.DataEntity entity = recordList.get(position);
        isPined = position == getFirstPosByMonth(entity.getMonth());*/
        if (isPined){
            view = bindPineView(position,convertView,parent);
        }else {
            view = bindNoPineView(position,convertView,parent);
        }
        return view;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return isPined;
    }

    private View bindNoPineView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_my_bill_no_pine,parent,false);
        }
        BillViewHolder holder = (BillViewHolder) view.getTag();
        if (holder == null) {
            holder = new BillViewHolder();
            holder.ivAvatar = (ImageView) view.findViewById(R.id.iv_bill_avatar);
            holder.tvWeek = (TextView) view.findViewById(R.id.tv_bill_week);
            holder.tvDate = (TextView) view.findViewById(R.id.tv_bill_date);
            holder.tvMoney = (TextView) view.findViewById(R.id.tv_bill_money);
            holder.tvDesc = (TextView) view.findViewById(R.id.tv_bill_desc);
            view.setTag(holder);
        }
        TransactionRecord.DataEntity entity = recordList.get(position);
        String type = entity.getType();//1 转入 2 转出
        switch (type){
            case "1":
                holder.tvMoney.setText("+"+entity.getMoney());
                break;
            case "2":
                holder.tvMoney.setText("-"+entity.getMoney());
                break;
        }
        holder.tvDesc.setText(entity.getMsg());
        holder.tvWeek.setText(entity.getWeek());
        holder.tvDate.setText(entity.getDate());
        return view;
    }

    private View bindPineView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_my_bill_pine,parent,false);
        }
        BillPineHolder holder = (BillPineHolder) view.getTag();
        if (holder == null) {
            holder = new BillPineHolder();
            holder.tvMonth = (TextView) view.findViewById(R.id.tv_bill_pine_month);
            view.setTag(holder);
        }
        int month = recordList.get(position).getMonth();
        holder.tvMonth.setText(month+"月");
        return view;
    }

    class BillViewHolder {
        TextView tvWeek, tvDate, tvMoney, tvDesc;
        ImageView ivAvatar;
    }

    class BillPineHolder {
        TextView tvMonth;
    }

    private int getFirstPosByMonth(int month){
        int index = -1;
        for (int i = 0; i < recordList.size(); i++) {
            if (recordList.get(i).getMonth() == month){
                return i;
            }
        }
        return index;
    }
}
