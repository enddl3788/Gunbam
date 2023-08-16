package military.gunbam.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import military.gunbam.model.PasswordResetModel;

public class PasswordResetViewModel extends ViewModel {

    private PasswordResetModel passwordResetModel = new PasswordResetModel();

    public Task<Void> sendPasswordResetEmail(String email) {
        return passwordResetModel.sendPasswordResetEmail(email);
    }
}