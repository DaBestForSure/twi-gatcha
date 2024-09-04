package org.abos.twi.gatcha.core;

import org.abos.common.Describable;
import org.abos.common.Named;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * The character as modified by a player outside of battle, based on {@link CharacterBase}.
 */
public class CharacterModified implements Named, Describable {

    protected final @NotNull CharacterBase base;

    public CharacterModified(final @NotNull CharacterBase base) {
        this.base = Objects.requireNonNull(base);
    }

    @Override
    public @NotNull String getName() {
        return base.getName();
    }

    @Override
    public @NotNull String getDescription() {
        return base.getDescription();
    }

    public @NotNull CharacterBase getBase() {
        return base;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getMaxHealth() {
        return base.stats().maxHealth();
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getSpeed() {
        return base.stats().speed();
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getAttack() {
        return base.stats().attack();
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getDefense() {
        return base.stats().defense();
    }

    public double getInitiative() {
        return base.stats().initiative();
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getMovement() {
        return base.stats().movement();
    }
}