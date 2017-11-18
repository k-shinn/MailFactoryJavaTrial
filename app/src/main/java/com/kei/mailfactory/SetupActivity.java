package com.kei.mailfactory;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kei.mailfactory.presetList.ListData;

import java.io.Serializable;

/**
 * プリセットデータの選択肢を基にメールをセットアップする
 */
public class SetupActivity extends AppCompatActivity {

    public static final String MAIL_DATA = "mail_data";

    private ListData data;

    static Intent createIntent(Context context, ListData data) {
        Intent intent = new Intent(context, SetupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MAIL_DATA, data);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Serializable extra = getIntent().getSerializableExtra(MAIL_DATA);
        data = (ListData) extra;
        setTitle(data.getTitle());
    }

    /**
     * アドレスデータを本体から取得する
     * 要Permission
     *
     * @return アドレスデータ
     */
    private String getAddress() {
        return null;
    }
}
