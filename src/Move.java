package src;

import src.team.*;
import src.battle.Battle;

public class Move {
    private String name;
    private String desc;

    //basic stats, power is base power, damage is raw hp damage (defense doesn't factor in)
    private int power;
    private double accuracy = 1;
    private int pp;
    private int maxPP;
    private int damage;
    private int priority;
    private int type;

    //0 = enemy, 1 = self, 2 = field, 3 = team, 4 = enemy team
    private int targetType;

    public int getTarget() {
        return targetType;
    }

    private boolean removeHazards;
    private boolean removeEnemyHazards;

    public boolean isRemoveHazards() {
        return removeHazards;
    }

    public boolean isRemoveEnemyHazards() {
        return removeEnemyHazards;
    }


    private boolean weightDependent;
    private double multiplier = 1;

    private int consecutiveUses;

    //physical = 0, status = 1, special = 2
    private int category;

    private boolean resetStats;

    //status effects; sleep =1 , para=2, freeze=3, poison=4, badly poisoned=5, burn=6 (sleep counter down on attacking, same freeze)
    private double effectChance;
    private int effectType;

    //flinch effects
    private double flinchChance;

    private double critChance = .0625;


    //stat boost effects 1-7 for stats or negative for enemy stats
    private double boostChance;
    private int boostType;
    private int boostStrength;

    public double getBoostChance() {
        return boostChance;
    }

    //life stealing moves
    private double damageRecover;

    private double healingPercent;

    private boolean protect;

    //field hazards
    private boolean tSpikes;
    private boolean spikes;
    private boolean rocks;
    private boolean leechSeed;

    private boolean contact;
    private boolean alwaysHit;
    private boolean dance;
    private boolean sound;
    private boolean breakprotect;
    private boolean bypassprotect;

    private boolean zMove;

    public boolean iszMove() {
        return zMove;
    }

    public Move makeZMove() {
        Move zMove = new Move();
        if(category == 1) {
            zMove.name = "Z-" + name;
        }
        else {
            zMove.name = Pokedex.zMoveNames[type];
            if(power <= 55) {
                zMove.power = 100;
            } else if(power <= 65) {
                zMove.power = 120;
            } else if(power <= 75) {
                zMove.power = 140;
            } else if(power <= 85) {
                zMove.power = 160;
            } else if(power <= 95) {
                zMove.power = 175;
            } else if(power <= 100) {
                zMove.power = 180;
            } else if(power <= 110) {
                zMove.power = 185;
            } else if(power <= 125) {
                zMove.power = 190;
            } else if(power <= 135) {
                zMove.power = 195;
            } else {
                zMove.power = 200;
            }
            zMove.zMove = true;
            zMove.alwaysHit = true;
            zMove.type = type;
            category = category;
        }

        return zMove;
    }

    private boolean knockOff;
    private boolean explode;

    private boolean switchPoke;
    private boolean fieldEffect;

    public boolean isFieldEffect() {
        return fieldEffect;
    }

    private boolean affectSameType = true;

    public boolean isAffectSameType() {
        return affectSameType;
    }

    public boolean doesSwitchPoke() {
        return switchPoke;
    }

    //constructs entire move
    public Move() {
        name = "BASE_NAME";
    }

    public Move(String name) {
        this.name = name;
    }

    public Move(Move move) {
        name = move.name;
        power = move.power;
        accuracy = move.accuracy;
        pp = move.pp;
        maxPP = pp;
        damage = move.damage;
        priority = move.priority;
        type = move.type;
        targetType = move.targetType;
        removeHazards = move.removeHazards;
        removeEnemyHazards = move.removeEnemyHazards;
        weightDependent = move.weightDependent;
        category = move.category;
        resetStats = move.resetStats;
        effectChance = move.effectChance;
        effectType = move.effectType;
        flinchChance = move.flinchChance;
        critChance = move.critChance;
        boostChance = move.boostChance;
        boostType = move.boostType;
        boostStrength = move.boostStrength;
        damageRecover = move.damageRecover;
        healingPercent = move.healingPercent;
        protect = move.protect;
        tSpikes = move.tSpikes;
        spikes = move.spikes;
        rocks = move.rocks;
        leechSeed = move.leechSeed;
        contact = move.contact;
        alwaysHit = move.alwaysHit;
        dance = move.dance;
        sound = move.sound;
        breakprotect = move.breakprotect;
        bypassprotect = move.bypassprotect;
        knockOff = move.knockOff;
        switchPoke = move.switchPoke;
        explode = move.explode;
        fieldEffect = move.fieldEffect;
        affectSameType = move.affectSameType;
        desc = move.desc;
    }

    public Move(String name, int power, double accuracy, int pp, int damage, int priority, int type, int targetType, boolean removeHazards, boolean removeEnemyHazards, boolean weightDependent, int category, boolean resetStats, double effectChance, int effectType, double flinchChance, double critChance, double boostChance, int boostType, int boostStrength, double damageRecover, double healingPercent, boolean protect, boolean tSpikes, boolean spikes, boolean rocks, boolean leechSeed, boolean contact, boolean alwaysHit, boolean dance, boolean sound, boolean breakprotect, boolean bypassprotect, boolean knockOff, boolean switchPoke, boolean explode, boolean fieldEffect, boolean affectSameType, String desc) {
        this.name = name;
        this.power = power;
        this.accuracy = accuracy;
        maxPP = pp;
        this.pp = pp;
        this.damage = damage;
        this.priority = priority;
        this.type = type;
        this.targetType = targetType;
        this.removeHazards = removeHazards;
        this.removeEnemyHazards = removeEnemyHazards;
        this.weightDependent = weightDependent;
        this.category = category;
        this.resetStats = resetStats;
        this.effectChance = effectChance;
        this.effectType = effectType;
        this.flinchChance = flinchChance;
        this.critChance = critChance;
        this.boostChance = boostChance;
        this.boostType = boostType;
        this.boostStrength = boostStrength;
        this.damageRecover = damageRecover;
        this.healingPercent = healingPercent;
        this.protect = protect;
        this.tSpikes = tSpikes;
        this.spikes = spikes;
        this.rocks = rocks;
        this.leechSeed = leechSeed;
        this.contact = contact;
        this.alwaysHit = alwaysHit;
        this.dance = dance;
        this.sound = sound;
        this.breakprotect = breakprotect;
        this.bypassprotect = bypassprotect;
        this.knockOff = knockOff;
        this.switchPoke = switchPoke;
        this.explode = explode;
        this.fieldEffect = fieldEffect;
        this.affectSameType = affectSameType;
        this.desc = desc;
    }


    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public int getPP() {
        return pp;
    }

    public int reducePP() {
        return --pp;
    }

    public int reducePP(int amount) {
        pp -= amount;
        return pp;
    }

    public void restorePP(int amount) {
        pp += amount;
        if(pp > maxPP) pp = maxPP;
    }

    public int getDamage() {
        return damage;
    }

    public int getPriority() {
        return priority;
    }

    public int getType() {
        return type;
    }

    public int getConsecutiveUses() {
        return consecutiveUses;
    }

    public int addConsecutiveUse() {
        if(++consecutiveUses == 7) --consecutiveUses;
        return consecutiveUses;
    }

    public void resetConsecutiveUses() {
        consecutiveUses = 0;
    }

    //to be implemented, need calculations for STAB, etc.
    public double calculateModifier(Team user, Team enemy, int weather) {
        double modifier = .85 + Math.random()*.15;

        if(knockOff) if(enemy.getPokemon(0).getItem() != null) {
            modifier *= 1.5;
            enemy.getPokemon(0).setHeldItem(null);
        }

        if(weightDependent) modifier *= Math.min((int) (user.getPokemon(0).getWeight()) / (int) (enemy.getPokemon(0).getWeight()) > 0 ? ((user.getPokemon(0).getWeight() / enemy.getPokemon(0).getWeight()) + 1) / 2 : 1, 3);

        if(user.getPokemon(0).getType1() == type || user.getPokemon(0).getType2() == type) modifier *= 1.5;
        if(Math.random() < critChance) {
            System.out.println("It's a critical hit!");
            modifier *= 1.5 * (enemy.getPokemon(0).calculateMultiplier(category + 2) > 1 ? enemy.getPokemon(0).calculateMultiplier(category + 2) : 1);
        } else if(enemy.getLightScreen() > 0 && category == 2 || enemy.getReflect() > 0 && category == 0) modifier *= .5;

        if(weather == 1 && type == 4 || weather == 2 && type == 5) modifier *= 1.5;
        else if(weather == 2 && type == 4 || weather == 1 && type ==5) modifier *= .5;

        if(user.getPokemon(0).getStatus() == 4 && category == 0) modifier *=.5;

        double effectivity = Pokedex.TYPE_CHART[type][enemy.getPokemon(0).getType1()] * Pokedex.TYPE_CHART[type][enemy.getPokemon(0).getType2()];
        modifier *= effectivity;

        if(effectivity > 1) {
            enemy.getPokemon(0).getAbility().doSuperEffective(enemy.getPokemon(0), user.getPokemon(0), this);
            System.out.println("It's super effective!");
        } else if(effectivity < 1) {
            System.out.println("It's not very effective...");
        }

        modifier *= user.getPokemon(0).getItem().getModifier(effectivity > 1);

        return modifier;
    }

    public int getCategory() {
        return category;
    }

    //status effects; sleep, para, freeze, poison, badly poisoned
    public double getEffectChance() {
        return effectChance;
    }
    public int getEffectType() {
        return effectType;
    }

    //flinch effects
    public double getFlinchChance() {
        return flinchChance;
    }

    //stat boost effects 1-5 for stats or 6-10 for enemy stats
    public int getBoostType() {
        return boostType;
    }

    public int getBoostStrength() {
        return boostStrength;
    }

    public boolean isContact() {
        return contact;
    }
    public boolean isAlwaysHit() {
        return alwaysHit;
    }

    public boolean isDance() {
        return dance;
    }

    public boolean doesBreakProtect() {
        return breakprotect;
    }

    public boolean doesBypassProtect() {
        return bypassprotect;
    }

    public boolean isSound() {
        return sound;
    }

    public boolean canZ() {
        return zMove;
    }

    public boolean isRocks() {
        return rocks;
    }

    public boolean istSpikes() {
        return tSpikes;
    }

    public boolean isSpikes() {
        return spikes;
    }

    public boolean isLeechSeed() {
        return leechSeed;
    }

    public boolean isProtect() {
        return protect;
    }
    public boolean getExplode() {
        return explode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHealingPercent(int weather) {
        return healingPercent;
    }

    public String toString() {
        return name + ":\n Type: " + Pokedex.typeNames[type] + "\n PP: " + pp + "/" + maxPP;
    }

    public String toStringLong() {
        return name + ": \n Description: " + desc + "\n Power: " + power + "\n PP: " + pp + "/" + maxPP + "\n Accuracy: " + ((int) (100 * accuracy)) + "%\n Type: " + Pokedex.typeNames[type] + "\n Damage: " + damage + "\n Priority: " + priority;
    }
}


//0: null
//1: normal
//2: fighting
//3: grass
//4: fire
//5: water
//6: electric
//7: ice
//8: poison
//9: rock
//10: ground
//11: flying
//12: bug
//13: psychic
//14: ghost
//15: dark
//16: dragon
//17: steel
//18: fairy