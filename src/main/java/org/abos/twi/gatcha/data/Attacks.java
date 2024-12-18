package org.abos.twi.gatcha.data;

import org.abos.twi.gatcha.core.battle.Attack;
import org.abos.twi.gatcha.core.battle.TeamKind;
import org.abos.twi.gatcha.core.effect.ApplicableEffect;
import org.abos.twi.gatcha.core.effect.EffectType;

import java.util.Collections;
import java.util.List;

public interface Attacks {

    Attack WEAK_PUNCH = new Attack(
            "Weak Punch",
            "This punch doesn't have much strength to it.",
            1, 1, 1,
            List.of(new ApplicableEffect(EffectType.DAMAGE_BLUNT, 1, 0, 0, 0, 1d, null, null, null)));

    Attack MEDIUM_PUNCH = new Attack(
            "Medium Punch",
            "Punch like an average person.",
            1, 1, 1,
            List.of(new ApplicableEffect(EffectType.DAMAGE_BLUNT, 2, 0, 0, 0, 1d, null, null, null)));

    Attack MINOTAUR_PUNCH = new Attack(
            "[Minotaur Punch]",
            "Punch like a Minotaur.",
            1, 1, 1,
            List.of(new ApplicableEffect(EffectType.DAMAGE_BLUNT, 3, 0, 0, 0, 1d, null, null, null)));

    Attack UNERRING_KNIFE_THROW = new Attack(
            "Unerring Knife Throw",
            "[Unerring Throw] with a knife.",
            2, 4, 2,
            List.of(new ApplicableEffect(EffectType.DAMAGE_SLASH, 2, 0, 0, 0, 1d, null, null, null)));

    Attack PASTA = new Attack(
            "Erin's lunch special",
            "Pasta with a glass of Blue Fruit Juice.",
            1, 1, 2,
            List.of(new ApplicableEffect(EffectType.HEALING, 2, 0, 0, 0, 1d, null, null, null)));

    Attack QUICK_SLASH = new Attack(
            "Quick Slash",
            "Quick slash with a sword or something similar.",
            1, 1, 2,
            List.of(new ApplicableEffect(EffectType.DAMAGE_SLASH, 3, 0, 0, 0, 1d, null, null, null)));

    Attack ARROW = new Attack(
            "Shoot Arrow",
            "Shoot a regular arrow at your target.",
            2, 6, 3,
            List.of(new ApplicableEffect(EffectType.DAMAGE_PIERCE, 3, 0, 0, 0, 1d, null, null, null)));

    Attack BONE_DART = new Attack(
            "[Bone Dart]",
            "Shoot a bit of sharp bone at your target.",
            2, 4, 1,
            List.of(new ApplicableEffect(EffectType.DAMAGE_SLASH, 2, 0, 0, 0, 1d, null, null, null)));

    Attack BONE_FRACTURE = new Attack(
            "Bone Fracture",
            "Directly damages the bones, bypassing armor.",
            1, 1, 5,
            List.of(new ApplicableEffect(EffectType.DAMAGE_DEATH, 5, 0, 0, 0, 1d, null, null, null)));

    Attack DEATH_BOLT = new Attack(
            "[Death Bolt]",
            "Fires a bolt of potent death mana, dealing minor damage but bypassing the opponents defense and armor.",
            2, 4, 1,
            List.of(new ApplicableEffect(EffectType.DAMAGE_DEATH, 10, 0, 0, 0, 1d, null, null, null)));

    Attack ICE_SHARD = new Attack(
            "[Ice Shard]",
            "Shoot a frozen bit of water at your target.",
            2, 4, 1,
            List.of(new ApplicableEffect(EffectType.DAMAGE_SLASH, 1, 0, 0, 0, 1d, null, null, null),
                    new ApplicableEffect(EffectType.DAMAGE_FROST, 1, 0, 0, 0, 1d, null, null, null)));

    Attack FROST_ARMOR = new Attack(
            "Frost Armor",
            "Cover yourself in a layer of ice.",
            0, 0, 5,
            List.of(new ApplicableEffect(EffectType.BUFF_DEFENSE, 5, 3, 0, 0, 1d, null, null, null)));

    Attack FROZEN_WIND = new Attack(
            "Frozen Wind",
            "Shoot cold wind at your targets.",
            2, 4, 5,
            List.of(new ApplicableEffect(EffectType.DAMAGE_FROST, 2, 0, 1, 0, 1d, null, null, null),
                    new ApplicableEffect(EffectType.DEBUFF_SPEED, 2, 3, 1, 0, 1d, null, null, null)));

    Attack ICE_SPRAY = new Attack(
            "Ice Spray",
            "Shot a spray of ice at your target",
            1, 3, 1,
            List.of(new ApplicableEffect(EffectType.DAMAGE_SLASH, 1, 0, 1, 0, 1d, null, null, null),
                    new ApplicableEffect(EffectType.DAMAGE_FROST, 1, 0, 1, 0, 1d, null, null, null)));

    Attack ICE_SPIKE = new Attack(
            "[Ice Spike]",
            "Shoot a sharp piece of ice at your target.",
            2, 5, 2,
            List.of(new ApplicableEffect(EffectType.DAMAGE_PIERCE, 3, 0, 0, 0, 1d, null, null, null),
                    new ApplicableEffect(EffectType.DAMAGE_FROST, 1, 0, 0, 0, 1d, null, null, null)));

    Attack BATTLEFIELD_OF_THE_FROZEN_WORLD = new Attack(
            "Battlefield of the Frozen World",
            "Freezes the entire battlefield.",
            0, 0, 6,
            List.of(new ApplicableEffect(EffectType.DAMAGE_FROST, 5, 0, Integer.MAX_VALUE, 0, 1d, null, null, List.of(TeamKind.ENEMY)),
                    new ApplicableEffect(EffectType.DEBUFF_SPEED, 5, 4, Integer.MAX_VALUE, 0, 1d, null, null, List.of(TeamKind.ENEMY))));

    Attack QUICK_MOVEMENT = new Attack(
            "Quick Movement",
            "Be faster for a while.",
            0, 0, 5,
            List.of(new ApplicableEffect(EffectType.BUFF_SPEED, 7, 3, 0, 0, 1d, null, null, null)));

    Attack SIDE_STEP = new Attack(
            "Side Step",
            "Dodge the next enemy attack.",
            0, 0, 5,
            List.of(new ApplicableEffect(EffectType.INVULNERABILITY, 0, 3, 0, 0, 1d, null, null, null)));

    Attack IGNORE_PAIN = new Attack(
            "Ignore Pain",
            "Just ignore it!",
            0, 0, 5,
            List.of(new ApplicableEffect(EffectType.BUFF_HEALTH, 15, 3, 0, 0, 1d, null, null, null)));

    Attack KEEN_EDGE = new Attack(
            "Keen Edge",
            "Use the sharp end of your weapon.",
            0, 0, 5,
            List.of(new ApplicableEffect(EffectType.BUFF_ATTACK, 5, 3, 0, 0, 1d, null, null, null)));

    Attack AF_DUELIST = new Attack(
            "Armform (Duelist)",
            "Transforms the arms into blades and attacks with it.",
            1, 1, 1,
            List.of(new ApplicableEffect(EffectType.DAMAGE_SLASH, 2, 0, 0, 0, 1d, null, null, null)));

    Attack AF_TELESCOPING = new Attack(
            "Armform (Telescoping Flesh)",
            "Elongates an arm to pierce an opponent.",
            1, 3, 2,
            List.of(new ApplicableEffect(EffectType.DAMAGE_PIERCE, 3, 0, 0, 0, 1d, null, null, null)));

    Attack AF_RAZORKIND = new Attack(
            "Armform (Razorkind)",
            "Makes one arm extra sharp to inflict bleeding damage.",
            1, 1, 4,
            List.of(new ApplicableEffect(EffectType.DAMAGE_SLASH, 4, 0, 0, 0, 1d, null, null, null),
                    new ApplicableEffect(EffectType.BLEED, 3, 4, 0, 0, 1d, null, null, null)));

    Attack KARATE_KICK = new Attack(
            "Karate Kick",
            "Delivers a powerful kick aimed at taking down a target.",
            1, 1, 1,
            List.of(new ApplicableEffect(EffectType.DAMAGE_BLUNT, 3, 0, 0, 0, 1d, null, null, null),
                    new ApplicableEffect(EffectType.STUN, 0, 1, 0, 0, 0.1, null, null, null)));

    Attack FLASHBANG = new Attack(
            "Flashbang",
            "Use light and sound to temporarily blind and disorient enemies.",
            1, 1, 2,
            List.of(new ApplicableEffect(EffectType.DAMAGE_SOUND, 2, 0, 1, 0, 1d, null, null, List.of(TeamKind.ENEMY)),
                    new ApplicableEffect(EffectType.LOWER_ACCURACY, 10, 2, 1, 0, 1d, null, null, List.of(TeamKind.ENEMY))));

    Attack TRIPVINE_BAG = new Attack(
            "Tripvine Bag",
            "A small bag filled with tripvines to entangle people.",
            1, 3, 3,
            List.of(new ApplicableEffect(EffectType.DAMAGE_BLUNT, 1, 0, 1, 0, 1d, null, null, null),
                    new ApplicableEffect(EffectType.DEBUFF_SPEED, 10, 2, 1, 0, 1d, null, null, null)));

    Attack FAST_RELOADING_CROSSBOW_SHOT = new Attack(
            "Crossbow Shot",
            "A shoot from a preloaded crossbow.",
            2, 5, 1,
            List.of(new ApplicableEffect(EffectType.DAMAGE_PIERCE, 3, 0, 0, 0, 1d, null, null, null)));

    Attack THREEFOLD_SHOT = new Attack(
            "[Threefold Shot]",
            "A triple shot from a crossbow.",
            2, 5, 3,
            Collections.nCopies(3, new ApplicableEffect(EffectType.DAMAGE_PIERCE, 3, 0, 0, 0, 1d, null, null, null)));

    Attack KNUCKLES_OF_IRON = new Attack(
            "Knuckles of Iron",
            "A Beatdown Combo",
            1, 1, 6,
            Collections.nCopies(6, new ApplicableEffect(EffectType.DAMAGE_BLUNT, 2, 0, 0, 0, 1d, null, null, null)));

    Attack INVISIBILITY = new Attack(
            "[Invisibility]",
            "Become invisible.",
            0, 0, 5,
            List.of(new ApplicableEffect(EffectType.INVISIBILITY, 0, 4, 0, 0, 1d, null, null, null)));

    Attack HIDE = new Attack(
            "Hide",
            "Hide if you can.",
            0, 0, 5,
            List.of(new ApplicableEffect(EffectType.INVISIBILITY, 0, 2, 0, 0, 0.5d, null, null, null)));

    Attack COMPLAIN = new Attack(
            "Complain",
            "Complain in battle.",
            0, 0, 2,
            List.of(new ApplicableEffect(EffectType.ANNOY, 0, 3, 1, 0, 1d, null, null, null)));

    Attack RELC_PUNCH = new Attack(
            "[Relc Punch]",
            "Punch like a Relc.",
            1, 1, 1,
            List.of(new ApplicableEffect(EffectType.DAMAGE_BLUNT, 2, 0, 0, 0, 1d, null, null, null)));

    Attack RELC_HEADBUTT = new Attack(
            "[Relc Headbutt]",
            "Headbutt like a Relc.",
            1, 1, 2,
            List.of(new ApplicableEffect(EffectType.DAMAGE_BLUNT, 4, 0, 0, 0, 1d, null, null, null)));

    Attack TRIPLE_THRUST = new Attack(
            "[Triple Thrust]",
            "Attack three times in a row.",
            1, 2, 3,
            Collections.nCopies(3, new ApplicableEffect(EffectType.DAMAGE_PIERCE, 3, 0, 0, 0, 1d, null, null, null)));

    Attack WIDE_SWEEP = new Attack(
            "Wide Sweep",
            "Attacks all enemies in range.",
            0, 0, 1,
            List.of(new ApplicableEffect(EffectType.DAMAGE_SLASH, 2, 0, 1, 0, 1d, null, null, List.of(TeamKind.ENEMY))));

    Attack ARMOR_PIERCING_BLOW = new Attack(
            "Armor Piercing Blow",
            "A quick and deliberate low strike aimed at bypassing heavy defenses.",
            1, 1, 3,
            List.of(new ApplicableEffect(EffectType.DAMAGE_ARMOR_PIERCE, 3, 0, 0, 0, 1d, null, null, null)));

    Attack ANTI_MAGIC_SLASH = new Attack(
            "Anti-magic Slash",
            "A precise strike that disrupts magic.",
            1, 1, 4,
            List.of(new ApplicableEffect(EffectType.DAMAGE_SLASH, 4, 0, 0, 0, 1d, null, null, null),
                    new ApplicableEffect(EffectType.DISPEL, 0, 0, 0, 0, 0.8, null, null, null)));

    Attack ANTI_MAGIC_CLAW = new Attack(
            "Anti-magic Claw",
            "A magic disrupting claw strike.",
            1, 1, 1,
            List.of(new ApplicableEffect(EffectType.DAMAGE_SLASH, 3, 0, 0, 0, 1d, null, null, null),
                    new ApplicableEffect(EffectType.DISPEL, 0, 0, 0, 0, 0.8, null, null, null)));

    Attack TAIL_SLAP = new Attack(
            "Tail Slap",
            "Slap the enemy in the face with a tail, stunning them for a moment.",
            1, 1, 3,
            List.of(new ApplicableEffect(EffectType.DAMAGE_BLUNT, 1, 0, 0, 0, 1d, null, null, null),
                    new ApplicableEffect(EffectType.STUN, 0, 2, 0, 0, 1d, null, null, null)));

    Attack TIDEBREAKER = new Attack(
            "Tidebreaker",
            "Increases the defense of nearby allies.",
            0, 0, 5,
            List.of(new ApplicableEffect(EffectType.BUFF_DEFENSE, 5, 3, 3, 0, 1d, null, null, List.of(TeamKind.PLAYER, TeamKind.ALLY))));
    Attack CRELER_BITE = new Attack(
            "Creler Bite",
            "A poisonous bite.",
            1, 1, 1,
            List.of(new ApplicableEffect(EffectType.DAMAGE_PIERCE, 1, 0, 0, 0, 1d, null, null, null)

                    ,  new ApplicableEffect(EffectType.POISON, 1, 2, 0, 0, 0.3d, null, null, null)
            ));
    Attack CHITIN_CHARGE = new Attack(
            "Chitin Charge",
            "The Baby Creler charges and scrapes its sharp chitinous legs against a target, leaving painful scratches.",
            1, 2, 2,
            List.of(new ApplicableEffect(EffectType.DAMAGE_BLUNT, 2, 0, 0, 0, 1d, null, null, null),
                    new ApplicableEffect(EffectType.LOWER_DEFENSE, 2, 2, 0, 0, 0.5, null, null, null)));
    Attack ADHESIVE_SPRAY = new Attack(
            "Adhesive Spray",
            "The Baby Creler spits a sticky substance, slowing down enemies.",
            1, 3, 3,
            List.of(new ApplicableEffect(EffectType.DEBUFF_SPEED, 1, 2, 0, 0, 1d, null, null, null)));
    Attack MANDIBLE_CRUSH = new Attack(
            "Mandible Crush",
            "The Juvenile Creler attempts to crush its opponent with its powerful mandibles.",
            1, 2, 2,
            List.of(new ApplicableEffect(EffectType.DAMAGE_PIERCE, 3, 2, 0, 0, 1d, null, null, null),
            new ApplicableEffect(EffectType.STUN, 0, 1, 0, 0, 0.3, null, null, null)));
    Attack CARAPACE_BASH = new Attack(
            "Carapace Bash",
            "The Juvenile Creler uses its hardened body to bash into an enemy, attempting to knock them off balance.",
            1, 2, 3,
            List.of(new ApplicableEffect(EffectType.DAMAGE_BLUNT, 2, 2, 0, 0, 1d, null, null, null),
                    new ApplicableEffect(EffectType.DEBUFF_SPEED, 0, 1, 0, 0, 1d, null, null, null)));
    Attack WEB_TRAP = new Attack(
            "Web Trap",
            "The Juvenile Creler releases a web to entangle foes, restricting their movement.",
            1, 3, 4,
            List.of(new ApplicableEffect(EffectType.ROOT, 3, 2, 0, 0, 1d, null, null, List.of(TeamKind.PLAYER, TeamKind.ALLY)),
                    new ApplicableEffect(EffectType.DAMAGE_BLUNT, 1, 1, 0, 0, 0.3, null, null, List.of(TeamKind.PLAYER, TeamKind.ALLY))));
    Attack REND_AND_TEAR = new Attack(
            "Rend and Tear",
            "The Adult Creler uses its multiple legs to slash at its opponent with vicious precision.",
            1, 3, 3,
            List.of(new ApplicableEffect(EffectType.DAMAGE_SLASH, 4, 2, 0, 0, 1d, null, null, null),
                    new ApplicableEffect(EffectType.BLEED, 1, 3, 0, 0, 0.7, null, null, null)));
    Attack ARMOR_SHRED = new Attack(
            "Armor Shred",
            "The Adult Creler focuses on destroying an enemy's defenses with a powerful, concentrated attack.",
            1, 3, 4,
            List.of(new ApplicableEffect(EffectType.DAMAGE_PIERCE, 3, 2, 0, 0, 1d, null, null, null),
                    new ApplicableEffect(EffectType.LOWER_DEFENSE, 2, 30, 0, 0, 0.7, null, null, null)));
    Attack HORRIFIC_ROAR = new Attack(
            "Horrific Roar",
            "TThe Adult Creler unleashes a deafening roar, striking fear into enemies.",
            1, 3, 5,
            List.of(new ApplicableEffect(EffectType.DAMAGE_PIERCE, 2, 2, 0, 0, 1d, null, null, List.of(TeamKind.PLAYER, TeamKind.ALLY)),
                    new ApplicableEffect(EffectType.DEBUFF_SPEED, 2, 2, 0, 0, 0.7, null, null, List.of(TeamKind.PLAYER, TeamKind.ALLY)),
                    new ApplicableEffect(EffectType.DEBUFF_ATTACK, 2, 2, 0, 0, 0.7, null, null, List.of(TeamKind.PLAYER, TeamKind.ALLY))));
    Attack TURN_SKELETON = new Attack(
            "Turn Skeleton",
            "Turn a skeleton friendly.",
            1, 3, 5,
            List.of(new ApplicableEffect(EffectType.TURN_FRIENDLY, 0, 3, 0, 0, 1d, null, Groups.WEAK_SKELETONS_ID, null)));

    Attack CONSTANT_FOE_UNDEAD = new Attack(
            "Constant Foe (Undead)",
            "Increases Attack against undead enemies.",
            0, 0, 5,
            List.of(new ApplicableEffect(EffectType.BUFF_ATTACK, 15, 3, 0, 0, 1d, null, Groups.UNDEAD_ID, null)));

    Attack UNDEAD_CLAW = new Attack(
            "Undead Claw",
            "Attack with a foul claw.",
            1, 1, 2,
            List.of(new ApplicableEffect(EffectType.DAMAGE_SLASH, 3, 0, 0, 0, 1d, null, null, null)));

    Attack UNDEAD_BITE = new Attack(
            "Undead Bite",
            "A foul bite.",
            1, 1, 3,
            List.of(new ApplicableEffect(EffectType.DAMAGE_PIERCE, 4, 0, 0, 0, 1d, null, null, null)));

}
