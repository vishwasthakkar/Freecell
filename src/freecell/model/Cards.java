package freecell.model;

import java.util.Objects;

/**
 * This class represents a card with value and suit.
 */
public class Cards {
  private int value;
  private String suit;

  /**
   * Constructs a card with value and suit. The values of A, J, Q, K are 1, 11, 12, 13 and there are
   * four suits: clubs (♣), diamonds (♦), hearts (♥), and spades (♠).
   *
   * @param value the value of this card
   * @param suit  the suit of this card
   * @throws IllegalArgumentException if the card is invalid
   */
  public Cards(int value, String suit) throws IllegalArgumentException {
    if (value < 1 || value > 13) {
      throw new IllegalArgumentException("Card cannot have this value.");
    }
    if ((!suit.equals("♣")) && (!suit.equals("♦")) && (!suit.equals("♥")) && (!suit.equals("♠"))) {
      throw new IllegalArgumentException("Card cannot have this suit.");
    }
    this.value = value;
    this.suit = suit;
  }

  @Override
  public String toString() {
    if (this.value == 1) {
      return "A" + this.suit;
    } else if (this.value == 11) {
      return "J" + this.suit;
    } else if (this.value == 12) {
      return "Q" + this.suit;
    } else if (this.value == 13) {
      return "K" + this.suit;
    } else {
      return value + this.suit;
    }
  }

  /**
   * Returns the value of this card.
   *
   * @return the value of this card
   */
  public int getValue() {
    return value;
  }

  /**
   * Set the value of this card.
   */
  public void setValue(int value) {
    this.value = value;
  }

  /**
   * Returns the suit of this card.
   *
   * @return the suit of this card
   */
  public String getSuit() {
    return suit;
  }

  /**
   * Set the suit of this card.
   */
  public void setSuit(String suit) {
    this.suit = suit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Cards cards = (Cards) o;
    return value == cards.value
            && suit.equals(cards.suit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, suit);
  }
}
