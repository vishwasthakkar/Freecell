package freecell.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import freecell.model.Cards;
import freecell.model.FreecellOperations;
import freecell.model.PileType;

/**
 * This class represents a FreecellController which “runs” the program, effectively facilitating it
 * through a sequence of operations. It will will work with any freecell model.
 */
public class FreecellController implements IFreecellController<Cards> {
  private final Readable in;
  private final Appendable out;

  private Map<Character, PileType> pileTypeMap;

  /**
   * Constructs a FreecellController with a Readable object and an Appendable object.
   *
   * @param rd user input
   * @param ap transmit output
   * @throws IllegalArgumentException if and only if the readable or appendable objects are null
   */
  public FreecellController(Readable rd, Appendable ap) throws IllegalArgumentException {
    if (rd == null || ap == null) {
      throw new IllegalArgumentException("Can't find input or output streams.");
    }
    this.in = rd;
    this.out = ap;

    pileTypeMap = new HashMap<>();
    pileTypeMap.put('C', PileType.CASCADE);
    pileTypeMap.put('F', PileType.FOUNDATION);
    pileTypeMap.put('O', PileType.OPEN);
  }

  @Override
  public void playGame(List<Cards> deck, FreecellOperations<Cards> model, boolean shuffle)
          throws IllegalArgumentException, IllegalStateException {
    if (deck == null || model == null) {
      throw new IllegalArgumentException("The deck or model passed is null.");
    }

    try {
      model.startGame(deck, shuffle);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("The deck is invalid.");
    }

    try {
      Scanner scan = new Scanner(this.in);
      String sourcePile;
      String destinationPile;
      Integer cardIndex;
      Integer pileNumber;
      Integer destPileNumber;

      while (true) {
        this.out.append(model.getGameState() + "\n");
        if (!model.isGameOver()) {
          char theSourcePile;
          PileType source;
          do {
            sourcePile = scan.next();
            if (sourcePile.contains("q") || sourcePile.contains("Q")) {
              this.out.append("Game quit prematurely.");
              return;
            }
            theSourcePile = sourcePile.charAt(0);
            if (sourcePile.length() < 2) {
              out.append("There should have both source pile name and index like C1 or O1. "
                      + "Please input again.\n");
              continue;
            }
            source = pileTypeMap.get(theSourcePile);
            if (source != null) {
              pileNumber = parseInt(sourcePile.substring(1));
              if (pileNumber == null || pileNumber < 1) {
                out.append("Source pile index should be a valid number starting from 1. "
                        + "Please input again.\n");
                continue;
              }
              pileNumber = pileNumber - 1;
              break;
            }
            out.append("Source pile number should be one of C, F or O. "
                    + "Please input again.\n");
          }
          while (true);

          do {
            String cardIndexAsString = scan.next();
            if (cardIndexAsString.contains("q") || cardIndexAsString.contains("Q")) {
              this.out.append("Game quit prematurely.");
              return;
            }
            cardIndex = parseInt(cardIndexAsString);
            if (cardIndex == null || cardIndex < 1) {
              out.append("Card index should be a valid number starting from 1. "
                      + "Please input again.\n");
              continue;
            }
            cardIndex = cardIndex - 1;
            break;
          }
          while (true);

          char theDestinationPile;
          PileType destination;
          do {
            destinationPile = scan.next();
            if (destinationPile.contains("q") || destinationPile.contains("Q")) {
              this.out.append("Game quit prematurely.");
              return;
            }
            theDestinationPile = destinationPile.charAt(0);
            if (destinationPile.length() < 2) {
              out.append("There should have both destination pile name and index like C1 or O1. "
                      + "Please input again.\n");
              continue;
            }
            destination = pileTypeMap.get(theDestinationPile);
            if (destination != null) {
              destPileNumber = parseInt(destinationPile.substring(1));
              if (destPileNumber == null || destPileNumber < 1) {
                out.append("Destination pile index should be a valid number starting from 1. "
                        + "Please input again.\n");
                continue;
              }
              destPileNumber = destPileNumber - 1;
              break;
            }
            out.append("Destination pile number should be one of C, F or O. "
                    + "Please input again.\n");
          }
          while (true);

          try {
            model.move(source, pileNumber, cardIndex, destination, destPileNumber);
          } catch (Exception e) {
            out.append("Invalid move. Try again." + e.getMessage() + "\n");
          }

        } else {
          this.out.append(model.getGameState() + "\n");
          this.out.append("Game over.\n");
          break;
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException();
    }
  }

  /**
   * Returns the Integer transformed from the given string. If the transformation fails, then it
   * will return null.
   *
   * @param target given string
   * @return the Integer transformed from the given string, if fails then return null
   */
  private Integer parseInt(String target) {
    try {
      return Integer.parseInt(target);
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
