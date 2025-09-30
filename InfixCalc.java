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
		pfix.setText("postfix expression");
		pfix.setFont(projectFont);
		
		answer.setBounds(5, 200, 200, 40);
		answer.setText("answer");
		answer.setFont(projectFont);
		
		b.setBounds(5, 80, 200, 40);
		b.setText("calculate");
		b.setBackground(Color.lightGray);
		b.setBorder(BorderFactory.createBevelBorder(0));
		b.setFont(projectFont);
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				URQueue<Character> postfixQueue = infixToPostfix(ifix.getText().toCharArray());
				pfix.setText(postfixQueue.toString());
			}
			
		});
	}
	
	public static double evaluateostfix(URQueue<Character> queue) {
		double output = 0;
		URStack<Double> stack = new URStack<Double>();
		
		while (!queue.isEmpty()) {
			Character c = queue.peek();
			if (c >= '0' && c <= '9') stack.push(Double.valueOf(queue.dequeue()));
			else if (c == '!') { //checks if the operator only takes one input
				Double tempDoub = stack.pop();
				if (tempDoub == 1 || tempDoub == 0) tempDoub = (double) ~tempDoub.intValue(); //bit flips for not operator
			}
		}
		
		return output;
	}
	
	public static URQueue<Character> infixToPostfix(char[] input) { //performs infix-to-postfix algorithm
		URQueue<Character> outputQueue = new URQueue<Character>();
		URStack<Character> stack = new URStack<Character>();
		
		for (int i = 0; i < input.length; i++) {
			Character c = input[i];
			
			//check is c is an operator or operand first
			if (c >= '0' && c <= '9') outputQueue.enqueue(c); //if operand, add to the queue
			else if (c == '(') stack.push(c); //if an open parenthesis, push to the stack
			else if (c == ')') { //if a closed parenthesis, pop from the stack until the open parenthesis is hit
				while (!stack.isEmpty() && stack.peek() != '(') outputQueue.enqueue(c);
				
				stack.pop();
			}
			else if (c != ' ') { // if an operator, enqueue operators until an operator of less priority is hit (spaces are ignored)
				while (getPriority(c) < getPriority(stack.peek())) {
					outputQueue.enqueue(stack.pop());
				}
				outputQueue.enqueue(stack.pop());
				stack.push(c);
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
