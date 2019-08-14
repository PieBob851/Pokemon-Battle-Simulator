package src.team;

import java.util.ArrayList;

import src.Pokedex;
import src.battle.Battle;
import src.player.*;

public class Team {
	//active pokemon is in slot 0
	private Pokemon[] team;
	private Player player;

	public void setPlayer(Player player) {
	    this.player = player;
    }

    public Player getPlayer() {
	    return player;
    }

	public void setTeam(Pokemon[] team) {
        this.team = team;
    }

	//set's name of team. Used in messages involving side, for example "[name] sent out [pokemon_name]!"

	private boolean megaUsed;

	public void setMegaUsed() {
	    megaUsed = true;
    }

	private boolean zUsed;

	public void setzUsed() {
	    zUsed = true;
    }

    private int reflect;
    private int lightScreen;

    private Battle battle;

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public Battle getBattle() {
        return battle;
    }

    private boolean rocks;

    private int spikes;

    private int poisonSpikes;

    public void addSpikes() {
        spikes++;
        if(spikes > 3) spikes++;
    }

    public void addPoisonSpikes() {
        poisonSpikes++;
        if(poisonSpikes > 2) poisonSpikes = 2;
    }

    public void addRocks() {
        if(rocks) System.out.println("But it failed!");
        rocks = true;
    }

    public void clearHazards() {
        rocks = false;
        spikes = 0;
        poisonSpikes = 0;
    }

    public int getReflect() {
        return reflect;
    }

    public int getLightScreen() {
        return lightScreen;
    }

    public Team() {
    }

	public Team(Team team) {
		this.team = team.team;
	}
	public Team(Pokemon[] team) {
		this.team = team;
	}

	public Pokemon getPokemon(int slot) {
		return team[slot];
	}

	public Pokemon[] getTeam() {
		return team;
	}

	public boolean isMegaUsed() {
		return megaUsed;
	}

	public boolean iszUsed() {
	    return zUsed;
    }

    //sets starting Pokemon
    public void setActive(int active) {
        System.out.println("Go, " + team[active].getName() + "!");

        team[active].getAbility().doEntranceEffects(team[active], battle.getEnemyTeam(this).team[0], battle);

        Pokemon temp = team[0];
        team[0] = team[active];
        team[active] = temp;
    }

    public void switchActive(int newActive) {
        //if active Pokemon is not fainted, do effects on switch
        System.out.println(player.getName() + ": Come back, " + team[0].getName() + "!");
        System.out.println(player.getName() + ": Go, " + team[newActive].getName() + "!");

        team[0].setSeeded(false);

        if(team[0].getStatus() != 7) {
            team[0].getItem().doSwitch(team[0]);
            team[0].getAbility().doSwitchEffects(team[0]);
            team[0].setSwitchPoke(0);

            for(int i = 0; i < 4; i++) {
                team[0].getMoves(i).resetConsecutiveUses();
            }
        }

        team[0].resetAbility();
        team[0].resetTurnsIn();

        team[newActive].getAbility().doEntranceEffects(team[newActive], battle.getEnemyTeam(this).team[0], battle);

        Pokemon temp = team[0];
        team[0] = team[newActive];
        team[newActive] = temp;

        System.out.println(player.getName() + " sent out " + team[0].getName() + "!");

        if(spikes > 0 && !team[0].getAbility().removesIndirectDamage() && !team[0].isImmune(10)) {
            team[0].damage((int) (team[0].getMaxHP()/(double) (10 - 2 * spikes)), new Pokemon());
            System.out.println(team[0] + "was hurt by the spikes!");

        }
        if(rocks && !team[0].getAbility().removesIndirectDamage()) {
            team[0].damage((int) (team[0].getMaxHP() * .125 * Pokedex.TYPE_CHART[9][team[0].getType1()] * Pokedex.TYPE_CHART[9][team[0].getType2()]), new Pokemon());
            System.out.println(team[0].getName() + " was hurt by Stealth Rocks!");
        }
        if(poisonSpikes > 0) {
            if(!team[0].isImmune(10))
                if(team[0].getType1() == 8 || team[1].getType2() == 8) {
                    poisonSpikes = 0;
                    System.out.println("The poison spikes disappeared from " + player.getName() + "'s side of the field!");
                } else if(!team[0].isImmune(8)) {
                    team[0].setStatus(3 + poisonSpikes);
                    System.out.println(team[0] + "was poisoned!");
                }
        }

        battle.getEnemyTeam(this).getPokemon(0).setTrapped(false);
    }

    public boolean areAllFainted() {
        boolean allFainted = true;
        for(int i = 0; i < 3; i++) {
            if(team[i].getStatus() != 7) allFainted = false;
        }
        return allFainted;
    }

    //find Pokemon from name of Pokemon
    public int findPokemonNum(String name) {
        for(int i = 0; i < 6; i++) {
            if(team[i].getName().equalsIgnoreCase(name)) return i;
        }

        return -1;
    }

    public String toString() {
        String out = "Team:\n";
        for(int j = 0; j < 3; j++) {
            if(team[j].getStatus() == 7) {
                out += "\t\t\t\t\t\t\t\t\t";
            } else {
                out += team[j].getName() +  ":" + new String(new char[9 - (team[j].getName().length() + 1)/4]).replace("\0", "\t");
            }
        }

        out += "\n";

        for(int j = 0; j < 3; j++) {
            if(team[j].getStatus() == 7) {
                out += "\t\t\t\t\t\t\t\t\t";
            } else {
                String hp = "HP: " + team[j].getHP() +  "/" + team[j].getMaxHP();
                out += hp + new String(new char[9 - (hp.length())/4]).replace("\0", "\t");
            }
        }

        out += "\n";

        for(int j = 0; j < 3; j++) {
            if(team[j].getStatus() == 7) {
                out += "\t\t\t\t\t\t\t\t\t";
            } else {
                out += "Status: " + Pokedex.statusName[team[j].getStatus()] + new String(new char[9 - (Pokedex.statusName[team[j].getStatus()].length() + 8)/4]).replace("\0", "\t");
            }
        }

        out += "\n";

        for(int j = 0; j < 3; j++) {
            if(team[j].getStatus() == 7) {
                out += "\t\t\t\t\t\t\t\t\t";
            } else {
                String types = (team[j].getType2() > 0 ? "Types: " + Pokedex.typeNames[team[j].getType1()] + ", " + Pokedex.typeNames[team[j].getType2()] : "Type: " + Pokedex.typeNames[team[j].getType1()]);
                out += types + new String(new char[9 - types.length()/4]).replace("\0", "\t");
            }
        }

        out += "\n\n";

        for(int j = 3; j < 6; j++) {
            if(team[j].getStatus() == 7) {
                out += "\t\t\t\t\t\t\t\t\t";
            } else {
                out += team[j].getName() +  ":" + new String(new char[9 - (team[j].getName().length() + 1)/4]).replace("\0", "\t");
            }
        }

        out += "\n";

        for(int j = 3; j < 6; j++) {
            if(team[j].getStatus() == 7) {
                out += "\t\t\t\t\t\t\t\t\t";
            } else {
                String hp = "HP: " + team[j].getHP() +  "/" + team[j].getMaxHP();
                out += hp + new String(new char[9 - (hp.length())/4]).replace("\0", "\t");
            }
        }

        out += "\n";

        for(int j = 3; j < 6; j++) {
            if(team[j].getStatus() == 7) {
                out += "\t\t\t\t\t\t\t\t\t";
            } else {
                out += "Status: " + Pokedex.statusName[team[j].getStatus()] + new String(new char[9 - (Pokedex.statusName[team[j].getStatus()].length() + 8)/4]).replace("\0", "\t");
            }
        }

        out += "\n";

        for(int j = 3; j < 6; j++) {
            if(team[j].getStatus() == 7) {
                out += "\t\t\t\t\t\t\t\t\t";
            } else {
                String types = (team[j].getType2() > 0 ? "Types: " + Pokedex.typeNames[team[j].getType1()] + ", " + Pokedex.typeNames[team[j].getType2()] : "Type: " + Pokedex.typeNames[team[j].getType1()]);
                out += types + new String(new char[9 - types.length()/4]).replace("\0", "\t");
            }
        }

        out += "\n\n";

        return out;

    }
}