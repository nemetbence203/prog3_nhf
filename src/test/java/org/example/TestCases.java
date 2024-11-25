package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TestCases {

    @Test
    public void testAllCellsDieWithNoSurviveRule() {
        LivingSpace space = new LivingSpace(3);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList());

        space.reviveAt(1, 1);
        space.nextState();

        assertFalse(space.getAt(1, 1).isAlive());
    }

    @Test
    public void testCellRevivalWithBornRule() {
        LivingSpace space = new LivingSpace(3);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList(2, 3));

        space.reviveAt(0, 1);
        space.reviveAt(1, 0);
        space.reviveAt(1, 2);
        space.nextState();

        assertTrue(space.getAt(1, 1).isAlive());
    }

    @Test
    public void testCellSurvivesWithSurviveRule() {
        LivingSpace space = new LivingSpace(3);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList(2, 3));

        space.reviveAt(1, 0);
        space.reviveAt(1, 1);
        space.reviveAt(1, 2);
        space.nextState();

        assertTrue(space.getAt(1, 1).isAlive());
    }

    @Test
    public void testCellDiesWithoutSurviveRule() {
        LivingSpace space = new LivingSpace(3);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList(3));

        space.reviveAt(0, 1);
        space.reviveAt(1, 0);
        space.reviveAt(1, 1);
        space.reviveAt(1, 2);
        space.reviveAt(2, 1);
        space.nextState();

        assertFalse(space.getAt(1, 1).isAlive());
    }

    @Test
    public void testBordersStayEmpty() {
        LivingSpace space = new LivingSpace(3);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList(2, 3));

        space.reviveAt(1, 1);
        space.nextState();

        assertFalse(space.getAt(0, 0).isAlive());
        assertFalse(space.getAt(0, 2).isAlive());
        assertFalse(space.getAt(2, 0).isAlive());
        assertFalse(space.getAt(2, 2).isAlive());
    }

    @Test
    public void testDeadCellIncreasesDeadSince() {
        LivingSpace space = new LivingSpace(3);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList(2, 3));

        space.killAt(1, 1);
        space.getAt(1,1).increaseDeadSince();
        assertEquals(2, space.getAt(1, 1).getDeadSince());
    }

    @Test
    public void testFlip(){
        LivingSpace space = new LivingSpace(3);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList(2, 3));
        space.getAt(1, 1).flip();
        space.getAt(1, 1).flip();
        assertFalse(space.getAt(1, 1).isAlive());
    }

    @Test
    public void testClearKill(){
        LivingSpace space = new LivingSpace(3);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList(2, 3));
        space.getAt(1, 1).clearKill();
        assertEquals(17, space.getAt(1, 1).getDeadSince());
    }

    @Test
    void testNextState_AllCellsRemainAlive() {
        LivingSpace space = new LivingSpace(3);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList(2, 3));
        space.reviveAt(1, 1);
        space.killAll();
        assertFalse(space.getAt(1, 1).isAlive());
    }

    @Test
    void testNextState_AllCellsRemainDead() {
        LivingSpace space = new LivingSpace(3);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList(2, 3));

        space.nextState();

        for (int i = 0; i < space.getSize(); i++) {
            for (int j = 0; j < space.getSize(); j++) {
                assertFalse(space.getAt(i, j).isAlive());
            }
        }
    }

    @Test
    void testNextState_CellComesToLife() {
        LivingSpace space = new LivingSpace(3);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList(2, 3));

        space.reviveAt(0, 0);
        space.reviveAt(0, 2);
        space.reviveAt(2, 2);

        space.nextState();

        assertTrue(space.getAt(1, 1).isAlive());

    }

    @Test
    void testNextState_Overpopulation() {
        LivingSpace space = new LivingSpace(3);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList(2, 3));

        space.reviveAt(0, 0);
        space.reviveAt(0, 1);
        space.reviveAt(0, 2);
        space.reviveAt(1, 0);
        space.reviveAt(1, 1);

        space.nextState();

        assertFalse(space.getAt(0, 1).isAlive());

    }

    @Test
    void testNextState_Underpopulation() {
        LivingSpace space = new LivingSpace(3);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList(2, 3));

        space.reviveAt(1, 1);
        space.reviveAt(1, 2);

        space.nextState();

        assertFalse(space.getAt(1, 1).isAlive());
        assertFalse(space.getAt(1, 2).isAlive());
    }
    @Test
    public void testResizeIncreaseSize() {
        int initialSize = 3;
        int newSize = 5;
        LivingSpace livingSpace = new LivingSpace(initialSize);

        livingSpace.resize(newSize);

        assertEquals(newSize, livingSpace.getSize());
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                assertNotNull(livingSpace.getAt(i, j));
            }
        }
    }

    @Test
    public void testResizeDecreaseSize() {
        int initialSize = 4;
        int newSize = 2;
        LivingSpace livingSpace = new LivingSpace(initialSize);

        livingSpace.resize(newSize);

        assertEquals(newSize, livingSpace.getSize());
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                assertNotNull(livingSpace.getAt(i, j));
            }
        }
        assertThrows(IndexOutOfBoundsException.class, () -> livingSpace.getAt(newSize, newSize));
    }

    @Test
    public void testResizeInvalidSize() {
        int initialSize = 3;
        int newSize = 0;
        LivingSpace livingSpace = new LivingSpace(initialSize);

        assertThrows(IllegalArgumentException.class, () -> livingSpace.resize(newSize));
    }

    @Test
    public void testResizePreserveExistingCells() {
        int initialSize = 3;
        int newSize = 5;
        LivingSpace livingSpace = new LivingSpace(initialSize);

        livingSpace.reviveAt(1, 1);
        livingSpace.reviveAt(2, 2);

        livingSpace.resize(newSize);

        assertTrue(livingSpace.getAt(1, 1).isAlive());
        assertTrue(livingSpace.getAt(2, 2).isAlive());
    }

    @Test
    public void testResizeAddsNewCells() {
        int initialSize = 3;
        int newSize = 5;
        LivingSpace livingSpace = new LivingSpace(initialSize);

        livingSpace.resize(newSize);

        for (int i = initialSize; i < newSize; i++) {
            for (int j = initialSize; j < newSize; j++) {
                assertFalse(livingSpace.getAt(i, j).isAlive());
            }
        }
    }

    @Test
    public void testGameAreaPanelClear(){
        LivingSpace space = new LivingSpace(3);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList(2, 3));
        GameAreaPanel gameAreaPanel = new GameAreaPanel(space, 10);
        space.reviveAt(1, 1);
        gameAreaPanel.clearLivingSpace();
        assertFalse(space.getAt(1, 1).isAlive());
    }
    @Test
    public void testGameAreaPanelNextState(){
        LivingSpace space = new LivingSpace(3);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList(2, 3));
        GameAreaPanel gameAreaPanel = new GameAreaPanel(space, 10);
        space.reviveAt(1, 1);
        gameAreaPanel.nextState();
        assertFalse(space.getAt(1, 1).isAlive());
    }

    @Test
    public void testGameAreaPanelPrefferedSize(){
        LivingSpace space = new LivingSpace(5);
        space.setBornRule(Arrays.asList(3));
        space.setSurviveRule(Arrays.asList(2, 3));
        GameAreaPanel gameAreaPanel = new GameAreaPanel(space, 10);
        assertEquals(50, gameAreaPanel.getPreferredSize().width);
        assertEquals(50, gameAreaPanel.getPreferredSize().height);
    }

}