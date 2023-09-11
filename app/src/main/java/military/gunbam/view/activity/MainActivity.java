package military.gunbam.view.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import military.gunbam.R;
import military.gunbam.utils.Util;
import military.gunbam.view.fragment.HomeFragment;
import military.gunbam.viewmodel.MainViewModel;

public class MainActivity extends BasicActivity {
    // 마지막으로 뒤로 가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;

    private MainViewModel mainViewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // ViewModel에서 사용자 로그인 상태와 회원 정보 여부를 관찰합니다.
        mainViewModel.getUserLoggedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean userLoggedIn) {
                if (userLoggedIn) {
                    mainViewModel.getUserHasInfo().observe(MainActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean userHasInfo) {
                            if (userHasInfo) {
                                // 사용자가 로그인하고 회원 정보가 있는 경우, 홈 화면으로 이동합니다.
                                // 초기 화면을 HomeFragment로 설정
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, new HomeFragment())
                                        .commit();
                            } else {
                                // 사용자가 로그인하고 회원 정보가 없는 경우, MemberInitActivity로 이동합니다.
                                startActivity(MemberInitActivity.class);
                                finish();
                            }
                        }
                    });
                } else {
                    // 사용자가 로그인하지 않은 경우, LoginActivity로 이동합니다.
                    startActivity(LoginActivity.class);
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.postList:
                        // 예시: 다른 섹션으로 이동하는 Fragment를 설정합니다.
                        //selectedFragment = new PostListFragment();
                        break;
                    case R.id.myPage:
                        // 예시: MyPage 섹션으로 이동하는 Fragment를 설정합니다.
                        //selectedFragment = new MyPageFragment();
                        break;
                    case R.id.tmoSearch:
                        // 예시: 다른 섹션으로 이동하는 Fragment를 설정합니다.
                         break;
                    case R.id.report:
                        // 예시: 다른 섹션으로 이동하는 Fragment를 설정합니다.
                        break;
                }

                if (selectedFragment != null) {
                    Util.replaceFragment(getSupportFragmentManager(), R.id.container, selectedFragment);
                }

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainViewModel.checkUserLoginStatus();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제

        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            showToast(MainActivity.this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.");
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            showToast(MainActivity.this, "앱을 종료합니다.");
        }
    }
}