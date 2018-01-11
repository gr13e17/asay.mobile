package asay.asaymobile.model;

import android.support.annotation.NonNull;

/**
 * Created by s123725 on 15/12/2017.
 */

public class CommentDTO implements Comparable<CommentDTO> {
    private double billId;
    private ArgumentType argumentType;
    private double id;
    private int score;
    private String text;
    private double userId;
    private double parentId;
    private double commentDepth;
    private double childrenCount;

    public CommentDTO() {
        //empty contsructor
    }

    public CommentDTO(final CommentDTO commentDTO) {
        argumentType = commentDTO.getArgumentType();
        billId = commentDTO.billId;
        id = commentDTO.getId();
        score = commentDTO.getScore();
        text = commentDTO.getText();
        userId = commentDTO.getUserId();
        parentId = commentDTO.getParentId();
        childrenCount = commentDTO.getChildrenCount();
        commentDepth = commentDTO.getCommentDepth();
    }

    public CommentDTO(final ArgumentType argument, double billId, final int id, final int score, final String text, final int userId, double parentId, double childrenCount, double commentDepth) {
        this.argumentType = argument;
        this.billId = billId;
        this.id = id;
        this.score = score;
        this.text = text;
        this.userId = userId;
        this.parentId = parentId;
        this.childrenCount = childrenCount;
        this.commentDepth = commentDepth;
    }

    public double getBillId() {
        return billId;
    }

    public void setBillId(double billId) {
        this.billId = billId;
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

    public double getUserId() {
        return userId;
    }

    public void setUserId(double userId) {
        this.userId = userId;
    }

    public double getParentId() {
        return parentId;
    }

    public void setCommentDepth(double commentDepth) {
        this.commentDepth = commentDepth;
    }

    public double getCommentDepth() {
        return commentDepth;
    }

    public void setChildrenCount(double childrenCount) {
        this.childrenCount = childrenCount;
    }

    public double getChildrenCount() {
        return childrenCount;
    }

    @Override
    public int compareTo(@NonNull CommentDTO o) {
        if (this.score < o.score)
            return -1;
        else if (this.score > o.score)
            return 1;
        else
            return 0;
    }
}




