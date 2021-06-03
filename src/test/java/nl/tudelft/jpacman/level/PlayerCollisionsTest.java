package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

/**
 * JPacman碰撞检测
 */
public class PlayerCollisionsTest {
    private PlayerCollisions playerCollisions;
    private PointCalculator pointCalculatorl;
    @BeforeEach
    void setUp(){
        pointCalculatorl = mock(PointCalculator.class);
        playerCollisions = new PlayerCollisions(pointCalculatorl);
    }

    @Test
    @DisplayName("魔鬼撞到豆子")
	@Order(1)
    void GhostVsPellet(){
        Ghost ghost = mock(Ghost.class);
        Pellet pellet = mock(Pellet.class);
        playerCollisions.collide(ghost,pellet);
        verifyZeroInteractions(ghost,pellet);
    }

    @Test
    @DisplayName("魔鬼撞到魔鬼")
	@Order(2)
    void GhostVsGhost(){
        Ghost ghost1 = mock(Ghost.class);
        Ghost ghost2 = mock(Ghost.class);
        playerCollisions.collide(ghost1,ghost2);
        verifyZeroInteractions(ghost1,ghost2);
    }

    @Test
    @DisplayName("魔鬼撞到吃豆人")
	@Order(3)
    void GhostvsPlayer(){
        Ghost ghost = mock(Ghost.class);
        Player player = mock(Player.class);
        playerCollisions.collide(ghost,player);
        verify(pointCalculatorl).collidedWithAGhost(player,ghost);
        verify(player).setAlive(false);
        verify(player).setKiller(ghost);
    }

    @Test
    @DisplayName("吃豆人撞到魔鬼")
	@Order(4)
    void playerVsGhost(){
        Ghost ghost = mock(Ghost.class);
        Player player = mock(Player.class);
        playerCollisions.collide(player,ghost);
        verify(pointCalculatorl).collidedWithAGhost(player,ghost);
        verify(player).setAlive(false);
        verify(player).setKiller(ghost);
    }


    @Test
    @DisplayName("吃豆人撞到豆子")
	@Order(5)
    void playerVsPellet(){
        Player player = mock(Player.class);
        Pellet pellet = mock(Pellet.class);
        playerCollisions.collide(player,pellet);
        verify(pointCalculatorl).consumedAPellet(player,pellet);
        verify(pellet).leaveSquare();
    }
}

