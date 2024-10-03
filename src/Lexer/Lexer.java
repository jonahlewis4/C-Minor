package Lexer;
import AST.Token;
import AST.Token.TokenType;

/*
---------------------------------------------------
                        ISSUES
---------------------------------------------------
                        N/A
 */

public class Lexer {

    private final String file;  // Program
    private int currPos;        // Position in file
    private char lookChar;      // Current lookahead
    private int line;           // Line Number
    private int col;            // Column Number
    private int lineStart;      // Starting Line
    private int colStart;       // Starting Column

    public static final char EOF = '\0';

    public Lexer(final String file) {
        this.file = file;
        this.currPos = 0;
        this.lookChar = file.charAt(currPos);
        this.line = 1;
        this.col = 0;
        this.lineStart = this.colStart = 0;
    }

    private void consume() {
        currPos += 1;
        col += 1;
        if(currPos < file.length())
            lookChar = file.charAt(currPos);
        else
            lookChar = EOF;
    }

    private boolean match(char expectedChar) {
        if(lookChar != expectedChar)
            return false;
        consume();
        return true;
    }

    private void consumeWhitespace() {
        while(lookChar == ' ' || lookChar == '\t' || lookChar == '\r')
            consume();
    }

    private void consumeComment() {
        while(lookChar != '\n')
            consume();
        nextLine();
    }

    private void nextLine() {
        consume();
        line += 1;
        col = 0;
    }

    public Token nextToken() {
        while(lookChar != EOF) {
            // Store the starting line/column before figuring out token
            lineStart = line;
            colStart = col;
            switch(lookChar) {
                case ' ', '\t', '\r':
                    consumeWhitespace();
                    break;
                case '\n':
                    nextLine();
                    break;
                case '=':
                    consume();
                    if(match('='))
                        return new Token(TokenType.EQEQ, "==", lineStart, line, colStart, col);
                    if(match('>'))
                        return new Token(TokenType.ARROW, "=>", lineStart, line, colStart, col);
                    return new Token(TokenType.EQ, "=", lineStart, line, colStart, col);
                case '+':
                    consume();
                    if(match('='))
                        return new Token(TokenType.PLUSEQ, "+=", lineStart, line, colStart, col);
                    return new Token(TokenType.PLUS, "+", lineStart, line, colStart, col);
                case '-':
                    consume();
                    if(match('='))
                        return new Token(TokenType.MINUSEQ, "-=", lineStart, line, colStart, col);
                    return new Token(TokenType.MINUS, "-", lineStart, line, colStart, col);
                case '*':
                    consume();
                    if(match('*')) {
                        if(match('='))
                            return new Token(TokenType.EXPEQ, "**=", lineStart, line, colStart, col);

                        return new Token(TokenType.EXP, "**", lineStart, line, colStart, col);
                    }

                    if(match('='))
                        return new Token(TokenType.MULTEQ, "*=", lineStart, line, colStart, col);

                    return new Token(TokenType.MULT, "*", lineStart, line, colStart, col);
                case '/':
                    consume();
                    if(match('/')) {
                        consumeComment();
                        break;
                    }
                    if(match('='))
                        return new Token(TokenType.DIVEQ, "/=", lineStart, line, colStart, col);

                    return new Token(TokenType.DIV, "/", lineStart, line, colStart, col);
                case '~':
                    consume();
                    return new Token(TokenType.TILDE, "~", lineStart, line, colStart, col);
                case '%':
                    consume();
                    if(match('='))
                        return new Token(TokenType.MODEQ, "%=", lineStart, line, colStart, col);

                    return new Token(TokenType.MOD, "%", lineStart, line, colStart, col);
                case '!':
                    consume();
                    if(match('='))
                        return new Token(TokenType.NEQ, "!=", lineStart, line, colStart, col);
                    // Error
                    return new Token(TokenType.ERROR, "ERROR", lineStart, line, colStart, col);
                case '<':
                    consume();
                    if(match('=')) {
                        if(match('>'))
                            return new Token(TokenType.UFO, "<=>", lineStart, line, colStart, col);

                        return new Token(TokenType.LTEQ, "<=", lineStart, line, colStart, col);
                    }

                    if(match('>'))
                        return new Token(TokenType.LTGT, "<>", lineStart, line, colStart, col);

                    if(match(':'))
                        return new Token(TokenType.MIN, "<:", lineStart, line, colStart, col);

                    if(match('<'))
                        return new Token(TokenType.SLEFT, "<<", lineStart, line, colStart, col);

                    return new Token(TokenType.LT, "<", lineStart, line, colStart, col);
                case '>':
                    consume();
                    if(match('='))
                        return new Token(TokenType.GTEQ, ">=", lineStart, line, colStart, col);

                    if(match('>'))
                        return new Token(TokenType.SRIGHT, ">>", lineStart, line, colStart, col);

                    return new Token(TokenType.GT, ">", lineStart, line, colStart, col);
                case ':':
                    consume();
                    if(match('>'))
                        return new Token(TokenType.MAX, ":>", lineStart, line, colStart, col);

                    return new Token(TokenType.COLON, ":", lineStart, line, colStart, col);
                case '.':
                    consume();
                    if(isDigit())
                        return realLit(new StringBuilder("."), lineStart, colStart);
                    if(match('.'))
                        return new Token(TokenType.INC, "..", lineStart, line, colStart, col);

                    return new Token(TokenType.PERIOD, ".", lineStart, line, colStart, col);
                case ',':
                    consume();
                    return new Token(TokenType.COMMA, ",", lineStart, line, colStart, col);
                case '(':
                    consume();
                    return new Token(TokenType.LPAREN, "(", lineStart, line, colStart, col);
                case ')':
                    consume();
                    return new Token(TokenType.RPAREN, ")", lineStart, line, colStart, col);
                case '{':
                    consume();
                    return new Token(TokenType.LBRACE, "{", lineStart, line, colStart, col);
                case '}':
                    consume();
                    return new Token(TokenType.RBRACE, "}", lineStart, line, colStart, col);
                case '[':
                    consume();
                    return new Token(TokenType.LBRACK, "[", lineStart, line, colStart, col);
                case ']':
                    consume();
                    return new Token(TokenType.RBRACK, "]", lineStart, line, colStart, col);
                case '@':
                    consume();
                    return new Token(TokenType.AT, "@", lineStart, line, colStart, col);
                case '\'':
                    consume();
                    if(match('\'') && match('\''))
                        return strLit(new StringBuilder(), lineStart, colStart);
                    return charLit(lineStart, colStart);
                case '?':
                    consume();
                    if(match('.'))
                        return new Token(TokenType.ELVIS, "?.", lineStart, line, colStart, col);
                    else return new Token(TokenType.ERROR, "ERROR", lineStart, line, colStart, col);
                case '|':
                    consume();
                    return new Token(TokenType.BOR, "|", lineStart, line, colStart, col);
                case '&':
                    consume();
                    return new Token(TokenType.BAND, "&", lineStart, line, colStart, col);
                case '^':
                    consume();
                    return new Token(TokenType.XOR, "^", lineStart, line, colStart, col);
                default:
                    if(isLetter() || match('#'))
                        return name(lineStart, colStart);
                    if(isDigit())
                        return number(lineStart, colStart);

                    return new Token(TokenType.ERROR, "ERROR", lineStart, line, colStart, col);
            }
        }
        return new Token(TokenType.EOF, "EOF", line, line, col, col);
    }

    private boolean isLetter() {
        return ((lookChar >= 'A' && lookChar <= 'Z')
                || (lookChar >= 'a' && lookChar <= 'z'));
    }

    private boolean isDigit() { return (lookChar >= '0' && lookChar <= '9'); }

    private boolean isEOF() { return currPos == file.length(); }

    private Token charLit(int lineStart, int colStart) {
        StringBuilder newChar = new StringBuilder();

        if(match('\\'))
            if(lookChar ==  'n' || lookChar == 'r' || lookChar == 't' || lookChar == '0')
                newChar.append('\\');

        if(isLetter()) {
            newChar.append(lookChar);
            consume();
        }

        if(!match('\''))
            return strLit(newChar, lineStart, colStart);

        return new Token(TokenType.CHAR_LIT, newChar.toString(), lineStart, line, colStart, col);
    }

    private Token strLit(StringBuilder newStr, int lineStart, int colStart) {
        while(!match('\'') && !isEOF()) {
            newStr.append(lookChar);
            consume();
        }
        if(match('\'') && match('\''))
            return new Token(TokenType.TEXT_LIT, newStr.toString(), lineStart, line, colStart, col);

        return new Token(TokenType.STR_LIT, newStr.toString(), lineStart, line, colStart, col);
    }

    private Token realLit(StringBuilder newReal, int lineStart, int colStart) {
        while(isDigit()) {
            newReal.append(lookChar);
            consume();
        }
        return new Token(TokenType.REAL_LIT, newReal.toString(), lineStart, line, colStart, col);
    }

    private Token createID(StringBuilder newID, int lineStart, int colStart) {
        while(isDigit() || isLetter()) {
            newID.append(lookChar);
            consume();
        }
        return new Token(TokenType.ID, newID.toString(), lineStart, line, colStart, col);
    }

    private Token name(int lineStart, int colStart) {
        StringBuilder createStr = new StringBuilder();

        while(isLetter() || match('#')) {
            createStr.append(lookChar);
            consume();
        }

        if(isDigit())
            return createID(createStr, lineStart, colStart);

        String nextStr = createStr.toString();
        return switch(nextStr) {
            // -----------------------------------------------------------------------------------------------
            //                                            KEYWORDS
            // -----------------------------------------------------------------------------------------------

            case "#include" -> new Token(TokenType.INCLUDE, "#include", lineStart, line, colStart, col);
            case "#exclude" -> new Token(TokenType.EXCLUDE, "#exclude", lineStart, line, colStart, col);
            case "abstr" -> new Token(TokenType.ABSTR, "abstr", lineStart, line, colStart, col);
            case "and" -> new Token(TokenType.AND, "and", lineStart, line, colStart, col);
            case "append" -> new Token(TokenType.APPEND, "append", lineStart, line, colStart, col);
            case "Array" -> new Token(TokenType.ARRAY, "Array", lineStart, line, colStart, col);
            case "Bool" -> new Token(TokenType.BOOL, "Bool", lineStart, line, colStart, col);
            case "cast" -> new Token(TokenType.CAST, "cast", lineStart, line, colStart, col);
            case "Char" -> new Token(TokenType.CHAR, "Char", lineStart, line, colStart, col);
            case "choice" -> new Token(TokenType.CHOICE, "choice", lineStart, line, colStart, col);
            case "cin" -> new Token(TokenType.CIN, "cin", lineStart, line, colStart, col);
            case "class" -> new Token(TokenType.CLASS, "class", lineStart, line, colStart, col);
            case "const" -> new Token(TokenType.CONST, "const", lineStart, line, colStart, col);
            case "cout" -> new Token(TokenType.COUT, "cout", lineStart, line, colStart, col);
            case "def" -> new Token(TokenType.DEF, "def", lineStart, line, colStart, col);
            case "discr" -> new Token(TokenType.DISCR, "discr", lineStart, line, colStart, col);
            case "do" -> new Token(TokenType.DO, "do", lineStart, line, colStart, col);
            case "else" -> new Token(TokenType.ELSE, "else", lineStart, line, colStart, col);
            case "endl" -> new Token(TokenType.ENDL, "endl", lineStart, line, colStart, col);
            case "except" -> new Token(TokenType.EXCEPT, "except", lineStart, line, colStart, col);
            case "final" -> new Token(TokenType.FINAL, "final", lineStart, line, colStart, col);
            case "for" -> new Token(TokenType.FOR, "for", lineStart, line, colStart, col);
            case "global" -> new Token(TokenType.GLOBAL, "global", lineStart, line, colStart, col);
            case "if" -> new Token(TokenType.IF, "if", lineStart, line, colStart, col);
            case "in" -> new Token(TokenType.IN, "in", lineStart, line, colStart, col);
            case "Int" -> new Token(TokenType.INT, "Int", lineStart, line, colStart, col);
            case "inherits" -> new Token(TokenType.INHERITS, "inherits", lineStart, line, colStart, col);
            case "inout" -> new Token(TokenType.INOUT, "inout", lineStart, line, colStart, col);
            case "inrev" -> new Token(TokenType.INREV, "inrev", lineStart, line, colStart, col);
            case "insert" -> new Token(TokenType.INSERT, "insert", lineStart, line, colStart, col);
            case "instanceof" -> new Token(TokenType.INSTANCEOF, "instanceof", lineStart, line, colStart, col);
            case "length" -> new Token(TokenType.LENGTH, "length", lineStart, line, colStart, col);
            case "List" -> new Token(TokenType.LIST, "List", lineStart, line, colStart, col);
            case "local" -> new Token(TokenType.LOCAL, "local", lineStart, line, colStart, col);
            case "loop" -> new Token(TokenType.LOOP, "loop", lineStart, line, colStart, col);
            case "main" -> new Token(TokenType.MAIN, "main", lineStart, line, colStart, col);
            case "method" -> new Token(TokenType.METHOD, "method", lineStart, line, colStart, col);
            case "new" -> new Token(TokenType.NEW, "new", lineStart, line, colStart, col);
            case "next" -> new Token(TokenType.NEXT, "next", lineStart, line, colStart, col);
            case "not" -> new Token(TokenType.NOT, "not", lineStart, line, colStart, col);
            case "on" -> new Token(TokenType.ON, "on", lineStart, line, colStart, col);
            case "only" -> new Token(TokenType.ONLY, "only", lineStart, line, colStart, col);
            case "operator" -> new Token(TokenType.OPERATOR, "operator", lineStart, line, colStart, col);
            case "or" -> new Token(TokenType.OR, "or", lineStart, line, colStart, col);
            case "other" -> new Token(TokenType.OTHER, "other", lineStart, line, colStart, col);
            case "out" -> new Token(TokenType.OUT, "out", lineStart, line, colStart, col);
            case "overload" -> new Token(TokenType.OVERLOAD, "overload", lineStart, line, colStart, col);
            case "override" -> new Token(TokenType.OVERRIDE, "override", lineStart, line, colStart, col);
            case "parent" -> new Token(TokenType.PARENT, "parent", lineStart, line, colStart, col);
            case "property" -> new Token(TokenType.PROPERTY, "property", lineStart, line, colStart, col);
            case "protected" -> new Token(TokenType.PROTECTED, "protected", lineStart, line, colStart, col);
            case "public" -> new Token(TokenType.PUBLIC, "public", lineStart, line, colStart, col);
            case "pure" -> new Token(TokenType.PURE, "pure", lineStart, line, colStart, col);
            case "Real" -> new Token(TokenType.REAL, "Real", lineStart, line, colStart, col);
            case "recurs" -> new Token(TokenType.RECURS, "recurs", lineStart, line, colStart, col);
            case "ref" -> new Token(TokenType.REF, "ref", lineStart, line, colStart, col);
            case "remove" -> new Token(TokenType.REMOVE, "remove", lineStart, line, colStart, col);
            case "rename" -> new Token(TokenType.RENAME, "rename", lineStart, line, colStart, col);
            case "return" -> new Token(TokenType.RETURN, "return", lineStart, line, colStart, col);
            case "scalar" -> new Token(TokenType.SCALAR, "scalar", lineStart, line, colStart, col);
            case "set" -> new Token(TokenType.SET, "set", lineStart, line, colStart, col);
            case "slice" -> new Token(TokenType.SLICE, "slice", lineStart, line, colStart, col);
            case "stop" -> new Token(TokenType.STOP, "stop", lineStart, line, colStart, col);
            case "String" -> new Token(TokenType.STRING, "String", lineStart, line, colStart, col);
            case "then" -> new Token(TokenType.THEN, "then", lineStart, line, colStart, col);
            case "Tuple" -> new Token(TokenType.TUPLE, "Tuple", lineStart, line, colStart, col);
            case "type" -> new Token(TokenType.TYPE, "type", lineStart, line, colStart, col);
            case "uninit" -> new Token(TokenType.UNINIT, "uninit", lineStart, line, colStart, col);
            case "until" -> new Token(TokenType.UNTIL, "until", lineStart, line, colStart, col);
            case "Void" -> new Token(TokenType.VOID, "Void", lineStart, line, colStart, col);
            case "while" -> new Token(TokenType.WHILE, "while", lineStart, line, colStart, col);

            // -----------------------------------------------------------------------------------------------
            //                                      BOOLEAN LITERALS
            // -----------------------------------------------------------------------------------------------

            case "true" -> new Token(TokenType.BOOL_LIT, "true", lineStart, line, colStart, col);
            case "false" -> new Token(TokenType.BOOL_LIT, "false", lineStart, line, colStart, col);

            default -> createID(createStr, lineStart, colStart);
        };
    }

    private Token number(int lineStart, int colStart) {
        StringBuilder newNum = new StringBuilder();
        while(isDigit()) {
            newNum.append(lookChar);
            consume();
        }

        if(match('.')) {
            newNum.append('.');
            if(isDigit())
                return realLit(newNum, lineStart, colStart);
            return new Token(TokenType.ERROR, "ERROR", lineStart, line, colStart, col);
        }

        return new Token(TokenType.INT_LIT, newNum.toString(), lineStart, line, colStart, col);
    }
}
