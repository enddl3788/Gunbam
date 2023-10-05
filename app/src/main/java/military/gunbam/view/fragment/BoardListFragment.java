package military.gunbam.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import military.gunbam.R;
import military.gunbam.view.activity.BoardListActivity;

public class BoardListFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board_list, container, false);

        view.findViewById(R.id.layoutBoard_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(BoardListActivity.class, "자유게시판");
            }
        });

        view.findViewById(R.id.layoutBoard_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(BoardListActivity.class, "군인게시판");
            }
        });

        view.findViewById(R.id.layoutBoard_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(BoardListActivity.class, "비밀게시판");
            }
        });

        view.findViewById(R.id.layoutBoard_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(BoardListActivity.class, "신병게시판");
            }
        });

        view.findViewById(R.id.layoutBoard_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(BoardListActivity.class, "정보게시판");
            }
        });

        return view;
    }

    private void myStartActivity(Class c, String boardName) {
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra("boardName", boardName);
        startActivity(intent);
    }
}