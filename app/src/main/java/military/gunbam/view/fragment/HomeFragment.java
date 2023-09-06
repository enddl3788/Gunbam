package military.gunbam.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

import military.gunbam.R;
import military.gunbam.listener.OnPostListener;
import military.gunbam.model.PostInfo;
import military.gunbam.view.activity.LoginActivity;
import military.gunbam.view.adapter.HomeAdapter;
import military.gunbam.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FirebaseFirestore firebaseFirestore;
    private HomeAdapter homeAdapter;
    private ArrayList<PostInfo> postList;
    private boolean updating;
    private boolean topScrolled;

    private Button logoutButton;

    private HomeViewModel homeViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // postList 초기화
        postList = new ArrayList<>();

        // ViewModel 초기화
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // RecyclerView 및 Adapter 설정
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeAdapter = new HomeAdapter(getActivity(), new ArrayList<>());
        recyclerView.setAdapter(homeAdapter);

        // ViewModel에서 LiveData를 관찰하여 데이터 업데이트를 처리
        homeViewModel.getPostListLiveData().observe(getViewLifecycleOwner(), postList -> {
            // Adapter에 데이터를 설정하여 화면 업데이트
            homeAdapter.setPostList(postList);
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
        homeViewModel.loadPosts(false);

        // 로그아웃 버튼 클릭 이벤트
        view.findViewById(R.id.logoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.logout();
                myStartActivity(LoginActivity.class);
            }
        });

        // 게시물 작성 버튼 클릭 이벤트
        view.findViewById(R.id.mainFloatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.navigateToWritePost(getActivity());
            }
        });

        // 게시물 수정 및 삭제 버튼 클릭 이벤트
        homeAdapter.setOnPostListener(new OnPostListener() {
            @Override
            public void onDelete(PostInfo postInfo) {
                postList.remove(postInfo);
                homeAdapter.notifyDataSetChanged();
                homeViewModel.refreshPosts();
            }

            @Override
            public void onModify() {
            }
        });
        return view;
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivityForResult(intent, 0);
    }
}