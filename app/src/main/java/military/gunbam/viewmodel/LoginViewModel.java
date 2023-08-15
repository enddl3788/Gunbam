package military.gunbam.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {
    private military.gunbam.viewmodel.LoginResult loginResult;
    private String message;
    private MutableLiveData<military.gunbam.viewmodel.LoginResult> loginResultLiveData = new MutableLiveData<>();
    private MutableLiveData<String> loginErrorMessage = new MutableLiveData<>();

    public LoginViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(String email, String password) {
        loginResult.login(email,password);

        if (email.isEmpty() || password.isEmpty()) {
            message = loginResult.getMessage();
            return;
        }
    }

    public LiveData<military.gunbam.viewmodel.LoginResult> getLoginResult() {
        return loginResultLiveData;
    }
    public LiveData<String> getMessage() {
        return new MutableLiveData<>(loginResult.getMessage());
    }

}