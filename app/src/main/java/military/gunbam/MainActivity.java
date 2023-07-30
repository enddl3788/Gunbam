package military.gunbam;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            startActivity(LoginActivity.class);
        } else {
            // 회원가입 or 로그인
            if (user != null) {
                // Name, email address, and profile photo Url
                for (UserInfo profile : user.getProviderData()) {
                    String name = user.getDisplayName();
                    Log.e(TAG, "이름 : " + name);
                    if (name == null) {
                        startActivity(MemberInitActivity.class);
                    }
                }
            }
        }

        findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(LoginActivity.class);
                    break;
            }
        }
    };

    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}