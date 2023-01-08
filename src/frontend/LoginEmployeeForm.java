package src.frontend;

import src.backend.systems.BaseSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginEmployeeForm extends JDialog{
    private JPanel mainPanel;
    private JLabel hotelLabel;
    private JLabel signInLabel;
    private JLabel idLabel;
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JLabel passwordLabel;
    private JButton returnButton;
    ;

    private String id;
    private char [] password;
    private boolean isCorrect = false;



    public LoginEmployeeForm(JFrame parent, BaseSystem baseSystem) {
        super(parent);
        setTitle("Sign in as employee");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(350, 400));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);



        setVisible(false);

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id = idField.getText();
                password = passwordField.getPassword();
                isCorrect = baseSystem.login(id, password, true);
                if(!isCorrect){
                    JOptionPane.showMessageDialog(mainPanel,
                            "Invalid data",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else {
                    dispose();
                    setVisible(false);
                    //pojawia się okno pracownika

                }
            }
        });
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                LoginForm loginForm = new LoginForm(null);
                dispose();

            }
        });
    }
}
