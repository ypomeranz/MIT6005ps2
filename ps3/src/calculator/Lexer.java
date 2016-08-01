package calculator;

import calculator.Type;

/**
 * Calculator lexical analyzer.
 */
public class Lexer {
	private int i;
	private String s;
	private String[] numbers = {"0","1","2","3","4","5","6","7","8","9"};

	/**
	 * Token in the stream.
	 */
	public static class Token {
		final Type type;
		final String text;

		Token(Type type, String text) {
			this.type = type;
			this.text = text;
		}

		Token(Type type) {
			this(type, null);
		}
	}

	@SuppressWarnings("serial")
	static class TokenMismatchException extends Exception {
	}
	
	static class BadTokenException extends RuntimeException{
		
	}

	// TODO write method spec
	public Lexer(String s) {
		this.s = s;
	}
	
	public Token next() {
		if (i >= s.length()){
			return new Token(Type.EOF, "");
		}
		
		String onechar = s.substring(i,i+1);
		//Next line for debugging
		//System.out.println(onechar);
		if (onechar.charAt(0) == ' '){
			i++;
		} else if (java.util.Arrays.asList(numbers).contains(onechar) == true) {
			boolean useddecimal = false;
			int start = i;
			i++;
			if (i == s.length()){
				return new Token(Type.NUMBER, s.substring(start, start+1));
			}
			while (java.util.Arrays.asList(numbers).contains(s.substring(i,i+1)) == true | (useddecimal == false && s.charAt(i)=='.')){
				if (s.charAt(i) == '.'){
					useddecimal = true;
				}
				i++;
				if (i == s.length()){
					return new Token(Type.NUMBER, s.substring(start, i));
				}
			}
			//making sure we get the right substring - debugging - remove eventually
			//System.out.println(s.substring(start, i));
			return new Token(Type.NUMBER, s.substring(start, i));
			
		} else if (onechar.charAt(0) == '+' | onechar.charAt(0) == '-' | onechar.charAt(0) == '*' | onechar.charAt(0) == '/') {
			//System.out.println("operator detected");
			i++;
			return new Token(Type.OPERATOR, onechar);
		} else if (onechar.charAt(0) == '('){
			i++;
			return new Token(Type.OPENPAREN, onechar);
		} else if (onechar.charAt(0) == ')'){
			i++;
			return new Token(Type.CLOSEPAREN, onechar);
		} else if (onechar.charAt(0) == 'i'){
			i++;
			if (s.substring(i,i+1).charAt(0) == 'n'){
				i++;
				return new Token(Type.UNIT, "in");
			} else {
				throw new BadTokenException();
			}
			
		} else if (onechar.charAt(0) == 'p'){
			i++;
			if (s.substring(i,i+1).charAt(0) == 't'){
				i++;
				return new Token(Type.UNIT, "pt");
			} else {
				throw new BadTokenException();
			}
		} else {
			
			throw new BadTokenException();
			
		}
			
		
	
	return next();
	}

}

