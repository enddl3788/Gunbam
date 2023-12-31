package military.gunbam.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import military.gunbam.R;
import military.gunbam.listener.OnPostListener;
import military.gunbam.model.Post.PostInfo;
import military.gunbam.view.adapter.BoardListAdapter;
import military.gunbam.viewmodel.BoardListViewModel;

public class BoardListActivity extends BasicActivity {

    private static final String TAG = "BoardListActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private ArrayList<PostInfo> postList;
 //   private String boardName;
    private String field;
    private String fieldValue;
    private MutableLiveData<ArrayList<PostInfo>> postListLiveData = new MutableLiveData<>();
    private boolean updating;

    private BoardListAdapter boardListAdapter;
    private BoardListViewModel boardListViewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);

        Intent intent = getIntent();
        field = intent.getStringExtra("field"); // "boardName"이라고 가져와지거나 "publisher"

        fieldValue = intent.getStringExtra("fieldValue"); //"자유게시판",  "사람아이디"
        TextView textViewBoardName = findViewById(R.id.tvTitle);

        if(field.equals("boardName")) {
            textViewBoardName.setText(fieldValue);
        }else if(field.equals("publisher")){
            textViewBoardName.setText("내가 쓴 게시글");
        }

        // postList 초기화
        postList = new ArrayList<>();

        // ViewModel 초기화
        boardListViewModel = new ViewModelProvider(this).get(BoardListViewModel.class);

        // RecyclerView 및 Adapter 설정
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BoardListActivity.this));
        boardListAdapter = new BoardListAdapter(BoardListActivity.this, new ArrayList<>());
        recyclerView.setAdapter(boardListAdapter);

        // ViewModel에서 LiveData를 관찰하여 데이터 업데이트를 처리
        boardListViewModel.getPostListLiveData().observe(this, postList -> {
            // Adapter에 데이터를 설정하여 화면 업데이트
            boardListAdapter.setPostList(postList);
        });

        // 스크롤 이벤트 및 초기 데이터 로딩 처리
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            // 스크롤 이벤트 처리 코드...

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 스크롤 이벤트 처리 코드...
            }
        });

        // 초기 데이터 로딩
        boardListViewModel.loadPosts(false,field, fieldValue);

        // 게시물 작성 버튼 클릭 이벤트
        findViewById(R.id.mainFloatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToWritePost(BoardListActivity.this);
            }
        });

        // 게시물 수정 및 삭제 버튼 클릭 이벤트
        boardListAdapter.setOnPostListener(new OnPostListener() {
            @Override
            public void onDelete(PostInfo postInfo) {
                postList.remove(postInfo);
                boardListAdapter.notifyDataSetChanged();
                boardListViewModel.refreshPosts(field, fieldValue);
            }

            @Override
            public void onModify() {
            }
        });
    }

    // 게시물 작성 화면으로 이동
    public void navigateToWritePost(Context context) {
        Intent intent = new Intent(context, WritePostActivity.class);
        intent.putExtra(field, fieldValue);
        context.startActivity(intent);
    }
}
