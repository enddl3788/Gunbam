package military.gunbam.view.activity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import military.gunbam.R;
import military.gunbam.model.PostInfo;

public class AdminActivity extends BasicActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        findViewById(R.id.admin_menu_1_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    String userUid = user.getUid();
                    // 테스트 게시물 10개를 작성합니다.
                    for (int i = 0; i < 10; i++) {
                        // 게시물 데이터 생성
                        String title = "테스트 게시물 " + (i + 1);
                        Date date = new Date();
                        int recommendationCount = i;
                        Boolean isAnonymous;
                        if (i % 2 == 1) {
                            isAnonymous = true;
                        } else {
                            isAnonymous = false;
                        }

                        // contentsList와 formatList를 생성
                        ArrayList<String> contentsList = new ArrayList<>();
                        ArrayList<String> formatList = new ArrayList<>();

                        // 텍스트 추가
                        String textContent = "이것은 텍스트 내용입니다.";
                        contentsList.add(textContent);
                        formatList.add("text");

                        if (i % 5 == 0) {
                            // 이미지 추가
                            String imageUrl = "https://www.gstatic.com/mobilesdk/160503_mobilesdk/logo/2x/firebase_28dp.png"; // 이미지 URL 예시
                            contentsList.add(imageUrl);
                            formatList.add("image");
                        }

                        // 게시물 데이터를 Map으로 매핑
                        Map<String, Object> postMap = new HashMap<>();
                        postMap.put("title", title);
                        postMap.put("contents", contentsList);
                        postMap.put("formats", formatList);
                        postMap.put("publisher", userUid);
                        postMap.put("createdAt", date);
                        postMap.put("isAnonymous", isAnonymous);
                        postMap.put("recommendationCount", recommendationCount);

                        // 게시물 Firestore에 추가
                        firestore.collection("posts")
                                .add(postMap)
                                .addOnSuccessListener(documentReference -> {
                                    showToast(AdminActivity.this, "게시물이 추가되었습니다");
                                })
                                .addOnFailureListener(e -> {
                                    showToast(AdminActivity.this, "게시물 추가 실패");
                                });

                        // ArrayList 초기화
                        contentsList.clear();
                        formatList.clear();
                    }
                } else {
                    showToast(AdminActivity.this, "사용자가 로그인되어 있지 않습니다.");
                }
                finish();
            }
        });
    }

}
