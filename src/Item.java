package src;

import src.battle.Battle;
import src.team.*;
import src.Move;

public class Item {
    private String name;

    private String desc;

    //only true if the item is a megaStone held by the correct Pokemon
    private boolean megaStone;
    //for actually doing the evolution
    private boolean evolving;
    public void setEvolving(boolean choice) {
        evolving = choice;
    }

    public boolean getEvolving() {
        return evolving;
    }
    private int[] megaStats;
    private Ability megaAbility;

    public int getMegaStat(int slot) {
        return megaStats[slot];
    }

    public Ability getMegaAbility() {
        return megaAbility;
    }

    private boolean onSwitch;
    private boolean onEntrance;
    private boolean onKill;
    private boolean onAttack;
    private boolean onDefend;
    private boolean afterDefense;
    private boolean onTurnEnd;
    private boolean onCrit;
    private boolean onSuperEffective;
    private boolean onTakeDamage;

    //change to only be true if able to be used; add vars for stat bonus, effect bonus, etc.
    private boolean zStone;
    private int zType;
    private String zPokeName;



    public void setUseZMove() {

    }

    private boolean healing;
    private double healingPercent;

    private double activeThreshold;

    private boolean choice;

    private int stat;
    private double statMultiplier = 1;

    private double damageMultiplier = 1;

    private int neededType;

    private boolean berry;
    private boolean consumed;

    private boolean disableStatusMoves;

    public String getName() {
        return name;
    }

    public Item() {
        name = "BASE_ITEM";
    }

    public Item(String name) {
        this.name = name;
    }

    public Item(Item item) {
        name = item.name;
        megaStone = item.megaStone;
        megaStats = item.megaStats;
        megaAbility = item.megaAbility;
        onSwitch = item.onSwitch;
        onEntrance = item.onEntrance;
        onKill = item.onKill;
        onAttack = item.onAttack;
        onDefend = item.onDefend;
        afterDefense = item.afterDefense;
        onTurnEnd = item.onTurnEnd;
        onCrit = item.onCrit;
        onSuperEffective = item.onSuperEffective;
        onTakeDamage = item.onTakeDamage;
        zStone = item.zStone;
        zType = item.zType;
        zPokeName = item.zPokeName;
        healing = item.healing;
        healingPercent = item.healingPercent;
        activeThreshold = item.activeThreshold;
        choice = item.choice;
        stat = item.stat;
        statMultiplier = item.statMultiplier;
        damageMultiplier = item.damageMultiplier;
        neededType = item.neededType;
        berry = item.berry;
        consumed = item.consumed;
        disableStatusMoves = item.disableStatusMoves;
        desc = item.desc;
    }

    public Item(String name, boolean megaStone, int[] megaStats, Ability megaAbility, boolean onSwitch, boolean onEntrance, boolean onKill, boolean onAttack, boolean onDefend, boolean afterDefense, boolean onTurnEnd, boolean onCrit, boolean onSuperEffective, boolean onTakeDamage, boolean zStone, int zType, String zPokeName, boolean healing, double healingPercent, double activeThreshold, boolean choice, int stat, double statMultiplier, double damageMultiplier, int neededType, boolean berry, boolean consumed, boolean disableStatusMoves, String desc) {
        this.name = name;
        this.megaStone = megaStone;
        this.megaStats = megaStats;
        this.megaAbility = megaAbility;
        this.onSwitch = onSwitch;
        this.onEntrance = onEntrance;
        this.onKill = onKill;
        this.onAttack = onAttack;
        this.onDefend = onDefend;
        this.afterDefense = afterDefense;
        this.onTurnEnd = onTurnEnd;
        this.onCrit = onCrit;
        this.onSuperEffective = onSuperEffective;
        this.onTakeDamage = onTakeDamage;
        this.zStone = zStone;
        this.zType = zType;
        this.zPokeName = zPokeName;
        this.healing = healing;
        this.healingPercent = healingPercent;
        this.activeThreshold = activeThreshold;
        this.choice = choice;
        this.stat = stat;
        this.statMultiplier = statMultiplier;
        this.damageMultiplier = damageMultiplier;
        this.neededType = neededType;
        this.berry = berry;
        this.consumed = consumed;
        this.disableStatusMoves = disableStatusMoves;
        this.desc = desc;
    }

    public boolean canMega(Pokemon poke, Team team) {
        return !team.isMegaUsed() && megaStone;
    }

    public boolean canZ(Move moveused, Team team) {
        return moveused.canZ() && !team.iszUsed() && zStone;
    }

    public void doTurnEnd(Pokemon ally) {
        if (onTurnEnd) {
            if(healing) {
                System.out.println(ally.getTeam().getPlayer().getName() + "'s " + ally.getName() + " restored its HP with " + name + ".");

                if(neededType == 0 || neededType == ally.getType1() || neededType == ally.getType2())
                    ally.restoreHP((int)(ally.getMaxHP() * healingPercent));
                else ally.restoreHP((int)-(ally.getMaxHP() * healingPercent));
            }
        }
    }

    public void doAttack(Pokemon ally, Move moveused) {
        if(onAttack) {
            if(choice) {
                ally.setChoice(moveused);
            }
        }
    }

    public void doDefense(Pokemon ally, Move moveUsed, Pokemon attacker) {

    }

    public void doTakeDamage(Pokemon ally) {
        if(onTakeDamage) {
            if(healing && activeThreshold >= (double) ally.getHP() / ally.getMaxHP()) {
                System.out.println(ally.getTeam().getPlayer().getName() + "'s " + ally.getName() + " restored its HP with " + name + ".");
                ally.restoreHP((int)(ally.getMaxHP() * healingPercent));
                if(berry) {
                    System.out.println(ally.getTeam().getPlayer().getName() + "'s " + ally.getName() + " ate it's " + name + ".");
                    consumed = true;
                }
            }
        }
    }


    public void doSwitch(Pokemon ally) {
        if(onSwitch) {
            System.out.println("(" + ally.getName() + "'s " + name + ")");
            ally.setChoice();
        }
    }

    public void doSuperEffective(Move moveUsed) {
        if(onSuperEffective) {
            moveUsed.setPower((int)(moveUsed.getPower()*damageMultiplier));
        }
    }

    public double getModifier(boolean superEffective) {
        if(superEffective && onSuperEffective) return 1.2;
        return 1;
    }

    public int getMultipliedStat() {
        return stat;
    }
    public double getStatMultiplier() {
        return statMultiplier;
    }

    public boolean doesDisableStatusMoves() {
        return disableStatusMoves;
    }

    public String toString() {
        return name + ":\n Description: " + desc;
    }

    public boolean isMegaStone() {
        return megaStone;
    }
}
