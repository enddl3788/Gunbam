package military.gunbam.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChattingModel {
    private DatabaseReference messageRef;
    private ValueEventListener valueEventListener;

    public ChattingModel() {

    }
    public void setInstance(String url, String path, String roodID) {
        messageRef = FirebaseDatabase.getInstance(url).getReference(path).child(roodID);
    }
    public void setValue(ChatData chatdata){
        messageRef.setValue(chatdata);
    }
    public void onDestroy(){
        messageRef.removeEventListener(valueEventListener);
    }







}
