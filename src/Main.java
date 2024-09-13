
import Lexer.Lexer;
import Parser.Parser;

public class Main {
    public static void main(String[] args) {
        String input = """
             < > <> <= >= [ ] {{ } } / % + *
             hello abstr return
             Array cast length hello'
             'hello world!' 123
             4567
             56.789 .4567
             """;
        var lexer = new Lexer(input);

        /*var token = lexer.nextToken();
        while(token.getTokenType() != Lexer.TokenType.EOF) {
            token.asString();
            token = lexer.nextToken();
            if(token.getTokenType() == Lexer.TokenType.ERROR) {
                break;
            }
        }*/

        var parser = new Parser(lexer);
        parser.compilation();
        System.out.println("Parsing is complete...");

    }
}
