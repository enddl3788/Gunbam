package military.gunbam.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import military.gunbam.R;

public class CalculatorFragment extends Fragment {

    private View view;

    private String TAG = "프래그먼트";

    public ImageView account_iv_profile;
    public TextView account_tv_nickname;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_calculator_main, container, false);

        account_iv_profile = view.findViewById(R.id.account_iv_profile);
        account_tv_nickname = view.findViewById(R.id.account_tv_nickname);
        // updateKakaoLoginUi();

        return view;
    }

    /*
    @Nullable
    private void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                // 로그인이 되어있으면
                if (user != null) {

                    // 유저의 아이디
                    Log.d(TAG, "invoke: id" + user.getId());
                    // 유저의 어카운트정보에 이메일
                    Log.d(TAG, "invoke: nickname" + user.getKakaoAccount().getEmail());
                    // 유저의 어카운트 정보의 프로파일에 닉네임
                    Log.d(TAG, "invoke: email" + user.getKakaoAccount().getProfile().getNickname());
                    // 유저의 어카운트 파일의 성별
                    Log.d(TAG, "invoke: gerder" + user.getKakaoAccount().getGender());
                    // 유저의 어카운트 정보에 나이
                    Log.d(TAG, "invoke: age" + user.getKakaoAccount().getAgeRange());

                    account_tv_nickname.setText(user.getKakaoAccount().getProfile().getNickname());

                    Glide.with(account_iv_profile).load(user.getKakaoAccount().
                            getProfile().getProfileImageUrl()).circleCrop().into(account_iv_profile);
                } else {
                    // 로그인이 되어 있지 않다면 위와 반대로
                    account_tv_nickname.setText("비회원");
                    //account_iv_profile.setImageBitmap(R.drawable.gunbam);
                }
                return null;
            }
        });
    }
     */
}
