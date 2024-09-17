package Lexer;

public class Token {

    // C Minor token has 6 fields
    private Lexer.TokenType type;
    private String text;
    // Positional data for error messaging
    private int lineStart;
    private int lineEnd;
    private int colStart;
    private int colEnd;

    public Token(Lexer.TokenType type, String text, int lStart, int lEnd, int cStart, int cEnd) {
        this.setTokenType(type);
        this.setText(text);
        this.setLineStart(lStart);
        this.setLineEnd(lEnd);
        this.setColStart(cStart);
        this.setColEnd(cEnd);
    }

    private void setTokenType(Lexer.TokenType type) { this.type = type; }
    private void setText(String text) { this.text = text; }
    private void setLineStart(int lineStart) { this.lineStart = lineStart; }
    private void setLineEnd(int lineEnd) { this.lineEnd = lineEnd; }
    private void setColStart(int colStart) { this.colStart = colStart; }
    private void setColEnd(int colEnd) { this.colEnd = colEnd; }

    public Lexer.TokenType getTokenType() { return this.type; }
    public String getText() { return this.text; }
    public int getLineStart() { return this.lineStart; }
    public int getLineEnd() { return this.lineEnd; }
    public int getColStart() { return this.colStart; }
    public int getColEnd() { return this.colEnd; }

    public void asString() {
        System.out.printf("Token: %s\nText: %s\nLines: %d to %d\nColumns: %d to %d",
            this.type, this.text, this.lineStart,this.lineEnd, this.colStart, this.colEnd);

    }

}
