import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import freecell.model.AbstractFreecellModel;
import freecell.model.Cards;
import freecell.model.FreecellMultiMoveModel;
import freecell.model.FreecellOperations;
import freecell.model.FreecellOperationsBuilder;
import freecell.model.PileType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * This class tests FreecellMultiMoveModel and all its functions.
 */
public class FreecellMultiMoveModelTest {

  private List<Cards> deckForTest;

  @Before
  public void setup() {
    deckForTest = new ArrayList<>();
    for (int i = 1; i <= 13; i++) {
      deckForTest.add(new Cards(i, "♣"));
    }
    for (int i = 1; i <= 13; i++) {
      deckForTest.add(new Cards(i, "♦"));
    }
    for (int i = 1; i <= 13; i++) {
      deckForTest.add(new Cards(i, "♥"));
    }
    for (int i = 1; i <= 13; i++) {
      deckForTest.add(new Cards(i, "♠"));
    }
  }

  @Test
  public void testFreeCellMultiMoveBuilder() {
    FreecellOperationsBuilder<Cards> builder = FreecellMultiMoveModel.getBuilder();
    builder.cascades(10);
    builder.opens(6);
    FreecellOperations<Cards> model = builder.build();
    model.startGame(model.getDeck(), false);
    String s = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" + "O3:\n" + "O4:\n"
            + "O5:\n" + "O6:\n" + "C1: A♣, J♣, 8♦, 5♥, 2♠, Q♠\n"
            + "C2: 2♣, Q♣, 9♦, 6♥, 3♠, K♠\n" + "C3: 3♣, K♣, 10♦, 7♥, 4♠\n"
            + "C4: 4♣, A♦, J♦, 8♥, 5♠\n" + "C5: 5♣, 2♦, Q♦, 9♥, 6♠\n"
            + "C6: 6♣, 3♦, K♦, 10♥, 7♠\n" + "C7: 7♣, 4♦, A♥, J♥, 8♠\n"
            + "C8: 8♣, 5♦, 2♥, Q♥, 9♠\n" + "C9: 9♣, 6♦, 3♥, K♥, 10♠\n"
            + "C10: 10♣, 7♦, 4♥, A♠, J♠";
    assertEquals(s, model.getGameState());
  }

  @Test
  public void testFreeCellMultiMoveBuilderCascadesException() {
    try {
      FreecellOperationsBuilder<Cards> builder = FreecellMultiMoveModel.getBuilder();
      builder.cascades(3);
      builder.opens(2);
      FreecellOperations<Cards> model = builder.build();
      model.startGame(model.getDeck(), false);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testFreeCellMultiMoveBuilderOpensException() {
    try {
      FreecellOperationsBuilder<Cards> builder = FreecellMultiMoveModel.getBuilder();
      builder.cascades(4);
      builder.opens(0);
      FreecellOperations<Cards> model = builder.build();
      model.startGame(model.getDeck(), false);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testFreeCellMultiMoveConstructor() {
    FreecellMultiMoveModel model = new FreecellMultiMoveModel(6, 2);
    model.startGame(model.getDeck(), false);
    String s = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n"
            + "C1: A♣, 7♣, K♣, 6♦, Q♦, 5♥, J♥, 4♠, 10♠\n"
            + "C2: 2♣, 8♣, A♦, 7♦, K♦, 6♥, Q♥, 5♠, J♠\n"
            + "C3: 3♣, 9♣, 2♦, 8♦, A♥, 7♥, K♥, 6♠, Q♠\n"
            + "C4: 4♣, 10♣, 3♦, 9♦, 2♥, 8♥, A♠, 7♠, K♠\n"
            + "C5: 5♣, J♣, 4♦, 10♦, 3♥, 9♥, 2♠, 8♠\n"
            + "C6: 6♣, Q♣, 5♦, J♦, 4♥, 10♥, 3♠, 9♠";
    assertEquals(s, model.getGameState());
  }

  @Test
  public void testGetDeck() {
    FreecellMultiMoveModel model = new FreecellMultiMoveModel(6, 2);
    String s0 = model.getDeck().toString();
    String s1 = "[A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣, A♦, 2♦, 3♦, 4♦,"
            + " 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦, A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥,"
            + " J♥, Q♥, K♥, A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠]";
    assertEquals(s0, s1);
  }

  @Test
  public void testStartGameDeckNullException() {
    FreecellMultiMoveModel model = new FreecellMultiMoveModel(6, 2);
    model.startGame(null, false);
    String s0 = model.getDeck().toString();
    String s1 = "[A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣, A♦, 2♦, 3♦, 4♦,"
            + " 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦, A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥,"
            + " J♥, Q♥, K♥, A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠]";
    assertEquals(s0, s1);
  }

  @Test
  public void testStartGameDeckNot52Exception() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(6, 2);
      deckForTest.remove(0);
      model.startGame(deckForTest, false);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testStartGameDeckDuplicateException() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(6, 2);
      Cards card = deckForTest.get(0);
      deckForTest.remove(51);
      deckForTest.add(card);
      model.startGame(deckForTest, false);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testStartGameDeckInvalidValueException() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(6, 2);
      deckForTest.remove(51);
      deckForTest.add(new Cards(14, "♣"));
      model.startGame(deckForTest, false);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testStartGameDeckInvalidSuitException() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(6, 2);
      deckForTest.remove(51);
      deckForTest.add(new Cards(13, "♣♣"));
      model.startGame(deckForTest, false);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testStartGameInvalidCascadesException() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(2, 2);
      model.startGame(model.getDeck(), false);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testStartGameInvalidOpensException() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(4, 0);
      model.startGame(model.getDeck(), false);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testStartGameShuffle() {
    FreecellMultiMoveModel model = new FreecellMultiMoveModel(6, 2);
    model.startGame(model.getDeck(), false);
    String s0 = model.getGameState();
    model.startGame(model.getDeck(), true);
    String s1 = model.getGameState();
    assertNotEquals(s1, s0);
  }

  @Test
  public void testStartGameNoShuffle() {
    FreecellMultiMoveModel model = new FreecellMultiMoveModel(6, 2);
    model.startGame(model.getDeck(), false);
    String s0 = model.getGameState();
    model.startGame(model.getDeck(), false);
    String s1 = model.getGameState();
    assertEquals(s1, s0);
  }

  @Test
  public void testMovesNotStartStatusException() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(6, 2);
      model.move(PileType.CASCADE, 0, 6, PileType.OPEN, 0);
      fail("The above line should have thrown an exception");
    } catch (IllegalStateException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testMovesGameOverStatusException() throws NoSuchFieldException,
          IllegalAccessException {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(8, 4);
      Field field1 = AbstractFreecellModel.class.getDeclaredField("foundationsPile");
      Field field2 = AbstractFreecellModel.class.getDeclaredField("opensPile");
      Field field3 = AbstractFreecellModel.class.getDeclaredField("cascadesPile");
      field1.setAccessible(true);
      field2.setAccessible(true);
      field3.setAccessible(true);

      List<List<Cards>> foundationsPile;
      List<List<Cards>> cascadesPile;
      List<List<Cards>> opensPile;
      foundationsPile = new ArrayList<>();
      cascadesPile = new ArrayList<>();
      opensPile = new ArrayList<>();
      for (int i = 0; i < 8; i++) {
        cascadesPile.add(new ArrayList<>());
      }
      for (int i = 0; i < 4; i++) {
        foundationsPile.add(new ArrayList<>());
      }
      for (int i = 0; i < 4; i++) {
        opensPile.add(new ArrayList<>());
      }
      for (int i = 1; i <= 13; i++) {
        foundationsPile.get(0).add(new Cards(i, "♣"));
      }
      for (int i = 1; i <= 13; i++) {
        foundationsPile.get(1).add(new Cards(i, "♦"));
      }
      for (int i = 1; i <= 13; i++) {
        foundationsPile.get(2).add(new Cards(i, "♠"));
      }
      for (int i = 1; i <= 13; i++) {
        foundationsPile.get(3).add(new Cards(i, "♥"));
      }

      field1.set(model, foundationsPile);
      field2.set(model, opensPile);
      field3.set(model, cascadesPile);
      model.isGameOver();
      model.move(PileType.FOUNDATION, 0, 12, PileType.CASCADE, 0);
      fail("The above line should have thrown an exception");
    } catch (IllegalStateException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testMovesNoPileException1() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(8, 4);
      model.startGame(model.getDeck(), false);
      model.move(PileType.CASCADE, 9, 6, PileType.OPEN, 0);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testMovesNoPileException2() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(8, 4);
      model.startGame(model.getDeck(), false);
      model.move(PileType.CASCADE, 0, 6, PileType.OPEN, 6);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testMovesToNotEmptyOpenPileException() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(8, 4);
      model.startGame(model.getDeck(), false);
      model.move(PileType.CASCADE, 0, 6, PileType.OPEN, 0);
      model.move(PileType.CASCADE, 0, 5, PileType.OPEN, 0);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testMovesMultiMove() {
    FreecellOperationsBuilder<Cards> builder = FreecellMultiMoveModel.getBuilder();
    builder.cascades(52);
    builder.opens(6);
    FreecellOperations<Cards> model = builder.build();
    model.startGame(model.getDeck(), false);
    model.move(PileType.CASCADE, 28, 0, PileType.CASCADE, 3);
    model.move(PileType.CASCADE, 3, 0, PileType.CASCADE, 17);
    model.move(PileType.CASCADE, 17, 1, PileType.CASCADE, 30);
    String s = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" + "O3:\n" + "O4:\n"
            + "O5:\n" + "O6:\n" + "C1: A♣\n" + "C2: 2♣\n" + "C3: 3♣\n" + "C4:\n" + "C5: 5♣\n"
            + "C6: 6♣\n" + "C7: 7♣\n" + "C8: 8♣\n" + "C9: 9♣\n" + "C10: 10♣\n" + "C11: J♣\n"
            + "C12: Q♣\n" + "C13: K♣\n" + "C14: A♦\n" + "C15: 2♦\n" + "C16: 3♦\n" + "C17: 4♦\n"
            + "C18: 5♦\n" + "C19: 6♦\n" + "C20: 7♦\n" + "C21: 8♦\n" + "C22: 9♦\n" + "C23: 10♦\n"
            + "C24: J♦\n" + "C25: Q♦\n" + "C26: K♦\n" + "C27: A♥\n" + "C28: 2♥\n" + "C29:\n"
            + "C30: 4♥\n" + "C31: 5♥, 4♣, 3♥\n" + "C32: 6♥\n" + "C33: 7♥\n" + "C34: 8♥\n"
            + "C35: 9♥\n" + "C36: 10♥\n" + "C37: J♥\n" + "C38: Q♥\n" + "C39: K♥\n" + "C40: A♠\n"
            + "C41: 2♠\n" + "C42: 3♠\n" + "C43: 4♠\n" + "C44: 5♠\n" + "C45: 6♠\n" + "C46: 7♠\n"
            + "C47: 8♠\n" + "C48: 9♠\n" + "C49: 10♠\n" + "C50: J♠\n" + "C51: Q♠\n" + "C52: K♠";
    assertEquals(s, model.getGameState());
  }

  @Test
  public void testMovesToCascadesPileNotMatchException() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(8, 4);
      model.startGame(model.getDeck(), false);
      model.move(PileType.CASCADE, 7, 1, PileType.CASCADE, 2);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testMovesToCascadesSelfNotMatchException() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(26, 4);
      model.startGame(model.getDeck(), false);
      model.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 1);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testMovesToCascadesMaximumNumNotMatchException() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(26, 1);
      model.startGame(model.getDeck(), false);
      model.move(PileType.CASCADE, 0, 1, PileType.OPEN, 0);
      model.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 1);
      model.move(PileType.CASCADE, 2, 1, PileType.CASCADE, 0);
      model.move(PileType.CASCADE, 1, 1, PileType.CASCADE, 2);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testMovesToFoundationPileNotMatchException() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(26, 4);
      model.startGame(model.getDeck(), false);
      model.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testMovesToEmptyCascadesPile() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(52, 4);
      model.startGame(model.getDeck(), false);
      model.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 14);
      model.move(PileType.CASCADE, 14, 0, PileType.CASCADE, 0);
    } catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown");
    }
  }

  @Test
  public void testMultiMoveToOpenPileException() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(52, 4);
      model.startGame(model.getDeck(), false);
      model.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 14);
      model.move(PileType.CASCADE, 14, 0, PileType.OPEN, 0);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testMultiMoveToOpenPile() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(52, 4);
      model.startGame(model.getDeck(), false);
      model.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    } catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown");
    }
  }

  @Test
  public void testMovesToEmptyFoundationPile() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(8, 4);
      model.startGame(model.getDeck(), false);
      model.move(PileType.CASCADE, 7, 5, PileType.FOUNDATION, 0);
    } catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown");
    }
  }

  @Test
  public void testMovesToFoundationPile() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(8, 4);
      model.startGame(model.getDeck(), false);
      model.move(PileType.CASCADE, 7, 5, PileType.FOUNDATION, 0);
      model.move(PileType.CASCADE, 0, 6, PileType.FOUNDATION, 0);
    } catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown");
    }
  }

  @Test
  public void testMovesFromCascadesToItselfPile() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(8, 4);
      model.startGame(model.getDeck(), false);
      model.move(PileType.CASCADE, 0, 2, PileType.CASCADE, 0);
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      //do not do anything except catch the exception and let the test continue
    }
  }

  @Test
  public void testMovesFromOpensToItselfPile() {
    try {
      FreecellMultiMoveModel model = new FreecellMultiMoveModel(8, 4);
      model.startGame(model.getDeck(), false);
      model.move(PileType.CASCADE, 0, 6, PileType.OPEN, 0);
      model.move(PileType.OPEN, 0, 0, PileType.OPEN, 0);
    } catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown");
    }
  }

  @Test
  public void testNotStartedGetGameState() {
    FreecellMultiMoveModel model = new FreecellMultiMoveModel(8, 4);
    assertEquals("", model.getGameState());
  }

  @Test
  public void testOriginalGetGameState() {
    FreecellMultiMoveModel model = new FreecellMultiMoveModel(8, 4);
    model.startGame(model.getDeck(), false);
    String s = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" + "O3:\n" + "O4:\n"
            + "C1: A♣, 9♣, 4♦, Q♦, 7♥, 2♠, 10♠\n" + "C2: 2♣, 10♣, 5♦, K♦, 8♥, 3♠, J♠\n"
            + "C3: 3♣, J♣, 6♦, A♥, 9♥, 4♠, Q♠\n" + "C4: 4♣, Q♣, 7♦, 2♥, 10♥, 5♠, K♠\n"
            + "C5: 5♣, K♣, 8♦, 3♥, J♥, 6♠\n" + "C6: 6♣, A♦, 9♦, 4♥, Q♥, 7♠\n"
            + "C7: 7♣, 2♦, 10♦, 5♥, K♥, 8♠\n" + "C8: 8♣, 3♦, J♦, 6♥, A♠, 9♠";
    assertEquals(s, model.getGameState());
  }

  @Test
  public void testGetGameStateAfterMoves() {
    FreecellMultiMoveModel model = new FreecellMultiMoveModel(8, 4);
    model.startGame(model.getDeck(), false);
    String s1 = model.getGameState();
    model.move(PileType.CASCADE, 0, 6, PileType.FOUNDATION, 0);
    model.move(PileType.CASCADE, 0, 5, PileType.OPEN, 0);
    String s2 = model.getGameState();
    String s3 = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" + "O3:\n" + "O4:\n"
            + "C1: A♣, 9♣, 4♦, Q♦, 7♥, 2♠, 10♠\n" + "C2: 2♣, 10♣, 5♦, K♦, 8♥, 3♠, J♠\n"
            + "C3: 3♣, J♣, 6♦, A♥, 9♥, 4♠, Q♠\n" + "C4: 4♣, Q♣, 7♦, 2♥, 10♥, 5♠, K♠\n"
            + "C5: 5♣, K♣, 8♦, 3♥, J♥, 6♠\n" + "C6: 6♣, A♦, 9♦, 4♥, Q♥, 7♠\n"
            + "C7: 7♣, 2♦, 10♦, 5♥, K♥, 8♠\n" + "C8: 8♣, 3♦, J♦, 6♥, A♠, 9♠";
    String s4 = "F1: 10♠\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1: 2♠\n" + "O2:\n" + "O3:\n"
            + "O4:\n" + "C1: A♣, 9♣, 4♦, Q♦, 7♥\n" + "C2: 2♣, 10♣, 5♦, K♦, 8♥, 3♠, J♠\n"
            + "C3: 3♣, J♣, 6♦, A♥, 9♥, 4♠, Q♠\n" + "C4: 4♣, Q♣, 7♦, 2♥, 10♥, 5♠, K♠\n"
            + "C5: 5♣, K♣, 8♦, 3♥, J♥, 6♠\n" + "C6: 6♣, A♦, 9♦, 4♥, Q♥, 7♠\n"
            + "C7: 7♣, 2♦, 10♦, 5♥, K♥, 8♠\n" + "C8: 8♣, 3♦, J♦, 6♥, A♠, 9♠";
    assertEquals(s1, s3);
    assertEquals(s2, s4);
  }

  @Test
  public void testStartNewGame() {
    FreecellMultiMoveModel model = new FreecellMultiMoveModel(8, 4);
    model.startGame(model.getDeck(), false);
    model.move(PileType.CASCADE, 0, 6, PileType.FOUNDATION, 0);
    String s = model.getGameState();
    model.startGame(model.getDeck(), false);
    String s1 = model.getGameState();
    assertNotEquals(s, s1);
  }

  @Test
  public void testIsGameOverForNotOver() {
    FreecellMultiMoveModel model = new FreecellMultiMoveModel(8, 4);
    model.startGame(model.getDeck(), false);
    assertFalse(model.isGameOver());
  }

  @Test
  public void testIsGameOverForOver() throws NoSuchFieldException, IllegalAccessException {
    FreecellMultiMoveModel model = new FreecellMultiMoveModel(8, 4);
    model.startGame(model.getDeck(), false);
    Field field1 = AbstractFreecellModel.class.getDeclaredField("foundationsPile");
    Field field2 = AbstractFreecellModel.class.getDeclaredField("opensPile");
    Field field3 = AbstractFreecellModel.class.getDeclaredField("cascadesPile");
    field1.setAccessible(true);
    field2.setAccessible(true);
    field3.setAccessible(true);

    List<List<Cards>> foundationsPile;
    List<List<Cards>> cascadesPile;
    List<List<Cards>> opensPile;
    foundationsPile = new ArrayList<>();
    cascadesPile = new ArrayList<>();
    opensPile = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      cascadesPile.add(new ArrayList<>());
    }
    for (int i = 0; i < 4; i++) {
      foundationsPile.add(new ArrayList<>());
    }
    for (int i = 0; i < 4; i++) {
      opensPile.add(new ArrayList<>());
    }
    for (int i = 1; i <= 13; i++) {
      foundationsPile.get(0).add(new Cards(i, "♣"));
    }
    for (int i = 1; i <= 13; i++) {
      foundationsPile.get(1).add(new Cards(i, "♦"));
    }
    for (int i = 1; i <= 13; i++) {
      foundationsPile.get(2).add(new Cards(i, "♠"));
    }
    for (int i = 1; i <= 13; i++) {
      foundationsPile.get(3).add(new Cards(i, "♥"));
    }

    field1.set(model, foundationsPile);
    field2.set(model, opensPile);
    field3.set(model, cascadesPile);
    assertTrue(model.isGameOver());
  }
}