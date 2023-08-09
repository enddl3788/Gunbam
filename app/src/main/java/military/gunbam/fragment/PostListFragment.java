package military.gunbam.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import military.gunbam.R;
import military.gunbam.WriteInfo;
import military.gunbam.adapter.PostAdapter;

public class PostListFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<WriteInfo> postList;
    private FirebaseFirestore firestore;

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the post list when the fragment is resumed
        loadPosts();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        recyclerView = view.findViewById(R.id.postRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);

        firestore = FirebaseFirestore.getInstance();

        return view;
    }

    private void loadPosts() {
        firestore.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            postList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String contents = document.getString("contents");
                                String publisherUid = document.getString("publisher");
                                int recommendationCount = document.getLong("recommendationCount").intValue();
                                boolean isAnonymous = document.getBoolean("isAnonymous");
                                // Assuming "uploadTime" is a Firestore Timestamp field
                                Timestamp uploadTime = document.getTimestamp("uploadTime");

                                // Fetch the nickname from the "users" collection using the publisherUid
                                if (isAnonymous) {
                                    String nickName = "익명";
                                    WriteInfo writeInfo = new WriteInfo(title, contents, nickName, recommendationCount, isAnonymous, uploadTime);
                                    postList.add(writeInfo);
                                    postAdapter.notifyDataSetChanged();
                                } else {
                                    firestore.collection("users")
                                            .document(publisherUid)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot userDocument) {
                                                    if (userDocument.exists()) {
                                                        String nickName = userDocument.getString("nickName");
                                                        WriteInfo writeInfo = new WriteInfo(title, contents, nickName, recommendationCount, isAnonymous, uploadTime);
                                                        postList.add(writeInfo);
                                                        postAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                            });
                                }
                            }
                        } else {
                            // Handle error
                        }
                    }
                });
    }
}
