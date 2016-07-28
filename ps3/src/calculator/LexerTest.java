package calculator;

public class LexerTest{

    public static void main(String[] args){
        testLexer();
    }
    


public static void testLexer(){
    //Test 1 "3+2.4"
    System.out.println("Test 1: 3+2.4");
    Lexer test1 = new Lexer("3+2.4");
    boolean done = false;
    while (done == false){
        Lexer.Token x=test1.next();
        if (x.text == ""){
            done = true;
        }
        System.out.println(x.type);
        System.out.println(x.text);
    }
        
    //Test 2 "3 + 2.4"
    System.out.println("Test 2: 3 + 2.4");
    Lexer test2 = new Lexer("3 + 2.4");
    done = false;
    while (done == false){
        Lexer.Token x=test2.next();
        if (x.text == ""){
            done = true;
        }
        System.out.println(x.type);
        System.out.println(x.text);
    }
    
    //Test 3 "(3 +4)pt * 2.4in"
    System.out.println("Test 3: (3 +4)pt * 2.4in");
    Lexer test3 = new Lexer("(3 +4)pt * 2.4in");
    done = false;
    while (done == false){
        Lexer.Token x=test3.next();
        if (x.text == ""){
            done = true;
        }
        System.out.println(x.type);
        System.out.println(x.text);
    }
    
    //Test 4"(3+4)icm * 2.456 + 2.345.349pt"
    System.out.println("(3+4)icm * 2.456 + 2.345.349pt");
    Lexer test4 = new Lexer("(3+4)icm * 2.456 + 2.345.349pt");
    done = false;
    while (done == false){
        Lexer.Token x=test4.next();
        if (x.text == ""){
            done = true;
        }
        System.out.println(x.type);
        System.out.println(x.text);
    }

}
}