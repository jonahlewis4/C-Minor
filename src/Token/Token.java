package Token;

public class Token {

    private TokenType type;
    private String text;
    private Location location;

    public Token(TokenType type, String text, Location location) {
        this.type = type;
        this.text = text;
        this.location = location;
        System.out.println(this.toString());
    }

    public TokenType getTokenType() { return type; }
    public String getText() { return text; }
    
    @Override
    public String toString() {
        return "[ " + getTokenType() + ", " + getText() + " @ " + location.toString() + " ]";
    }
}
