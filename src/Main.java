package src;

import org.json.simple.parser.ParseException;
import src.team.*;
import src.battle.Battle;

import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.regex.*;

import java.util.ArrayList;

import src.player.*;

public class Main {
    public static void main(String[] args) throws ParseException, IOException {
        Pokedex dex = new Pokedex();

        Scanner sc = new Scanner(System.in);

        Team team1 = importTeam("src/sampleteams/team1.txt", dex);
        Team team2 = importTeam("src/sampleteams/team1.txt", dex);

        Battle battle = new Battle(team1, team2);

        Player player1 = new HumanPlayer(team1, "PieBob");
        Player player2 = new HumanPlayer(team2, "5space_");


        System.out.println(player1.getName() + ", choose your starting Pokemon. \n" + team1);
        team1.setActive(player1.getStartingInput());
        team1.setPlayer(player1);
        System.out.println(player2.getName() + ", choose your starting Pokemon. \n" + team2);
        team2.setActive(player2.getStartingInput());
        team2.setPlayer(player2);

        while(battle.getWinner() == null) {
            System.out.println(team1.getPokemon(0).toString(team2.getPokemon(0)));

            System.out.println(player1.getName() + ":");
            player1.storeTurnInput();
            System.out.println(player2.getName() + ":");
            player2.storeTurnInput();

            battle.doTurn();
        }

    }


    public static Team importTeam(String filePath, Pokedex dex) throws IOException{
        File file = new File(filePath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        Team team = new Team();

        Pokemon[] teamImport = new Pokemon[6];
        //runs through file for each of the 6 Pokemon
        for(int i = 0; i < 6; i++) {
            String line = br.readLine();

            String name = line.substring(0, line.indexOf(" "));
            String itemName = line.substring(line.indexOf("@") + 2);

            line = br.readLine();

            String abilityName = line.substring(line.indexOf(":") + 2);

            line = br.readLine();

            String[] evsString = line.substring(line.indexOf(":") + 1).split("/");

            int[] evs = new int[6];
            int count = 0;
            for (int j = 0; j < 6; j++) {
                if (evsString[count].contains(Pokedex.shortStatName[j])) {
                    evs[j] = Integer.parseInt(evsString[count].substring(1, evsString[count].indexOf(" ", 1)));
                    count++;
                }
                if(count == evsString.length) break;
            }


            line = br.readLine();

            String natureString = line.substring(0, line.indexOf(" "));

            double nature[] = new double[6];

            for(int j = 0; j < Pokedex.natures.length; j++) {
                if(Pokedex.natures[j].equalsIgnoreCase(natureString)) {
                    nature = Pokedex.natureValues[j];
                    break;
                }
            }

            line = br.readLine();

            int[] ivs = new int[6];

            if (line.contains("IVs")) {
                String[] ivsString = line.substring(line.indexOf(":") + 1).split("/");
                count = 0;
                for (int j = 0; j < 6; j++) {
                    if (ivsString[count].contains(Pokedex.shortStatName[j])) {
                        ivs[j] = Integer.parseInt(ivsString[count].substring(1, ivsString[count].indexOf(" ", 1)));
                        count++;
                        if(count == ivsString.length) count--;
                    }
                    else ivs[j] = 31;
                }

                line = br.readLine();
            } else {
                for (int j = 0; j < 6; j++) {
                    ivs[j] = 31;
                }
            }

            String[] movesString = new String[4];

            for (int j = 0; j < 4; j++) {
                movesString[j] = line.substring(2);
                line = br.readLine();
            }

            Pokemon template = new Pokemon();

            for (int j = 0; j < dex.POKEMON_LIST.length; j++) {
                if (dex.POKEMON_LIST[j].getName().equals(name)) {
                    template = dex.POKEMON_LIST[j];
                    break;
                }
            }

            Item heldItem = new Item();

            for (int j = 0; j < dex.ITEM_LIST.length; j++) {
                if (dex.ITEM_LIST[j].getName().equals(itemName)) {
                    heldItem = new Item(dex.ITEM_LIST[j]);
                    break;
                }
            }

            Ability ability = new Ability();

            for (int j = 0; j < dex.ABILITY_LIST.length; j++) {
                if (dex.ABILITY_LIST[j].getName().equals(abilityName)) {
                    ability = new Ability(dex.ABILITY_LIST[j]);
                    break;
                }
            }

            Move[] moves = new Move[4];

            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < dex.MOVE_LIST.length; k++) {
                    if (dex.MOVE_LIST[k].getName().equals(movesString[j])) {
                        moves[j] = new Move(dex.MOVE_LIST[k]);
                        break;
                    }
                }
            }

            teamImport[i] = new Pokemon(template, ability, heldItem, moves, ivs, evs, nature, team);
        }

        team.setTeam(teamImport);

        return team;
    }
}
