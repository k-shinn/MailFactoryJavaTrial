package com.kei.mailfactory;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.kei.mailfactory.databinding.FragmentPresetListBinding;
import com.kei.mailfactory.presetList.ListAdapter;
import com.kei.mailfactory.presetList.ListData;
import com.kei.mailfactory.presetList.ListEventHandler;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PresetListActivityFragment extends Fragment {

    private FragmentPresetListBinding binding;
    private ArrayList<ListData> dataList;

    public PresetListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_preset_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        generateData();
        initList();
    }

    /**
     * Listの初期化
     */
    void initList() {
        final RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        // setAdapter
        final ListAdapter listAdapter = new ListAdapter(dataList, new ListEventHandler() {
            @Override
            public void onItemClick(View view) {
                ListData item = ((ListAdapter) recyclerView.getAdapter()).getItem(recyclerView.getChildAdapterPosition(view));
                // 基本画面の起動
                Intent intent = SetupActivity.createIntent(getContext(), item);
                getContext().startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view) {
                ListData item = ((ListAdapter) recyclerView.getAdapter()).getItem(recyclerView.getChildAdapterPosition(view));
                // 編集及び削除ダイアログの表示
            }
        });
        recyclerView.setAdapter(listAdapter);
    }

    /**
     * presetDataのリストを取得
     */
    private void generateData() {
        // TODO: 2017/11/18 リストデータをDBから取得するように差し替えたい
        // TestData
        dataList = new ArrayList<>();
        ListData data = new ListData();
        data.setTitle("preset1");
        dataList.add(data);
        data = new ListData();
        data.setTitle("preset2");
        dataList.add(data);
    }

}
