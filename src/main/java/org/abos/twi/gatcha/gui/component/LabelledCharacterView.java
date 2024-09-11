package org.abos.twi.gatcha.gui.component;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.abos.common.Vec2d;
import org.abos.twi.gatcha.core.CharacterModified;
import org.abos.twi.gatcha.gui.shape.Hexagon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LabelledCharacterView extends VBox {

    protected @Nullable CharacterModified character;
    protected final boolean hex;
    protected final double size;

    protected @Nullable CharacterView view;
    protected @Nullable Hexagon hexagon;
    protected final @NotNull Label label = new Label();

    public LabelledCharacterView(final @Nullable CharacterModified character, final boolean hex, final double size) {
        this.hex = hex;
        this.size = size;
        if (hex) {
            hexagon = new Hexagon((int) Math.round(size / 2), new Vec2d(0d, 0d));
        }
        setCharacter(character);
        setAlignment(Pos.CENTER);
    }

    public @Nullable CharacterModified getCharacter() {
        return character;
    }

    public void setCharacter(@Nullable CharacterModified character) {
        getChildren().removeAll(view, label);
        if (hexagon != null) {
            getChildren().remove(hexagon);
        }
        this.character = character;
        if (character != null) {
            view = new CharacterView(character.getBase(), hex, size);
            label.setText(character.getName());
            getChildren().addAll(view, label);
        }
        else if (hex) {
            label.setText("empty");
            getChildren().addAll(hexagon, label);
        }
    }
}