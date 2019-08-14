package src.team;

import src.*;
import src.battle.Battle;

import java.util.Scanner;

public class Pokemon {
    private String name;

    private Team team;

    public Team getTeam() {
        return team;
    }

    private int level;
    private int[] iv = new int[6];
    private int[] ev = new int[6];
    private double[] nature = new double[6];

    private int maxHP;
    private int[] stats = new int[6];

    private int type1;
    private int type2;
    private Ability originalAbility;
    private Ability ability;
    private Item heldItem;

    private int status; //0 is base, 1-5 are status effects (par, slp, psn, brn, frz), 6 is fainted
    private int[] counter;
    private int[] statBoosts = new int[8];

    private double weight;

    private Move[] moves;
    private boolean[] disabledMoves = new boolean[4];
    private int choice = -1;

    private boolean protect;

    //suffering from bind of some sort
    private boolean bound;
    private boolean trapped;

    public void resetAbility() {
        ability = originalAbility;
    }

    public void setBound(boolean bound) {
        this.bound = bound;
    }

    public void setTrapped(boolean trapped) {
        this.trapped = trapped;
    }

    public boolean isBound() {
        return bound;
    }

    public boolean isTrapped() {
        return trapped || bound;
    }

    //if this pokemon flinched
    private boolean flinch;

    public boolean isFlinched() {
        return flinch;
    }

    public void setFlinch(boolean flinch) {
        this.flinch = flinch;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    private int turnsIn;

    public int getTurnsIn() {
        return turnsIn;
    }

    public void resetTurnsIn() {
        turnsIn = 0;
    }

    //for Toxic damage
    public int incrementTurnsIn() {
        return ++turnsIn;
    }

    private int switchPoke;

    private boolean seeded;

    public boolean getSeeded() {
        return seeded;
    }

    public void setSeeded(boolean seeded) {
        this.seeded = seeded;
    }

    public int getSwitchPoke() {
        return switchPoke;
    }

    public void setSwitchPoke(int slot) {
        switchPoke = slot;
    }

    private int moveNum;

    public Move getMove() {
        return moves[moveNum];
    }

    public void setMoveNum(int moveNum) {
        this.moveNum = moveNum;
    }

    public void setBaseStats(int[] baseStats) {
        for(int i = 1; i < 6; i++) {
            stats[i] = (int)(((2 * baseStats[i] + iv[i] + ev[i] / 4) * level / 100.0 + 5) * nature[i]);
        }
    }

    //mega evolution checked to be possible already
    public void megaEvolve() {
        for(int i = 1; i < 6; i++) {
            stats[i] = (int)(((2 * heldItem.getMegaStat(i) + iv[i] + ev[i] / 4) * level / 100.0 + 5) * nature[i]);
        }

        ability = heldItem.getMegaAbility();

        ability.doEntranceEffects(this, team.getBattle().getEnemyTeam(team).getPokemon(0), team.getBattle());

        team.setMegaUsed();

        System.out.println(name + " evolved into Mega " + name + "!\n");

        name = "Mega " + name;
        heldItem.setEvolving(false);
    }

    public Pokemon() {
        name = "BASE_POKE";
        ability = new Ability();
        heldItem = new Item();
    }

    public Pokemon(Pokemon template, Ability ability, Item heldItem, Move[] moves, int[] ivs, int[] evs, double[] nature, Team team) {
        name = template.name;
        type1 = template.type1;
        type2 = template.type2;
        weight = template.weight;
        this.ability = ability;
        originalAbility = ability;
        this.heldItem = heldItem;
        this.moves = moves;
        this.iv = ivs;
        this.ev = evs;
        this.nature = nature;
        this.team = team;

        level = 100;

        stats[0] = (int)((2 * template.stats[0] + iv[0] + ev[0]/4) * 100 / 100.0) + 100 + 10;
        for(int i = 1; i < 6; i++) {
            stats[i] = (int)(((2 * template.stats[i] + iv[i] + ev[i]/4) * level / 100.0 + 5) * nature[i]);
        }

        maxHP = stats[0];
    }

    public Pokemon(String name, int type1, int type2, int[] stats, double weight) {
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.stats = stats;
        this.weight = weight;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public int getHP() {
        return stats[0];
    }

    public int getAttack(int weather) {
        double modifier = calculateMultiplier(1);
        if(ability.getBoostedStat() == 1) {
            modifier *= ability.getStatMultiplier();
        }
        if(heldItem.getMultipliedStat() == 1) {
            modifier *= heldItem.getStatMultiplier();
        }
        return (int) (stats[1] * modifier);
    }

    public int getDefense(int weather) {
        double modifier = calculateMultiplier(2);
        if(ability.getBoostedStat() == 2) {
            modifier *= ability.getStatMultiplier();
        }
        if(heldItem.getMultipliedStat() == 2) {
            modifier *= heldItem.getStatMultiplier();
        }
        return (int) (stats[2] * modifier);
    }

    public int getSpecialAttack(int weather) {
        double modifier = calculateMultiplier(3);
        if(ability.getBoostedStat() == 3) {
            modifier *= ability.getStatMultiplier();
        }
        if(heldItem.getMultipliedStat() == 3) {
            modifier *= heldItem.getStatMultiplier();
        }
        return (int) (stats[3] * modifier);
    }

    public int getSpecialDefense(int weather) {
        double modifier = calculateMultiplier(1);
        if(ability.getBoostedStat() == 4) {
            modifier *= ability.getStatMultiplier();
        }
        if(heldItem.getMultipliedStat() == 4) {
            modifier *= heldItem.getStatMultiplier();
        }
        if(weather == 3 && type1 == 9 || type2 == 9) modifier *= 1.5;
        return (int) (stats[4] * modifier);
    }

    public int getSpeed(int weather) {
        double modifier = calculateMultiplier(5);
        if(ability.getBoostedStat() == 5) {
            modifier *= ability.getStatMultiplier();
        }
        if(heldItem.getMultipliedStat() == 5) {
            modifier *= heldItem.getStatMultiplier();
        }
        if(status == 2) modifier *= .5;
        return (int) (stats[5] * modifier);
    }

    public int getMaxHP() {
        return maxHP;
    }
    public Move getMoves(int slot) {
        return moves[slot];
    }
    public boolean[] getDisabledMoves() {
        return disabledMoves;
    }
    //returns index Move is at in array moves[]
    public int getMoveNum(Move move) {
        for(int i = 0; i < 4; i++) {
            if(move.getName().equals(moves[i].getName())) return i;
        }
        return -1;
    }
    public void setChoice(Move move) {
        choice = getMoveNum(move);
    }

    public void setChoice() {
        choice = -1;
    }

    public int getChoice() {
        return choice;
    }

    public void setDisabledMoves(int slot) {
        disabledMoves[slot] = !disabledMoves[slot];
    }

    public void doStatusEffects() {
        if(!ability.removesIndirectDamage()) {
            if(status == 4) {
                if(!ability.isPoisonHeal()) {
                    System.out.println(team.getPlayer().getName() + "'s " + name + " was hurt by poison.");
                    damage((int)(maxHP * .125), new Pokemon());
                } else {
                    System.out.println(team.getPlayer().getName() + "'s " + name + " restored HP with poison heal.");
                    restoreHP((int)(maxHP * .125));
                }
            } if(status == 5) {
                if(!ability.isPoisonHeal()) {
                    System.out.println(team.getPlayer().getName() + "'s " + name + " was hurt by poison.");
                    damage((int)(maxHP * .0625 * turnsIn), new Pokemon());
                } else {
                    System.out.println(team.getPlayer().getName() + "'s " + name + " restored HP with poison heal.");
                    restoreHP((int)(maxHP * .125));
                }
            } if(status == 6) {
                System.out.println(team.getPlayer().getName() + "'s " + name + " was hurt by its burn.");
                damage((int)(maxHP * .0625), new Pokemon());
            }
        }
    }


    //a single attack by Pokemon to enemy with moveUsed
    public void attack(Move move, Pokemon enemy, int weather) {
        Scanner sc = new Scanner(System.in);
        Move moveUsed = new Move(move);

        ability.doAttackEffects(this, enemy, moveUsed);
        heldItem.doAttack(this, moveUsed);


        System.out.println(name + " used " + moveUsed.getName() + "!");
        if (enemy.protect && moveUsed.doesBreakProtect()) {
            System.out.println(enemy.name + "'s protect was broken!");
            enemy.protect = false;
        }

        ability.doDefendEffects(this, enemy, moveUsed);
        heldItem.doDefense(this, moveUsed, enemy);

        if (enemy.isImmune(moveUsed)) {
            System.out.println("It doesn't affect " + enemy.name + "...");
        } else if (enemy.protect) {
            System.out.println(enemy.name + " protected itself!");
            if (moveUsed.iszMove()) {
                System.out.println(enemy.name + " couldn't fully protect against the attack!");
                enemy.damage((int) ((((double) 2 * level / 5 + 2) * moveUsed.getPower() / 4 * (moveUsed.getCategory() == 0 ? getAttack(weather) / (double) enemy.getDefense(weather) : (double) getSpecialAttack(weather) / enemy.getSpecialDefense(weather)) / 50 + 2) * moveUsed.calculateModifier(team, enemy.team, weather)) + moveUsed.getDamage(), this);
            }
        } else if (Math.random() > moveUsed.getAccuracy() * calculateMultiplier(6) / enemy.calculateMultiplier(7)) {
            System.out.println(name + "'s attack missed!");
        } else {
            if(move.getCategory() != 1) enemy.damage((int) ((((double) 2 * level / 5 + 2) * moveUsed.getPower() * (moveUsed.getCategory() == 0 ? getAttack(weather) / (double) enemy.getDefense(weather) : (double) getSpecialAttack(weather) / enemy.getSpecialDefense(weather)) / 50 + 2) * moveUsed.calculateModifier(team, enemy.team, weather)) + moveUsed.getDamage(), this);
            if (Math.random() < moveUsed.getEffectChance()) {
                if (enemy.status == 0) enemy.status = moveUsed.getEffectType();
            }
            if (Math.random() < moveUsed.getBoostChance()) {
                if (moveUsed.getBoostType() > 0) {
                    addBoost(moveUsed.getBoostType(), moveUsed.getBoostStrength());
                } else if (moveUsed.getBoostType() < 0) {
                    enemy.addBoost(Math.abs(moveUsed.getBoostType()), moveUsed.getBoostStrength());
                }
            }
            if (Math.random() < moveUsed.getFlinchChance()) {
                enemy.flinch = true;
            }
            if (moveUsed.isContact()) enemy.ability.doContact(this, enemy);
            if (moveUsed.doesSwitchPoke()) {
                System.out.println(name + " is returning!");
                int switchNum = -1;
                while(switchNum == -1) switchNum = team.getPlayer().getSwitchInput();
                team.switchActive(switchNum);
            } else if (moveUsed.getExplode()) {
                damage(maxHP, new Pokemon());
            } else if (moveUsed.isLeechSeed()) {
                System.out.println(enemy.team.getPlayer().getName() + "'s " + enemy.name + " was seeded!");
                enemy.seeded = true;

            }
        }

        if (moveUsed.isRemoveHazards() && moveUsed.isRemoveEnemyHazards()) {
            System.out.println("Hazards were removed from the field!");
            team.clearHazards();
            enemy.team.clearHazards();
        } else if (moveUsed.isRemoveHazards()) {
            System.out.println("Hazards were removed from " + team.getPlayer().getName() + "'s side of the field!");
            team.clearHazards();
        }
    }

    public void useMove(Move moveUsed, Pokemon enemy, int weather) {
        ability.doAttackEffects(this, enemy, moveUsed);
        heldItem.doAttack(this, moveUsed);

        System.out.println(team.getPlayer().getName() + "'s " + name +  " used " + moveUsed.getName() + "!");

        if(moveUsed.getBoostType() > 0) {
            addBoost(moveUsed.getBoostType(), moveUsed.getBoostStrength());
        }

        if(moveUsed.isProtect()) {
            if(Math.random() < 1/Math.pow(3, moveUsed.getConsecutiveUses())) {
                protect = true;
                System.out.println(team.getPlayer().getName() + "'s " + name + " is protecting itself!\n");
            } else {
                System.out.println("But it failed!\n");
            }
        }

        if(moveUsed.getHealingPercent(weather) > 0) {
            System.out.println(team.getPlayer().getName() + "'s " + name + " restored its HP with " + moveUsed.getName() + ".");
            restoreHP((int)(maxHP * moveUsed.getHealingPercent(weather)));
        }
    }

    public void setHazard(Move moveUsed, Team enemy) {
        System.out.println(team.getPlayer().getName() + "'s " + name + " used " + moveUsed.getName() + "!");

        if(moveUsed.isSpikes()) {
            System.out.println("Spikes were set on " + team.getPlayer().getName() + "'s side of the field!\n");
            enemy.addSpikes();
        }
        if(moveUsed.istSpikes()) {
            System.out.println("Poisonous spikes were set on " + team.getPlayer().getName() + "'s side of the field!\n");
            enemy.addPoisonSpikes();
        }
        if(moveUsed.isRocks()) {
            System.out.println("Pointed rocks started levitating above " + team.getPlayer().getName() + "'s side of the field!\n");
            enemy.addRocks();
        }
    }

    //checks immunity based on **TO BE IMPLEMENTED
    public boolean isImmune(Move moveUsed) {
        return Pokedex.TYPE_CHART[moveUsed.getType()][type1] * Pokedex.TYPE_CHART[moveUsed.getType()][type1] == 0 ||
                ability.getImmune() == moveUsed.getType() || !moveUsed.isAffectSameType() && (moveUsed.getType() == type1
                || moveUsed.getType() == type2);
    }

    public boolean isImmune(int type) {
        return Pokedex.TYPE_CHART[type][type1] * Pokedex.TYPE_CHART[type][type1] == 0 ||
                ability.getImmune() == type;
    }

    //returns multiplier based on stat boosts
    public double calculateMultiplier(int slot) {
        return slot < 6 ? statBoosts[slot] > 0 ? (double) statBoosts[slot] / 2 + 1 : -1 / ((double) statBoosts[slot] / 2 - 1) : statBoosts[slot] > 0 ? (double) statBoosts[slot] / 3 + 1 : -1 / ((double) statBoosts[slot] / 3 - 1);
    }

    public void addBoost(int slot, int amount) {
        statBoosts[slot] += amount;
        System.out.println(name + "'s " + Pokedex.statName[slot] + " " + Pokedex.boostStrengthText[Math.abs(amount)] + (amount > 0 ? "rose" : "fell") + "!\n");

    }

    public int restoreHP(int amount) {
        int startHP = stats[0];

        stats[0] += amount;
        if(stats[0] > maxHP) stats[0] = maxHP;

        System.out.println(team.getPlayer().getName() + "'s " + name + " restored " + ((double) Math.floor((stats[0] - startHP) * 1000.0 / maxHP)/10  + "%") + " of its health!\n");
        return stats[0] - startHP;
    }

    public void changeType(int type) {
        type1 = type;
        type2 = 0;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public int getType1() {
        return type1;
    }

    public int getType2() {
        return type2;
    }

    public double getWeight() {
        return weight;
    }

    public Item getItem() {
        return heldItem;
    }

    public void setHeldItem(Item newItem) {
        heldItem = newItem;
    }

    public int damage(int amount, Pokemon attacker) {
        int startHP = stats[0];
        stats[0] -= amount;
        if(stats[0] < 1) {
            System.out.println(name + " fainted!\n");
            attacker.ability.doKillEffects(attacker, this);
            status = 7;
            stats[0] = 0;
            if(team.areAllFainted()) team.getBattle().setLoser(team);
        } else {
            heldItem.doTakeDamage(this);
        }

        System.out.println(team.getPlayer().getName() + "'s " + name + " lost " + ((Math.floor((startHP - stats[0]) * 1000.0 / maxHP)/10)  + "%") + " of its health!\n");

        return startHP - stats[0];
    }

    public int getHighestStat() {
        int high = 1;
        for(int i = 2; i < 6; i++) {
            if(stats[i] > stats[high]) high = i;
        }

        return high;
    }

    //printing Pokemon name and Percent HP
    public String getHPPercent() {
        return Math.floor(stats[0] * 1000.0 / maxHP)/10  + "%";
    }

    //returns an array of the moves available for the Pokemon to use
    public Move[] getAvailableMoves() {
        if(choice == -1) {
            int count = 4;
            for (boolean disabled : disabledMoves) {
                if(disabled) count--;
            }

            Move[] availableMoves = new Move[count];

            count = 0;
            for (int i = 0; i < 4; i++) {
                if (!disabledMoves[i]) {
                    availableMoves[count] = moves[i];
                    count++;
                }
            }

            return availableMoves;
        } else {
            Move[] arr = {moves[choice]};
            return arr;
        }
    }

    public String getAvailableMovesString() {
        String out = "";
        for(Move move: getAvailableMoves()) {
            out += move.toString() + "\n";
        }

        return out;
    }

    public String toString() {
        return name + ":\nStats:\nHP: " + stats[0] + "/" + maxHP + "\t\tAttack: " + stats[1] + "\t\tDefense: " + stats[2] +
                "\nSp. Attack: " + stats[3] + "\t\tSp. Defense: " + stats[4] + "\t\tSpeed: " + stats[5] + "\nStat Multipliers: "
                + (statBoosts[1] != 0 ? "\nAttack: x" + calculateMultiplier(statBoosts[1]) : "") +
                (statBoosts[2] != 0 ? "\nDefense: x" + calculateMultiplier(statBoosts[2]) : "") +
                (statBoosts[3] != 0 ? "\nSp. Attack: x" + calculateMultiplier(statBoosts[3]) : "") +
                (statBoosts[4] != 0 ? "\nSp. Defense: x" + calculateMultiplier(statBoosts[4]) : "") +
                (statBoosts[5] != 0 ? "\nSpeed: x" + calculateMultiplier(statBoosts[5]) : "") +
                (statBoosts[6] != 0 ? "\nAccuracy: x" + calculateMultiplier(statBoosts[6]) : "") +
                (statBoosts[7] != 0 ? "\nEvasion: x" + calculateMultiplier(statBoosts[7]) : "") +
                "\n\nStatus: " + Pokedex.statusName[status] + "\n\n" + ability + "\n\n" + heldItem + "\n\n";
    }

    public String toString(Pokemon enemy) {
        return team.getPlayer().getName() + ":" +  new String(new char[9-(team.getPlayer().getName().length() + 1)/4]).replace("\0", "\t") + enemy.team.getPlayer().getName() + ":\n" +
                name + ":" +  new String(new char[9-(name.length() + 1)/4]).replace("\0", "\t") + enemy.name + ":\n" +
                "Health: " + getHPPercent() + new String(new char[7-(getHPPercent().length())/4]).replace("\0", "\t") + "Health: " + enemy.getHPPercent() + ":\n" +
                "Status: " + Pokedex.statusName[status] + new String(new char[7-Pokedex.statusName[status].length()/4]).replace("\0", "\t") + "Status: " + Pokedex.statusName[enemy.status] + "\n\n\n";
    }
}