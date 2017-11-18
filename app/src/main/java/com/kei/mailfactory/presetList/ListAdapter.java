package com.kei.mailfactory.presetList;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kei.mailfactory.BR;
import com.kei.mailfactory.R;

import java.util.ArrayList;

/**
 * PresetListのAdapter
 * Created by kei on 2017/11/18.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private final ListEventHandler listEventHandler;
    private ArrayList<ListData> dataList;

    public ListAdapter(ArrayList<ListData> dataList, ListEventHandler listEventHandler) {
        this.dataList = dataList;
        this.listEventHandler = listEventHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prest_list_row_layout, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listEventHandler != null) {
                    listEventHandler.onItemClick(view);
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listEventHandler != null) {
                    listEventHandler.onItemLongClick(view);
                }
                return false;
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListData data = dataList.get(position);

        holder.getBinding().setVariable(BR.data, data);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public ListData getItem(int position) {
        return dataList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        ViewDataBinding getBinding() {
            return binding;
        }
    }
}
