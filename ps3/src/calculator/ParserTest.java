package calculator;

public class ParserTest{

    public static void main(String[] args){
        testParser();
    }
    


public static void testParser(){
    //Test 1 "3+2.4"
    System.out.println("Test 1: 3+2.4");
    Lexer test1 = new Lexer("3+2.4");
    Parser test01 = new Parser(test1);
    Parser.Value value1 = test01.evaluate(test01.alltokens);
    System.out.println(value1.value);
    System.out.println(value1.type);

        
    //Test 2 "3in * 5.5in"
    System.out.println("Test 2: 3in * 5.5in");
    System.out.println("Expected answer: 16.5 INCHES");
    System.out.println("Expected answer is problematic b/c we need to divide by 72 squared");
    Parser test02 = new Parser(new Lexer("3in * 5.5in"));
    Parser.Value value2 = test02.evaluate(test02.alltokens);
    System.out.println(value2.value);
    System.out.println(value2.type);
    
    
            
    //Test 3 "3in + 5.5in"
    System.out.println("Test 3: 3in + 5.5in");
    System.out.println("Expected answer: 612.0 INCHES");
    Parser test03 = new Parser(new Lexer("3in + 5.5in"));
    Parser.Value value3 = test03.evaluate(test03.alltokens);
    System.out.println(value3.value);
    System.out.println(value3.type);
    
    
    //Test 4 "5 * (10in - 3in)"
    System.out.println("Test 4: 5 * (10in - 3in)");
    System.out.println("Expected answer 2520.0 INCHES");
    Parser test04 = new Parser(new Lexer("5 * (10in - 3in)"));
    Parser.Value value4 = test04.evaluate(test04.alltokens);
    System.out.println(value4.value);
    System.out.println(value4.type);
    
    //Test 5 "3 * (10pt + (10 / 2)pt)"
    System.out.println("test 5: 3 * (10pt + (10 / 2)pt)");
    System.out.println("expected answer 45 pt");
    Parser test05 = new Parser(new Lexer("3 * (10pt + (10 / 2)pt)"));
    Parser.Value value5 = test05.evaluate(test05.alltokens);
    System.out.println(value5.value);
    System.out.println(value5.type);
    
    //Test 6 - Testing an invalid expression
    System.out.println("Test 6: Expected Parser Error");
    System.out.println("Input: 3 + -2");
    try {
        Parser test06 = new Parser(new Lexer("3 + -2"));
        Parser.Value value6 = test06.evaluate(test06.alltokens);
        System.out.println("test failed - no exception occurred");
    } catch (Parser.ParserException p){
        System.out.println("Success! Parser exception occurred");
    }
    
    //Test 7 - Divide by zero error
    System.out.println("Test 7: Expected Parser Error");
    System.out.println("Input: 3/0");
    try {
        Parser test07 = new Parser(new Lexer("3/0"));
        Parser.Value value7 = test07.evaluate(test07.alltokens);
        System.out.println("test failed - no exception occurred");
    } catch (Parser.ParserException p){
        System.out.println(p.getMessage());
        System.out.println("Success! Parser exception occurred");
    }
    

}
}