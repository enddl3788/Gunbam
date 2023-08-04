package military.gunbam;

import android.widget.EditText;

public class MemberInfo {
    private String nickName;
    private String name;
    private String phoneNumber;
    private String birthDate;
    private String joinDate;
    private String dischargeDate;
    private String rank;
    private String draftUrl;

    public MemberInfo(String nickName, String name, String phoneNumber, String birthDate, String joinDate, String dischargeDate, String rank, String draftUrl){
        this.nickName = nickName;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.joinDate = joinDate;
        this.dischargeDate = dischargeDate;
        this.rank = rank;
        this.draftUrl = draftUrl;
    }

    public MemberInfo(String nickName, String name, String phoneNumber, String birthDate, String joinDate, String dischargeDate, String rank){
        this.nickName = nickName;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.joinDate = joinDate;
        this.dischargeDate = dischargeDate;
        this.rank = rank;
    }

    public String getNickName(){
        return this.nickName;
    }
    public void setNickName(String nickName){
        this.nickName = nickName;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getBirthDate(){
        return this.birthDate;
    }
    public void setBirthDate(String birthDate){
        this.birthDate = birthDate;
    }

    public String getJoinDate(){return this.joinDate;}
    public void setJoinDate(String joinDate){
        this.joinDate = joinDate;
    }

    public String getDischargeDate(){
        return this.dischargeDate;
    }
    public void setDischargeDate(String dischargeDate){
        this.dischargeDate = dischargeDate;
    }

    public String getRank(){
        return this.rank;
    }
    public void setRank(String rank){
        this.rank = rank;
    }


    public String getDraftUrl(){
        return this.draftUrl;
    }
    public void setDraftUrl(String draftUrl){
        this.draftUrl = draftUrl;
    }
}
