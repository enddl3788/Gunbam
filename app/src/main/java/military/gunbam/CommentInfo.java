package military.gunbam;

import com.google.firebase.Timestamp;

public class CommentInfo {
    private String commentId;
    private String commentContent;
    private String commentAuthor;
    private boolean isAnonymous;
    private String parentCommentId;
    private Timestamp commentUploadTime;

    /**
     * 생성자 메서드: CommentInfo 객체를 생성하는 역할을 수행합니다.
     *
     * @param commentId        댓글 고유 식별자 정보
     * @param commentContent   댓글 내용 정보
     * @param commentAuthor    댓글 작성자 정보
     * @param isAnonymous      익명 여부 정보
     * @param parentCommentId  부모 댓글의 식별자 정보 (대댓글의 경우)
     * @param commentUploadTime 댓글 업로드 시간 정보
     */
    public CommentInfo(String commentId, String commentContent, String commentAuthor, boolean isAnonymous, String parentCommentId, Timestamp commentUploadTime) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.commentAuthor = commentAuthor;
        this.isAnonymous = isAnonymous;
        this.parentCommentId = parentCommentId;
        this.commentUploadTime = commentUploadTime;
    }
    public CommentInfo(String commentId, String commentContent, String commentAuthor, boolean isAnonymous, Timestamp commentUploadTime) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.commentAuthor = commentAuthor;
        this.isAnonymous = isAnonymous;
        this.commentUploadTime = commentUploadTime;
    }

    public String getCommentId() {
        return this.commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return this.commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentAuthor() {
        return this.commentAuthor;
    }

    public void setCommentAuthor(String commentAuthor) {
        this.commentAuthor = commentAuthor;
    }

    public boolean isAnonymous() {
        return this.isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.isAnonymous = anonymous;
    }

    public String getParentCommentId() {
        return this.parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public Timestamp getCommentUploadTime() {
        return this.commentUploadTime;
    }

    public void setCommentUploadTime(Timestamp commentUploadTime) {
        this.commentUploadTime = commentUploadTime;
    }
}