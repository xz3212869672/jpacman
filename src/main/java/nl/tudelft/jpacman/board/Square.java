package nl.tudelft.jpacman.board;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import nl.tudelft.jpacman.sprite.Sprite;
/**
 * A square on a {@link Board}, which can (or cannot, depending on the type) be
 * occupied by units.
 *{@link Board}上的一个正方形，它可以（或不能，取决于类型）被单位占据。
 * @author Jeroen Roosen 
 */
public abstract class Square {

    /**
     * The units occupying this square, in order of appearance.
     * 占据这个方格的单位，按出现顺序排列。
     */
    private final List<Unit> occupants; //占领者

    /**
     * The collection of squares adjacent to this square.
     * 与这个方格相邻的方格的集合。
     */
    private final Map<Direction, Square> neighbours; //邻居

    /**
     * Creates a new, empty square.
     * 创建一个新的空方格
     */
    protected Square() {
        this.occupants = new ArrayList<>();
        this.neighbours = new EnumMap<>(Direction.class);
        assert invariant();
    }

    /**
     * Returns the square adjacent to this square.
     * 返回指定方向的下一个方格
     * @param direction
     *            The direction of the adjacent square.
     * @return The adjacent square in the given direction.
     */
    public Square getSquareAt(Direction direction) {
        return neighbours.get(direction);
    }

    /**
     * Links this square to a neighbour in the given direction. Note that this
     * is a one-way connection.
     * 将这个方格与给定方向的邻居相连。请注意，这是单向连接。
     * @param neighbour
     *            The neighbour to link.
     * @param direction
     *            The direction the new neighbour is in, as seen from this cell.
     */
    public void link(Square neighbour, Direction direction) {
        neighbours.put(direction, neighbour);
        assert invariant();
    }

    /**
     * Returns an immutable list of units occupying this square, in the order in
     * which they occupied this square (i.e. oldest first.)
     * 返回占据此方格的单位的不可变列表，按占据的的顺序排列
     * @return An immutable list of units occupying this square, in the order in
     *         which they occupied this square (i.e. oldest first.)
     */
    public List<Unit> getOccupants() {
        return ImmutableList.copyOf(occupants);
    }

    /**
     * Adds a new occupant to this square.
     * 添加新的占领者
     * @param occupant
     *            The unit to occupy this square.
     */
    void put(Unit occupant) {
        assert occupant != null;
        assert !occupants.contains(occupant);

        occupants.add(occupant);
    }

    /**
     * Removes the unit from this square if it was present.
     * 删除占领者
     * @param occupant
     *            The unit to be removed from this square.
     */
    void remove(Unit occupant) {
        assert occupant != null;
        occupants.remove(occupant);
    }

    /**
     * Verifies that all occupants on this square have indeed listed this square
     * as the square they are currently occupying.
     * 确认这个方格上的所有占领者确实都列出了这个广场
     * @return <code>true</code> iff all occupants of this square have this
     *         square listed as the square they are currently occupying.
     */
    protected final boolean invariant(Square this) {
        for (Unit occupant : occupants) {
            if (occupant.hasSquare() && occupant.getSquare() != this) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether the unit is allowed to occupy this square.
     * 确定是否允许单元占用此方格。
     * @param unit
     *            The unit to grant or deny access.
     * @return <code>true</code> iff the unit is allowed to occupy this square.
     */
    public abstract boolean isAccessibleTo(Unit unit);

    /**
     * Returns the sprite of this square.
     * 返回此方块的幽灵
     * @return The sprite of this square.
     */
    public abstract Sprite getSprite();

}
