package src.player;

import src.team.*;

public abstract class Player {
    private Team team;
    private String name;

    public Team getTeam() {
        return team;
    }

    public String getName() {
        return name;
    }

    public Player(Team team, String name) {
        this.team = team;
        this.name = name;
    }

    public abstract int getStartingInput();

    public abstract void storeTurnInput();

    public abstract int getSwitchInput();

    public abstract int getMoveInput();
}
