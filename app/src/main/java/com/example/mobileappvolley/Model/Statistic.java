package com.example.mobileappvolley.Model;

public class Statistic{
    private Long allAttack = 0L;
    private Long blockAttack = 0L;
    private Long errorAttack = 0L;
    private Long perfAttack = 0L;
    private Long perfAttackPerc = 0L;
    private Long allBlock = 0L;
    private int points = 0;
    private Long allReceive = 0L;
    private Long errorReceive = 0L;
    private Long negativeReceive = 0L;
    private Long perfReceive = 0L;
    private Long perfReceivePerc = 0L;
    private Long positiveReceive = 0L;
    private Long positiveReceivePerc = 0L;
    private Long allServe = 0L;
    private Long aceServe = 0L;
    private Long errorServe = 0L;
    private String idPlayer;

    public Long getAllAttack() {
        return allAttack;
    }

    public void setAllAttack(Long allAttack) {
        this.allAttack = allAttack;
    }

    public Long getBlockAttack() {
        return blockAttack;
    }

    public void setBlockAttack(Long blockAttack) {
        this.blockAttack = blockAttack;
    }

    public Long getErrorAttack() {
        return errorAttack;
    }

    public void setErrorAttack(Long errorAttack) {
        this.errorAttack = errorAttack;
    }

    public Long getPerfAttack() {
        return perfAttack;
    }

    public void setPerfAttack(Long perfAttack) {
        this.perfAttack = perfAttack;
    }

    public Long getPerfAttackPerc() {
        return perfAttackPerc;
    }

    public void setPerfAttackPerc(Long perfAttackPerc) {
        this.perfAttackPerc = perfAttackPerc;
    }

    public Long getAllBlock() {
        return allBlock;
    }

    public void setAllBlock(Long allBlock) {
        this.allBlock = allBlock;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Long getAllReceive() {
        return allReceive;
    }

    public void setAllReceive(Long allReceive) {
        this.allReceive = allReceive;
    }

    public Long getErrorReceive() {
        return errorReceive;
    }

    public void setErrorReceive(Long errorReceive) {
        this.errorReceive = errorReceive;
    }

    public Long getNegativeReceive() {
        return negativeReceive;
    }

    public void setNegativeReceive(Long negativeReceive) {
        this.negativeReceive = negativeReceive;
    }

    public Long getPerfReceive() {
        return perfReceive;
    }

    public void setPerfReceive(Long perfReceive) {
        this.perfReceive = perfReceive;
    }

    public Long getPerfReceivePerc() {
        return perfReceivePerc;
    }

    public void setPerfReceivePerc(Long perfReceivePerc) {
        this.perfReceivePerc = perfReceivePerc;
    }

    public Long getPositiveReceive() {
        return positiveReceive;
    }

    public void setPositiveReceive(Long positiveReceive) {
        this.positiveReceive = positiveReceive;
    }

    public Long getPositiveReceivePerc() {
        return positiveReceivePerc;
    }

    public void setPositiveReceivePerc(Long positiveReceivePerc) {
        this.positiveReceivePerc = positiveReceivePerc;
    }

    public Long getAllServe() {
        return allServe;
    }

    public void setAllServe(Long allServe) {
        this.allServe = allServe;
    }

    public Long getAceServe() {
        return aceServe;
    }

    public void setAceServe(Long aceServe) {
        this.aceServe = aceServe;
    }

    public Long getErrorServe() {
        return errorServe;
    }

    public void setErrorServe(Long errorServe) {
        this.errorServe = errorServe;
    }


    public String getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(String idPlayer) {
        this.idPlayer = idPlayer;
    }
}
