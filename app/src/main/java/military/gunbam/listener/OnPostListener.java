package military.gunbam.listener;

import military.gunbam.model.PostInfo;

public interface OnPostListener {
    void onDelete(PostInfo postInfo);
    void onModify();
}
