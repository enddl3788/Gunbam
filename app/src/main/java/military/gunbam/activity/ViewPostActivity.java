package military.gunbam.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

import military.gunbam.R;

public class ViewPostActivity extends BasicActivity {

    private TextView titleTextView;
    private TextView contentsTextView;
    private TextView publisherTextView;
    private TextView uploadTimeTextView;
    private String publisher;

    private FirebaseFirestore firestore;
    private static final String TAG = "ViewPostActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        titleTextView = findViewById(R.id.postViewTitleTextView);
        contentsTextView = findViewById(R.id.postViewContentsTextView);
        publisherTextView = findViewById(R.id.postViewPublisherTextView);
        uploadTimeTextView = findViewById(R.id.postViewUploadTimeTextView);

        firestore = FirebaseFirestore.getInstance();

        // Get the post ID from the intent
        String postId = getIntent().getStringExtra("postId");

        // Retrieve the post details from Firestore
        firestore.collection("posts").document(postId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String title = document.getString("title");
                                String contents = document.getString("contents");
                                boolean isAnonymous = document.getBoolean("isAnonymous");

                                if (isAnonymous) {
                                    publisher = "익명";
                                    publisherTextView.setText(publisher);
                                } else {
                                    String publisherUid = document.getString("publisher");
                                    firestore.collection("users").document(publisherUid)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot userDocument) {
                                                    if (userDocument.exists()) {
                                                        String nickName = userDocument.getString("nickName");
                                                        publisher = nickName;
                                                        publisherTextView.setText(publisher);
                                                    }
                                                }
                                            });
                                }
                                titleTextView.setText(title);
                                contentsTextView.setText(contents);

                                // Convert Timestamp to desired date format
                                Timestamp uploadTimestamp = document.getTimestamp("uploadTime");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String formattedUploadTime = sdf.format(uploadTimestamp.toDate());
                                uploadTimeTextView.setText(formattedUploadTime);
                            }
                        } else {
                            // Handle error
                        }
                    }
                });
    }
}



