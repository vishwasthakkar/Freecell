package freecell.model;

/**
 * This is the interface of the FreecellOperationsBuilder. It is used to receive cascades number and
 * opens number input by users and create a build.
 */
public interface FreecellOperationsBuilder<K> {
  FreecellOperationsBuilder cascades(int c);

  FreecellOperationsBuilder opens(int o);

  FreecellOperations<K> build();
}
