package com.cili.ivan.belotblock.model;

/**
 * Created by ivan on 24-Sep-17.
 */

public class Session {

    private String gameCreatedOn;
    private String sessionStartedOn;
    private String teamOneScore;
    private String teamTwoScore;

    public Session(String gameCreatedOn, String sessionStartedOn, String teamOneScore, String teamTwoScore) {
        this.gameCreatedOn = gameCreatedOn;
        this.sessionStartedOn = sessionStartedOn;
        this.teamOneScore = teamOneScore;
        this.teamTwoScore = teamTwoScore;
    }

    public String getGameCreatedOn() {
        return gameCreatedOn;
    }

    public String getSessionStartedOn() {
        return sessionStartedOn;
    }

    public String getTeamOneScore() {
        return teamOneScore;
    }

    public String getTeamTwoScore() {
        return teamTwoScore;
    }
}
