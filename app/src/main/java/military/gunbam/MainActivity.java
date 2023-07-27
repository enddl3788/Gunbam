package military.gunbam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startSignActivity();
        }

        findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    startSignActivity();
                    break;
            }
        }
    };

    private void startSignActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}