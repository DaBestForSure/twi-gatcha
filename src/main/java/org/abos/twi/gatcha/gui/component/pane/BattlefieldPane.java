package org.abos.twi.gatcha.gui.component.pane;

import javafx.application.Platform;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.abos.common.Vec2d;
import org.abos.common.Vec2i;
import org.abos.twi.gatcha.core.CharacterModified;
import org.abos.twi.gatcha.core.battle.Battle;
import org.abos.twi.gatcha.core.battle.BattlePhase;
import org.abos.twi.gatcha.core.battle.BattleUi;
import org.abos.twi.gatcha.core.battle.CharacterInBattle;
import org.abos.twi.gatcha.core.effect.AttackEffect;
import org.abos.twi.gatcha.core.effect.EffectType;
import org.abos.twi.gatcha.gui.component.CharacterView;
import org.abos.twi.gatcha.gui.shape.Hexagon;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class BattlefieldPane extends Pane implements BattleUi {

    protected final @NotNull BidiMap<Vec2i, Hexagon> hexagons = new DualHashBidiMap<>();
    protected final @NotNull BattleScreen screen;
    protected final @NotNull Map<Hexagon, Tooltip> tooltips = new HashMap<>();
    protected final @NotNull Map<CharacterInBattle, CharacterView> characterViews = new HashMap<>();

    protected final @NotNull Battle battle;
    protected final @Range(from = 1, to = Integer.MAX_VALUE) int radius;
    protected boolean playerMoving;
    protected boolean playerAttacking;
    protected @Nullable CompletableFuture<Object> playerDone;

    public BattlefieldPane(final @NotNull BattleScreen screen, final @NotNull Battle battle, final @Range(from = 1, to = Integer.MAX_VALUE) int radius) {
        this.screen = Objects.requireNonNull(screen);
        this.battle = Objects.requireNonNull(battle);
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be positive!");
        }
        this.radius = radius;
//        final double width = 2 * radius * this.battle.getWidth() + (this.battle.getHeight() > 1 ? radius : 0);
//        final double height = (this.battle.getHeight() - 1) * radius * (0.5 + Hexagon.RADII_FACTOR) + 2 * radius;
        addHexagons();
        battle.setUi(this);
        addEventHandler(MouseEvent.MOUSE_MOVED, mouseEvent -> updateGrid(mouseEvent.getX(), mouseEvent.getY()));
        addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.PRIMARY) {
                return;
            }
            final Optional<Hexagon> hexagon = findHexagonAt(mouseEvent.getX(), mouseEvent.getY());
            if (hexagon.isPresent()) {
                final Vec2i position = hexagons.getKey(hexagon.get());
                if (battle.getPhase() == BattlePhase.PLACEMENT) {
                    // place player character
                    if (battle.isPlayerSpawnAt(position) && !battle.getPlacementParty().isEmpty() && !battle.isCharacterAt(position)) {
                        CharacterModified character = battle.getPlacementParty().poll();
                        final CharacterInBattle cib = battle.placePlayerCharacterAt(character, position);
                        final CharacterView characterView = new CharacterView(character.getBase(), true, 60d);
                        characterView.setX(hexagon.get().getLeftUpperCorner().x());
                        characterView.setY(hexagon.get().getLeftUpperCorner().y());
                        BattlefieldPane.this.characterViews.put(cib, characterView);
                        BattlefieldPane.this.getChildren().add(characterView);
                        screen.update();
                    }
                    if (battle.getPlacementParty().isEmpty() || battle.noFreeSpawns()) {
                        battle.start();
                        screen.update();
                    }
                }
                else if (battle.getPhase() == BattlePhase.IN_PROGRESS) {
                    // move with the player character
                    if (isPlayerMoving() && battle.getCurrentCharacter() != null && battle.getPossiblePlayerFields().containsKey(position)) {
                        battle.getCurrentCharacter().setMoved((int) Math.round(battle.getPossiblePlayerFields().get(position)));
                        battle.getCurrentCharacter().setPosition(position);
                        final CharacterView characterView = characterViews.get(battle.getCurrentCharacter());
                        characterView.setX(hexagon.get().getLeftUpperCorner().x());
                        characterView.setY(hexagon.get().getLeftUpperCorner().y());
                        playerMoving = false;
                        playerAttacking = true;
                        screen.update();
                    }
                    // attack with the player character
                    else if (isPlayerAttacking() && battle.getCurrentCharacter() != null && battle.getSelectedAttack() != null && battle.getPossibleAttackFields().contains(position)) {
                        for (final AttackEffect effect : battle.getSelectedAttack().effects()) {
                            effect.apply(battle.getCurrentCharacter(), position, battle);
                        }
                        playerAttacking = false;
                        if (playerDone != null) {
                            playerDone.complete(null);
                            playerDone = null;
                        }
                        screen.update();
                    }
                }
                updateGrid(mouseEvent.getX(), mouseEvent.getY());
            }
        });
        updateGrid(0d, 0d);
    }

    public void addHexagons() {
        final double yOffset = radius * (0.5 + Hexagon.RADII_FACTOR); // the amount we go down with the next row
        var children = getChildren();
        for (int y = 0; y < battle.getHeight(); y++) {
            for (int x = 0; x < battle.getWidth(); x++) {
                final Hexagon hexagon = new Hexagon(radius, new Vec2d(
                        radius + 2 * radius * x + (y % 2 == 0 ? 0 : radius),
                        radius + yOffset * y));
                hexagons.put(new Vec2i(x, y), hexagon);
                children.add(hexagon);
                final Tooltip tooltip = new Tooltip("empty");
                Tooltip.install(hexagon, tooltip);
                tooltips.put(hexagon, tooltip);
            }
        }
    }

    @Override
    public @NotNull Battle getBattle() {
        return battle;
    }

    @Override
    public void characterPlaced(final @NotNull CharacterInBattle character, final @NotNull Vec2i position) {
        Platform.runLater(screen::update);
    }

    @Override
    public void characterMoved(final @NotNull CharacterInBattle character, final @NotNull Vec2i from, final @NotNull Vec2i to) {
        Platform.runLater(screen::update);
    }

    @Override
    public void characterAttacked(final @NotNull CharacterInBattle attacker, final @Nullable CharacterInBattle defender, final @NotNull EffectType type, final @Range(from = 0, to = Integer.MAX_VALUE) int damage) {
        Platform.runLater(screen::update);
    }

    @Override
    public boolean isPlayerMoving() {
        return playerMoving;
    }

    @Override
    public boolean isPlayerAttacking() {
        return playerAttacking;
    }

    @Override
    public CompletableFuture<Object> waitForPlayer() {
        playerDone = new CompletableFuture<>();
        playerMoving = true;
        return playerDone;
    }

    protected void updateGrid(double mouseX, double mouseY) {
        // remove downed character views
        if (battle.getPhase().ordinal() >= BattlePhase.IN_PROGRESS.ordinal()) {
            final Iterator<CharacterInBattle> it = characterViews.keySet().iterator();
            CharacterInBattle current = null;
            while (it.hasNext()) {
                current = it.next();
                if (!battle.getCharacterOrder().contains(current)) {
                    getChildren().remove(characterViews.get(current));
                    it.remove();
                }
            }
        }
        // update the hexagons
        final Optional<Hexagon> hexagon = findHexagonAt(mouseX, mouseY);
        for (final Map.Entry<Vec2i, Hexagon> other : hexagons.entrySet()) {
            if (hexagon.isPresent() && hexagon.get() == other.getValue()) {
                continue;
            }
            final Optional<CharacterInBattle> character = battle.getCharacterAt(other.getKey());
            if (character.isEmpty()) {
                // show possible spawn fields
                if (battle.getPhase() == BattlePhase.PLACEMENT && battle.isPlayerSpawnAt(other.getKey())) {
                    other.getValue().setFill(Color.AQUA);
                }
                // show possible move fields
                else if (isPlayerMoving() && battle.getPossiblePlayerFields().containsKey(other.getKey())) {
                    other.getValue().setFill(Color.LIGHTBLUE);
                }
                // show possible attack fields
                else if (isPlayerAttacking() && !isPlayerMoving() && battle.getPossibleAttackFields().contains(other.getKey())) {
                    other.getValue().setFill(Color.MISTYROSE);
                }
                else {
                    other.getValue().setFill(Color.TRANSPARENT);
                }
                tooltips.get(other.getValue()).setText("empty");
            }
            else {
                switch (character.get().getTeam()) {
                    case ENEMY -> other.getValue().setFill(Color.RED);
                    case PLAYER -> other.getValue().setFill(Color.BLUE);
                    default -> throw new IllegalStateException("Unhandled enum encountered!");
                }
                tooltips.get(other.getValue()).setText(String.format("%s (%d/%d)", character.get().getName(), character.get().getHealth(), character.get().getMaxHealth()));
            }
        }
        if (hexagon.isPresent()) {
            hexagon.get().setFill(Color.ORANGE);
            final Optional<CharacterInBattle> character = battle.getCharacterAt(hexagons.getKey(hexagon.get()));
            if (character.isPresent()) {
                tooltips.get(hexagon.get()).setText(String.format("%s (%d/%d)", character.get().getName(), character.get().getHealth(), character.get().getMaxHealth()));
            }
        }
    }

    protected Optional<Hexagon> findHexagonAt(double x, double y) {
        for (final Hexagon hexagon : hexagons.values()) {
            if (hexagon.intersects(x, y, 1, 1)) {
                return Optional.of(hexagon);
            }
        }
        return Optional.empty();
    }

}
