package military.gunbam.model;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;


public class SignUpModel {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public Task<AuthResult> signUp(String email, String password, String passwordCheck) {
        if (email.isEmpty() || password.isEmpty() || passwordCheck.isEmpty()) {
            // 입력되지 않은 필드가 있는 경우 실패 처리
            return Tasks.forException(new IllegalArgumentException("이메일과 비밀번호를 입력해주세요."));
        }

        // 비밀번호 일치 여부 검사
        if (!passwordsMatch(password, passwordCheck)) {
            // 비밀번호 불일치 시 실패 처리
            return Tasks.forException(new IllegalArgumentException("비밀번호가 일치하지 않습니다."));
        }

        // 회원가입 실행
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public String getSignUpErrorMessage(Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            return "비밀번호는 6자 이상이어야 합니다.";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return "유효한 이메일 주소를 입력해주세요.";
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            return "이미 등록된 이메일 주소입니다. 다른 이메일을 사용해주세요.";
        } else {
            return "회원가입 도중 오류가 발생했습니다. 다시 시도해주세요.";
        }
    }

    private boolean passwordsMatch(String password, String passwordCheck) {
        return password.equals(passwordCheck);
    }
}