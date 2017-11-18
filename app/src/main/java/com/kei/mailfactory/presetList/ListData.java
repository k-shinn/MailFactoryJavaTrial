package com.kei.mailfactory.presetList;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.kei.mailfactory.BR;

/**
 * PresetListのリストデータBinding
 * Created by kei on 2017/11/18.
 */

public class ListData extends BaseObservable {

    private String title;

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }
}
