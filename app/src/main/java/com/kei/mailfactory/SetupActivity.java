package com.kei.mailfactory;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kei.mailfactory.databinding.ActivitySetupBinding;
import com.kei.mailfactory.presetList.ListData;

import java.io.Serializable;

/**
 * プリセットデータの選択肢を基にメールをセットアップする
 */
public class SetupActivity extends AppCompatActivity {

    private static final String MAIL_DATA = "mail_data";
    private static final int ADDRESS_REQUEST = 100;

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
//        setContentView(R.layout.activity_setup);

        ActivitySetupBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_setup);
        binding.setOnGetAddressClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddress();
            }
        });

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
    private void getAddress() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE);
        startActivityForResult(intent, ADDRESS_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADDRESS_REQUEST) {
            if (data != null && data.getData() != null) {
                Toast.makeText(this, data.getData().toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "uri Null", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
