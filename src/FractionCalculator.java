import java.util.Scanner;
/*
Coding Challenge

Write a command line program in the language of your choice that will take operations on fractions as an input
and produce a fractional result.
Legal operators shall be *, /, +, - (multiply, divide, add, subtract)
Operands and operators shall be separated by one or more spaces
Mixed numbers will be represented by whole_numerator/denominator. e.g. "3_1/4"
Improper fractions and whole numbers are also allowed as operands
Example run:
? 1/2 * 3_3/4
= 1_7/8

? 2_3/8 + 9/8
= 3_1/2
*/
class FractionCalculator {
	/*
	 * Node class is use to represent the mix fraction. A format mix fraction number hash follow property:
	 * 1. the sign of whole and numerator is same, like the - 3_1/2, the whole will be -3, numerator will be -1;
	 * 2. the denominator will always be positive;
	 * 3. the denominator will never be zero or negative;
	 * 4. improper fraction is allowed during calculation or as input, but after format, abs of numerator will be smaller than denominator
	 * */
  static class Node {
    int whole = 0;
    int numerator = 0;
    int denominator = 1;

    public Node() {

    }
    
    // like 2_3/8, whole will be 2, numerator will be 3, denominator will be 8
    public Node(String mix) {
    	int underScoreIdx = mix.indexOf('_');
		int slashIdx = mix.indexOf('/');
    	if (underScoreIdx < 0) {
    		if (slashIdx < 0) {
    			this.whole = Integer.parseInt(mix);
    		} else {
    			this.numerator = Integer.parseInt(mix.substring(0, slashIdx));
    			this.denominator = Integer.parseInt(mix.substring(slashIdx+1));
    		}
    	} else {
    		this.whole = Integer.parseInt(mix.substring(0, underScoreIdx));
    		this.numerator = Integer.parseInt(mix.substring(underScoreIdx+1, slashIdx));
    		this.denominator = Integer.parseInt(mix.substring(slashIdx+1));
    	}
    }
    
    public Node(int whole, int numerator, int denominator) {
        this.whole = whole;
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      if (whole != 0) {
        sb.append(whole);
      }
      if (whole != 0 && numerator != 0) {
    	  sb.append("_");
      }
      if (numerator != 0) {
    	  sb.append(numerator);
	      if (denominator != 0 && denominator != 1) {
	        sb.append('/').append(denominator);
	      }
      }
      return sb.toString();
    }

    public Node format() {
      // 4/4  5/4  1_6/4
      if (this.numerator >= this.denominator) {
    	  this.whole += this.numerator/this.denominator;
    	  this.numerator = numerator % this.denominator;
      }
      int gcd = gcd(this.numerator, this.denominator);
      this.numerator /= gcd;
      this.denominator /= gcd;
      // the sign of format fraction should be same
      if (this.whole * this.numerator < 0) {
        if (this.whole < 0) {
        	this.whole += 1;
        	this.numerator -= this.denominator;
        } else {
        	this.whole -= 1;
        	this.numerator += this.denominator;
        }
      }
      return this;
    }
  }// end Node

  // add
  public static Node add(Node left, Node right) {
    Node res = new Node();
    res.whole = left.whole + right.whole;
    int lcm = lcm(left.denominator, right.denominator);
    res.denominator = lcm;
    res.numerator = left.numerator * lcm/left.denominator + right.numerator * lcm/right.denominator;
    return res.format();
  }

  // multiple
  public static Node multiple(Node left, Node right) {
    Node res = new Node();
    res.numerator = (left.whole * left.denominator + left.numerator) * (right.whole * right.denominator + right.numerator);
    res.denominator = left.denominator * right.denominator;
    return res.format();
  }

  // greatest common divisor
  public static int gcd(int a, int b) {
    if (b == 0) return a;
    return gcd(b, a % b);
  }

  // least common multiple
  public static int lcm(int a, int b) {
    return (a*b) / gcd(b, a % b);
  }
  
  public static void helpInfo() {
	  System.out.println("Instruction:\n"
	  		+ "Legal operators shall be *, /, +, - (multiply, divide, add, subtract)\n"
			+ "Operands and operators shall be separated by one or more spaces\n"
	  		+ "Mixed numbers will be represented by whole_numerator/denominator. e.g. \"3_1/4\"\n"
	  		+ "Improper fractions and whole numbers are also allowed as operands\n"
	  		+ "Example run:\n"
	  		+ "? 1/2 * 3_3/4\n"
	  		+ "= 1_7/8\n\n"
	  		+ "? 2_3/8 + 9/8\n"
	  		+ "= 3_1/2\n"
	  		+ "h or help to print this info\n"
	  		+ "q or quit to exit this program!");
  }

  //? 1/2 * 3_3/4
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNextLine()) {
    	String input = scanner.nextLine();
    	String[] words = input.split("\\s+");
    	if (input.equals("q") || input.equals("quit")) {
    		return;
    	} else if (input.equals("h") || input.equals("help")) {
    		helpInfo();
    		continue;
    	}
    	if (words.length != 4) {
    		System.out.println("Please start with ? and seperate with space, mixed numbers will be represented by whole_numerator/denominator."
    				+ "like ? 1/2 * 3_3/4\n"
    				+ "type h or help for more help info\n");
    	} else {
    		Node left = new Node(words[1]);
    		Node right = new Node(words[3]);
    		String operator = words[2];
    		Node result;
    		if (operator.equals("-") || operator.equals("+")) {
    			if (operator.equals("-")) {
    				right.whole = -right.whole;
    				right.numerator = -right.numerator;
    			}
    			result = add(left, right);
    		} else {
    			if (operator.equals("/")) {
    				int denominator = right.denominator;
    				right.denominator = right.whole * denominator + right.numerator;
    				right.numerator = denominator;
    				right.whole = 0;
    				right.format();
    			}
    			result = multiple(left, right);
    		}
    		System.out.println("= "+ result.toString());
    	}
    	
    }
  }
}
