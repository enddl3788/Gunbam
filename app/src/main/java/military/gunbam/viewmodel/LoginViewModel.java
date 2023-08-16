package military.gunbam.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseUser;

import military.gunbam.model.LoginModel;
import military.gunbam.model.LoginResult;
import military.gunbam.view.activity.BasicActivity;

public class LoginViewModel extends ViewModel {

    private LoginModel loginModel = new LoginModel();
    private MutableLiveData<LoginResult> loginResultLiveData = new MutableLiveData<>();

    public void login(String email, String password) {
        loginModel.login(email, password, new LoginModel.LoginCallback() {
            @Override
            public void onLoginSuccess(FirebaseUser user) {
                loginResultLiveData.setValue(new LoginResult(user));
            }

            @Override
            public void onLoginFailure(String errorMessage) {
                loginResultLiveData.setValue(new LoginResult(errorMessage));
            }
        });
    }

    public MutableLiveData<LoginResult> getLoginResultLiveData() {
        return loginResultLiveData;
    }
}