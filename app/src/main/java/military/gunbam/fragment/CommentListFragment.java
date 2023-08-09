package military.gunbam.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import military.gunbam.CommentInfo;
import military.gunbam.R;
import military.gunbam.adapter.CommentAdapter;

public class CommentListFragment extends Fragment {
    private RecyclerView commentRecyclerView;
    private CommentAdapter commentAdapter;
    private List<CommentInfo> commentList;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;

    public CommentListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        commentRecyclerView = view.findViewById(R.id.commentRecyclerView);
        progressBar = view.findViewById(R.id.commentProgressBar);

        // Set up Firestore instance
        firestore = FirebaseFirestore.getInstance();

        // Initialize commentList and commentAdapter
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(getContext(), commentList);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentRecyclerView.setAdapter(commentAdapter);

        loadComments();

        return view;
    }

    private void loadComments() {
        progressBar.setVisibility(View.VISIBLE); // 프로그레스 바를 보이도록 설정

        firestore.collection("comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            commentList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String commentId = document.getId();
                                String commentContent = document.getString("commentContent");
                                String commentAuthor = document.getString("commentAuthor");
                                boolean isAnonymous = document.getBoolean("isAnonymous"); // 익명 여부 가져오기
                                String parentCommentId = document.getString("parentCommentId"); // 부모 댓글 ID 가져오기
                                Timestamp commentUploadTime = document.getTimestamp("commentUploadTime");

                                // CommentInfo 객체 생성
                                CommentInfo commentInfo = new CommentInfo(commentId, commentContent, commentAuthor, isAnonymous, parentCommentId, commentUploadTime);
                                commentList.add(commentInfo);
                            }
                            commentAdapter.notifyDataSetChanged();
                        } else {
                            // Handle error
                        }

                        progressBar.setVisibility(View.GONE); // 프로그레스 바를 감추도록 설정
                    }
                });
    }
}