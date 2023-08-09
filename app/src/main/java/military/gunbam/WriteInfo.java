package military.gunbam;

import com.google.firebase.Timestamp;

public class WriteInfo {
    private String title;
    private String contents;
    private String publisher;
    private int recommendationCount;
    private boolean isAnonymous;
    private Timestamp uploadTime;

    /**
     * 생성자 메서드: WriteInfo 객체를 생성하는 역할을 수행합니다.
     *
     * @param title               게시물의 제목
     * @param contents            게시물의 내용
     * @param publisher           게시물의 작성자 또는 출판자
     * @param recommendationCount 게시물의 추천 수
     * @param isAnonymous         익명 여부 (true면 익명, false면 비익명)
     * @param uploadTime          게시물 업로드 시간 (Timestamp 형식)
     */
    public WriteInfo(String title, String contents, String publisher, int recommendationCount, boolean isAnonymous, Timestamp uploadTime){
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
        this.recommendationCount = recommendationCount;
        this.isAnonymous = isAnonymous;
        this.uploadTime = uploadTime;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getContents(){
        return this.contents;
    }
    public void setContents(String contents){
        this.contents = contents;
    }

    public String getPublisher(){
        return this.publisher;
    }
    public void setPublisher(String publisher){
        this.publisher = publisher;
    }

    public int getRecommendationCount() {
        return recommendationCount;
    }
    public void setRecommendationCount(int recommendationCount) {
        this.recommendationCount = recommendationCount;
    }

    public boolean getIsAnonymous() {
        return isAnonymous;
    }
    public void setIsAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }
    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }
}