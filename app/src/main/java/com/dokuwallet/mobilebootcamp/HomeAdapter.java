package com.dokuwallet.mobilebootcamp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private List<HomeDetilModel> dataList;
    private OnItemClickListener mItemClickListener;

    public HomeAdapter(List<HomeDetilModel> transactionList) {
        this.dataList = transactionList;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View mView;
        TextView textviewDataTitle;
        TextView textviewDataContent;
        Button buttonAllList;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(itemView);
            mView = itemView;

            textviewDataTitle = itemView.findViewById(R.id.textview_data_title);
            textviewDataContent = itemView.findViewById(R.id.textview_data_content);
            buttonAllList = itemView.findViewById(R.id.button_all_list);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_list_data, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        HomeDetilModel item = dataList.get(position);
        MyViewHolder viewHolder = holder;

        viewHolder.textviewDataTitle.setText(item.getTitle());
        viewHolder.textviewDataContent.setText(item.getDataContent());

        //viewHolder.buttonAllList.setOnClickListener(v -> {
          //  System.out.println("Click ID :"+item.getTitle());
        //});

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}