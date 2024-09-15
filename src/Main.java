
import Lexer.Lexer;
import Parser.Parser;

public class Main {
    public static void main(String[] args) {
        String input = """
             Enum  {alice, bob, mary, cathy, problem}
             """;

        var lexer = new Lexer(input);
//        var token = lexer.nextToken();
//        while(token.getTokenType() != Lexer.TokenType.EOF) {
//            token.asString();
//            token = lexer.nextToken();
//            if(token.getTokenType() == Lexer.TokenType.ERROR) {
//                break;
//            }
//        }
        var parser = new Parser(lexer);
        parser.compilation();
        System.out.println("Parsing is complete...");

    }
}
