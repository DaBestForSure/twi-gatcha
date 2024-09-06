package org.abos.twi.gatcha.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.abos.common.Vec2i;
import org.abos.twi.gatcha.core.CharacterModified;
import org.abos.twi.gatcha.core.Player;
import org.abos.twi.gatcha.core.battle.Battle;
import org.abos.twi.gatcha.core.battle.TeamKind;
import org.abos.twi.gatcha.core.battle.Wave;
import org.abos.twi.gatcha.core.battle.ai.DirectRandomAttacker;
import org.abos.twi.gatcha.data.Characters;
import org.abos.twi.gatcha.gui.component.pane.BattleScreen;
import org.abos.twi.gatcha.gui.component.pane.MainMenu;

import java.util.List;

public final class Gui extends Application {

    public static final int DEFAULT_WIDTH = 1280;
    public static final int DEFAULT_HEIGHT = 720;

    private final Scene mainMenuScene = new Scene(new MainMenu(this), DEFAULT_WIDTH, DEFAULT_HEIGHT);
    private final BattleScreen battleScreen = new BattleScreen(this);
    private final Scene battleScreenScene = new Scene(battleScreen, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    private Stage stage;

    private Player player;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.stage.setScene(mainMenuScene);
        this.stage.show();
    }

    public void newGame() {
        final Battle battle = new Battle(10, 10, List.of());
        battle.addWave(new Wave(0, List.of(new DirectRandomAttacker(new CharacterModified(Characters.ZOMBIE), battle, TeamKind.ENEMY, new Vec2i(9, 9)))));
        battle.addPlayerSpawn(new Vec2i(0, 0));
        battle.addPlayerSpawn(new Vec2i(0, 1));
        battle.addPlayerSpawn(new Vec2i(1, 0));
        battle.addPlayerSpawn(new Vec2i(1, 1));
        battle.startPlacement(List.of(new CharacterModified(Characters.ERIN)));
        stage.setScene(battleScreenScene);
        battleScreen.setBattle(battle);
    }

    public static void main(String[] args) {
        launch();
    }
}
