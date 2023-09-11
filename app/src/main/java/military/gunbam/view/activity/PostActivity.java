package military.gunbam.view.activity;

import static military.gunbam.view.fragment.CommentListFragment.loadComments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import military.gunbam.R;
import military.gunbam.model.CommentInfo;
import military.gunbam.model.Post.PostInfo;
import military.gunbam.view.ReadContentsView;
import military.gunbam.view.fragment.CommentListFragment;
import military.gunbam.viewmodel.PostViewModel;

public class PostActivity extends BasicActivity {
    private PostInfo postInfo;

    private ReadContentsView readContentsVIew;
    private LinearLayout contentsLayout;
    private EditText commentEditText;
    private ImageButton postCommentButton;
    private CheckBox anonymousCheckBox;

    private PostViewModel postViewModel;

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

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);


        postInfo = (PostInfo) getIntent().getSerializableExtra("postInfo");
        //contentsLayout = findViewById(R.id.contentsLayout);
        readContentsVIew = findViewById(R.id.readContentsView);



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
                storageDelete(postInfo);
                return true;
            case R.id.modify:
                myStartActivity(WritePostActivity.class, postInfo);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void uiUpdate(){
        readContentsVIew.setPostInfo(postInfo);
    }

    private void myStartActivity(Class c, PostInfo postInfo) {
        Intent intent = new Intent(this, c);
        intent.putExtra("postInfo", postInfo);
        startActivityForResult(intent, 0);
    }

    private void storageDelete(PostInfo postInfo){
        postViewModel.firebaseHelperStorageDelete(postInfo);
    }
    private CollectionReference collection(String comment) {
        return postViewModel.firebaseStoreCollection(comment);
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

                        CollectionReference commentsCollection = collection("comments");
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