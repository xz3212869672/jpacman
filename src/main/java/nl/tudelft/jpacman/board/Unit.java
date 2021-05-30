package nl.tudelft.jpacman.board;

import nl.tudelft.jpacman.sprite.Sprite;

/**
 * A unit that can be placed on a {@link Square}.
 * 一个单元可以放置在一个方格列表中
 * @author Jeroen Roosen 
 */
public abstract class Unit {

    /**
     * The square this unit is currently occupying.
     * 这个单位目前占用的方格。
     */
    private Square square;

    /**
     * The direction this unit is facing.
     * 这个单位目前的朝向。
     */
    private Direction direction;

    /**
     * Creates a unit that is facing east.
     * 创建一个朝向东的单元。
     */
    protected Unit() {
        this.direction = Direction.EAST;
    }

    /**
     * Sets this unit to face the new direction.
     * 将此单位设置为面向新方向。
     * @param newDirection The new direction this unit is facing.
     */
    public void setDirection(Direction newDirection) {
        this.direction = newDirection;
    }

    /**
     * Returns the current direction this unit is facing.
     * 返回此单元所面对的当前方向。
     * @return The current direction this unit is facing.
     */
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * Returns the square this unit is currently occupying.
     * 返回此单位当前占用的方格。
     * Precondition: <code>hasSquare()</code>.
     *
     * @return The square this unit is currently occupying.
     */
    public Square getSquare() {
        assert invariant();
        assert square != null;
        return square;
    }

    /**
     * Returns whether this unit is currently on  a square.
     *返回此单位当前是否在正方形上，如果单位现在占用一个正方形，则为真。
     * @return True iff the unit is occupying a square at the moment.
     */
    public boolean hasSquare() {
        return square != null;
    }

    /**
     * Occupies the target square iff this unit is allowed to as decided by
     * {@link Square#isAccessibleTo(Unit)}.
     * 占据目标方格
     * @param target
     *            The square to occupy.
     */
    public void occupy(Square target) {
        assert target != null;

        if (square != null) {
            square.remove(this);
        }
        square = target;
        target.put(this);
        assert invariant();
    }

    /**
     * Leaves the currently occupying square, thus removing this unit from the board.
     * 离开当前方格
     */
    public void leaveSquare() {
        if (square != null) {
            square.remove(this);
            square = null;
        }
        assert invariant();
    }

    /**
     * Tests whether the square this unit is occupying has this unit listed as
     * one of its occupiers.
     * 测试此单元所占用的方格是否将此单元列为占领者之一。
     * @return <code>true</code> if the square this unit is occupying has this
     *         unit listed as one of its occupiers, or if this unit is currently
     *         not occupying any square.
     */
    protected boolean invariant() {
        return square == null || square.getOccupants().contains(this);
    }

    /**
     * Returns the sprite of this unit.
     *
     * @return The sprite of this unit.
     */
    public abstract Sprite getSprite();

    /**
     * A utility method for implementing the ghost AI.
     * 一种实用的魔鬼实现方法。
     * @param amountToLookAhead the amount of squares to follow this units direction in.（延此方向的方向数）
     * @return The square amountToLookAhead spaces in front of this unit.
     */
    public Square squaresAheadOf(int amountToLookAhead) {
        Direction targetDirection = this.getDirection();
        Square destination = this.getSquare();
        for (int i = 0; i < amountToLookAhead; i++) {
            destination = destination.getSquareAt(targetDirection);
        }

        return destination;
    }
}
