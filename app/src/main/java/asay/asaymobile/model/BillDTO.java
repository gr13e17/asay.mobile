package asay.asaymobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by s123725 on 15/12/2017.
 */

public class BillDTO implements Parcelable{
    private String createdBy;
    private String deadline;
    private String department;
    private int forumId;
    public int id;
    public String number;
    private String title;
    private String titleShort;
    private String resume;
    public ArrayList<Vote> votes = new ArrayList<>();
    private int typeId;
    private  int actorId;
    private String status;
    public ArrayList<CaseStep> caseSteps = new ArrayList<>();

    public BillDTO(){}

    public BillDTO(String createdBy, String deadline, String department, int forumId, int id, String number, String title, String titleShort, String resume,ArrayList<Vote> votes, int typeId,int actorId,String status, ArrayList<CaseStep> caseSteps){
        this.createdBy = createdBy;
        this.deadline = deadline;
        this.department = department;
        this.forumId = forumId;
        this.id = id;
        this.number = number;
        this.title = title;
        this.titleShort = titleShort;
        this.resume = resume;
        this.votes = votes;
        this.typeId = typeId;
        this.actorId = actorId;
        this.status = status;
        this.caseSteps = caseSteps;

    }
    public BillDTO(String createdBy, String deadline, String department, int forumId, int id, String number, String title, String titleShort, String resume, int typeId, int actorId, String status, ArrayList<CaseStep> caseSteps){
        this.createdBy = createdBy;
        this.deadline = deadline;
        this.department = department;
        this.forumId = forumId;
        this.id = id;
        this.number = number;
        this.title = title;
        this.titleShort = titleShort;
        this.resume = resume;
        this.typeId = typeId;
        this.actorId = actorId;
        this.status = status;
        this.caseSteps = caseSteps;
    }

    public BillDTO(BillDTO billDTO){
        this.createdBy = billDTO.createdBy;
        this.deadline = billDTO.deadline;
        this.department = billDTO.department;
        this.forumId = billDTO.forumId;
        this.id = billDTO.id;
        this.number = billDTO.number;
        this.title = billDTO.title;
        this.titleShort = billDTO.titleShort;
        this.resume = billDTO.resume;
        this.votes = billDTO.votes;
        this.typeId = billDTO.typeId;
        this.actorId = billDTO.actorId;
        this.status = billDTO.status;
        this.caseSteps = billDTO.caseSteps;
    }

    protected BillDTO(Parcel in){
        this.createdBy = in.readString();
        this.deadline = in.readString();
        this.department = in.readString();
        this.forumId = in.readInt();
        this.id = in.readInt();
        this.number = in.readString();
        this.title = in.readString();
        this.titleShort = in.readString();
        this.resume = in.readString();
        in.readTypedList(this.votes, Vote.CREATOR);
        this.typeId = in.readInt();
        this.actorId = in.readInt();
        this.status = in.readString();
        in.readTypedList(this.caseSteps, CaseStep.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.createdBy);
        dest.writeString(this.deadline);
        dest.writeString(this.department);
        dest.writeInt(this.forumId);
        dest.writeInt(this.id);
        dest.writeString(this.number);
        dest.writeString(this.title);
        dest.writeString(this.titleShort);
        dest.writeString(this.resume);
        dest.writeTypedList(this.votes);
        dest.writeInt(this.typeId);
        dest.writeInt(this.actorId);
        dest.writeString(this.status);
        dest.writeTypedList(this.caseSteps);
    }

    public static final Creator<BillDTO> CREATOR = new BillDTOCreator();

    private static class BillDTOCreator implements Creator<BillDTO> {
        @Override
        public BillDTO createFromParcel(final Parcel source) {
            return new BillDTO(source);
        }

        @Override
        public BillDTO[] newArray(final int size) {
            return new BillDTO[size];
        }
    }

    public ArrayList<CaseStep> getCaseSteps() { return caseSteps; }
    public void setCaseSteps(ArrayList<CaseStep> caseSteps) { this.caseSteps = caseSteps; }
    public String getStatus(){return status; }
    public void setStatus(String status){ this.status = status;}
    public int getActorId() { return actorId; }
    public void setActorId(int actorId) { this.actorId = actorId; }
    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public int getForumId() { return forumId; }
    public void setForumId(int forumId) { this.forumId = forumId; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTitleShort() { return titleShort; }
    public void setTitleShort(String titleShort) { this.titleShort = titleShort; }
    public String getResume(){ return resume; }
    public void setResume(String resume){ this.resume = resume; }
    public ArrayList<Vote> getVotes() { return votes; }
    public void setVotes(ArrayList<Vote> votes) { this.votes = votes; }
    public void addVote(Vote vote){ this.votes.add(vote); }
    public void removeVote(String userHash){
        for(int i = this.votes.size() -1 ; i>=0; i--){
            if (votes.get(i).getUserHash().equals(userHash) )
                votes.remove(i);
        }
    }

    public static class CaseStep implements Parcelable{
        private double id;
        private String title;
        private String date;
        private double typeid;
        private double statusid;
        private String updateDate;

        public CaseStep(){}

        public CaseStep(double id, String title, String date, double typeid, double statusid, String updateDate) {
            this.id = id;
            this.title = title;
            this.date = date;
            this.typeid = typeid;
            this.statusid = statusid;
            this.updateDate = updateDate;
        }

        private CaseStep(Parcel in){
            this.id = in.readDouble();
            this.title = in.readString();
            this.date = in.readString();
            this.typeid = in.readDouble();
            this.statusid = in.readDouble();
            this.updateDate = in.readString();
        }


        public double getId() { return id; }
        public void setId(double id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public double getTypeid() { return typeid; }
        public void setTypeid(double typeid) { this.typeid = typeid; }
        public double getStatusid() { return statusid; }
        public void setStatusid(double statusid) { this.statusid = statusid; }
        public String getUpdateDate() { return updateDate; }
        public void setUpdateDate(String updateDate) { this.updateDate = updateDate; }

        public static final Creator<CaseStep> CREATOR = new Creator<CaseStep>() {
            @Override
            public CaseStep createFromParcel(Parcel in) {
                return new CaseStep(in);
            }

            @Override
            public CaseStep[] newArray(int size) {
                return new CaseStep[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(this.id);
            dest.writeString(this.title);
            dest.writeString(this.date);
            dest.writeDouble(this.typeid);
            dest.writeDouble(this.statusid);
            dest.writeString(this.updateDate);
        }
    }

    public static class Vote implements Parcelable{
        public int id;
        private String userHash;
        private ArgumentType vote;

        public Vote(){}

        public Vote(int id, String userhash, ArgumentType argumentType){
            this.id = id;
            this.userHash = userhash;
            this.vote = argumentType;
        }

        private Vote(Parcel in){
            this.id = in.readInt();
            this.userHash = in.readString();
            this.vote = ArgumentType.valueOf(in.readString());
        }

        public static final Creator<Vote> CREATOR = new Creator<Vote>() {
            @Override
            public Vote createFromParcel(Parcel in) {
                return new Vote(in);
            }

            @Override
            public Vote[] newArray(int size) {
                return new Vote[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.userHash);
            dest.writeString(this.vote.name());
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getUserHash() { return userHash; }
        public void setUserHash(String userHash) { this.userHash = userHash; }
        public ArgumentType getVote() { return vote; }
        public void setVote(ArgumentType vote) { this.vote = vote; }
    }
}

