package src.player;

import src.team.Team;

public class AIPlayer extends Player {

    public AIPlayer(Team team, String name) {
        super(team, name);
    }

    public void storeTurnInput() {
    }

    public int getSwitchInput() {
        return 1;
    }

    public int getMoveInput() {
        return 1;
    }

    public int getStartingInput() {
        return 1;
    }

    
}
