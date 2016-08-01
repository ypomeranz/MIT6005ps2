package calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Multi-unit calculator.
 */
public class MultiUnitCalculator {

	/**
	 * @param expression
	 *            a String representing a multi-unit expression, as defined in
	 *            the problem set
	 * @return the value of the expression, as a number possibly followed by
	 *         units, e.g. "72pt", "3", or "4.882in"
	 */
	public String evaluate(String expression) {
		try{
			Parser parser = new Parser(new Lexer(expression));
			Parser.Value answer = parser.evaluate(parser.alltokens);
			return answer.toString();
		}
		catch (Lexer.BadTokenException b){
			System.out.println("Lexer failed to recognize part of expression");
			
		} catch (Parser.ParserException p){
			System.out.println("Error with Parser with message:");
			System.out.println(p.getMessage());
		
		}
		return null;
		
	}

	/**
	 * Repeatedly reads expressions from the console, and outputs the results of
	 * evaluating them. Inputting an empty line will terminate the program.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) throws IOException {
		MultiUnitCalculator calculator;
		String result;

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String expression;
		while (true) {
			// display prompt
			System.out.print("> ");
			// read input
			expression = in.readLine();
			// terminate if input empty
			if (expression.equals(""))
				break;

			// evaluate
			calculator = new MultiUnitCalculator();
			result = calculator.evaluate(expression);
			// display result
			System.out.println(result);
		}
	}
}
