package military.gunbam.view.activity;

import static military.gunbam.utils.Util.replaceFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import military.gunbam.R;
import military.gunbam.utils.Util;
import military.gunbam.view.fragment.HomeFragment;
import military.gunbam.viewmodel.MainViewModel;

public class MainActivity extends BasicActivity {

    private MainViewModel mainViewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // ViewModel에서 사용자 로그인 상태를 관찰합니다.
        mainViewModel.getUserLoggedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean userLoggedIn) {
                if (userLoggedIn) {
                    // 사용자가 로그인한 경우, 홈 화면으로 이동합니다.
                    // 초기 화면을 HomeFragment로 설정
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new HomeFragment())
                            .commit();
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
}