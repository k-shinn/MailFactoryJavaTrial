package com.kei.mailfactory.presetList;

import android.view.View;

/**
 * PresetListのイベントハンドラ
 * Created by kei on 2017/11/18.
 */

public interface ListEventHandler {
    void onItemClick(View view);

    void onItemLongClick(View view);
}
