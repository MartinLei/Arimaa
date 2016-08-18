package de.htwg.se.arimaa.util.position;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.htwg.se.arimaa.util.position.Position;

public class PositionTest {

	Position position;

	@Before
	public void setUp() throws Exception {
		position = new Position(0, 1);
	}

	@Test
	public void testGetPosition() {
		assertEquals(0, position.getX());
		assertEquals(1, position.getY());
		position.setPositon(1, 0);
		assertEquals(1, position.getX());
		assertEquals(0, position.getY());
	}

	@Test
	public void testPositionOnPitch() {
		Position end = new Position(0, 0);
		assertTrue(Position.positionOnPitch(end));

		end = new Position(7, 7);
		assertTrue(Position.positionOnPitch(end));
	}

	@Test
	public void testIlligalArgumentConstrukor() {
		testIlligalArgumentConstrukorhelper(-1, 2);
		testIlligalArgumentConstrukorhelper(1, 8);
		testIlligalArgumentConstrukorhelper(8, 1);
		testIlligalArgumentConstrukorhelper(2, -1);
	}

	private void testIlligalArgumentConstrukorhelper(int x, int y) {
		@SuppressWarnings("unused")
		Position test = null;
		boolean catched = false;
		try {
			test = new Position(x, y);
		} catch (Exception e) {
			catched = true;
		}
		assertTrue(catched);
	}

	@Test
	public void testEqual() {
		Position pos = new Position(0, 1);

		assertTrue(position.equals(pos));
		pos.setPositon(1, 0);
		assertFalse(position.equals(pos));
		pos.setPositon(0, 0);
		assertFalse(position.equals(pos));

		assertFalse(position.equals(null));
	}

	@Test
	public void testHashCode() {
		Position pos1 = new Position(0, 0);
		Position pos2 = new Position(0, 0);
		assertEquals(pos1.hashCode(), pos2.hashCode());

		Position pos3 = new Position(1, 1);
		assertNotEquals(pos1.hashCode(), pos3.hashCode());
	}

	@Test
	public void testToString() {
		String target = "(0, 1)";
		assertEquals(target, position.toString());
	}

	@Test
	public void testGetSurroundPosition() {
		List<Position> leftUpperCorner = Position.getSurroundPositionForPitch(new Position(0, 0));
		assertEquals(2, leftUpperCorner.size());
		assertTrue(leftUpperCorner.contains(new Position(0, 1)));
		assertTrue(leftUpperCorner.contains(new Position(1, 0)));

		List<Position> rightUpperCorner = Position.getSurroundPositionForPitch(new Position(7, 0));
		assertEquals(2, rightUpperCorner.size());
		assertTrue(rightUpperCorner.contains(new Position(6, 0)));
		assertTrue(rightUpperCorner.contains(new Position(7, 1)));

		List<Position> leftBottomCorner = Position.getSurroundPositionForPitch(new Position(0, 7));
		assertEquals(2, leftBottomCorner.size());
		assertTrue(leftBottomCorner.contains(new Position(1, 7)));
		assertTrue(leftBottomCorner.contains(new Position(0, 6)));

		List<Position> rightBottomCorner = Position.getSurroundPositionForPitch(new Position(7, 7));
		assertEquals(2, rightBottomCorner.size());
		assertTrue(rightBottomCorner.contains(new Position(6, 7)));
		assertTrue(rightBottomCorner.contains(new Position(7, 6)));

		List<Position> middel = Position.getSurroundPositionForPitch(new Position(3, 3));
		assertEquals(4, middel.size());
		assertTrue(middel.contains(new Position(3, 2)));
		assertTrue(middel.contains(new Position(3, 4)));
		assertTrue(middel.contains(new Position(2, 3)));
		assertTrue(middel.contains(new Position(4, 3)));
	}

	@Test
	public void testGetDirection() {
		assertEquals(DIRECTION.NORD, Position.getDirection(new Position(3, 3), new Position(3, 2)));
		assertEquals(DIRECTION.EAST, Position.getDirection(new Position(3, 3), new Position(4, 3)));
		assertEquals(DIRECTION.SOUTH, Position.getDirection(new Position(3, 3), new Position(3, 4)));
		assertEquals(DIRECTION.WEST, Position.getDirection(new Position(3, 3), new Position(2, 3)));
		assertEquals(null, Position.getDirection(new Position(3, 3), new Position(0, 0)));
	}
}
