package src.player;

import src.team.*;

import java.util.Scanner;
import java.util.regex.Pattern;

public class HumanPlayer extends Player {
    public HumanPlayer(Team team, String name) {
        super(team, name);
    }

    //called at the start of the turn when picking a move. Returns a move picked by the player
    public void storeTurnInput() {
        if(getTeam().getPokemon(0).isTrapped()) {
            int getMoveChoice = -1;
            while(getMoveChoice == -1) getMoveChoice = getMoveInput();

            getTeam().getPokemon(0).setMoveNum(getMoveChoice);
            return;
        }

        System.out.println("What should " + getTeam().getPokemon(0).getName() + " do? [attack / switch]");

        while(true) {
            String input = takeMessage();
            if(input.equalsIgnoreCase("Attack")) {

                int moveChoice = getMoveInput();
                if (moveChoice != -1) {
                    getTeam().getPokemon(0).setMoveNum(moveChoice);
                    return;
                } else {
                    System.out.println("What should " + getTeam().getPokemon(0).getName() + " do? [attack / switch]");
                }
            } else if(input.equalsIgnoreCase("Switch")) {
                System.out.println("Choose one of the following Pokemon to switch to. Type \"/back\" to go back.\n" + getTeam());
                int switchChoice = getSwitchInput();
                if(switchChoice != -1) {
                    getTeam().getPokemon(0).setSwitchPoke(switchChoice);
                    return;
                } else {
                    System.out.println("What should " + getTeam().getPokemon(0).getName() + " do? [attack / switch]");
                }
            } else {
                System.out.println("Please enter a valid input.");
            }
        }

    }

    //takes input for switching Pokemon
    public int getSwitchInput() {
        while(true) {
            String input = takeMessage();
            if(Pattern.matches("[0-5]+", input)) {
                int in = Integer.parseInt(input);
                if(in == 0) {
                    System.out.println(getTeam().getPokemon(0).getName() + " is already battling!");
                } else if(getTeam().getPokemon(in).getStatus() == 7) {
                    System.out.println(getTeam().getPokemon(in).getName() + " is unable to battle!");
                } else {
                    return in;
                }
            } else {
                int pokeNum = getTeam().findPokemonNum(input);
                if(pokeNum != -1) {
                    if (pokeNum == 0) {
                        System.out.println(getTeam().getPokemon(0).getName() + " is already battling!");
                    } else if (getTeam().getPokemon(pokeNum).getStatus() == 7) {
                        System.out.println(getTeam().getPokemon(pokeNum).getName() + " is unable to battle!");
                    } else {
                        return pokeNum;
                    }
                }
            } if(input.equals("/back")) {
                return -1;
            }
            System.out.println("Please enter a valid input.");
        }
    }

    public int getMoveInput() {
        if(getTeam().getPokemon(0).getChoice() != -1) {
            System.out.println(getTeam().getPokemon(0).getName() + " will use " + getTeam().getPokemon(0).getMoves(getTeam().getPokemon(0).getChoice()).getName() + ". Press [enter] to continue.");
            String input = takeMessage();
            if(!input.equals("/back")) {
                return getTeam().getPokemon(0).getChoice();
            }
            return -1;
        } else {
            if(getTeam().getPokemon(0).getItem().isMegaStone() && !getTeam().isMegaUsed()) {
                System.out.println(getTeam().getPokemon(0).getName() + " can Mega Evolve. Type \"/mega\" to Mega Evolve.");
            }

            while(true) {
                System.out.println(getTeam().getPokemon(0).getAvailableMovesString());

                String input = takeMessage();

                if (Pattern.matches("[0-3]", input)) {
                    return Integer.parseInt(input);
                } else {
                    for (int i = 0; i < getTeam().getPokemon(0).getAvailableMoves().length; i++) {
                        if (input.equalsIgnoreCase(getTeam().getPokemon(0).getAvailableMoves()[i].getName())) {
                            return i;
                        }
                    }

                    if(input.equals("/mega") && getTeam().getPokemon(0).getItem().isMegaStone() && !getTeam().isMegaUsed()) {
                        getTeam().getPokemon(0).getItem().setEvolving(!getTeam().getPokemon(0).getItem().getEvolving());
                        if(getTeam().getPokemon(0).getItem().getEvolving()) System.out.println(getTeam().getPokemon(0).getName() + " will Mega Evolve before it attacks.");
                        else System.out.println(getTeam().getPokemon(0).getName() + " will not Mega Evolve before it attacks.");
                    } else if(input.equals("\"/back\"")) {
                        getTeam().getPokemon(0).getItem().setEvolving(false);
                        return -1;
                    } else System.out.println("Please enter a valid input.");
                }
            }
        }
    }

    private String takeMessage() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            String input = sc.nextLine();
            if(Pattern.matches("/team .+", input)) {
                if(Pattern.matches("[0-5]", input.substring(input.indexOf(" ") + 1))) {
                    int num = Integer.parseInt(input.substring(input.indexOf(" ") + 1));
                    System.out.println(getTeam().getPokemon(num) + "\n" + getTeam().getPokemon(num).getAvailableMovesString());
                } else {
                    int number = getTeam().findPokemonNum(input.substring(input.indexOf(" ") + 1));
                    if (number != -1) System.out.println(getTeam().getPokemon(number) + "\n" + getTeam().getPokemon(number).getAvailableMovesString());
                    else System.out.println("Could not find \"" + input + "\" in your team.");
                    continue;
                }
            } else if (input.equals("/team")) {
                System.out.println(getTeam());
            } else if (Pattern.matches("/enemy .+", input)) {
                Team enemy = getTeam().getBattle().getEnemyTeam(getTeam());
                if(Pattern.matches("[0-5]+", input.substring(input.indexOf(" ") + 1))) {
                    int num = Integer.parseInt(input.substring(input.indexOf(" ") + 1));
                    System.out.println(enemy.getPokemon(num) + "\n" + enemy.getPokemon(num).getAvailableMovesString());
                } else {
                    int number = enemy.findPokemonNum(input.substring(input.indexOf(" ") + 1));
                    if (number != -1) System.out.println(enemy.getPokemon(number) + "\n" + enemy.getPokemon(number).getAvailableMovesString());

                    else
                        System.out.println("Could not find \"" + input + "\" in " + enemy.getPlayer().getName() + "'s team.");
                    continue;
                }
            } else if (input.equals("/enemy")) {
                System.out.println(getTeam().getBattle().getEnemyTeam(getTeam()));
            } else return input;
        }
    }

    public int getStartingInput() {
        while(true) {
            String input = takeMessage();
            if(Pattern.matches("[0-5]", input)) {
                return Integer.parseInt(input);
            } else {
                int pokeNum = getTeam().findPokemonNum(input);
                if (pokeNum != -1) {
                     return pokeNum;
                }
            }
            System.out.println("Please enter a valid input.");
        }
    }
}
