package asay.asaymobile.model;

/**
 * Created by s123725 on 15/12/2017.
 */

public class CommentDTO {
    public double getBillId() {
        return billId;
    }

    public void setBillId(double billId) {
        this.billId = billId;
    }

    double billId;
    ArgumentType argumentType;
    double id;
    int score;
    String text;
    double userid;
    double parrentId;

    public CommentDTO(){
        //empty contsructor
    }

    public CommentDTO(final CommentDTO commentDTO){
        argumentType = commentDTO.getArgumentType();
        billId = commentDTO.billId;
        id = commentDTO.getId();
        score = commentDTO.getScore();
        text = commentDTO.getText();
        userid = commentDTO.getUserid();
        parrentId = commentDTO.getParrentId();
    }

    public CommentDTO(final ArgumentType argument,double billId, final int id, final int score, final String text, final int userId, double parrentId ){
        this.argumentType = argument;
        this.billId = billId;
        this.id = id;
        this.score = score;
        this.text = text;
        this.userid = userId;
        this.parrentId = parrentId;
        }

    public ArgumentType getArgumentType() {
        return argumentType;
    }

    public void setArgumentType(ArgumentType argumentType) {
        this.argumentType = argumentType;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getUserid() {
        return userid;
    }

    public void setUserid(double userid) {
        this.userid = userid;
    }


    public double getParrentId() {
        return parrentId;
    }
}




