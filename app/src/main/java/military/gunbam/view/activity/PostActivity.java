package military.gunbam.view.activity;

import static military.gunbam.view.fragment.CommentListFragment.loadComments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import military.gunbam.R;
import military.gunbam.listener.OnPostListener;
import military.gunbam.model.CommentInfo;
import military.gunbam.model.PostInfo;
import military.gunbam.FirebaseHelper;
import military.gunbam.view.ReadContentsView;
import military.gunbam.view.fragment.CommentListFragment;

public class PostActivity extends BasicActivity {
    private PostInfo postInfo;
    private FirebaseHelper firebaseHelper;
    private ReadContentsView readContentsVIew;
    private LinearLayout contentsLayout;

    private FirebaseFirestore firestore;

    private EditText commentEditText;
    private ImageButton postCommentButton;
    private CheckBox anonymousCheckBox;

    protected void onResume() {
        super.onResume();

        String postId = postInfo.getId();

        uiUpdate();

        CommentListFragment fragment = CommentListFragment.newInstance(postId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.commentListFragment, fragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        firestore = FirebaseFirestore.getInstance();

        postInfo = (PostInfo) getIntent().getSerializableExtra("postInfo");
        contentsLayout = findViewById(R.id.contentsLayout);
        readContentsVIew = findViewById(R.id.readContentsView);

        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.setOnPostListener(onPostListener);

        findViewById(R.id.viewPostBackButton).setOnClickListener(onClickListener);
        findViewById(R.id.postCommentButton).setOnClickListener(onClickListener);

        commentEditText = findViewById(R.id.commentEditText);
        anonymousCheckBox = findViewById(R.id.commentAnonymousCheckBox);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    postInfo = (PostInfo)data.getSerializableExtra("postinfo");
                    contentsLayout.removeAllViews();
                    uiUpdate();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                firebaseHelper.storageDelete(postInfo);
                return true;
            case R.id.modify:
                myStartActivity(WritePostActivity.class, postInfo);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(PostInfo postInfo) {
            Log.e("로그 ","삭제 성공");
        }

        @Override
        public void onModify() {
            Log.e("로그 ","수정 성공");
        }
    };

    private void uiUpdate(){
        readContentsVIew.setPostInfo(postInfo);
    }

    private void myStartActivity(Class c, PostInfo postInfo) {
        Intent intent = new Intent(this, c);
        intent.putExtra("postInfo", postInfo);
        startActivityForResult(intent, 0);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case(R.id.viewPostBackButton) : {
                    finish();
                    break;
                }

                case R.id.postCommentButton: {
                    String postId = postInfo.getId();
                    String commentContent = commentEditText.getText().toString();
                    String commentAuthor;
                    Timestamp commentUploadTime = Timestamp.now();

                    boolean isAnonymous = anonymousCheckBox.isChecked(); // Get the state of the anonymous checkbox

                    commentAuthor = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    if (!TextUtils.isEmpty(commentContent)) {
                        CommentInfo newComment = new CommentInfo(postId, commentContent, commentAuthor, isAnonymous, null, commentUploadTime);
                        // Here, parentCommentId is set to null. You can modify this to add replies.

                        CollectionReference commentsCollection = firestore.collection("comments");
                        commentsCollection.add(newComment)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        showToast(PostActivity.this, "댓글이 작성되었습니다.");
                                        commentEditText.setText(""); // Clear the comment input
                                        loadComments(postId);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showToast(PostActivity.this, "댓글 작성에 실패했습니다.");
                                    }
                                });
                    } else {
                        showToast(PostActivity.this, "댓글을 입력해주세요.");
                    }
                    break;
                }
            }
        }
    };
}
