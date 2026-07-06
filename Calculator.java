package com.mycompany.Calculator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Calculator implements ActionListener, KeyListener {

    JFrame frame;
    JTextField textfield;

    JButton[] numberButtons = new JButton[10];
    JButton[] functionButtons = new JButton[9];

    JButton addButton, subButton, mulButton, divButton;
    JButton decButton, delButton, clrButton, eqlButton, signButton;

    JPanel panel;

    Font myFont = new Font("Arial", Font.BOLD, 22);

    double num1 = 0;
    double num2 = 0;
    double result = 0;
    char operator = ' ';
    boolean startNewNumber = false; // true right after an operator/equals so typing starts a fresh number

    public Calculator() {

        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 550);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.BLACK);

        textfield = new JTextField();
        textfield.setBounds(50, 25, 300, 50);
        textfield.setFont(myFont);
        textfield.setEditable(false);
        textfield.setFocusable(true);

        addButton = new JButton("+");
        subButton = new JButton("-");
        mulButton = new JButton("×");
        divButton = new JButton("÷");
        decButton = new JButton(".");
        eqlButton = new JButton("=");
        delButton = new JButton("DEL");
        clrButton = new JButton("C");
        signButton = new JButton("±");

        functionButtons[0] = addButton;
        functionButtons[1] = subButton;
        functionButtons[2] = mulButton;
        functionButtons[3] = divButton;
        functionButtons[4] = decButton;
        functionButtons[5] = eqlButton;
        functionButtons[6] = delButton;
        functionButtons[7] = clrButton;
        functionButtons[8] = signButton;

        for (int i = 0; i < functionButtons.length; i++) {
            functionButtons[i].addActionListener(this);
            functionButtons[i].setFont(myFont);
            functionButtons[i].setFocusable(false);
            functionButtons[i].setBackground(new Color(255, 149, 0));
            functionButtons[i].setForeground(Color.WHITE);
        }

        for (int i = 0; i < numberButtons.length; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFont(myFont);
            numberButtons[i].setFocusable(false);
            numberButtons[i].setBackground(Color.GRAY);
            numberButtons[i].setForeground(new Color(28, 28, 28));
        }

        panel = new JPanel();
        panel.setBounds(50, 100, 300, 300);
        panel.setLayout(new GridLayout(5, 4, 10, 10));
        panel.setBackground(Color.BLACK);

        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(addButton);

        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(subButton);

        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(mulButton);

        panel.add(signButton);
        panel.add(numberButtons[0]);
        panel.add(decButton);
        panel.add(divButton);

        panel.add(eqlButton);

        delButton.setBounds(50, 430, 150, 50);
        clrButton.setBounds(200, 430, 150, 50);

        frame.add(textfield);
        frame.add(panel);
        frame.add(delButton);
        frame.add(clrButton);

        frame.addKeyListener(this);
        textfield.addKeyListener(this);
        frame.setFocusTraversalKeysEnabled(false);

        frame.setVisible(true);
        frame.requestFocusInWindow();
    }

    public static void main(String[] args) {
        new Calculator();
    }

    // Resolves any pending operation against the current field value.
    // Called whenever an operator is pressed while one is already active,
    // so expressions like 5 + 3 + 2 chain correctly instead of discarding 5+3.
    private void resolvePending() {
        if (textfield.getText().isEmpty()) {
            return;
        }

        num2 = Double.parseDouble(textfield.getText());

        if (operator == '/' && num2 == 0) {
            textfield.setText("Error");
            num1 = 0;
            num2 = 0;
            result = 0;
            operator = ' ';
            return;
        }

        switch (operator) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                result = num1 / num2;
                break;
            default:
                result = num2;
        }

        num1 = result;
        displayResult(result);
    }

    private void displayResult(double value) {
        if (value == (long) value) {
            textfield.setText(String.valueOf((long) value));
        } else {
            textfield.setText(String.valueOf(value));
        }
    }

    private void handleOperator(char op) {
        if (textfield.getText().isEmpty() && operator == ' ') {
            return;
        }

        if (!textfield.getText().isEmpty()) {
            if (operator != ' ') {
                resolvePending();
            } else {
                num1 = Double.parseDouble(textfield.getText());
            }
        }

        operator = op;
        startNewNumber = true;
    }

    private void handleEquals() {
        if (textfield.getText().isEmpty() || operator == ' ') {
            return;
        }

        resolvePending();
        operator = ' ';
        startNewNumber = true;
    }

    private void handleDigit(int digit) {
        if (startNewNumber) {
            textfield.setText("");
            startNewNumber = false;
        }
        textfield.setText(textfield.getText() + digit);
    }

    private void handleDecimal() {
        if (startNewNumber) {
            textfield.setText("");
            startNewNumber = false;
        }

        if (textfield.getText().isEmpty()) {
            textfield.setText("0.");
        } else if (!textfield.getText().contains(".")) {
            textfield.setText(textfield.getText() + ".");
        }
    }

    private void handleClear() {
        textfield.setText("");
        num1 = 0;
        num2 = 0;
        result = 0;
        operator = ' ';
        startNewNumber = false;
    }

    private void handleDelete() {
        String text = textfield.getText();
        if (!text.isEmpty()) {
            textfield.setText(text.substring(0, text.length() - 1));
        }
    }

    private void handleSignToggle() {
        String text = textfield.getText();
        if (text.isEmpty() || text.equals("Error") || text.equals("Invalid Input")) {
            return;
        }
        if (text.startsWith("-")) {
            textfield.setText(text.substring(1));
        } else {
            textfield.setText("-" + text);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            // Number buttons
            for (int i = 0; i < numberButtons.length; i++) {
                if (e.getSource() == numberButtons[i]) {
                    handleDigit(i);
                    return;
                }
            }

            if (e.getSource() == decButton) {
                handleDecimal();
                return;
            }

            if (e.getSource() == clrButton) {
                handleClear();
                return;
            }

            if (e.getSource() == delButton) {
                handleDelete();
                return;
            }

            if (e.getSource() == signButton) {
                handleSignToggle();
                return;
            }

            if (e.getSource() == addButton) {
                handleOperator('+');
                return;
            }

            if (e.getSource() == subButton) {
                handleOperator('-');
                return;
            }

            if (e.getSource() == mulButton) {
                handleOperator('*');
                return;
            }

            if (e.getSource() == divButton) {
                handleOperator('/');
                return;
            }

            if (e.getSource() == eqlButton) {
                handleEquals();
                return;
            }

        } catch (NumberFormatException ex) {
            textfield.setText("Invalid Input");
            num1 = 0;
            num2 = 0;
            result = 0;
            operator = ' ';
            startNewNumber = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        if (Character.isDigit(c)) {
            handleDigit(Character.getNumericValue(c));
        } else if (c == '.') {
            handleDecimal();
        } else if (c == '+') {
            handleOperator('+');
        } else if (c == '-') {
            handleOperator('-');
        } else if (c == '*') {
            handleOperator('*');
        } else if (c == '/') {
            handleOperator('/');
        } else if (c == '=' || c == '\n') {
            handleEquals();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ENTER) {
            handleEquals();
        } else if (code == KeyEvent.VK_BACK_SPACE) {
            handleDelete();
        } else if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_DELETE) {
            handleClear();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // not needed
    }
}