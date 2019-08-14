package src.battle;

import java.util.ArrayList;

import src.Main;
import src.Pokedex;
import src.team.*;
import src.Move;

import java.util.Scanner;

public class Battle {

	private Team team1;
	private Team team2;

	private Team winner;

	private int terrain;
	private int terrainCounter;
    //0 is default, 1 is sun, 2 is rain, 3 is sand, 4 is hail
	private int weather;
	private int weatherCounter;

    public Battle(Team team1, Team team2) {
		this.team1 = team1;
		this.team2 = team2;

		team1.setBattle(this);
		team2.setBattle(this);
	}

	private Pokemon getHigherPriority(Pokemon poke1, Move move1, Pokemon poke2, Move move2) {
        if(move1.getPriority() == move2.getPriority()) {
            return getFaster(poke1, poke2);
        } else return move1.getPriority() > move2.getPriority() ? poke1 : poke2;
    }

	private Pokemon getFaster(Pokemon poke1, Pokemon poke2) {
        return poke1.getSpeed(weather) == poke2.getSpeed(weather) ? (Math.random() < .5 ? poke1 : poke2) : (poke1.getSpeed(weather) > poke2.getSpeed(weather) ? poke1 : poke2);
    }

    //does 1 turn with new move objects (in attack method of Pokemon)
	public void doTurn() {
        if(team1.getPokemon(0).getSwitchPoke() > 0 && team2.getPokemon(0).getSwitchPoke() > 0) {//2 switches, no need to calculate moves
            Pokemon faster = getFaster(team1.getPokemon(0), team2.getPokemon(0));
            faster.getTeam().switchActive(faster.getSwitchPoke());
            getEnemyTeam(faster.getTeam()).switchActive(getEnemyTeam(faster.getTeam()).getPokemon(0).getSwitchPoke());
        } else {
            if(team1.getPokemon(0).getSwitchPoke() > 0 || team2.getPokemon(0).getSwitchPoke() > 0) {//1 switch
                Pokemon switcher = team1.getPokemon(0).getSwitchPoke() > 0 ? team1.getPokemon(0) : team2.getPokemon(0);
                Pokemon attacker = getEnemyTeam(switcher.getTeam()).getPokemon(0);

                switcher.getTeam().switchActive(switcher.getSwitchPoke());

                if(attacker.getItem().getEvolving()) attacker.megaEvolve();

                if(attacker.getStatus() == 2 && Math.random() < .25) {
                    System.out.println(attacker.getName() + " is paralyzed! It can't move!");
                } else { // general use move
                    if (attacker.getMove().getTarget() == 0) attacker.attack(attacker.getMove(), switcher.getTeam().getPokemon(0), weather);
                    else if (attacker.getMove().isFieldEffect()) {
                        attacker.setHazard(attacker.getMove(), switcher.getTeam());
                    } else {
                        attacker.useMove(attacker.getMove(), switcher, weather);
                    }
                    //track consecutive uses
                    if(attacker.getMove().reducePP() <= 0) {
                        attacker.getMove().reducePP(attacker.getMove().getPP());
                        attacker.setDisabledMoves(attacker.getMoveNum(attacker.getMove()));
                    }

                    if(attacker.getMove().addConsecutiveUse() == 1) {
                        for(int i = 0; i < 4; i++) {
                            attacker.getMoves(i).resetConsecutiveUses();
                        }
                        attacker.getMove().addConsecutiveUse();
                    }
                }
            } else {//0 switches
                //do mega evolutions
                if(team1.getPokemon(0).getItem().getEvolving() && team2.getPokemon(0).getItem().getEvolving()) {
                    getFaster(team1.getPokemon(0), team2.getPokemon(0)).megaEvolve();
                    if(team1.getPokemon(0).getItem().getEvolving()) team1.getPokemon(0).megaEvolve();
                    else team2.getPokemon(0).megaEvolve();
                } else {
                    if(team1.getPokemon(0).getItem().getEvolving()) {
                        team1.getPokemon(0).megaEvolve();
                    } else if(team2.getPokemon(0).getItem().getEvolving()) {
                        team2.getPokemon(0).megaEvolve();
                    }
                }

                Pokemon moveFirst = getHigherPriority(team1.getPokemon(0), team1.getPokemon(0).getMove(), team2.getPokemon(0), team2.getPokemon(0).getMove());
                Pokemon moveAfter = getEnemyTeam(moveFirst.getTeam()).getPokemon(0);


                if(moveFirst.getStatus() == 2 && Math.random() < .25) {
                    System.out.println(moveFirst.getName() + " is paralyzed! It can't move!\n");
                } else { // first mover uses move against slower pokemon
                    if (moveFirst.getMove().getTarget() == 0) moveFirst.attack(moveFirst.getMove(), moveAfter, weather);
                    else if (moveFirst.getMove().isFieldEffect()) {
                        moveFirst.setHazard(moveFirst.getMove(), moveAfter.getTeam());
                    } else {
                        moveFirst.useMove(moveFirst.getMove(), moveAfter, weather);
                    }

                    System.out.println();
                    //track consecutive uses
                    if(moveFirst.getMove().reducePP() <= 0) {
                        moveFirst.getMove().reducePP(moveFirst.getMove().getPP());
                        moveFirst.setDisabledMoves(moveFirst.getMoveNum(moveFirst.getMove()));
                    }

                    if(moveFirst.getMove().addConsecutiveUse() == 1) {
                        for(int i = 0; i < 4; i++) {
                            moveFirst.getMoves(i).resetConsecutiveUses();
                        }
                        moveFirst.getMove().addConsecutiveUse();
                    }
                }
                if(moveAfter.getStatus() == 7);
                else if(moveAfter.getStatus() == 2 && Math.random() < .25) {
                    System.out.println(moveAfter.getName() + " is paralyzed! It can't move!\n");
                } else if(moveAfter.isFlinched()) {
                    System.out.println(moveAfter.getName() + " flinched! It couldn't attack!\n");
                } else { // second mover uses move against faster pokemon
                    if (moveAfter.getMove().getTarget() == 0) moveAfter.attack(moveAfter.getMove(), moveFirst, weather);
                    else if (moveAfter.getMove().isFieldEffect()) {
                        moveAfter.setHazard(moveFirst.getMove(), moveFirst.getTeam());
                    } else {
                        moveAfter.useMove(moveAfter.getMove(), moveFirst, weather);
                    }
                    //track consecutive uses
                    System.out.println();

                    if(moveAfter.getMove().reducePP() <= 0) {
                        moveAfter.getMove().reducePP(moveAfter.getMove().getPP());
                        moveAfter.setDisabledMoves(moveAfter.getMoveNum(moveAfter.getMove()));
                    }

                    if(moveFirst.getMove().addConsecutiveUse() == 1) {
                        for(int i = 0; i < 4; i++) {
                            moveFirst.getMoves(i).resetConsecutiveUses();
                        }
                        moveFirst.getMove().addConsecutiveUse();
                    }
                }

            }
        }

        Pokemon faster = getFaster(team1.getPokemon(0), team2.getPokemon(0));
        Pokemon slower = getEnemyTeam(faster.getTeam()).getPokemon(0);

        if(weather != 0) {
            System.out.println(Pokedex.weatherMessage[weather]);
            if(--weatherCounter == 0) weather = 0;
            if(weather > 2) {
                int type1 = faster.getType1();
                int type2 = faster.getType2();
                if(weather == 3 && type1 != 10 && type1 != 9 && type1 != 17 && type2 != 10 && type2 != 9 && type2 != 17) {
                    System.out.println(faster.getName() + " is buffeted by the sandstorm.");
                    faster.damage((int)(faster.getMaxHP() * .0625), new Pokemon());
                } else if(weather == 4 && type1 != 7 && type2 !=7) {
                    System.out.println(faster.getName() + " is buffeted by the hail.");
                    faster.damage((int)(faster.getMaxHP() * .0625), new Pokemon());
                }
                type1 = slower.getType1();
                type2 = slower.getType2();
                if(weather == 3 && type1 != 10 && type1 != 9 && type1 != 17 && type2 != 10 && type2 != 9 && type2 != 17) {
                    System.out.println(slower.getName() + " is buffeted by the sandstorm.");
                    slower.damage((int)(slower.getMaxHP() * .0625), new Pokemon());
                } else if(weather == 4 && type1 != 7 && type2 != 7) {
                    System.out.println(slower.getName() + " is buffeted by the hail.");
                    slower.damage((int)(slower.getMaxHP() * .0625), new Pokemon());
                }
            }
        }

        if(faster.getHP() > 0) faster.getItem().doTurnEnd(faster);
        if(slower.getHP() > 0) slower.getItem().doTurnEnd(slower);

        if(faster.getSeeded() && slower.getStatus() != 7 && faster.getStatus() != 7) {
            System.out.println(faster.getName() + "'s health was sapped by leech seed!");
            slower.restoreHP(faster.damage((int) (faster.getMaxHP() * .125), new Pokemon()));
        }
        if(slower.getSeeded() && slower.getStatus() != 7 && faster.getStatus() != 7) {
            System.out.println(slower.getName() + "'s health was sapped by leech seed!");
            faster.restoreHP(slower.damage((int) (slower.getMaxHP() * .125), new Pokemon()));
        }

        faster.doStatusEffects();
        slower.doStatusEffects();
        //have to remake OOF
        while(team1.getPokemon(0).getStatus() == 7 && team2.getPokemon(0).getStatus() == 7) {
            int team1Switch = -1;
            System.out.println(team1.getPlayer().getName() + ", choose a Pokemon to switch in.\n" + team1);
            while(team1Switch == -1) team1Switch = team1.getPlayer().getSwitchInput();
            int team2Switch = -1;
            System.out.println(team2.getPlayer().getName() + ", choose a Pokemon to switch in.\n" + team2);

            while(team2Switch == -1) team2Switch = team2.getPlayer().getSwitchInput();

            if(team1.getPokemon(team1Switch).getSpeed(weather) > team2.getPokemon(team2Switch).getSpeed(weather)) {
                team1.switchActive(team1Switch);
                team2.switchActive(team2Switch);
            } else {
                team2.switchActive(team2Switch);
                team1.switchActive(team1Switch);
            }
        }
        while(team1.getPokemon(0).getStatus() == 7) {
            int switchNum = -1;
            System.out.println(team1.getPlayer().getName() + ", choose a Pokemon to switch in.");
            while(switchNum == -1) switchNum = team1.getPlayer().getSwitchInput();
            team1.switchActive(switchNum);

        }
        while(team2.getPokemon(0).getStatus() == 7) {
            int switchNum = -1;
            System.out.println(team2.getPlayer().getName() + ", choose a Pokemon to switch in.");
            while(switchNum == -1) switchNum = team2.getPlayer().getSwitchInput();
            team2.switchActive(switchNum);
        }

        faster.getAbility().doAfterTurn(faster, slower, weather);
        slower.getAbility().doAfterTurn(slower, faster, weather);

        faster.setFlinch(false);
        slower.setFlinch(false);
    }

	public void setWeather(int type) {
		weather = type;
		weatherCounter = 5;
		System.out.println(Pokedex.weatherEntrance[weather]);
	}

	public int getWeather() {
		return weather;
	}

	public void setWinner(Team team) {
        winner = team;
    }

    public void setLoser(Team team) {
        if(team1 == team) {
            winner = team2;
        } else {
            winner = team1;
        }
    }

    public Team getWinner() {
        return winner;
    }

    public Team getEnemyTeam(Team team) {
        if(team1 == team) {
            return team2;
        } else {
            return team1;
        }

    }
}