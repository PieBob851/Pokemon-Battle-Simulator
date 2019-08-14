package src;

import src.team.Pokemon;
import src.battle.Battle;

public class Ability {
    private String name;
	//activation type
	private boolean onSwitch;
	private boolean onEntrance;
	private boolean onKill;
	private boolean onAttack;
	private boolean onDefend;
	private boolean onAfterTurn;
	private boolean onCrit;
	private boolean onSuperEffective;
	private boolean onContact;
	//changes target from self to opponent
	private boolean affectOpponent;
	//boosts
	private boolean boost;
	//for boosting stats
	private int boostStrength;
	private int boostedStat;
	//for boosting moves of a type
	private int boostedType;
	//changes actual stats, i.e. Pure Power
	private double statMultiplier = 1;
	//healing abilities
	private boolean healing;
	private double healingPercent;
	//ignore enemy ability during activation effect
	private boolean ignoreEnemy;
	//0 is default, 1 is sun, 2 is rain, 3 is sand, 4 is hail
	private boolean setWeather;
	private int weatherCreated;

	//disables this pokemon's ability, i.e. affected by Mold Breaker
	private boolean disabled;
	//removes indirect damage, calculated upon taking indirect damage
	private boolean removeIndirectDamage;
	//amount of contact damage, calculated upon being contacted
	private double contactDamage;
	//changes Pokemon's type, i.e. Protean
	private boolean changeType;
	//makes immune to type of value immune

	private boolean transformation;

	private int immune;
	//for recalculating base stats after transformation
	private int[] addBaseStats;
	//for abilities that become activated once
	private boolean activated;

	private boolean poisonHeal;

	private boolean trap;
	//type (fire, water) needed to activate
	private int activeType;

	private boolean needWeather;
	private int activeWeather;
	//for damage reduction abilities giving resistances
	private double reduceDamage = 1;

	//active on supereffective hit

	private boolean unaware;

	private double healStatus;

	private boolean pressure;

	private boolean copyEnemyAbility;

	private String desc;

    public int getBoostedStat() {
        return boostedStat;
    }

    public Ability() {
        name = "BASE_ABILITY";
    }

    public Ability(String name) {
    	this.name = name;
	}

	public Ability(String name, boolean onSwitch, boolean onEntrance, boolean onKill, boolean onAttack, boolean onDefend, boolean onAfterTurn, boolean onCrit, boolean onSuperEffective, boolean onContact, boolean affectOpponent, boolean boost, int boostStrength, int boostedStat, int boostedType, double statMultiplier, boolean healing, double healingPercent, boolean ignoreEnemy, boolean setWeather, int weatherCreated, boolean removeIndirectDamage, double contactDamage, boolean changeType, boolean transformation, int immune, int[] addBaseStats, boolean activated, boolean poisonHeal, boolean trap, int activeType, boolean needWeather, int activeWeather, double reduceDamage, boolean unaware, double healStatus, boolean pressure, boolean copyEnemyAbility, String desc) {
		this.name = name;
		this.onSwitch = onSwitch;
		this.onEntrance = onEntrance;
		this.onKill = onKill;
		this.onAttack = onAttack;
		this.onDefend = onDefend;
		this.onAfterTurn = onAfterTurn;
		this.onCrit = onCrit;
		this.onSuperEffective = onSuperEffective;
		this.onContact = onContact;
		this.affectOpponent = affectOpponent;
		this.boost = boost;
		this.boostStrength = boostStrength;
		this.boostedStat = boostedStat;
		this.boostedType = boostedType;
		this.statMultiplier = statMultiplier;
		this.healing = healing;
		this.healingPercent = healingPercent;
		this.ignoreEnemy = ignoreEnemy;
		this.setWeather = setWeather;
		this.weatherCreated = weatherCreated;
		this.removeIndirectDamage = removeIndirectDamage;
		this.contactDamage = contactDamage;
		this.changeType = changeType;
		this.transformation = transformation;
		this.immune = immune;
		this.addBaseStats = addBaseStats;
		this.activated = activated;
		this.poisonHeal = poisonHeal;
		this.trap = trap;
		this.activeType = activeType;
		this.needWeather = needWeather;
		this.activeWeather = activeWeather;
		this.reduceDamage = reduceDamage;
		this.unaware = unaware;
		this.healStatus = healStatus;
		this.pressure = pressure;
		this.copyEnemyAbility = copyEnemyAbility;
		this.desc = desc;
	}

	public Ability(Ability ability) {
		name = ability.name;
		onSwitch = ability.onSwitch;
		onEntrance = ability.onEntrance;
		onKill = ability.onKill;
		onAttack = ability.onAttack;
		onDefend = ability.onDefend;
		onAfterTurn = ability.onAfterTurn;
		onCrit = ability.onCrit;
		onSuperEffective = ability.onSuperEffective;
		onContact = ability.onContact;
		affectOpponent = ability.affectOpponent;
		boost = ability.boost;
		boostStrength = ability.boostStrength;
		boostedStat = ability.boostedStat;
		boostedType = ability.boostedType;
		statMultiplier = ability.statMultiplier;
		healing = ability.healing;
		healingPercent = ability.healingPercent;
		ignoreEnemy = ability.ignoreEnemy;
		setWeather = ability.setWeather;
		weatherCreated = ability.weatherCreated;
		removeIndirectDamage = ability.removeIndirectDamage;
		contactDamage = ability.contactDamage;
		changeType = ability.changeType;
		transformation = ability.transformation;
		immune = ability.immune;
		addBaseStats = ability.addBaseStats;
		activated = ability.activated;
		poisonHeal = ability.poisonHeal;
		trap = ability.trap;
		activeType = ability.activeType;
		needWeather = ability.needWeather;
		activeWeather = ability.activeWeather;
		reduceDamage = ability.reduceDamage;
		unaware = ability.unaware;
		healStatus = ability.healStatus;
		pressure = ability.pressure;
		copyEnemyAbility = ability.copyEnemyAbility;
		desc = ability.desc;
	}

	public boolean ignoresEnemy() {
		return ignoreEnemy;
	}

	//when ability is suppressed (i.e. mold breaker or core enforcer)
	public void setDisabled(boolean state) {
		disabled = state;
	}

	public boolean removesIndirectDamage() {
		return removeIndirectDamage;
	}

	public int calculateContactDamage(Pokemon poke) {
		return (int) (poke.getMaxHP() * contactDamage);
	}

	public void doKillEffects(Pokemon ally, Pokemon enemy) {
		if(onKill) {
			if(boost) {
				int boostedStat = this.boostedStat;
				if(boostedStat == 8)  boostedStat = ally.getHighestStat();
				System.out.println(ally.getTeam().getPlayer().getName() + " boosted its " + Pokedex.statName[boostedStat] + " with " + ally.getName() + ".");
				ally.addBoost(boostedStat, boostStrength);
				return;
			}
		} else if(transformation && !activated) {
			ally.setBaseStats(addBaseStats);
			activated = true;
		}

	}

	public void doSwitchEffects(Pokemon poke) {
		if(onSwitch) {
			if(healing) {
				poke.restoreHP((int) (poke.getMaxHP()*healingPercent));
				return;
			} else if(boostedType!=0) boost = false;
			else if(healStatus > 0) {
				if(healStatus > Math.random()) poke.setStatus(0);
			}

			activated = false;
		}
	}

	public void doEntranceEffects(Pokemon ally, Pokemon enemy, Battle battle) {
		if(onEntrance) {
			if(boost) {
				if(affectOpponent) {
					enemy.addBoost(boostedStat, boostStrength);
					return;
				}
			} if(setWeather) {
				battle.setWeather(weatherCreated);
			} if(copyEnemyAbility) {
				ally.setAbility(enemy.getAbility());
				System.out.println("(" + ally.getName() + "'s Trace)\n" + ally.getName() + " changed its ability to " + enemy.getAbility() + ".");
			}
		}
	}

	public void doAttackEffects(Pokemon ally, Pokemon enemy, Move moveUsed) {
		if(onAttack) {
			if(changeType) {
				System.out.println(ally.getName() + " changed its type to " + Pokedex.typeNames[moveUsed.getType()] + ".");
				ally.changeType(moveUsed.getType());
			}
		}
	}
	//
	public void doDefendEffects(Pokemon attacker, Pokemon defender, Move moveUsed) {
		if(onDefend) {
			if(moveUsed.getType() == immune) {
				if (boostedType != 0) {
					boost = true;
					activated = true;
				}
			}
		}
	}

	public void doContact(Pokemon attacker, Pokemon defender) {
	    if(onContact) {
	        if(contactDamage > 0) {
	        	System.out.println(attacker.getName() + " was hurt by " + defender.getName() + "'s " + name + ".");
	        	attacker.damage((int)(attacker.getMaxHP() * contactDamage), defender);
			}
        }
    }

	public boolean getUnaware() {
		return unaware;
	}

	public boolean isPoisonHeal() {
        return true;
    }

	public void doAfterTurn(Pokemon ally, Pokemon enemy, int weather) {
		if(onAfterTurn) {
			if(poisonHeal && ally.getStatus() == 3) {
				ally.restoreHP((int) (ally.getMaxHP() * .125));
			} else if(trap) {
				if((activeType == enemy.getType1() || activeType == enemy.getType2()) || activated) {
					enemy.setTrapped(true);
					return;
				}
			} else if(needWeather) {
				if (boostedStat > 0) {
					if(activeWeather == weather) {
						statMultiplier = 2;
						return;
					} else {
						statMultiplier = 1;
						return;
					}
				}
			}
		}
	}

	public void doSuperEffective(Pokemon defender, Pokemon attacker, Move moveUsed) {
		if(onSuperEffective) {
			moveUsed.setPower((int) (moveUsed.getPower() * reduceDamage));
		}
	}
	//for checking immunity
	public int getImmune() {
		return immune;
	}

	public double getStatMultiplier() {
		return statMultiplier;
	}

	public boolean getPressure() {
		return pressure;
	}

	public String toString() {
    	return name + ": \n Description: " + desc;
	}

	public String getName() {
    	return name;
	}


}