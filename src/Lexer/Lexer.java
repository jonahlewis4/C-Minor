package Lexer;

import Token.*;

/*
                    -------------------
                       C Minor Lexer
                    -------------------
________________________________________________________
*/
public class Lexer {

    private final String file;  // Program
    private int currPos;        // Position in file
    private char lookChar;      // Current lookahead
    private Location currLoc;   // Current location

    public static final char EOF = '\0';

    public Lexer(final String file) {
        this.file = file;
        this.currPos = 0;
        this.lookChar = file.charAt(currPos);
        this.currLoc = new Location();
    }

    private void consume() {
        currPos += 1;
        currLoc.addCol();
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
        while(lookChar == ' ' || lookChar == '\t' || lookChar == '\r' || lookChar == '\n') {
            if(lookChar == ' ' || lookChar == '\t')
                consume();
            else {
                consume();
                currLoc.addLine();
            }
        }
        currLoc.resetStart();
    }

    private void consumeComment() {
        while(lookChar != '\n')
            consume();
        consumeWhitespace();
    }

    public Token nextToken() {
        while(lookChar != EOF) {
            switch(lookChar) {
                case ' ', '\t', '\r', '\n':
                    consumeWhitespace();
                    break;
                case '=':
                    consume();
                    if(match('='))
                        return new Token(TokenType.EQEQ, "==", currLoc.copy());
                    if(match('>'))
                        return new Token(TokenType.ARROW, "=>", currLoc.copy());
                    return new Token(TokenType.EQ, "=", currLoc.copy());
                case '+':
                    consume();
                    if(match('='))
                        return new Token(TokenType.PLUSEQ, "+=", currLoc.copy());
                    return new Token(TokenType.PLUS, "+", currLoc.copy());
                case '-':
                    consume();
                    if(match('='))
                        return new Token(TokenType.MINUSEQ, "-=", currLoc.copy());
                    return new Token(TokenType.MINUS, "-", currLoc.copy());
                case '*':
                    consume();
                    if(match('*')) {
                        if(match('='))
                            return new Token(TokenType.EXPEQ, "**=", currLoc.copy());

                        return new Token(TokenType.EXP, "**", currLoc.copy());
                    }

                    if(match('='))
                        return new Token(TokenType.MULTEQ, "*=", currLoc.copy());

                    return new Token(TokenType.MULT, "*", currLoc.copy());
                case '/':
                    consume();
                    if(match('/')) {
                        consumeComment();
                        break;
                    }
                    if(match('='))
                        return new Token(TokenType.DIVEQ, "/=", currLoc.copy());

                    return new Token(TokenType.DIV, "/", currLoc.copy());
                case '~':
                    consume();
                    return new Token(TokenType.TILDE, "~", currLoc.copy());
                case '%':
                    consume();
                    if(match('='))
                        return new Token(TokenType.MODEQ, "%=", currLoc.copy());

                    return new Token(TokenType.MOD, "%", currLoc.copy());
                case '!':
                    consume();
                    if(match('='))
                        return new Token(TokenType.NEQ, "!=", currLoc.copy());
                    // Error
                    return new Token(TokenType.ERROR, "ERROR", currLoc.copy());
                case '<':
                    consume();
                    if(match('=')) {
                        if(match('>'))
                            return new Token(TokenType.UFO, "<=>", currLoc.copy());

                        return new Token(TokenType.LTEQ, "<=", currLoc.copy());
                    }

                    if(match('>'))
                        return new Token(TokenType.LTGT, "<>", currLoc.copy());

                    if(match(':'))
                        return new Token(TokenType.MIN, "<:", currLoc.copy());

                    if(match('<'))
                        return new Token(TokenType.SLEFT, "<<", currLoc.copy());

                    return new Token(TokenType.LT, "<", currLoc.copy());
                case '>':
                    consume();
                    if(match('='))
                        return new Token(TokenType.GTEQ, ">=", currLoc.copy());

                    if(match('>'))
                        return new Token(TokenType.SRIGHT, ">>", currLoc.copy());

                    return new Token(TokenType.GT, ">", currLoc.copy());
                case ':':
                    consume();
                    if(match('>'))
                        return new Token(TokenType.MAX, ":>", currLoc.copy());

                    return new Token(TokenType.COLON, ":", currLoc.copy());
                case '.':
                    consume();
                    if(isDigit())
                        return realLit(new StringBuilder("."));
                    if(match('.'))
                        return new Token(TokenType.INC, "..", currLoc.copy());

                    return new Token(TokenType.PERIOD, ".", currLoc.copy());
                case ',':
                    consume();
                    return new Token(TokenType.COMMA, ",", currLoc.copy());
                case '(':
                    consume();
                    return new Token(TokenType.LPAREN, "(", currLoc.copy());
                case ')':
                    consume();
                    return new Token(TokenType.RPAREN, ")", currLoc.copy());
                case '{':
                    consume();
                    return new Token(TokenType.LBRACE, "{", currLoc.copy());
                case '}':
                    consume();
                    return new Token(TokenType.RBRACE, "}", currLoc.copy());
                case '[':
                    consume();
                    return new Token(TokenType.LBRACK, "[", currLoc.copy());
                case ']':
                    consume();
                    return new Token(TokenType.RBRACK, "]", currLoc.copy());
                case '@':
                    consume();
                    return new Token(TokenType.AT, "@", currLoc.copy());
                case '\'':
                    consume();
                    if(match('\'') && match('\''))
                        return strLit(new StringBuilder());
                    return charLit();
                case '?':
                    consume();
                    if(match('.'))
                        return new Token(TokenType.ELVIS, "?.", currLoc.copy());
                    else return new Token(TokenType.ERROR, "ERROR", currLoc.copy());
                case '|':
                    consume();
                    return new Token(TokenType.BOR, "|", currLoc.copy());
                case '&':
                    consume();
                    return new Token(TokenType.BAND, "&", currLoc.copy());
                case '^':
                    consume();
                    return new Token(TokenType.XOR, "^", currLoc.copy());
                default:
                    if(isLetter() || match('#'))
                        return name();
                    if(isDigit())
                        return number();

                    return new Token(TokenType.ERROR, "ERROR", currLoc.copy());
            }
        }
        return new Token(TokenType.EOF, "EOF", currLoc.copy());
    }

    private boolean isLetter() { return ((lookChar >= 'A' && lookChar <= 'Z')
                                     || (lookChar >= 'a' && lookChar <= 'z'));
    }

    private boolean isDigit() { return (lookChar >= '0' && lookChar <= '9'); }

    private boolean isEOF() { return currPos == file.length(); }

    private Token charLit() {
        StringBuilder newChar = new StringBuilder();

        if(match('\\'))
            if(lookChar ==  'n' || lookChar == 'r' || lookChar == 't' || lookChar == '0')
                newChar.append('\\');

        if(isLetter()) {
            newChar.append(lookChar);
            consume();
        }

        if(!match('\''))
            return strLit(newChar);

        return new Token(TokenType.CHAR_LIT, newChar.toString(), currLoc.copy());
    }

    private Token strLit(StringBuilder newStr) {
        while(!match('\'') && !isEOF()) {
            newStr.append(lookChar);
            consume();
        }
        if(match('\'') && match('\''))
            return new Token(TokenType.TEXT_LIT, newStr.toString(), currLoc.copy());

        return new Token(TokenType.STR_LIT, newStr.toString(), currLoc.copy());
    }

    private Token realLit(StringBuilder newReal) {
        while(isDigit()) {
            newReal.append(lookChar);
            consume();
        }
        return new Token(TokenType.REAL_LIT, newReal.toString(), currLoc.copy());
    }

    private Token createID(StringBuilder newID) {
        while(isDigit() || isLetter()) {
            newID.append(lookChar);
            consume();
        }
        return new Token(TokenType.ID, newID.toString(), currLoc.copy());
    }

    private Token name() {
        StringBuilder createStr = new StringBuilder();

        while(isLetter() || match('#')) {
            createStr.append(lookChar);
            consume();
        }

        if(isDigit())
            return createID(createStr);

        String nextStr = createStr.toString();
        return switch(nextStr) {
            // -----------------------------------------------------------------------------------------------
            //                                            KEYWORDS
            // -----------------------------------------------------------------------------------------------

            case "#include" -> new Token(TokenType.INCLUDE, "#include", currLoc.copy());
            case "#exclude" -> new Token(TokenType.EXCLUDE, "#exclude", currLoc.copy());
            case "abstr" -> new Token(TokenType.ABSTR, "abstr", currLoc.copy());
            case "and" -> new Token(TokenType.AND, "and", currLoc.copy());
            case "append" -> new Token(TokenType.APPEND, "append", currLoc.copy());
            case "Array" -> new Token(TokenType.ARRAY, "Array", currLoc.copy());
            case "Bool" -> new Token(TokenType.BOOL, "Bool", currLoc.copy());
            case "cast" -> new Token(TokenType.CAST, "cast", currLoc.copy());
            case "Char" -> new Token(TokenType.CHAR, "Char", currLoc.copy());
            case "choice" -> new Token(TokenType.CHOICE, "choice", currLoc.copy());
            case "cin" -> new Token(TokenType.CIN, "cin", currLoc.copy());
            case "class" -> new Token(TokenType.CLASS, "class", currLoc.copy());
            case "const" -> new Token(TokenType.CONST, "const", currLoc.copy());
            case "cout" -> new Token(TokenType.COUT, "cout", currLoc.copy());
            case "def" -> new Token(TokenType.DEF, "def", currLoc.copy());
            case "discr" -> new Token(TokenType.DISCR, "discr", currLoc.copy());
            case "do" -> new Token(TokenType.DO, "do", currLoc.copy());
            case "else" -> new Token(TokenType.ELSE, "else", currLoc.copy());
            case "endl" -> new Token(TokenType.ENDL, "endl", currLoc.copy());
            case "except" -> new Token(TokenType.EXCEPT, "except", currLoc.copy());
            case "final" -> new Token(TokenType.FINAL, "final", currLoc.copy());
            case "for" -> new Token(TokenType.FOR, "for", currLoc.copy());
            case "global" -> new Token(TokenType.GLOBAL, "global", currLoc.copy());
            case "if" -> new Token(TokenType.IF, "if", currLoc.copy());
            case "in" -> new Token(TokenType.IN, "in", currLoc.copy());
            case "Int" -> new Token(TokenType.INT, "Int", currLoc.copy());
            case "inherits" -> new Token(TokenType.INHERITS, "inherits", currLoc.copy());
            case "inout" -> new Token(TokenType.INOUT, "inout", currLoc.copy());
            case "inrev" -> new Token(TokenType.INREV, "inrev", currLoc.copy());
            case "insert" -> new Token(TokenType.INSERT, "insert", currLoc.copy());
            case "instanceof" -> new Token(TokenType.INSTANCEOF, "instanceof", currLoc.copy());
            case "length" -> new Token(TokenType.LENGTH, "length", currLoc.copy());
            case "List" -> new Token(TokenType.LIST, "List", currLoc.copy());
            case "local" -> new Token(TokenType.LOCAL, "local", currLoc.copy());
            case "loop" -> new Token(TokenType.LOOP, "loop", currLoc.copy());
            case "main" -> new Token(TokenType.MAIN, "main", currLoc.copy());
            case "method" -> new Token(TokenType.METHOD, "method", currLoc.copy());
            case "new" -> new Token(TokenType.NEW, "new", currLoc.copy());
            case "next" -> new Token(TokenType.NEXT, "next", currLoc.copy());
            case "not" -> new Token(TokenType.NOT, "not", currLoc.copy());
            case "on" -> new Token(TokenType.ON, "on", currLoc.copy());
            case "only" -> new Token(TokenType.ONLY, "only", currLoc.copy());
            case "operator" -> new Token(TokenType.OPERATOR, "operator", currLoc.copy());
            case "or" -> new Token(TokenType.OR, "or", currLoc.copy());
            case "other" -> new Token(TokenType.OTHER, "other", currLoc.copy());
            case "out" -> new Token(TokenType.OUT, "out", currLoc.copy());
            case "overload" -> new Token(TokenType.OVERLOAD, "overload", currLoc.copy());
            case "override" -> new Token(TokenType.OVERRIDE, "override", currLoc.copy());
            case "parent" -> new Token(TokenType.PARENT, "parent", currLoc.copy());
            case "property" -> new Token(TokenType.PROPERTY, "property", currLoc.copy());
            case "protected" -> new Token(TokenType.PROTECTED, "protected", currLoc.copy());
            case "public" -> new Token(TokenType.PUBLIC, "public", currLoc.copy());
            case "pure" -> new Token(TokenType.PURE, "pure", currLoc.copy());
            case "Real" -> new Token(TokenType.REAL, "Real", currLoc.copy());
            case "recurs" -> new Token(TokenType.RECURS, "recurs", currLoc.copy());
            case "ref" -> new Token(TokenType.REF, "ref", currLoc.copy());
            case "remove" -> new Token(TokenType.REMOVE, "remove", currLoc.copy());
            case "rename" -> new Token(TokenType.RENAME, "rename", currLoc.copy());
            case "return" -> new Token(TokenType.RETURN, "return", currLoc.copy());
            case "scalar" -> new Token(TokenType.SCALAR, "scalar", currLoc.copy());
            case "set" -> new Token(TokenType.SET, "set", currLoc.copy());
            case "slice" -> new Token(TokenType.SLICE, "slice", currLoc.copy());
            case "stop" -> new Token(TokenType.STOP, "stop", currLoc.copy());
            case "String" -> new Token(TokenType.STRING, "String", currLoc.copy());
            case "then" -> new Token(TokenType.THEN, "then", currLoc.copy());
            case "Tuple" -> new Token(TokenType.TUPLE, "Tuple", currLoc.copy());
            case "type" -> new Token(TokenType.TYPE, "type", currLoc.copy());
            case "uninit" -> new Token(TokenType.UNINIT, "uninit", currLoc.copy());
            case "until" -> new Token(TokenType.UNTIL, "until", currLoc.copy());
            case "Void" -> new Token(TokenType.VOID, "Void", currLoc.copy());
            case "while" -> new Token(TokenType.WHILE, "while", currLoc.copy());

            // -----------------------------------------------------------------------------------------------
            //                                      BOOLEAN LITERALS
            // -----------------------------------------------------------------------------------------------

            case "true" -> new Token(TokenType.BOOL_LIT, "true", currLoc.copy());
            case "false" -> new Token(TokenType.BOOL_LIT, "false", currLoc.copy());

            default -> createID(createStr);
        };
    }

    private Token number() {
        StringBuilder newNum = new StringBuilder();
        while(isDigit()) {
            newNum.append(lookChar);
            consume();
        }

        if(match('.')) {
            newNum.append('.');
            if(isDigit())
                return realLit(newNum);
            return new Token(TokenType.ERROR, "ERROR", currLoc.copy());
        }

        return new Token(TokenType.INT_LIT, newNum.toString(), currLoc.copy());
    }
}
