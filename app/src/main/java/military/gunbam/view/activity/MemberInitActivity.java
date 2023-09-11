package military.gunbam.view.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import military.gunbam.R;
import military.gunbam.viewmodel.MemberInitViewModel;

public class MemberInitActivity extends BasicActivity {
    private static final String TAG = "MemberInitActivity";
    private ImageView draftImageView;
    private String draftPath;
    MemberInitViewModel memberInitViewModel;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        showToast(MemberInitActivity.this, "회원정보를 입력해주세요.");

        memberInitViewModel = new ViewModelProvider(this).get(MemberInitViewModel.class);
        memberInitViewModel.getIsUploaded().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUploaded) {
                showToast(MemberInitActivity.this,memberInitViewModel.getUploadMessage());
                if (isUploaded) {
                    finish();
                } else {
                }
            }
        });
        memberInitViewModel.getToastMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showToast(MemberInitActivity.this,memberInitViewModel.getErrorMessage());
            }
        });




        draftImageView = findViewById(R.id.draftImageView);
        draftImageView.setOnClickListener(onClickListener);

        findViewById(R.id.checkButton).setOnClickListener(onClickListener);
        findViewById(R.id.pictureButton).setOnClickListener(onClickListener);
        findViewById(R.id.galleryButton).setOnClickListener(onClickListener);

        cardView = findViewById(R.id.draftCardView);
    }

    @Override
    // 뒤로가기 로그인 방지
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (resultCode == Activity.RESULT_OK) {
                    draftPath = data.getStringExtra("draftPath");
                    Glide.with(this)
                            .load(draftPath)
                            .centerCrop()
                            .override(500)
                            .into(draftImageView);
                }
            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.checkButton:
                    profileUpdate();
                    break;
                case R.id.draftImageView:
                    if (cardView.getVisibility() == View.VISIBLE) {
                        cardView.setVisibility(View.GONE);
                    } else {
                        cardView.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.pictureButton:
                    if (ContextCompat.checkSelfPermission(MemberInitActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(MemberInitActivity.this,
                                Manifest.permission.CAMERA)) {
                            showToast(MemberInitActivity.this, "카메라 권한이 필요합니다.\n권한을 허용해 주세요.");
                        }

                        ActivityCompat.requestPermissions(MemberInitActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                2); // 다른 권한 요청과 겹치지 않도록 새로운 requestCode인 2로 변경합니다.
                    } else {
                        cardView.setVisibility(View.GONE);
                        startActivity(CameraActivity.class);
                    }
                    break;
                case R.id.galleryButton:
                    if (ContextCompat.checkSelfPermission(MemberInitActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(MemberInitActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showToast(MemberInitActivity.this, "외부 저장소 읽기 권한이 필요합니다.\n권한을 허용해 주세요.");
                        }

                        ActivityCompat.requestPermissions(MemberInitActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                1);
                    } else {
                        cardView.setVisibility(View.GONE);
                        startActivity(GalleryActivity.class);
                    }
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(GalleryActivity.class);
                } else {
                    showToast(MemberInitActivity.this, "파일 읽기 권한을 허용해 주세요.");
                }
                break;
            }
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(CameraActivity.class);
                } else {
                    showToast(MemberInitActivity.this, "카메라 권한을 허용해 주세요.");
                }
                break;
            }
            default:
                // Handle other permission request results if needed.
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void profileUpdate() {
        final String nickName = ((EditText)findViewById(R.id.nickNameEditText)).getText().toString();
        final String name = ((EditText)findViewById(R.id.nameEditText)).getText().toString();
        final String phoneNumber = ((EditText)findViewById(R.id.phoneNumberEditText)).getText().toString();
        final String birthDate = ((EditText)findViewById(R.id.birthDateEditText)).getText().toString();
        final String joinDate = ((EditText)findViewById(R.id.joinDateEditText)).getText().toString();
        final String dischargeDate = ((EditText)findViewById(R.id.dischargeDateEditText)).getText().toString();
        final String rank = "user";
        memberInitViewModel.profileUpdate(nickName,name,phoneNumber,birthDate,joinDate,dischargeDate,rank,draftPath);

    }

}
