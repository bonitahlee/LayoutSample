package com.example.bonita.filemanager;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.bonita.filemanager.define.FileManagerDefine;

/**
 * MainActivity
 * - storage에 접근할 수 있는 권한을 확인 후, FileListFragment를 띄움
 */
public class MainActivity extends AppCompatActivity {

    private FileListFragment mFileListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFileListFragment = new FileListFragment();

        if (hasStoragePermission()) {
            // storage 권한이 있다면 fragment를 띄움
            attachFragment();
        }
    }

    /**
     * 권한 요청에 대한 응답 및 결과
     *
     * @param requestCode  요청코드
     * @param permissions  사용자가 요청한 권한들
     * @param grantResults 권한에 대한 응답들(인덱스별로 매칭)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FileManagerDefine.EXTERNAL_REQUEST) {
            boolean isAllPermitted = false;
            for (int grantResult : grantResults) {
                isAllPermitted = grantResult == PackageManager.PERMISSION_GRANTED;
            }

            if (isAllPermitted) {
                // 요청한 권한을 사용자가 허용 했다면 앱 시작
                attachFragment();
            } else {
                Toast.makeText(this, R.string.MSG_PERMISSION_DENIED, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mFileListFragment.onKeyUp(keyCode)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * attach FileListFragment
     */
    private void attachFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_main, mFileListFragment).commit();
    }

    /**
     * Storage 경로에 읽고 쓰는 권한이 있는지 확인 후 요청
     *
     * @return true: 권한 있음, false: 권한 없음->요청 intent 보냄
     */
    private boolean hasStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermission(FileManagerDefine.EXTERNAL_PERMS)) {
                requestPermissions(FileManagerDefine.EXTERNAL_PERMS, FileManagerDefine.EXTERNAL_REQUEST);
                return false;
            }
        }
        return true;
    }

    /**
     * parameter로 넘어온 권한들이 있는지 확인
     */
    private boolean hasPermission(String[] perms) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perms[0])
                && PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perms[1]));

    }
}
