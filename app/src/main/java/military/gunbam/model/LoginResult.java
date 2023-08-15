package military.gunbam.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginResult {
    private FirebaseAuth mAuth;
    private FirebaseUser success;
    private String error;
    private String message;


    public LoginResult(FirebaseUser success) {
        this.success = success;
    }

    public LoginResult(String error) {
        this.error = error;
    }

    public FirebaseUser getSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
    public String getMessage() { return message;}

    public void login(String email, String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$";

        if (email.isEmpty() || password.isEmpty()) {
            message = "이메일 또는 비밀번호를 입력해주세요.";
            //loginResultLiveData.setValue(new military.gunbam.viewmodel.LoginResult("이메일 또는 비밀번호를 입력해주세요."));
            return;
        }

        if (!password.matches(passwordPattern)) {
            message = "비밀번호는 최소 6자 이상이고, 영어와 숫자를 포함해야 합니다.";
            //loginResultLiveData.setValue(new military.gunbam.viewmodel.LoginResult("비밀번호는 최소 6자 이상이고, 영어와 숫자를 포함해야 합니다."));
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            //loginResultLiveData.setValue(new military.gunbam.viewmodel.LoginResult(user));
                        } else {
                            if (task.getException() != null) {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    //loginResultLiveData.setValue(new military.gunbam.viewmodel.LoginResult("가입되지 않은 이메일 주소입니다. 회원가입해주세요."));
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    //loginResultLiveData.setValue(new military.gunbam.viewmodel.LoginResult("이메일 주소 또는 비밀번호가 잘못되었습니다."));
                                } catch (FirebaseNetworkException e) {
                                    //loginResultLiveData.setValue(new military.gunbam.viewmodel.LoginResult("인터넷 연결 상태를 확인해주세요."));
                                } catch (Exception e) {
                                   // loginResultLiveData.setValue(new military.gunbam.viewmodel.LoginResult("로그인 도중 오류가 발생했습니다. 다시 시도해주세요."));
                                    Log.e("LoginViewModel", "로그인 실패", e);
                                }
                            }
                        }
                    }
                });
    }

}