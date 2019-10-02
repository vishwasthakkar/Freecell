import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import freecell.controller.FreecellController;
import freecell.controller.IFreecellController;
import freecell.model.Cards;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import freecell.model.FreecellOperationsBuilder;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * A JUnit test class to test the operations of the Controller.
 */
public class FreecellControllerTest {

  private List<Cards> deck;
  private FreecellOperations<Cards> model;
  private static final String CLUB = "♣";
  private static final String DIAMOND = "♦";
  private static final String HEART = "♥";
  private static final String SPADE = "♠";

  @Before
  public void setDeck() {
    deck = new ArrayList<>();
    for (int i = 1; i <= 13; i++) {
      this.deck.add(new Cards(i, CLUB));
    }
    for (int i = 1; i <= 13; i++) {
      this.deck.add(new Cards(i, DIAMOND));
    }
    for (int i = 1; i <= 13; i++) {
      this.deck.add(new Cards(i, HEART));
    }
    for (int i = 1; i <= 13; i++) {
      this.deck.add(new Cards(i, SPADE));
    }

    FreecellOperationsBuilder<Cards> builder = FreecellModel.getBuilder();
    builder.cascades(12);
    builder.opens(6);
    model = builder.build();
  }

  @Test
  public void testControllerConstructor() {
    try {
      StringBuffer out = new StringBuffer();
      Reader in = new StringReader("C1 4 C5 q");
      IFreecellController<Cards> controller = new FreecellController(in, out);
      controller.playGame(deck, model, false);
      assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" + "O3:\n"
              + "O4:\n" + "O5:\n" + "O6:\n" + "C1: A♣, K♣, Q♦, J♥, 10♠\n"
              + "C2: 2♣, A♦, K♦, Q♥, J♠\n" + "C3: 3♣, 2♦, A♥, K♥, Q♠\n"
              + "C4: 4♣, 3♦, 2♥, A♠, K♠\n" + "C5: 5♣, 4♦, 3♥, 2♠\n" + "C6: 6♣, 5♦, 4♥, 3♠\n"
              + "C7: 7♣, 6♦, 5♥, 4♠\n" + "C8: 8♣, 7♦, 6♥, 5♠\n" + "C9: 9♣, 8♦, 7♥, 6♠\n"
              + "C10: 10♣, 9♦, 8♥, 7♠\n" + "C11: J♣, 10♦, 9♥, 8♠\n" + "C12: Q♣, J♦, 10♥, 9♠\n" +
              "Invalid move. Try again.This card cannot be moved or there is no such card.\n"
              + "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" + "O3:\n" + "O4:\n"
              + "O5:\n" + "O6:\n" + "C1: A♣, K♣, Q♦, J♥, 10♠\n" + "C2: 2♣, A♦, K♦, Q♥, J♠\n"
              + "C3: 3♣, 2♦, A♥, K♥, Q♠\n" + "C4: 4♣, 3♦, 2♥, A♠, K♠\n" + "C5: 5♣, 4♦, 3♥, 2♠\n"
              + "C6: 6♣, 5♦, 4♥, 3♠\n" + "C7: 7♣, 6♦, 5♥, 4♠\n" + "C8: 8♣, 7♦, 6♥, 5♠\n"
              + "C9: 9♣, 8♦, 7♥, 6♠\n" + "C10: 10♣, 9♦, 8♥, 7♠\n" + "C11: J♣, 10♦, 9♥, 8♠\n"
              + "C12: Q♣, J♦, 10♥, 9♠\n" + "Game quit prematurely.", out.toString());
    } catch (Exception any) {
      fail("An exception was thrown.");
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testControllerThrowsInvalidArgumentExceptionIfNullDeck() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C1 4 C5 q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    deck = null;
    controller.playGame(deck, model, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testControllerThrowsInvalidArgumentExceptionIfNullModel() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C1 4 C5 q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    model = null;
    controller.playGame(deck, model, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testControllerConstructorWhenAppendableNull() {
    StringBuffer out = null;
    Reader in = new StringReader("C1 4 C5 q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(deck, model, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testControllerConstructorWhenReadableNull() {
    StringBuffer out = new StringBuffer();
    Reader in = null;
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(deck, model, true);
  }

  @Test
  public void testPlayGame() {
    try {
      FreecellOperationsBuilder<Cards> builder = FreecellModel.getBuilder();
      builder.cascades(12);
      builder.opens(6);
      FreecellOperations<Cards> model = builder.build();

      StringBuffer out = new StringBuffer();
      Reader in = new StringReader("C1 1 O1\nC2 2 F1\nC6 2 O3 q");
      IFreecellController<Cards> controller = new FreecellController(in, out);
      controller.playGame(deck, model, false);
      String expected = "F1:\n" +
              "F2:\n" +
              "F3:\n" +
              "F4:\n" +
              "O1:\n" +
              "O2:\n" +
              "O3:\n" +
              "O4:\n" +
              "O5:\n" +
              "O6:\n" +
              "C1: A♣, K♣, Q♦, J♥, 10♠\n" +
              "C2: 2♣, A♦, K♦, Q♥, J♠\n" +
              "C3: 3♣, 2♦, A♥, K♥, Q♠\n" +
              "C4: 4♣, 3♦, 2♥, A♠, K♠\n" +
              "C5: 5♣, 4♦, 3♥, 2♠\n" +
              "C6: 6♣, 5♦, 4♥, 3♠\n" +
              "C7: 7♣, 6♦, 5♥, 4♠\n" +
              "C8: 8♣, 7♦, 6♥, 5♠\n" +
              "C9: 9♣, 8♦, 7♥, 6♠\n" +
              "C10: 10♣, 9♦, 8♥, 7♠\n" +
              "C11: J♣, 10♦, 9♥, 8♠\n" +
              "C12: Q♣, J♦, 10♥, 9♠\n" +
              "Invalid move. Try again.This card cannot be moved or there is no such card.\n" +
              "F1:\n" +
              "F2:\n" +
              "F3:\n" +
              "F4:\n" +
              "O1:\n" +
              "O2:\n" +
              "O3:\n" +
              "O4:\n" +
              "O5:\n" +
              "O6:\n" +
              "C1: A♣, K♣, Q♦, J♥, 10♠\n" +
              "C2: 2♣, A♦, K♦, Q♥, J♠\n" +
              "C3: 3♣, 2♦, A♥, K♥, Q♠\n" +
              "C4: 4♣, 3♦, 2♥, A♠, K♠\n" +
              "C5: 5♣, 4♦, 3♥, 2♠\n" +
              "C6: 6♣, 5♦, 4♥, 3♠\n" +
              "C7: 7♣, 6♦, 5♥, 4♠\n" +
              "C8: 8♣, 7♦, 6♥, 5♠\n" +
              "C9: 9♣, 8♦, 7♥, 6♠\n" +
              "C10: 10♣, 9♦, 8♥, 7♠\n" +
              "C11: J♣, 10♦, 9♥, 8♠\n" +
              "C12: Q♣, J♦, 10♥, 9♠\n" +
              "Invalid move. Try again.This card cannot be moved or there is no such card.\n" +
              "F1:\n" +
              "F2:\n" +
              "F3:\n" +
              "F4:\n" +
              "O1:\n" +
              "O2:\n" +
              "O3:\n" +
              "O4:\n" +
              "O5:\n" +
              "O6:\n" +
              "C1: A♣, K♣, Q♦, J♥, 10♠\n" +
              "C2: 2♣, A♦, K♦, Q♥, J♠\n" +
              "C3: 3♣, 2♦, A♥, K♥, Q♠\n" +
              "C4: 4♣, 3♦, 2♥, A♠, K♠\n" +
              "C5: 5♣, 4♦, 3♥, 2♠\n" +
              "C6: 6♣, 5♦, 4♥, 3♠\n" +
              "C7: 7♣, 6♦, 5♥, 4♠\n" +
              "C8: 8♣, 7♦, 6♥, 5♠\n" +
              "C9: 9♣, 8♦, 7♥, 6♠\n" +
              "C10: 10♣, 9♦, 8♥, 7♠\n" +
              "C11: J♣, 10♦, 9♥, 8♠\n" +
              "C12: Q♣, J♦, 10♥, 9♠\n" +
              "Invalid move. Try again.This card cannot be moved or there is no such card.\n" +
              "F1:\n" +
              "F2:\n" +
              "F3:\n" +
              "F4:\n" +
              "O1:\n" +
              "O2:\n" +
              "O3:\n" +
              "O4:\n" +
              "O5:\n" +
              "O6:\n" +
              "C1: A♣, K♣, Q♦, J♥, 10♠\n" +
              "C2: 2♣, A♦, K♦, Q♥, J♠\n" +
              "C3: 3♣, 2♦, A♥, K♥, Q♠\n" +
              "C4: 4♣, 3♦, 2♥, A♠, K♠\n" +
              "C5: 5♣, 4♦, 3♥, 2♠\n" +
              "C6: 6♣, 5♦, 4♥, 3♠\n" +
              "C7: 7♣, 6♦, 5♥, 4♠\n" +
              "C8: 8♣, 7♦, 6♥, 5♠\n" +
              "C9: 9♣, 8♦, 7♥, 6♠\n" +
              "C10: 10♣, 9♦, 8♥, 7♠\n" +
              "C11: J♣, 10♦, 9♥, 8♠\n" +
              "C12: Q♣, J♦, 10♥, 9♠\n" +
              "Game quit prematurely.";
      assertEquals(expected, out.toString());
    } catch (Exception any) {
      fail("playGame method has thrown an exception.");
    }
  }


  @Test
  public void testTransmissionOutputFromModel() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C1 5 O1 q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(deck, model, false);
    String expected = "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "O5:\n" +
            "O6:\n" +
            "C1: A♣, K♣, Q♦, J♥, 10♠\n" +
            "C2: 2♣, A♦, K♦, Q♥, J♠\n" +
            "C3: 3♣, 2♦, A♥, K♥, Q♠\n" +
            "C4: 4♣, 3♦, 2♥, A♠, K♠\n" +
            "C5: 5♣, 4♦, 3♥, 2♠\n" +
            "C6: 6♣, 5♦, 4♥, 3♠\n" +
            "C7: 7♣, 6♦, 5♥, 4♠\n" +
            "C8: 8♣, 7♦, 6♥, 5♠\n" +
            "C9: 9♣, 8♦, 7♥, 6♠\n" +
            "C10: 10♣, 9♦, 8♥, 7♠\n" +
            "C11: J♣, 10♦, 9♥, 8♠\n" +
            "C12: Q♣, J♦, 10♥, 9♠\n" +
            "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1: 10♠\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "O5:\n" +
            "O6:\n" +
            "C1: A♣, K♣, Q♦, J♥\n" +
            "C2: 2♣, A♦, K♦, Q♥, J♠\n" +
            "C3: 3♣, 2♦, A♥, K♥, Q♠\n" +
            "C4: 4♣, 3♦, 2♥, A♠, K♠\n" +
            "C5: 5♣, 4♦, 3♥, 2♠\n" +
            "C6: 6♣, 5♦, 4♥, 3♠\n" +
            "C7: 7♣, 6♦, 5♥, 4♠\n" +
            "C8: 8♣, 7♦, 6♥, 5♠\n" +
            "C9: 9♣, 8♦, 7♥, 6♠\n" +
            "C10: 10♣, 9♦, 8♥, 7♠\n" +
            "C11: J♣, 10♦, 9♥, 8♠\n" +
            "C12: Q♣, J♦, 10♥, 9♠\n" +
            "Game quit prematurely.";
    assertEquals(expected, out.toString());
  }

  @Test
  public void testInputToModel() {

    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C1 1 F1 q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(deck, model, false);
    assertEquals("F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "O5:\n" +
            "O6:\n" +
            "C1: A♣, K♣, Q♦, J♥, 10♠\n" +
            "C2: 2♣, A♦, K♦, Q♥, J♠\n" +
            "C3: 3♣, 2♦, A♥, K♥, Q♠\n" +
            "C4: 4♣, 3♦, 2♥, A♠, K♠\n" +
            "C5: 5♣, 4♦, 3♥, 2♠\n" +
            "C6: 6♣, 5♦, 4♥, 3♠\n" +
            "C7: 7♣, 6♦, 5♥, 4♠\n" +
            "C8: 8♣, 7♦, 6♥, 5♠\n" +
            "C9: 9♣, 8♦, 7♥, 6♠\n" +
            "C10: 10♣, 9♦, 8♥, 7♠\n" +
            "C11: J♣, 10♦, 9♥, 8♠\n" +
            "C12: Q♣, J♦, 10♥, 9♠\n" +
            "Invalid move. Try again.This card cannot be moved or there is no such card.\n" +
            "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "O5:\n" +
            "O6:\n" +
            "C1: A♣, K♣, Q♦, J♥, 10♠\n" +
            "C2: 2♣, A♦, K♦, Q♥, J♠\n" +
            "C3: 3♣, 2♦, A♥, K♥, Q♠\n" +
            "C4: 4♣, 3♦, 2♥, A♠, K♠\n" +
            "C5: 5♣, 4♦, 3♥, 2♠\n" +
            "C6: 6♣, 5♦, 4♥, 3♠\n" +
            "C7: 7♣, 6♦, 5♥, 4♠\n" +
            "C8: 8♣, 7♦, 6♥, 5♠\n" +
            "C9: 9♣, 8♦, 7♥, 6♠\n" +
            "C10: 10♣, 9♦, 8♥, 7♠\n" +
            "C11: J♣, 10♦, 9♥, 8♠\n" +
            "C12: Q♣, J♦, 10♥, 9♠\n" +
            "Game quit prematurely.", out.toString());
  }

  @Test
  public void testGameQuitUsingInputsFromPlayer() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(deck, model, false);
    String expected = "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "O5:\n" +
            "O6:\n" +
            "C1: A♣, K♣, Q♦, J♥, 10♠\n" +
            "C2: 2♣, A♦, K♦, Q♥, J♠\n" +
            "C3: 3♣, 2♦, A♥, K♥, Q♠\n" +
            "C4: 4♣, 3♦, 2♥, A♠, K♠\n" +
            "C5: 5♣, 4♦, 3♥, 2♠\n" +
            "C6: 6♣, 5♦, 4♥, 3♠\n" +
            "C7: 7♣, 6♦, 5♥, 4♠\n" +
            "C8: 8♣, 7♦, 6♥, 5♠\n" +
            "C9: 9♣, 8♦, 7♥, 6♠\n" +
            "C10: 10♣, 9♦, 8♥, 7♠\n" +
            "C11: J♣, 10♦, 9♥, 8♠\n" +
            "C12: Q♣, J♦, 10♥, 9♠\n" +
            "Game quit prematurely.";
    assertEquals(expected, out.toString());
  }

  @Test
  public void testGameQuitUsingInputsFromPlayerCapitalQ() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("Q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(deck, model, false);
    String expected = "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "O5:\n" +
            "O6:\n" +
            "C1: A♣, K♣, Q♦, J♥, 10♠\n" +
            "C2: 2♣, A♦, K♦, Q♥, J♠\n" +
            "C3: 3♣, 2♦, A♥, K♥, Q♠\n" +
            "C4: 4♣, 3♦, 2♥, A♠, K♠\n" +
            "C5: 5♣, 4♦, 3♥, 2♠\n" +
            "C6: 6♣, 5♦, 4♥, 3♠\n" +
            "C7: 7♣, 6♦, 5♥, 4♠\n" +
            "C8: 8♣, 7♦, 6♥, 5♠\n" +
            "C9: 9♣, 8♦, 7♥, 6♠\n" +
            "C10: 10♣, 9♦, 8♥, 7♠\n" +
            "C11: J♣, 10♦, 9♥, 8♠\n" +
            "C12: Q♣, J♦, 10♥, 9♠\n" +
            "Game quit prematurely.";
    assertEquals(expected, out.toString());
  }

  @Test
  public void testGameQuitUsingInputsFromPlayerUsingSecondInput() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C1 q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(deck, model, false);
    String expected = "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "O5:\n" +
            "O6:\n" +
            "C1: A♣, K♣, Q♦, J♥, 10♠\n" +
            "C2: 2♣, A♦, K♦, Q♥, J♠\n" +
            "C3: 3♣, 2♦, A♥, K♥, Q♠\n" +
            "C4: 4♣, 3♦, 2♥, A♠, K♠\n" +
            "C5: 5♣, 4♦, 3♥, 2♠\n" +
            "C6: 6♣, 5♦, 4♥, 3♠\n" +
            "C7: 7♣, 6♦, 5♥, 4♠\n" +
            "C8: 8♣, 7♦, 6♥, 5♠\n" +
            "C9: 9♣, 8♦, 7♥, 6♠\n" +
            "C10: 10♣, 9♦, 8♥, 7♠\n" +
            "C11: J♣, 10♦, 9♥, 8♠\n" +
            "C12: Q♣, J♦, 10♥, 9♠\n" +
            "Game quit prematurely.";
    assertEquals(expected, out.toString());
  }

  @Test
  public void testGameQuitUsingInputsFromPlayerUsingThirdInput() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C1 1 q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(deck, model, false);
    String expected = "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "O5:\n" +
            "O6:\n" +
            "C1: A♣, K♣, Q♦, J♥, 10♠\n" +
            "C2: 2♣, A♦, K♦, Q♥, J♠\n" +
            "C3: 3♣, 2♦, A♥, K♥, Q♠\n" +
            "C4: 4♣, 3♦, 2♥, A♠, K♠\n" +
            "C5: 5♣, 4♦, 3♥, 2♠\n" +
            "C6: 6♣, 5♦, 4♥, 3♠\n" +
            "C7: 7♣, 6♦, 5♥, 4♠\n" +
            "C8: 8♣, 7♦, 6♥, 5♠\n" +
            "C9: 9♣, 8♦, 7♥, 6♠\n" +
            "C10: 10♣, 9♦, 8♥, 7♠\n" +
            "C11: J♣, 10♦, 9♥, 8♠\n" +
            "C12: Q♣, J♦, 10♥, 9♠\n" +
            "Game quit prematurely.";
    assertEquals(expected, out.toString());
  }

  @Test
  public void testHandlingUnexpectedInputOnlyPileGivenNotIndex() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C 1 O1 q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(deck, model, false);
    assertTrue(out.toString().contains("There should have both source pile name and index " +
            "like C1 or O1. Please input again.\n"));
  }

  @Test
  public void testHandlingUnexpectedInputOnlyDestinationPileGivenNotIndex() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C1 1 O q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(deck, model, false);
    assertTrue(out.toString().contains("There should have both destination pile name and index" +
            " like C1 or O1. Please input again.\n"));
  }

  @Test
  public void testHandlingUnexpectedInputPileAndQTogether() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("CQ");
    IFreecellController controller = new FreecellController(in, out);
    controller.playGame(deck, model, false);
    assertTrue(out.toString().contains("Game quit prematurely"));
  }

  @Test
  public void testHandlingUnexpectedInputInvalidCardIndexGiven() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C1 100 O1 q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(deck, model, false);

    assertTrue(out.toString().contains("Invalid move. Try again.This card cannot be moved or" +
            " there is no such card.\n"));
  }

  @Test
  public void testHandlingUnexpectedInputInvalidCardIndexGivenNotNumber() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C1 abc O1 q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(deck, model, false);
    assertTrue(out.toString().contains("Card index should be a valid number starting from 1." +
            " Please input again.\n"));
  }

  @Test
  public void testHandlingUnexpectedInputSource() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("input q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(deck, model, false);
    assertTrue(out.toString().contains("Source pile number should be one of C, F or O." +
            " Please input again.\n"));
  }

  @Test
  public void testHandlingUnexpectedInputDestinationPile() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C1 3 xyz q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(deck, model, false);
    assertTrue(out.toString().contains("Destination pile number should be one of C, F or O." +
            " Please input again.\n"));
  }

  @Test
  public void testHandlingUnexpectedInputNoSuchPileDestination() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C1 1 F10 q");
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(deck, model, false);
    assertTrue(out.toString().contains("Invalid move. Try again.There is no such pile.\n"));
  }

  @Test
  public void testOutputWhenGameIsWon() {
    model = new FreecellModel(52, 4);
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i <= 13; i++) {
      sb.append("C");
      sb.append(i);
      sb.append(" ");
      sb.append("1");
      sb.append(" ");
      sb.append("F");
      sb.append("1");
      sb.append(" ");
    }
    for (int i = 14; i <= 26; i++) {
      sb.append("C");
      sb.append(i);
      sb.append(" ");
      sb.append("1");
      sb.append(" ");
      sb.append("F");
      sb.append("2");
      sb.append(" ");
    }
    for (int i = 27; i <= 39; i++) {
      sb.append("C");
      sb.append(i);
      sb.append(" ");
      sb.append("1");
      sb.append(" ");
      sb.append("F");
      sb.append("3");
      sb.append(" ");
    }
    for (int i = 40; i <= 52; i++) {
      sb.append("C");
      sb.append(i);
      sb.append(" ");
      sb.append("1");
      sb.append(" ");
      sb.append("F");
      sb.append("4");
      sb.append(" ");
    }

    sb.append("q");
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader(sb.toString());
    IFreecellController<Cards> controller = new FreecellController(in, out);
    controller.playGame(model.getDeck(), model, false);
    assertTrue(out.toString().contains("Game over.\n"));
  }


}