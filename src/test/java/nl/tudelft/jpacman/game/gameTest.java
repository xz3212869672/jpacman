package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.ghost.Navigation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class gameTest {
	private Launcher launcher = new Launcher();

	@Test
	@DisplayName("开始游戏")
	@Order(1)
	void startGame() {
		launcher.withMapFile("/board.txt");
		launcher.launch();
		//模拟点击开始
		launcher.getGame().start();
		assertThat(launcher.getGame().isInProgress()).isTrue();
	}

	@Test
	@DisplayName("暂停游戏")
	@Order(2)
	void pauseGame() {
		launcher.withMapFile("/board.txt");
		launcher.launch();
		//模拟点击开始
		launcher.getGame().start();
		assertThat(launcher.getGame().isInProgress()).isTrue();
		//模拟点击暂停
		launcher.getGame().stop();
		assertThat(launcher.getGame().isInProgress()).isFalse();
	}
	@Test
	@DisplayName("继续游戏")
	@Order(3)
	void keepPlayingGame() {
		launcher.withMapFile("/board.txt");
		launcher.launch();
		//模拟点击开始
		launcher.getGame().start();
		assertThat(launcher.getGame().isInProgress()).isTrue();
		//模拟点击暂停
		launcher.getGame().stop();
		assertThat(launcher.getGame().isInProgress()).isFalse();
		//模拟点击继续
		launcher.getGame().start();
		assertThat(launcher.getGame().isInProgress()).isTrue();
	}
	@Test
	@DisplayName("游戏成功")
	@Order(4)
	void winGame(){
		//设计只有一个小球的地图
		launcher.withMapFile("/board.txt");
		launcher.launch();
		//模拟点击开始
		launcher.getGame().start();
		assertThat(launcher.getGame().isInProgress()).isTrue();
		//再棋盘中找到玩家
		Player p = Navigation.findUnitInBoard(Player.class,launcher.getGame().getLevel().getBoard());
		//模拟玩家吃完小球
		launcher.getGame().getLevel().move(p, Direction.WEST);
		//游戏结束
		assertThat(launcher.getGame().isInProgress()).isTrue();
	}
	@Test
	@DisplayName("游戏失败")
	@Order(5)
	void loseGame(){
		//设计只有鬼魂吃掉玩家的地图
		launcher.withMapFile("/board.txt");
		launcher.launch();
		//模拟点击开始
		launcher.getGame().start();
		assertThat(launcher.getGame().isInProgress()).isTrue();
		//再棋盘中找到玩家
		Player p = Navigation.findUnitInBoard(Player.class,launcher.getGame().getLevel().getBoard());
		//模拟鬼魂吃掉玩家
		launcher.getGame().getLevel().move(p, Direction.EAST);
		//游戏结束
		assertThat(launcher.getGame().isInProgress()).isFalse();
	}
 
}
