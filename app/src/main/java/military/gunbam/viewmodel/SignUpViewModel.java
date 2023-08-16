package military.gunbam.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import military.gunbam.model.SignUpModel;

public class SignUpViewModel extends ViewModel {

    private SignUpModel signUpModel = new SignUpModel();

    public Task<AuthResult> signUp(String email, String password, String passwordCheck) {
        return signUpModel.signUp(email, password, passwordCheck);
    }

    public String getSignUpErrorMessage(Exception exception) {
        // 모델에서 반환된 예외 메시지를 처리하여 반환
        if (exception instanceof IllegalArgumentException) {
            return exception.getMessage();
        } else {
            return signUpModel.getSignUpErrorMessage(exception);
        }
    }
}
