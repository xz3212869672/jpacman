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
public class InkyTest {
	private MapParser mapParser;
	private Player player;
  
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
	
	@Test
	@DisplayName("没有Blinky")
	@Order(1)
	void noBlinky() {
		//Arrange
		List<String> text = Lists.newArrayList( 
				"##############",
				"#.#....I.....P",
				"##############");
		Level level = mapParser.parseMap(text);

        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());
      //创建player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);
        assertThat(blinky).isNull();
        assertThat(inky).isNull();
       //act
        Optional<Direction> opt = inky.nextAiMove();
       //asser
        assertThat(opt.isPresent()).isFalse();
    }
	@Test
	@DisplayName("没有Player")
	@Order(2)
    void noPlayer() {
        List<String> text = Lists.newArrayList( 
        		"##############",
        		"#.#....I.....B",
        		"##############");
        Level level = mapParser.parseMap(text);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertThat(level.isAnyPlayerAlive()).isFalse();
        Optional<Direction> opt = inky.nextAiMove();
        assertThat(opt.isPresent()).isFalse();
    }
	@Test
	@Order(3)
    void noPathBetweenInkyAndDestination() {

		 List<String> text = Lists.newArrayList( 
            "##########",
            "#I # B   #",
            "######## #",
            "########P#");
		Level level = mapParser.parseMap(text);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);
        Optional<Direction> opt = inky.nextAiMove();
        assertThat(opt.isPresent()).isFalse();
    }
	@Test
	@Order(4)
    void noPathBetweenBlinkyAndPlayer() {

		List<String> text = Lists.newArrayList( 
                "###########",
                "#I  B   # #",
                "######### #",
                "#########P#");
		Level level = mapParser.parseMap(text);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);
        Optional<Direction> opt = inky.nextAiMove();
        assertThat(opt.isPresent()).isFalse();
    }
    @Test
    @Order(5)
    void existPath() {

    	List<String> text = Lists.newArrayList( 
                "##########",
                "#I   B   #",
                "######## #",
                "########P#");
    	Level level = mapParser.parseMap(text);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);
        Optional<Direction> opt = inky.nextAiMove();
        assertThat(opt.get()).isEqualTo(Direction.valueOf("EAST"));
    }


}
