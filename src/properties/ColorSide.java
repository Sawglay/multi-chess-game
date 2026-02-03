package properties;
// package properties;

public enum ColorSide {
    WHITE, BLACK; /*The only two possible values for the ColorSide type. */

    public ColorSide opposite() { /*     * A simple helper method to get the opposing color.
     * BLACK if the current color is WHITE, and WHITE if it's BLACK.
     */ 
        return this == WHITE ? BLACK : WHITE;
    }
}
