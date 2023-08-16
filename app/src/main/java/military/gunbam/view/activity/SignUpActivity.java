package military.gunbam.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import androidx.lifecycle.ViewModelProvider;


import military.gunbam.R;
import military.gunbam.viewmodel.SignUpViewModel;

public class SignUpActivity extends BasicActivity {

    private static final String TAG = "SignUpActivity";
    private SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        findViewById(R.id.gotoLoginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivityAndClearStack(LoginActivity.class);
            }
        });
    }

    private void signUp() {
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        String passwordCheck = ((EditText) findViewById(R.id.passwordCheckEditText)).getText().toString();

        signUpViewModel.signUp(email, password, passwordCheck)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showToast(SignUpActivity.this,"회원가입을 축하드립니다.");
                        startNewActivityAndClearStack(MainActivity.class);
                    } else {
                        Exception exception = task.getException();
                        if (exception != null) {
                            String errorMessage = signUpViewModel.getSignUpErrorMessage(exception);
                            showToast(SignUpActivity.this, errorMessage);
                            Log.e(TAG, "회원가입 실패", exception);
                        }
                    }
                });
    }
}