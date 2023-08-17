package military.gunbam.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import military.gunbam.model.ChatData;
import military.gunbam.model.ChattingModel;


public class ChattingViewModel extends ViewModel {
    private DatabaseReference messageRef;
    private MutableLiveData<List<ChatData>> chatDataListLiveData = new MutableLiveData<>();
    private ChattingModel model = new ChattingModel();

    public void setValue(ChatData chatData){
        model.setValue(chatData);

    }

    public void initMessageReference(String roomID) {
        messageRef = FirebaseDatabase.getInstance("https://militaryapp-800ed-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("chat")
                .child(roomID);

        // Add ValueEventListener to messageRef
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ChatData> chatDataList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String message = snapshot.child("message").getValue(String.class);
                    String senderUid = snapshot.child("senderUid").getValue(String.class);
                    long timestamp = snapshot.child("timestamp").getValue(Long.class);
                    String userEmail = snapshot.child("userEmail").getValue(String.class);
                    String userName = snapshot.child("userName").getValue(String.class);
                    ChatData chatData = new ChatData(message, senderUid, timestamp, userName, userEmail);
                    chatDataList.add(chatData);
                }
                chatDataListLiveData.setValue(chatDataList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
    }



    public void onDestroy(){
        model.onDestroy();
    }
    public LiveData<List<ChatData>> getChatDataListLiveData() {
        return chatDataListLiveData;
    }

}
