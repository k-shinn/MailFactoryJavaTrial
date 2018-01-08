package com.kei.mailfactory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kei.mailfactory.databinding.ActivitySubmitMailBinding;

import java.util.Objects;

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

    static Intent createIntent(Context context) {
        Intent intent = new Intent(context, SubmitMailActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_submit_mail);

        setSupportActionBar(binding.toolbar);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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
    void getEmailAddress(Intent data) {
        Cursor cursor = null;
        String emailAddress = "";
        try {
            Uri uri = data.getData();
            String id = uri.getLastPathSegment();
//            Uri email = Uri.parse("content://com.android.contacts/data/emails");
//            grantUriPermission(getPackageName(), email, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cursor = getContentResolver().query(
                    Email.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
            if (cursor.moveToFirst()) {
                // TODO: 2018/01/09  CONTACT_IDをQueryで指定するとCursorが取れないのでなんとかしたい。↓のやり方でも結局該当するカラムがわからん
                do {
                    String cursorString = cursor.getString(cursor.getColumnIndex(Email.CONTACT_ID));
                    if (cursorString.equals(id)) {
                        emailAddress += cursor.getString(cursor.getColumnIndex(Email.DATA));
                    }
                } while (cursor.moveToNext());
            }
            Toast.makeText(this, "Address(" + id + "):" + emailAddress, Toast.LENGTH_SHORT).show();
        } catch (Exception err) {
            err.printStackTrace();
            Toast.makeText(this, err.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
        }
//        return emailAddress;
    }

//    @NeedsPermission(Manifest.permission.READ_CONTACTS)
//    void getEmailAddressVoid(Intent data) {
//        Cursor cursor = null;
//        String emailAddress = "";
//        try {
//            Uri uri = data.getData();
//            String id = uri.getLastPathSegment();
//            Uri email = Uri.parse("content://com.android.contacts/data/emails");
////            grantUriPermission(getPackageName(), email, Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            cursor = getContentResolver().query(
//                    Email.CONTENT_URI,
//                    null,
//                    Email.CONTACT_ID + "=?",
//                    new String[]{id},
//                    null);
//            if (cursor.moveToFirst()) {
//                emailAddress = cursor.getString(
//                        cursor.getColumnIndex(Email.DATA));
//            }
//        } catch (Exception err) {
//            err.printStackTrace();
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//
////        return emailAddress;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADDRESS_TO_REQUEST:
                if (data != null && data.getData() != null) {
                    Toast.makeText(this, "To: " + data.getData().toString(), Toast.LENGTH_SHORT).show();
//                    binding.addressTo.setText(data.getData().toString());

//                    String emailAddress = getEmailAddress(data);
//                    getEmailAddress(data);
                    SubmitMailActivityPermissionsDispatcher.getEmailAddressWithPermissionCheck(SubmitMailActivity.this, data);
//                    binding.addressTo.setText(emailAddress);
                } else {
                    Toast.makeText(this, "uri Null", Toast.LENGTH_SHORT).show();
                }
                break;
            case ADDRESS_CC_REQUEST:
                if (data != null && data.getData() != null) {
                    Toast.makeText(this, "Cc: " + data.getData().toString(), Toast.LENGTH_SHORT).show();
//                    binding.addressCc.setText(data.getData().toString());
//                    String emailAddress = getEmailAddress(data);
                    getEmailAddress(data);
//                    binding.addressCc.setText(emailAddress);
                } else {
                    Toast.makeText(this, "uri Null", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        SubmitMailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

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

}
