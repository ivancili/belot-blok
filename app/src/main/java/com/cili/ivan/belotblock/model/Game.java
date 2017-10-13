package com.cili.ivan.belotblock.model;

public class Game {

    private String gameCreatedOn;
    private String teamOneName;
    private String teamTwoName;

    public Game(String gameCreatedOn, String teamOneName, String teamTwoName) {
        this.gameCreatedOn = gameCreatedOn;
        this.teamOneName = teamOneName;
        this.teamTwoName = teamTwoName;
    }

    public String getGameCreatedOn() {
        return gameCreatedOn;
    }

    public String getTeamOneName() {
        return teamOneName;
    }

    public String getTeamTwoName() {
        return teamTwoName;
    }
}
