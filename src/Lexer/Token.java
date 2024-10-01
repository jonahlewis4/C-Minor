package Lexer;

public class Token {
    // There are currently 118 possible different tokens
    public enum TokenType {
        EOF,        // $
        ERROR,      // Error


        /*
        -----------------------------
                   KEYWORDS
        -----------------------------
         */
        ABSTR,      // abstr
        APPEND,     // append
        ARRAY,      // Array
        BOOL,       // Bool
        CAST,       // cast
        CHAR,       // Char
        CHOICE,     // choice
        CIN,        // cin
        CLASS,      // class
        CONST,      // const
        COUT,       // cout
        DEF,        // def
        DISCR,      // discr
        DO,         // do
        ELSE,       // else
        ENDL,       // endl
        EXCEPT,     // except
        EXCLUDE,    // #exclude
        FINAL,      // final
        FOR,        // for
        GLOBAL,     // global
        IF,         // if
        IN,         // in
        INCLUDE,    // #include
        INT,        // Int
        INHERITS,   // inherits
        INOUT,      // inout
        INREV,      // inrev
        INSERT,     // insert
        LENGTH,     // length
        LIST,       // List
        LOCAL,      // local
        LOOP,       // loop
        MAIN,       // main
        METHOD,     // method
        NEW,        // new
        NEXT,       // next
        NOT,        // not
        ON,         // on
        ONLY,       // only
        OPERATOR,   // operator
        OTHER,      // other
        OUT,        // out
        OVERLOAD,   // overload
        OVERRIDE,   // override
        PARENT,     // parent
        PROPERTY,   // property
        PROTECTED,  // protected
        PUBLIC,     // public
        PURE,       // pure
        REAL,       // Real
        RECURS,     // recurs
        REF,        // ref
        REMOVE,     // remove
        RENAME,     // rename
        RETURN,     // return
        SCALAR,     // scalar
        SET,        // set
        SLICE,      // slice
        STOP,       // stop
        STRING,     // String
        THEN,       // then
        TUPLE,      // Tuple
        TYPE,       // type
        UNINIT,     // uninit
        UNTIL,      // until
        VOID,       // Void
        WHILE,      // while


        /*
        -----------------------------
                   LITERALS
        -----------------------------
        */
            /*
                letter = a | ... | z | A | ... | Z
                digit = 0 | ... | 9
                letter-digit = letter | digit
            */
        ID,         // letter letter-digit*
        INT_LIT,    // digit+
        REAL_LIT,   // digit digit* . digit+ | . digit+
        CHAR_LIT,   // 'char'
        STR_LIT,    // 'char*'
        TEXT_LIT,   // '''char*'''
        BOOL_LIT,   // true || false


        /*
        -----------------------------
                  OPERATORS
        -----------------------------
         */
        EQ,         // =
        PLUS,       // +
        MINUS,      // -
        MULT,       // *
        DIV,        // /
        MOD,        // %
        EXP,        // **
        TILDE,      // ~
        EQEQ,       // ==
        NEQ,        // !=
        LT,         // <
        LTEQ,       // <=
        GT,         // >
        GTEQ,       // >=
        LTGT,       // <>
        UFO,        // <=>
        MIN,        // <:
        MAX,        // :>
        INC,        // ..
        INSTANCEOF, // instanceof
        AND,        // and
        OR,         // or
        ELVIS,      // ?.
        BAND,       // &
        BOR,        // |
        XOR,        // ^
        SLEFT,      // <<
        SRIGHT,     // >>


        /*
        -----------------------------
                  SEPARATORS
        -----------------------------
         */
        LPAREN,     // (
        RPAREN,     // )
        LBRACE,     // {
        RBRACE,     // }
        LBRACK,     // [
        RBRACK,     // ]
        COLON,      // :
        PERIOD,     // .
        COMMA,      // ,
        AT,         // @
        ARROW,      // =>
    }

    // C Minor token has 6 fields
    private TokenType type;
    private String text;
    // Positional data for error messaging
    private int lineStart;
    private int lineEnd;
    private int colStart;
    private int colEnd;

    public Token(TokenType type, String text, int lStart, int lEnd, int cStart, int cEnd) {
        this.setTokenType(type);
        this.setText(text);
        this.setLineStart(lStart);
        this.setLineEnd(lEnd);
        this.setColStart(cStart);
        this.setColEnd(cEnd);
    }

    private void setTokenType(TokenType type) { this.type = type; }
    private void setText(String text) { this.text = text; }
    private void setLineStart(int lineStart) { this.lineStart = lineStart; }
    private void setLineEnd(int lineEnd) { this.lineEnd = lineEnd; }
    private void setColStart(int colStart) { this.colStart = colStart; }
    private void setColEnd(int colEnd) { this.colEnd = colEnd; }

    public TokenType getTokenType() { return this.type; }
    public String getText() { return this.text; }
    public int getLineStart() { return this.lineStart; }
    public int getLineEnd() { return this.lineEnd; }
    public int getColStart() { return this.colStart; }
    public int getColEnd() { return this.colEnd; }

    public void asString() {
        System.out.printf("\nToken: %s (%s)\tLines %s-%s\tColumns %s-%s",
                this.type, this.text, this.lineStart, this.lineEnd, this.colStart, this.colEnd);
    }
}
