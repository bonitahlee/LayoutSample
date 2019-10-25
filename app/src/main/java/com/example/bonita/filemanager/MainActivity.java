package com.example.bonita.filemanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * FileListFragment를 뿌려줌
 */
public class MainActivity extends AppCompatActivity {
    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public final int EXTERNAL_REQUEST = 138;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestForPermission();
        // attach FileListFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_main, new FileListFragment()).commit();
    }

    public boolean requestForPermission() {
        boolean isPermissionOn = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }
        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));

    }

    /**
     * 권한 요청에 대한 응답을 이곳에서 가져온다.
     * @param requestCode 요청코드
     * @param permissions 사용자가 요청한 권한들
     * @param grantResults 권한에 대한 응답들(인덱스별로 매칭)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 138) { // 요청한 권한을 사용자가 "허용" 했다면...

        }
    }

}
