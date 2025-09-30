import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.*;

public class InfixCalc {

	public static void main(String[] args) { //sets up Java Swing GUI elements and adds button action event
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(300, 100, 300, 300);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel ifixInputLabel = new JLabel();
		JTextField ifix = new JTextField();
		JLabel pfix = new JLabel();
		JLabel answer = new JLabel();
		JButton b = new JButton();
		frame.add(ifixInputLabel);
		frame.add(pfix);
		frame.add(ifix);
		frame.add(answer);
		frame.add(b);
		
		ifixInputLabel.setBounds(5, 0, 200, 20);
		ifixInputLabel.setText("enter infix expression:");
		
		Font projectFont = new Font("Arial", Font.PLAIN, 20);
		ifix.setBounds(5, 20, 200, 40);
		ifix.setFocusCycleRoot(true);
		ifix.setFont(projectFont);
		
		pfix.setBounds(5, 140, 200, 40);
		pfix.setFont(projectFont);
		
		answer.setBounds(5, 200, 200, 40);
		answer.setFont(projectFont);
		
		b.setBounds(5, 80, 200, 40);
		b.setText("calculate");
		b.setBackground(Color.lightGray);
		b.setBorder(BorderFactory.createBevelBorder(0));
		b.setFont(projectFont);
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				URQueue<String> postfixQueue = infixToPostfix(ifix.getText());
				pfix.setText(postfixQueue.toString());
				answer.setText(String.valueOf(evaluatePostfix(postfixQueue)));
			}
			
		});
	}
	
	public static double evaluatePostfix(URQueue<String> queue) {
		double output = 0;
		URStack<Double> stack = new URStack<Double>();
		
		while (!queue.isEmpty()) {
			String s = queue.dequeue();
			char c = s.substring(0, 1).charAt(0);
			if (c >= 48 && c <= 57) stack.push(Double.valueOf(s));
			else if (s == "!") { //checks if the operator only takes one input
				Double tempDoub = stack.pop();
				if (tempDoub == 1 || tempDoub == 0) tempDoub = (double) ~tempDoub.intValue(); //bit flips for not operator
				else return 0;
				output = tempDoub;
			}
			else if (stack.size() != 1) {
				Double d1 = stack.pop();
				Double d2 = stack.pop();
				try {
				output = performOperation(s, d1, d2);
				} catch (InvalidOperatorException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
				
				stack.push(output);
			}
		}
		return output;
	}
	
	public static double performOperation(String operation, double d1, double d2) throws InvalidOperatorException {
		switch (operation) {
		case "+": return d1 + d2;
		case "-": return d1 - d2;
		case "*": return d1 * d2;
		case "/": return d1 / d2;
		case "^": return Math.pow(d1,  d2);
		case "%": return (int) d1 % (int) d2;
		case ">": return (d1 > d2) ? 1 : 0; //ternary operator to map true to one and false to 0
		case "<": return (d1 < d2) ? 1 : 0;
		case "=": return (d1 == d2) ? 1 : 0;
		
		//for logical operators, the inputs must be ensured to be either 1 or 0
		case "&":
			if ((d1 == 1 || d1 == 0) && (d2 == 1 || d2 == 0)) return ((d1 != 0) && (d2 != 0)) ? 1 : 0;
			else throw new InvalidOperatorException("one or more inputs for a logical operator are not 0 or 1");
		case "|":
			if ((d1 == 1 || d1 == 0) && (d2 == 1 || d2 == 0)) return ((d1 != 0) || (d2 != 0)) ? 1 : 0;
			else throw new InvalidOperatorException("one or more inputs for a logical operator are not 0 or 1");
		}
		throw new InvalidOperatorException("not a known operator");
	}
	
	
	
	public static URQueue<String> infixToPostfix(String input) { //performs infix-to-postfix algorithm
		URQueue<String> outputQueue = new URQueue<String>();
		URStack<String> stack = new URStack<String>();
		
		for (int i = 0; i < input.length(); i++) {
			String s = input.substring(i, i + 1);
			
			//check is c is an operator or operand first
			if (s.charAt(0) >= '0' && s.charAt(0) <= '9') {
				char nextChar = s.charAt(0);
				if (input.length() > i + 1) nextChar = input.substring(i + 1, i + 2).charAt(0);
				while (((nextChar >= '0' && nextChar <= '9') || nextChar == '.') && input.length() > i + 1) { //handles numbers with more than one digit and numbers with decimal values
					i++;
					s += nextChar;
					nextChar = input.substring(i + 1, i + 2).charAt(0);
				}
				outputQueue.enqueue(s); //if operand, add to the queue
			}
			else if (s == "(") stack.push(s); //if an open parenthesis, push to the stack
			else if (s == ")") { //if a closed parenthesis, pop from the stack until the open parenthesis is hit
				while (!stack.isEmpty() && stack.peek() != "(") outputQueue.enqueue(s);
				
				stack.pop();
			}
			else if (s != " ") { // if an operator, enqueue operators until an operator of less priority is hit (spaces are ignored)
				while (!stack.isEmpty() && getPriority(s.charAt(0)) > getPriority(stack.peek().charAt(0))) {
					outputQueue.enqueue(stack.pop());
				}
				//if (!stack.isEmpty()) outputQueue.enqueue(stack.pop());
				stack.push(s);
			}
		}
		while (!stack.isEmpty()) outputQueue.enqueue(stack.pop());

		return outputQueue;
	}
	
	public static int getPriority(Character c) {
		//priority form https://en.wikipedia.org/wiki/Order_of_operations#Programming_languages
		// highest priority gets the smallest return value to allow easy expansion to further operators
		if (c == '^') return 1;
		if (c == '!') return 2;
		if (c == '/' || c == '*' || c == '%') return 3;
		if (c == '-' || c == '+') return 4;
		if (c == '>' || c == '<') return 5;
		if (c == '=') return 6;
		if (c == '&') return 7;
		if (c == '|') return 8;
		else return Integer.MAX_VALUE; //throw lowest possible priority if input is not an operator/ an unknown operator
	}
}
