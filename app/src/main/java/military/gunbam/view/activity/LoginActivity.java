package military.gunbam.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import military.gunbam.R;

public class LoginActivity extends BasicActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.loginButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoSignButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoPasswordResetButton).setOnClickListener(onClickListener);
    }

    @Override
    // 뒤로가기 로그인 방지
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.loginButton:
                    login();
                    break;

                case R.id.gotoSignButton:
                    startActivity(SignUpActivity.class);
                    break;

                case R.id.gotoPasswordResetButton:
                    startActivity(PasswordResetActivity.class);
                    break;
            }
        }
    };

    private void login() {
        // 이메일 주소의 유효성을 확인하는 정규표현식
        // String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        // 비밀번호의 유효성을 확인하는 정규표현식
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$";

        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();

        // 공백 체크
        if (email.length() > 0 && password.length() > 0){
            // 비밀번호 유효성 확인
            if (password.matches(passwordPattern)) {
                // 로그인 실행
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // startToast("로그인 성공: " + user.getEmail());
                                    startActivity(MainActivity.class);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    if (task.getException() != null) {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidUserException e) {
                                            startToast("가입되지 않은 이메일 주소입니다. 회원가입해주세요.");
                                        } catch (FirebaseAuthInvalidCredentialsException e) {
                                            startToast("이메일 주소 또는 비밀번호가 잘못되었습니다.");
                                        } catch (FirebaseNetworkException e) {
                                            startToast("인터넷 연결 상태를 확인해주세요.");
                                        } catch (Exception e) {
                                            startToast("로그인 도중 오류가 발생했습니다. 다시 시도해주세요.");
                                            Log.e(TAG, "로그인 실패", e);
                                        }
                                    }
                                }
                            }
                        });
            } else {
                startToast("비밀번호는 최소 6자 이상이고, 영어와 숫자를 포함해야 합니다.");
            }
        } else {
            startToast("이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}