package com.kei.mailfactory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kei.mailfactory.databinding.ActivitySubmitMailBinding;
import com.kei.mailfactory.setupData.SetupData;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SubmitMailActivity extends AppCompatActivity {

    private ActivitySubmitMailBinding binding;
    private static final int ADDRESS_TO_REQUEST = 101;
    private static final int ADDRESS_CC_REQUEST = 102;
    private static final int ADDRESS_REQUEST = 100;

    interface GetAddressListener {
        void onSuccess(String address);
    }

    static Intent createIntent(Context context) {
        Intent intent = new Intent(context, SubmitMailActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_submit_mail);
        SetupData setupData = new SetupData();
        binding.setSetupData(setupData);
        setSupportActionBar(binding.toolbar);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitMail(binding.getSetupData());
            }
        });

    }

    /**
     * アドレスデータを本体から取得する
     * 要Permission
     *
     * @return アドレスデータ
     */
    private void getAddress(int request) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(Email.CONTENT_TYPE);
        startActivityForResult(intent, request);
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    void getEmailAddress(Intent data, GetAddressListener listener) {
        Cursor cursor = null;
        String emailAddress = "";
        try {
            Uri uri = data.getData();
            String id = uri.getLastPathSegment();
            cursor = getContentResolver().query(
                    Email.CONTENT_URI,
                    null,
                    Email._ID + "=?",
                    new String[]{id},
                    null);
            if (cursor.moveToFirst()) {
                emailAddress = cursor.getString(cursor.getColumnIndex(Email.DATA));
                if (!emailAddress.isEmpty()) {
                    listener.onSuccess(emailAddress);
                }
            }
            // TODO: 2018/01/09 _IDで取得解決…。コールバックでもらえるようにこの一連の取得処理を別クラスに持っていきたい
            Toast.makeText(this, "Address(" + id + "):" + emailAddress, Toast.LENGTH_SHORT).show();
        } catch (Exception err) {
            err.printStackTrace();
            Toast.makeText(this, err.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADDRESS_TO_REQUEST:
                if (data != null && data.getData() != null) {
                    SubmitMailActivityPermissionsDispatcher.getEmailAddressWithPermissionCheck(SubmitMailActivity.this, data,
                            new GetAddressListener() {
                                @Override
                                public void onSuccess(String address) {
                                    binding.getSetupData().addToAddress(address);
                                }
                            });
                }
                break;
            case ADDRESS_CC_REQUEST:
                if (data != null && data.getData() != null) {
                    SubmitMailActivityPermissionsDispatcher.getEmailAddressWithPermissionCheck(SubmitMailActivity.this, data,
                            new GetAddressListener() {
                                @Override
                                public void onSuccess(String address) {
                                    binding.getSetupData().addCcAddress(address);
                                }
                            });
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        SubmitMailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_to:
                getAddress(ADDRESS_TO_REQUEST);
                break;
            case R.id.add_cc:
                getAddress(ADDRESS_CC_REQUEST);
                break;
            case R.id.edit_message:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnShowRationale(Manifest.permission.READ_CONTACTS)
    void onShowRationaleReadExternalStorage(final PermissionRequest request) {
        // すでに１度パーミッションのリクエストが行われていて、
        // ユーザーに「許可しない（二度と表示しないは非チェック）」をされていると
        // この処理が呼ばれます。
        Toast.makeText(this, "パーミッション許可がOFFになっています。", Toast.LENGTH_SHORT).show();
    }

    @OnPermissionDenied(Manifest.permission.READ_CONTACTS)
    void onPermissionDeniedReadExternalStorage() {
        Toast.makeText(this, "リクエストが拒否されました。", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_CONTACTS)
    void onNeverAskAgainReadExternalStorage() {
        Toast.makeText(this, "パーミッションが拒絶されています。", Toast.LENGTH_SHORT).show();
    }

    void submitMail(SetupData data) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + data.getToAddress()));
        intent.putExtra(Intent.EXTRA_CC, data.getCcAddress());
        intent.putExtra(Intent.EXTRA_BCC, data.getBccAddress());
        intent.putExtra(Intent.EXTRA_SUBJECT, data.getSubject());
        intent.putExtra(Intent.EXTRA_TEXT, data.createMessage());
        startActivity(intent);
    }

}
