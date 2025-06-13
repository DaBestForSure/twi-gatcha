package org.abos.twi.gatcha.core.effect;

import org.abos.common.Vec2i;
import org.abos.twi.gatcha.core.Group;
import org.abos.twi.gatcha.core.battle.Battle;
import org.abos.twi.gatcha.core.battle.CharacterInBattle;
import org.abos.twi.gatcha.core.battle.TeamKind;
import org.abos.twi.gatcha.data.Lookups;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jgrapht.alg.shortestpath.BFSShortestPath;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class ApplicableEffect extends Effect {

    protected final @Range(from = 0, to = Integer.MAX_VALUE) int characterAoeRadius;
    protected final @Range(from = 0, to = Integer.MAX_VALUE) int terrainAoeRadius;
    protected final double applicableChance;
    protected final @Nullable String applicableGroupId;
    private @Nullable Optional<Group> applicableGroup;
    protected final @NotNull List<TeamKind> applicableTeam;

    public ApplicableEffect(final @NotNull EffectType effectType,
                            final @Range(from = 0, to = Integer.MAX_VALUE) int maxPower,
                            final @Range(from = 0, to = Integer.MAX_VALUE) int maxDuration,
                            final @Range(from = 0, to = Integer.MAX_VALUE) int characterAoeRadius,
                            final @Range(from = 0, to = Integer.MAX_VALUE) int terrainAoeRadius,
                            final double applicableChance,
                            final @Nullable String affectedGroupId,
                            final @Nullable String applicableGroupId,
                            final @Nullable List<TeamKind> applicableTeam) {
        super(effectType, maxPower, maxDuration, affectedGroupId);
        if (characterAoeRadius < 0 || terrainAoeRadius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative!");
        }
        // Cap AOE radius to prevent performance issues
        final int MAX_REASONABLE_AOE = 50; // Adjust this value based on your typical map sizes
        if (characterAoeRadius > MAX_REASONABLE_AOE) {
            System.err.println("Warning: Character AOE radius capped from " + characterAoeRadius + " to " + MAX_REASONABLE_AOE);
        }
        if (terrainAoeRadius > MAX_REASONABLE_AOE) {
            System.err.println("Warning: Terrain AOE radius capped from " + terrainAoeRadius + " to " + MAX_REASONABLE_AOE);
        }
        this.characterAoeRadius = Math.min(characterAoeRadius, MAX_REASONABLE_AOE);
        this.terrainAoeRadius = Math.min(terrainAoeRadius, MAX_REASONABLE_AOE);
        if (applicableChance < 0d || applicableChance > 1d) {
            throw new IllegalArgumentException("Applicable Chance must be a probability!");
        }
        this.applicableChance = applicableChance;
        this.applicableGroupId = applicableGroupId;
        this.applicableTeam = Objects.requireNonNullElseGet(applicableTeam, () -> Arrays.asList(TeamKind.values()));
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getCharacterAoeRadius() {
        return characterAoeRadius;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getTerrainAoeRadius() {
        return terrainAoeRadius;
    }

    public Optional<Group> getApplicableGroup() {
        if (applicableGroup == null) {
            if (applicableGroupId == null) {
                applicableGroup = Optional.empty();
            }
            else {
                applicableGroup = Optional.of(Lookups.GROUPS.get(applicableGroupId));
            }
        }
        return applicableGroup;
    }

    protected List<CharacterInBattle> getApplicableTargets(final CharacterInBattle from, final @NotNull Vec2i center, final @NotNull Battle battle) {
        final Optional<Group> applicableGroup = getApplicableGroup();
        // if no AoE effect, just return character at target position
        if (characterAoeRadius == 0) {
            final Optional<CharacterInBattle> character = battle.getCharacterAt(center);
            if (character.isPresent() && (applicableGroup.isEmpty() || applicableGroup.get().characters().contains(character.get().getModified().getBase()))
                    && applicableTeam.contains(character.get().getTeam())) {
                // calculate miss chance
                int lowerAcc = 0;
                // Create a defensive copy to avoid concurrent modification
                List<PersistentEffect> effects = new LinkedList<>(from.getPersistentEffects());
                for (final PersistentEffect effect : effects) {
                    if (effect.getEffectType() == EffectType.LOWER_ACCURACY) {
                        lowerAcc -= effect.maxPower;
                    }
                }
                // if hit, return the character
                if (Math.max(0, 100-lowerAcc) / 100d > battle.getRandom().nextDouble()) {
                    return List.of(character.get());
                } // else we miss our single target
            }
            return List.of();
        }
        // else calculate all targets via BFS
        final var graph = battle.getTerrainGraph();
        final var paths = new BFSShortestPath<>(graph).getPaths(center);
        final List<CharacterInBattle> aoeTargets = new LinkedList<>();

        // Safety check: limit the number of positions we check
        int positionsChecked = 0;
        final int MAX_POSITIONS_TO_CHECK = 10000; // Prevent infinite loops on huge maps

        for (final Vec2i position : graph.vertexSet()) {
            if (++positionsChecked > MAX_POSITIONS_TO_CHECK) {
                System.err.println("Warning: AOE search hit position limit, truncating results");
                break;
            }

            var path = paths.getPath(position);
            if (path == null) continue; // No path to this position

            double distance = path.getWeight();
            if (distance <= characterAoeRadius && battle.isCharacterAt(position)) {
                aoeTargets.add(battle.getCharacterAt(position).get());
            }
        }
        // filter through applicable targets if needed
        return aoeTargets.stream()
                .filter(character -> applicableTeam.contains(character.getTeam()))
                .filter(character -> applicableGroup.isEmpty() || applicableGroup.get().characters().contains(character.getModified().getBase()))
                .toList();
    }

    public void apply(final CharacterInBattle from, final Vec2i target, final Battle battle, boolean... test) {
        // Synchronize on the battle object to prevent concurrent modifications
        synchronized (battle) {
            applyEffectsSafely(from, target, battle, test);
        }
    }

    private void applyEffectsSafely(final CharacterInBattle from, final Vec2i target, final Battle battle, boolean... test) {
        List<CharacterInBattle> aoeTargets = getApplicableTargets(from, target, battle);
        boolean flag = test.length > 0 && test[0];  // More explicit boolean check

        for (final CharacterInBattle aoeTarget : aoeTargets) {
            // Skip if target is no longer valid (could have been removed/killed by previous effects)
            if (!battle.isCharacterAt(aoeTarget.getPosition())) {
                continue;
            }

            // applicable chance is tested for each target
            if (applicableChance < battle.getRandom().nextDouble()) {
                continue;
            }

            // Remove the problematic sleep - this was masking the real issue
            // if (flag) {
            //     Thread.sleep(1000 * 15);
            // }

            int dmg = 0;
            try {
                switch (effectType) {
                    case DAMAGE_BLUNT, DAMAGE_SLASH -> {
                        dmg = Math.max(1, from.getAttack(aoeTarget) - aoeTarget.getDefense(from) + maxPower);
                        aoeTarget.takeDamage(dmg);
                    }
                    case DAMAGE_PIERCE -> {
                        dmg = Math.max(1, from.getAttack(aoeTarget) - aoeTarget.getDefense(from) + maxPower) * 2;
                        aoeTarget.takeDamage(dmg);
                    }
                    case DAMAGE_ARMOR_PIERCE -> {
                        dmg = Math.max(1, from.getAttack(aoeTarget) - aoeTarget.getDefense(from)/3 + maxPower) * 2;
                        aoeTarget.takeDamage(dmg);
                    }
                    case DAMAGE_DEATH -> {
                        int resistance = 0;
                        // Create defensive copy to avoid concurrent modification
                        List<PersistentEffect> effects = new LinkedList<>(aoeTarget.getPersistentEffects());
                        for (final PersistentEffect effect : effects) {
                            if (effect.getEffectType() == EffectType.RESIST_DEATH) {
                                resistance += effect.getMaxPower();
                            }
                        }
                        dmg = (int)Math.round(maxPower * (1 - Math.min(100, resistance) / 100d));
                        aoeTarget.takeDamage(dmg);
                    }
                    case DAMAGE_SOUND -> {
                        dmg = Math.max(1, (from.getAttack(aoeTarget) + maxPower) / 5);
                        aoeTarget.takeDamage(dmg);
                    }
                    case DAMAGE_FROST -> {
                        dmg = Math.max(1, (from.getAttack(aoeTarget) + maxPower) / 3);
                        aoeTarget.takeDamage(dmg);
                    }
                    case HEALING -> {
                        final int heal = from.getAttack(aoeTarget) + maxPower;
                        aoeTarget.heal(heal);
                        dmg = heal;
                    }
                    case INVISIBILITY, INVULNERABILITY, STUN, TURN_FRIENDLY, BUFF_ATTACK, BUFF_DEFENSE, BUFF_SPEED, DEBUFF_SPEED, RESIST_DEATH,
                            LOWER_ACCURACY, BLEED, ANNOY, POISON, ROOT -> {
                        // Thread-safe addition to persistent effects
                        addPersistentEffectSafely(aoeTarget, new PersistentEffect(effectType, maxPower, maxDuration, affectedGroupId));
                    }
                    case BUFF_HEALTH -> {
                        // Thread-safe addition to persistent effects
                        addPersistentEffectSafely(aoeTarget, new DeterioratingEffect(effectType, maxPower, maxDuration, affectedGroupId));
                    }
                    case DISPEL -> {
                        // Clear effects safely
                        clearPersistentEffectsSafely(aoeTarget);
                    }
                    case SUMMON -> throw new IllegalStateException(String.format("Use %s for summon effects!", SummonEffect.class.getSimpleName()));
                    default -> throw new AssertionError("An unknown effect type has been associated with this " + ApplicableEffect.class.getSimpleName() + "!");
                }

                // UI update should be done safely - consider using SwingUtilities.invokeLater if this is a Swing UI
                if (battle.getUi() != null) {
                    updateUISafely(battle, from, aoeTarget, effectType, dmg);
                }
            } catch (Exception e) {
                // Log the error instead of crashing
                System.err.println("Error applying effect " + effectType + " to " + aoeTarget + ": " + e.getMessage());
                e.printStackTrace();
                // Continue with other targets instead of failing completely
            }
        }
    }

    private void addPersistentEffectSafely(CharacterInBattle target, PersistentEffect effect) {
        // If using a regular ArrayList, synchronize access
        synchronized (target.getPersistentEffects()) {
            target.getPersistentEffects().add(effect);
        }

        // Alternative: If you can modify the CharacterInBattle class,
        // consider using CopyOnWriteArrayList for getPersistentEffects()
        // which is thread-safe for this use case
    }

    private void clearPersistentEffectsSafely(CharacterInBattle target) {
        synchronized (target.getPersistentEffects()) {
            target.getPersistentEffects().clear();
        }
    }

    private void updateUISafely(Battle battle, CharacterInBattle from, CharacterInBattle target, EffectType effectType, int dmg) {
        // If this is a Swing application, use:
        // javax.swing.SwingUtilities.invokeLater(() ->
        //     battle.getUi().characterAttacked(from, target, effectType, dmg)
        // );

        // For now, call directly but catch any exceptions
        try {
            battle.getUi().characterAttacked(from, target, effectType, dmg);
        } catch (Exception e) {
            System.err.println("Error updating UI: " + e.getMessage());
        }
    }
}