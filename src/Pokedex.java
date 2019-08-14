package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import src.team.Pokemon;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Pokedex {
	public final Move[] MOVE_LIST;
	public final Item[] ITEM_LIST;
	public final Ability[] ABILITY_LIST;
	public final Pokemon[] POKEMON_LIST;

	public static final String[] natures = {"Hardy", "Lonely", "Adamant", "Naughty", "Brave", "Bold", "Docile", "Impish", "Lax", "Relaxed", "Modest", "Mild", "Bashful", "Rash", "Quiet", "Calm", "Gentle", "Careful", "Quirky", "Sassy", "Timid", "Hasty", "Jolly", "Naive", "Serious"};
	public static final double[][] natureValues = {
			{1,1,1,1,1,1},
			{1,1.1,.9,1,1,1},
			{1,1.1,1,.9,1,1},
			{1,1.1,1,1,.9,1},
			{1,1.1,1,1,1,.9},
			{1,.9,1.1,1,1,1},
			{1,1,1,1,1,1},
			{1,1,1.1,.9,1,1},
			{1,1,1.1,1,.9,1},
			{1,1,1.1,1,1,.9},
			{1,.9,1,1.1,1,1},
			{1,1,.9,1.1,1,1},
			{1,1,1,1,1,1},
			{1,1,1,1.1,.9,1},
			{1,1,1,1.1,1,.9},
			{1,.9,1,1,1.1,1},
			{1,1,.9,1,1.1,1},
			{1,1,1,.9,1.1,1},
			{1,1,1,1,1,1},
			{1,1,1,1,1.1,.9},
			{1,.9,1,1,1,1.1},
			{1,1,.9,1,1,1.1},
			{1,1,1,.9,1,1.1},
			{1,1,1,1,.9,1.1},
			{1,1,1,1,1,1},
	};

	public static final String[] typeNames = {"", "Normal", "Fighting", "Grass", "Fire", "Water", "Electric", "Ice", "Poison", "Rock", "Ground", "Flying", "Bug", "Psychic", "Ghost", "Dark", "Dragon", "Steel", "Fairy"};
	public static final String[] zMoveNames = {"","","","","","","","","","","","Supersonic Skystrike","","","","","","",""};
	public static final String[] statName = {"Health", "attack", "defense", "special attack", "special defense", "speed", "accuracy", "evasion"};
	public static final String[] shortStatName = {"HP","Atk","Def","SpA","SpD","Spe"};
	public static final String[] boostStrengthText = {"", "", "sharply ", "dramatically ", "dramatically ", "dramatically ", "dramatically"};
	public static final String[] weatherName = {"","sun","rain","sandstorm","hail"};
	public static final String[] weatherMessage = {"","The sunlight is strong!","Rain continues to fall.","The sandstorm is raging.","The hail is crashing down."};
	public static final String[] weatherEntrance = {"The weather cleared up.","The sunlight turned harsh!","It started to rain!","A sandstorm kicked up!","It started to hail!"};
	public static final String[] statusName = {"Healthy", "Asleep", "Paralyzed", "Frozen", "Poisoned", "Badly Poisoned", "Burned", "Fainted"};

	public static final double[][] TYPE_CHART = {
		//  {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18}
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,},//0 null
			{1, 1, 1, 1, 1, 1, 1, 1, 1, .5, 1, 1, 1, 1, 0, 1, 1, .5, 1},//1 normal
			{1, 2, 1, 1, 1, 1, 1, 2, .5, 2, 1, .5, .5, .5, 0, 2, 1, 2, .5},//2 fighting
			{1, 1, 1, .5, .5, 2, 1, 1, .5, 2, 2, .5, .5, 1, 1, .5, .5, 1, 1},//3 grass
			{1, 1, 1, 2, .5, .5, 1, 2, 1, .5, 1, 1, 2, 1, 1, 1, .5, 2, 1},//4 fire
			{1, 1, 1, .5, 2, .5, 1, 1, 1, 2, 2, 1, 1, 1, 1, .5, 1, 1, 1},//5 water
			{1, 1, 1, .5, 1, 2, .5, 1, 1, 1, 0, 2, 1, 1, 1, 1, .5, 1, 1},//6 electric
			{1, 1, 1, 2, .5, .5, 1, .5, 1, 1, 2, 2, 1, 1, 1, 1, 2, .5, 1},//7 ice
			{1, 1, 1, 2, 1, 1, 1, 1, .5, .5, .5, 1, 1, 1, .5, 1, 1, 0, 2},//8 poison
			{1, 1, .5, 1, 2, 1, 1, 2, 1, 1, .5, 2, 2, 1, 1, 1, 1, .5, 1},//9 rock
			{1, 1, 1, .5, 2, 1, 2, 1, 2, 2, 1, 0, .5, 1, 1, 1, 1, 2, 1},//10 ground
			{1, 1, 2, 2, 1, 1, .5, 1, 1, .5, 1, 2, 1, 1, 1, 1, 1, .5, 1},//11 flying
			{1, 1, .5, 2, .5, 1, 1, 1, .5, .5, 1, .5, 1, 2, .5, 2, 1, .5, .5},//12 bug
			{1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, .5, 1, 0, 1, .5, 1},//13 psychic
			{1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, .5, 1, 1, 1},//14 ghost
			{1, 1, .5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, .5, 1, 1, .5},//15 dark
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, .5, 0},//16 dragon
			{1, 1, 1, 1, .5, .5, .5, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, .5, 2},//17 steel
			{1, 1, 2, 1, .5, 1, 1, 1, .5, 1, 1, 1, 1, 1, 1, 2, 2, .5, 1}//18 fairy
	};
	
	public Pokedex() throws ParseException, IOException {
		MOVE_LIST = new Move[742];
		ITEM_LIST = new Item[278];
		ABILITY_LIST = new Ability[233];
		POKEMON_LIST = new Pokemon[878];

		File file = new File("src/MOVE_LIST.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);

		Object obj = new JSONParser().parse(new FileReader("src/Move.json"));
		JSONObject moves = (JSONObject) obj;

		for (int i = 0; i <= 741; i++) {
			String name = br.readLine();
			if (moves.get(name) != null) {
				JSONObject move = (JSONObject) moves.get(name);
				int power = 0;
				double accuracy = 1;
				int pp = 1;
				int damage = 0;
				int priority = 0;
				int type = ((Long) move.get("type")).intValue();

				if (move.get("power") != null) power = ((Long) move.get("power")).intValue();
				if (move.get("accuracy") != null) accuracy = (Double) move.get("accuracy");
				if (move.get("pp") != null) pp = ((Long) move.get("pp")).intValue();
				if (move.get("damage") != null) damage = ((Long) move.get("damage")).intValue();
				if (move.get("priority") != null) priority = ((Long) move.get("priority")).intValue();

				int targetType = 0;

				if (move.get("targetType") != null) targetType = ((Long) move.get("targetType")).intValue();

				boolean removeHazards = false;
				boolean removeEnemyHazards = false;

				if (move.get("removeHazards") != null) removeHazards = true;
				if (move.get("removeEnemyHazards") != null) removeEnemyHazards = true;

				boolean weightDependent = false;

				if (move.get("weightDependent") != null) weightDependent = true;


				int category = ((Long) move.get("category")).intValue();

				boolean resetStats = false;

				if (move.get("resetStats") != null) resetStats = true;

				double effectChance = 0;
				int effectType = 0;

				if (move.get("effectChance") != null) effectChance = ((Double) move.get("effectChance"));
				if (move.get("effectType") != null) effectType = ((Long) move.get("effectType")).intValue();

				double flinchChance = 0;
				double critChance = .0625;

				if (move.get("flinchChance") != null) flinchChance = ((Double) move.get("flinchChance"));
				if (move.get("critChance") != null) critChance = ((Double) move.get("critChance"));

				double boostChance = 0;
				int boostType = 0;
				int boostStrength = 0;

				if (move.get("boostChance") != null) boostChance = ((Double) move.get("boostChance"));
				if (move.get("boostType") != null) boostType = ((Long) move.get("boostType")).intValue();
				if (move.get("boostStrength") != null) boostStrength = ((Long) move.get("boostStrength")).intValue();

				double damageRecover = 0;
				double healingPercent = 0;

				if (move.get("damageRecover") != null) damageRecover = ((Double) move.get("damageRecover"));
				if (move.get("healingPercent") != null) healingPercent = ((Double) move.get("healingPercent"));

				boolean protect = false;

				if (move.get("protect") != null) protect = true;

				boolean tSpikes = false;
				boolean spikes = false;
				boolean rocks = false;
				boolean leechSeed = false;

				if (move.get("tSpikes") != null) tSpikes = true;
				if (move.get("spikes") != null) spikes = true;
				if (move.get("rocks") != null) rocks = true;
				if (move.get("leechSeed") != null) leechSeed = true;


				boolean contact = false;
				boolean alwaysHit = false;
				boolean dance = false;
				boolean sound = false;
				boolean breakprotect = false;
				boolean bypassprotect = false;

				if (move.get("contact") != null) contact = true;
				if (move.get("alwaysHit") != null) alwaysHit = true;
				if (move.get("dance") != null) dance = true;
				if (move.get("sound") != null) sound = true;
				if (move.get("breakprotect") != null) breakprotect = true;
				if (move.get("bypassprotect") != null) bypassprotect = true;

				boolean knockOff = false;
				boolean switchPoke = false;
				boolean explode = false;
				boolean fieldEffect = false;
				boolean affectSameType = true;

				if (move.get("knockOff") != null) knockOff = true;
				if (move.get("switchPoke") != null) switchPoke = true;
				if (move.get("explode") != null) explode = true;
				if (move.get("fieldEffect") != null) fieldEffect = true;
				if (move.get("rocks") != null) rocks = true;
				if (move.get("affectSameType") != null) affectSameType = false;


				String desc = "";

				if (move.get("desc") != null) desc = ((String) move.get("desc"));

				MOVE_LIST[i] = new Move(name, power, accuracy, pp, damage, priority, type, targetType, removeHazards, removeEnemyHazards, weightDependent, category, resetStats, effectChance, effectType, flinchChance, critChance, boostChance, boostType, boostStrength, damageRecover, healingPercent, protect, tSpikes, spikes, rocks, leechSeed, contact, alwaysHit, dance, sound, breakprotect, bypassprotect, knockOff, switchPoke, explode, fieldEffect, affectSameType, desc);
			} else MOVE_LIST[i] = new Move(name);
		}

		fr.close();
		br.close();

		file = new File("src/ABILITIES_LIST.txt");
		fr = new FileReader(file);
		br = new BufferedReader(fr);

		obj = new JSONParser().parse(new FileReader("src/Abilities.json"));
		JSONObject abilities = (JSONObject) obj;

		for (int i = 0; i <= 232; i++) {
			String name = br.readLine();
			if (abilities.get(name) != null) {
				JSONObject ability = (JSONObject) abilities.get(name);

				boolean onSwitch = false;
				boolean onEntrance = false;
				boolean onKill = false;
				boolean onAttack = false;
				boolean onDefend = false;
				boolean onAfterTurn = false;
				boolean onCrit = false;
				boolean onSuperEffective = false;
				boolean onContact = false;

				boolean affectOpponent = false;

				boolean boost = false;
				int boostStrength = 0;
				int boostedStat = 0;
				int boostedType = 0;

				double statMultiplier = 1;

				boolean healing = false;
				double healingPercent = 0.0;

				boolean ignoreEnemy = false;

				boolean setWeather = false;
				int weatherCreated = 0;

				boolean removeIndirectDamage = false;
				double contactDamage = 0.0;

				boolean changeType = false;
				boolean transformation = false;

				int immune = 0;

				int[] addBaseStats = {0, 0, 0, 0, 0, 0};

				boolean activated = false;
				boolean poisonHeal = false;
				boolean trap = false;

				int activeType = 0;

				boolean needWeather = false;
				int activeWeather = 0;

				double reduceDamage = 1;

				boolean unaware = false;

				double healStatus = 0.0;
				boolean pressure = false;
				boolean copyEnemyAbility = false;

				String desc = "";

				if (ability.get("onSwitch") != null) onSwitch = true;
				if (ability.get("onEntrance") != null) onEntrance = true;
				if (ability.get("onKill") != null) onKill = true;
				if (ability.get("onAttack") != null) onAttack = true;
				if (ability.get("onDefend") != null) onDefend = true;
				if (ability.get("onAfterTurn") != null) onAfterTurn = true;
				if (ability.get("onCrit") != null) onCrit = true;
				if (ability.get("onSuperEffective") != null) onSuperEffective = true;
				if (ability.get("onContact") != null) onContact = true;

				if (ability.get("affectOpponent") != null) affectOpponent = true;

				if (ability.get("boost") != null) boost = true;
				if (ability.get("boostStrength") != null)
					boostStrength = ((Long) ability.get("boostStrength")).intValue();
				if (ability.get("boostedStat") != null) boostedStat = ((Long) ability.get("boostedStat")).intValue();
				if (ability.get("boostedType") != null) boostedType = ((Long) ability.get("boostedType")).intValue();

				if (ability.get("statMultiplier") != null) statMultiplier = ((Double) ability.get("statMultiplier"));

				if (ability.get("healing") != null) healing = true;
				if (ability.get("healingPercent") != null) healingPercent = ((Double) ability.get("healingPercent"));

				if (ability.get("ignoreEnemy") != null) ignoreEnemy = true;

				if (ability.get("setWeather") != null) setWeather = true;
				if (ability.get("weatherCreated") != null)
					weatherCreated = ((Long) ability.get("weatherCreated")).intValue();

				if (ability.get("removeIndirectDamage") != null) removeIndirectDamage = true;
				if (ability.get("contactDamage") != null) contactDamage = ((Double) ability.get("contactDamage"));

				if (ability.get("changeType") != null) changeType = true;
				if (ability.get("transformation") != null) transformation = true;

				if (ability.get("immune") != null) immune = ((Long) ability.get("immune")).intValue();

				if (ability.get("addBaseStats") != null) {
					for (int j = 0; j < 6; j++) {
						addBaseStats[j] = ((Long) ((JSONArray) ability.get("addBaseStats")).get(j)).intValue();
					}
				}

				if (ability.get("activated") != null) activated = true;
				if (ability.get("poisonHeal") != null) poisonHeal = true;
				if (ability.get("trap") != null) trap = true;

				if (ability.get("activeType") != null) activeType = ((Long) ability.get("activeType")).intValue();

				if (ability.get("needWeather") != null) needWeather = true;
				if (ability.get("activeWeather") != null)
					activeWeather = ((Long) ability.get("activeWeather")).intValue();

				if (ability.get("reduceDamage") != null) reduceDamage = ((Double) ability.get("reduceDamage"));

				if (ability.get("unaware") != null) unaware = true;

				if (ability.get("healStatus") != null) healStatus = ((Double) ability.get("healStatus"));
				if (ability.get("pressure") != null) pressure = true;
				if (ability.get("copyEnemyAbility") != null) copyEnemyAbility = true;

				if (ability.get("desc") != null) desc = ((String) ability.get("desc"));

				ABILITY_LIST[i] = new Ability(name, onSwitch, onEntrance, onKill, onAttack, onDefend, onAfterTurn, onCrit, onSuperEffective, onContact, affectOpponent, boost, boostStrength, boostedStat, boostedType, statMultiplier, healing, healingPercent, ignoreEnemy, setWeather, weatherCreated, removeIndirectDamage, contactDamage, changeType, transformation, immune, addBaseStats, activated, poisonHeal, trap, activeType, needWeather, activeWeather, reduceDamage, unaware, healStatus, pressure, copyEnemyAbility, desc);
			} else ABILITY_LIST[i] = new Ability(name);
		}


		fr.close();
		br.close();

		file = new File("src/ITEM_LIST.txt");
		fr = new FileReader(file);
		br = new BufferedReader(fr);

		obj = new JSONParser().parse(new FileReader("src/Items.json"));
		JSONObject items = (JSONObject) obj;

		for (int i = 0; i <= 277; i++) {
			String name = br.readLine();
			if (items.get(name) != null) {
				JSONObject item = (JSONObject) items.get(name);

				boolean megaStone = false;

				int[] megaStats = {0, 0, 0, 0, 0, 0};
				Ability megaAbility = new Ability();

				boolean onSwitch = false;
				boolean onEntrance = false;
				boolean onKill = false;
				boolean onAttack = false;
				boolean onDefend = false;
				boolean afterDefense = false;
				boolean onTurnEnd = false;
				boolean onCrit = false;
				boolean onSuperEffective = false;
				boolean onTakeDamage = false;

				boolean zStone = false;
				int zType = 0;
				String zPokeName = "";

				boolean healing = false;
				double healingPercent = 0;

				double activeThreshold = 0;

				boolean choice = false;

				int stat = 0;
				double statMultiplier = 1;

				double damageMultiplier = 1;

				int neededType = 0;

				boolean berry = false;
				boolean consumed = false;

				boolean disableStatusMoves = false;

				String desc = "";

				if (item.get("megaStone") != null) megaStone = true;

				if (item.get("megaStats") != null) {
					for (int j = 0; j < 6; j++) {
						megaStats[j] = ((Long) ((JSONArray) item.get("megaStats")).get(j)).intValue();
					}
				}
				if (item.get("megaAbility") != null) {
					for (Ability ability : ABILITY_LIST) {
						if (ability.getName().equalsIgnoreCase((String) item.get("megaAbility"))) {
							megaAbility = ability;
							break;
						}
					}
				}

				if (item.get("onSwitch") != null) onSwitch = true;
				if (item.get("onEntrance") != null) onEntrance = true;
				if (item.get("onKill") != null) onKill = true;
				if (item.get("onAttack") != null) onAttack = true;
				if (item.get("onDefend") != null) onDefend = true;
				if (item.get("afterDefense") != null) afterDefense = true;
				if (item.get("onTurnEnd") != null) onTurnEnd = true;
				if (item.get("onCrit") != null) onCrit = true;
				if (item.get("onSuperEffective") != null) onSuperEffective = true;
				if (item.get("onTakeDamage") != null) onTakeDamage = true;

				if (item.get("zStone") != null) zStone = true;
				if (item.get("zType") != null) zType = ((Long) item.get("zType")).intValue();
				if (item.get("zPokeName") != null) zPokeName = ((String) item.get("zPokeName"));

				if (item.get("healing") != null) healing = true;
				if (item.get("healingPercent") != null) healingPercent = ((Double) item.get("healingPercent"));

				if (item.get("activeThreshold") != null) activeThreshold = ((Double) item.get("activeThreshold"));

				if (item.get("choice") != null) choice = true;

				if (item.get("stat") != null) stat = ((Long) item.get("stat")).intValue();
				if (item.get("statMultiplier") != null) statMultiplier = ((Double) item.get("statMultiplier"));

				if (item.get("damageMultiplier") != null) damageMultiplier = ((Double) item.get("damageMultiplier"));

				if (item.get("neededType") != null) neededType = ((Long) item.get("neededType")).intValue();

				if (item.get("berry") != null) berry = true;
				if (item.get("consumed") != null) consumed = true;

				if (item.get("disableStatusMoves") != null) disableStatusMoves = true;

				if (item.get("desc") != null) desc = ((String) item.get("desc"));

				ITEM_LIST[i] = new Item(name, megaStone, megaStats, megaAbility, onSwitch, onEntrance, onKill, onAttack, onDefend, afterDefense, onTurnEnd, onCrit, onSuperEffective, onTakeDamage, zStone, zType, zPokeName, healing, healingPercent, activeThreshold, choice, stat, statMultiplier, damageMultiplier, neededType, berry, consumed, disableStatusMoves, desc);

			} else ITEM_LIST[i] = new Item(name);
		}

		fr.close();
		br.close();

		file = new File("src/POKEMON_LIST.txt");
		fr = new FileReader(file);
		br = new BufferedReader(fr);

		obj = new JSONParser().parse(new FileReader("src/Pokemon.json"));
		JSONObject pokemon = (JSONObject) obj;

		for (int i = 0; i <= 877; i++) {
			String name = br.readLine();
			JSONObject poke = (JSONObject) pokemon.get(name);

			int type1 = ((Long) poke.get("type1")).intValue();
			int type2 = poke.get("type2") == null ? 0 : ((Long) poke.get("type2")).intValue();

			int[] stats = new int[6];
			for (int j = 0; j < 6; j++) {
				stats[j] = ((Long) ((JSONArray) poke.get("stats")).get(j)).intValue();
			}

			double weight = 0;
			if(poke.get("weight") != null) weight = ((Double) poke.get("weight"));

			POKEMON_LIST[i] = new Pokemon(name, type1, type2, stats, weight);
		}
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
