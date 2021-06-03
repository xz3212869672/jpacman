package nl.tudelft.jpacman.npc.ghost;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.google.common.collect.Lists;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClydeTest {
	private MapParser mapParser; 
	Player player;
	
	/**
	 * 为不同的测试方法创建统一的所需对象集，在每一个测试之前执行
	 */
	@BeforeEach
	void setUp() {
	PacManSprites sprites = new PacManSprites();
	LevelFactory levelFactory = new LevelFactory(
			sprites,
			new GhostFactory(sprites),
			mock(PointCalculator.class));
	BoardFactory boardFactory = new BoardFactory(sprites);
	GhostFactory ghostFactory = new GhostFactory(sprites);
	mapParser = new GhostMapParser(levelFactory,boardFactory,ghostFactory);
	player = new PlayerFactory(new PacManSprites()).createPacMan();
	}
	
	/**
	 * 测试Clyde离吃豆人在8格以内时会逃跑
	 */
	@Test
	@DisplayName("Clyde距离player小于8个方块")
	@Order(1)
	void departMoreThanEight() {
		//Arrange
		List<String> text = Lists.newArrayList( 
				"##############",
				"#.#....C.....P",
				"##############");
		Level level = mapParser.parseMap(text);
		
		Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
		assertThat(clyde).isNotNull();
		assertThat(clyde.getDirection()).isEqualTo(Direction.valueOf("EAST"));
		
		//创建player
		player.setDirection(Direction.valueOf("WEST"));
		level.registerPlayer(player);
		Player p = Navigation.findUnitInBoard(Player.class, level.getBoard());
		assertThat(p).isNotNull();
		assertThat(p.getDirection()).isEqualTo(Direction.valueOf("WEST"));
		
		//act
		Optional<Direction> opt = clyde.nextAiMove();
		
		//assert:
		assertThat(opt.get()).isEqualTo(Direction.valueOf("WEST"));
	}
	
	/**
	 * 测试Clyde离吃豆人在8格以内时会逃跑测试Clyde在远离吃豆人（8格以外）时会靠近吃豆人
	 */
	@Test
	@DisplayName("Clyde距离player大于8个方块")
	@Order(2)
	void departMoreThanEight2() {
		//Arrange
		List<String> text = Lists.newArrayList(
				"##############",
				"#.C..........P",
				"##############");
		Level level = mapParser.parseMap(text);
		
		Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
		assertThat(clyde).isNotNull();
		assertThat(clyde.getDirection()).isEqualTo(Direction.valueOf("EAST"));
		
		//创建player
		player.setDirection(Direction.valueOf("WEST"));
		level.registerPlayer(player);
		
		//act
		Optional<Direction> opt = clyde.nextAiMove();
		
		//assert:
		assertThat(opt.get()).isEqualTo(Direction.valueOf("EAST"));
	}
	
	/**
	 * 测试Clyde在地图上没有吃豆人时不知道移动方向，即返回空的方向
	 */
	@Test
	@DisplayName("Clyde没有player")
	@Order(3)
	void departWithoPlayer() {
		//Arrange
		List<String> text = Lists.newArrayList(
				"##############",
				"#.C...........",
				"##############");
		Level level = mapParser.parseMap(text);
		
		Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
		assertThat(clyde).isNotNull();
		assertThat(clyde.getDirection()).isEqualTo(Direction.valueOf("EAST"));
		
		//act
		Optional<Direction> opt = clyde.nextAiMove();
		
		//assert:
		assertThat(opt.isPresent()).isFalse();
	}
	
	/**
	 * 测试Clyde在地图上没有到达吃豆人的路线时不知道的移动路线，即返回空的方向
	 */
	@Test
	@DisplayName("Clyde距离player没有路径到达")
	@Order(4)
	void withoutPathToPlayer() {
		//Arrange
		List<String> text = Lists.newArrayList(
				"##############",
				"#.#....C.....#",
				"#############P");
		Level level = mapParser.parseMap(text);
		
		Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
		assertThat(clyde).isNotNull();
		assertThat(clyde.getDirection()).isEqualTo(Direction.valueOf("EAST"));
		
		assertThat(level.isAnyPlayerAlive()).isFalse();
		
		//创建player
		player.setDirection(Direction.valueOf("WEST"));
		level.registerPlayer(player);
	
		//act
		Optional<Direction> opt = clyde.nextAiMove();
		
		//assert:
		assertThat(opt.isPresent()).isFalse();
	}
}

