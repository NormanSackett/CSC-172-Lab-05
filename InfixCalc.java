import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;

public class InfixCalc {

	public static void main(String[] args) { //sets up Java Swing GUI elements and adds button action event
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(300, 100, 400, 300);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// declare objects and add them to frame
		JLabel ifixInputLabel = new JLabel();
		JTextField ifix = new JTextField();
		JLabel pfix = new JLabel();
		JLabel answer = new JLabel();
		JButton b = new JButton();
		JButton fileButton = new JButton();
		JButton downloadButton = new JButton();
		frame.add(ifixInputLabel);
		frame.add(pfix);
		frame.add(ifix);
		frame.add(answer);
		frame.add(b);
		frame.add(fileButton);
		frame.add(downloadButton);
		
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
				try {
				URQueue<String> postfixQueue = infixToPostfix(ifix.getText().toLowerCase());
				pfix.setText(postfixQueue.toString());
				double output = evaluatePostfix(postfixQueue);
				
				if (output == Math.floor(output)) answer.setText(String.valueOf((int) output)); //gets rid of yucky decimal point if the output is an integer value
				else answer.setText(String.valueOf(output));
				
				} catch(InvalidOperatorException except) {
					JOptionPane.showMessageDialog(null, except.getMessage());
				}
			}});
		downloadButton.setBounds(220, 80, 150, 40);
		downloadButton.setText("download output file");
		downloadButton.setBackground(Color.lightGray);
		downloadButton.setBorder(BorderFactory.createBevelBorder(0));
		downloadButton.setVisible(false);
		
		fileButton.setBounds(220, 20, 150, 40);
		fileButton.setText("upload file");
		fileButton.setBackground(Color.lightGray);
		fileButton.setBorder(BorderFactory.createBevelBorder(0));
		fileButton.setFont(projectFont);
		fileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { //handles GUI aspects of file input
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				fileChooser.setBackground(Color.lightGray);
				fileChooser.setBorder(BorderFactory.createBevelBorder(0));
				//sets the default file view to scroll vertically
				fileChooser.getActionMap().get("viewTypeDetails").actionPerformed(new ActionEvent(fileChooser, ActionEvent.ACTION_PERFORMED, ""));
				fileChooser.setFont(projectFont);
				
				if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					File f = fileChooser.getSelectedFile();
					try {
						String[] output = getFileInput(f);
						downloadButton.setVisible(true);
						
						downloadButton.addActionListener( new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if (output != null) {
									try {
										String path = System.getProperty("user.home") + "\\Downloads\\INFIX_TO_POSTFIX_CALC_OUTPUT.txt";
										FileWriter writer = new FileWriter(path);
										for (int i = 0; i < output.length; i++) {
											writer.write(output[i] + "\n");
										}
										writer.close();
									} catch (IOException err) {
										err.printStackTrace();
									}
									downloadButton.setVisible(false);
								}
							}});
					} catch (InvalidOperatorException err) {
						JOptionPane.showMessageDialog(null, err.getMessage());
					}
				}
			}});
	}
	
	public static String[] getFileInput(File f) throws InvalidOperatorException {
		try {
			String type = Files.probeContentType(f.toPath());
			if (type.contains("text")) {
				BufferedReader reader = new BufferedReader(new FileReader(f));
				int lineNum = (int) Files.lines(f.toPath()).count();
				String[] output = new String[lineNum];
				String line;
				for (int i = 0; i < lineNum; i++) {
					line = reader.readLine();
					output[i] = String.valueOf(evaluatePostfix(infixToPostfix(line)));
				}
				reader.close();
				return output;
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return null; //if there is an error, do not return outputs
	}
	
	public static double evaluatePostfix(URQueue<String> queue) throws InvalidOperatorException {
		double output = 0;
		URStack<Double> stack = new URStack<Double>();
		
		while (!queue.isEmpty()) {
			String s = queue.dequeue();
			char c = s.substring(0, 1).charAt(0);
			if (c >= 48 && c <= 57) stack.push(Double.valueOf(s));
			else if (s.equals("!") || s.equals("sin") || s.equals("cos") || s.equals("tan")) { //checks for operators that take one input
				output = performOperation(s, stack.pop());
				stack.push(output);
			}
			else if (stack.size() != 1) {
				Double d1 = stack.pop();
				Double d2 = stack.pop();
				try {
				output = performOperation(s, d2, d1);
				} catch (InvalidOperatorException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
				
				stack.push(output);
			}
		}
		return output;
	}
	
	public static double performOperation(String operation, double d) throws InvalidOperatorException {
		switch (operation) {
		case "!":
			if (d == 1 || d == 0) return ~(int) d + 2;
			else throw new InvalidOperatorException("the ! logical operator requires inputs of 0 or 1");
		case "sin": return Math.sin(d);
		case "cos": return Math.cos(d);
		case "tan": return Math.tan(d);
		}
		throw new InvalidOperatorException("\"" + operation + "\" is not a known operator");
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
			else throw new InvalidOperatorException("the & logical operator requires inputs of 0 or 1");
		case "|":
			if ((d1 == 1 || d1 == 0) && (d2 == 1 || d2 == 0)) return ((d1 != 0) || (d2 != 0)) ? 1 : 0;
			else throw new InvalidOperatorException("the | logical operator requires inputs of 0 or 1");
		}
		throw new InvalidOperatorException("\"" + operation + "\" is not a known operator");
	}
	
	
	
	public static URQueue<String> infixToPostfix(String input) throws InvalidOperatorException { //performs infix-to-postfix algorithm
		URQueue<String> outputQueue = new URQueue<String>();
		URStack<String> stack = new URStack<String>();
		
		for (int i = 0; i < input.length(); i++) {
			String s = input.substring(i, i + 1);
			if (!s.equals(" ")) { //ignore spaces
				
				//check is c is an operator or operand first
				if (s.charAt(0) >= '0' && s.charAt(0) <= '9') {
					if (input.length() > i + 1) {
						char nextChar = input.substring(i + 1, i + 2).charAt(0);
						while (((nextChar >= '0' && nextChar <= '9') || nextChar == '.') && input.length() > i + 1) { //handles numbers with more than one digit and numbers with decimal values
							i++;
							s += nextChar;
							if (input.length() > i + 1) nextChar = input.substring(i + 1, i + 2).charAt(0);
							else nextChar = 0;
						}
					}
					outputQueue.enqueue(s); //if operand, add to the queue
				}
				else if (s.equals("(")) stack.push(s); //if an open parenthesis, push to the stack
				else if (s.equals(")")) { //if a closed parenthesis, pop from the stack until the open parenthesis is hit
					while (!stack.peek().equals("(")) {
						if (stack.isEmpty()) throw new InvalidOperatorException("missing parenthesis");
						outputQueue.enqueue(stack.pop());
					}
					stack.pop();
				}
				else {
					if (input.length() > i && (s.equals("s") || s.equals("c") || s.equals("t"))) { // identify trig functions
						s = input.substring(i, i + 3);
						i += 2;
					}
					
					// if an operator, enqueue operators until an operator of less priority is hit
					while (!stack.isEmpty() && getPriority(s.charAt(0)) > getPriority(stack.peek().charAt(0))) {
						outputQueue.enqueue(stack.pop());
					}
					stack.push(s);
				}
			}
		}
		while (!stack.isEmpty()) outputQueue.enqueue(stack.pop());

		return outputQueue;
	}
	
	public static int getPriority(Character c) {
		//priority form https://en.wikipedia.org/wiki/Order_of_operations#Programming_languages (although unary operators are put first in priority)
		// highest priority gets the smallest return value to allow easy expansion to further operators
		if (c == '!' || c == 's' || c == 'c' || c == 't') return 1;
		if (c == '^') return 2;
		if (c == '/' || c == '*' || c == '%') return 3;
		if (c == '-' || c == '+') return 4;
		if (c == '>' || c == '<') return 5;
		if (c == '=') return 6;
		if (c == '&') return 7;
		if (c == '|') return 8;
		else return Integer.MAX_VALUE; //throw lowest possible priority if input is not an operator/ an unknown operator
	}
}
