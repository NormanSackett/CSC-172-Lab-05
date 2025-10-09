name: Norman Sackett
email: nsackett@u.rochester.edu

lab partner: Julien Diamond

CSC 172 lab 5, project 1 has us implement a program to convert an infix expression to a postfix expression and evaluate it. The code takes input from a text field and uses a button action listener within the Java Swing GUI to run the infixToPostfix method. The method takes the input text and scans through it for useful tokens to work with each operand or operator individually. It then enqueues operands and pushes open parthenses to a stack, and closed parenthesis enqueue operators until the open parenthesis is hit, ensuring all operations within the parentheses are evaluated first. Other operators enqueue operators from the stack until an operator of lesser or greater riority is found. The program supports the modulo operator and trigonometric functions and handles invalid inputs with a custom error exception.
