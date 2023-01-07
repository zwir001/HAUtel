package src.frontend;

import src.backend.systems.BaseSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JDialog{
    private JPanel mainPanel;
    private JTextField enterEmailTextField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JLabel hotelNameLabel;
    private JLabel signInLabel;
    private JButton createAnAccountButton;
    private JButton signInAsEmployeeButton;
    private JLabel enterEmailLabel;
    private JLabel enterPassfordLabel;
    private boolean isCorrect = false;
    BaseSystem baseSystem = new BaseSystem();



    public LoginForm(JFrame parent) {
        super(parent);
        setTitle("Login");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);



        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = enterEmailTextField.getText();
                char [] password = passwordField.getPassword();
                isCorrect = baseSystem.login(email, password, false);



            }
        });
        setVisible(true);
    }

}