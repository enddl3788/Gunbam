package military.gunbam.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetModel {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public Task<Void> sendPasswordResetEmail(String email) {
        return mAuth.sendPasswordResetEmail(email);
    }
}