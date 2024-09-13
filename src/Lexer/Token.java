package Lexer;

/*
-------------------------------------
            LexerPack.Token Class
-------------------------------------
A C Minor token will have 6 fields
   - 1) TokenType
   - 2) Lexeme
   - 3) Starting Line
   - 4) Starting Column
   - 5) Ending Line
   - 6) Ending Column
*/
public class Token {

    private Lexer.TokenType type;
    private String text;

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

    private void setTokenType(Lexer.TokenType type) {
        this.type = type;
    }

    public Lexer.TokenType getTokenType() {
        return this.type;
    }

    private void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    private void setLineStart(int lineStart) {
        this.lineStart = lineStart;
    }

    public int getLineStart() {
        return this.lineStart;
    }

    private void setLineEnd(int lineEnd) {
        this.lineEnd = lineEnd;
    }

    public int getLineEnd() {
        return this.lineEnd;
    }

    private void setColStart(int colStart) {
        this.colStart = colStart;
    }

    public int getColStart() {
        return this.colStart;
    }

    private void setColEnd(int colEnd) {
        this.colEnd = colEnd;
    }

    public int getColEnd() {
        return this.colEnd;
    }

    public void asString() {
        System.out.println("Token: " + this.type + ", Text: " + this.text + ", Lines: "
                + this.lineStart + " to " + this.lineEnd + ", Cols: " + this.colStart + " to " + this.colEnd);
    }

}
