package src.frontend;

import src.backend.systems.BaseSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountForm extends JDialog {
    private JPanel mainPanel;
    private JLabel hotelLabel;
    private JTabbedPane tabbedPane1;
    private JPanel reservationHistoryPanel;
    private JPanel changeDataPanel;
    private JPanel reservationPanel;
    private JPanel servicesPanel;
    private JPanel petPanel;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField phoneNumberField;
    private JButton passwordButton;
    private JButton emailButton;
    private JButton phoneNumButton;
    private JLabel passwordLabel;
    private JLabel emailLabel;
    private JLabel phoneNumLabel;
    private JTextField petNameField;
    private JTextField drugsField;
    private JTextField allergyField;
    private JButton addPetButton;
    private JLabel petNameLabel;
    private JLabel speciesLabel;
    private JComboBox spieciesComboBox;
    private JLabel drugsLabel;
    private JLabel allergyLabel;

    BaseSystem baseSystem = new BaseSystem();

    private char[] password;
    private String email;
    private boolean isValidEmail = false;
    private String phoneNum;
    private boolean isValidPhoneNum = false;

    public AccountForm(JFrame parent) {
        super(parent);
        setTitle("Your account");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(800, 700));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);



        passwordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                password = passwordField.getPassword();
                baseSystem.changeClientPassword(password);
                JOptionPane.showMessageDialog(mainPanel,
                        "Password has been changed.",
                        "Password changed",
                        JOptionPane.INFORMATION_MESSAGE);
                return;

            }
        });
        setVisible(false);
        emailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                email = emailField.getText();
                isValidEmail = baseSystem.changeClientEmail(email);
                if(!isValidEmail){
                    JOptionPane.showMessageDialog(mainPanel,
                            "Email is invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel,
                            "Email address has been changed.",
                            "Email changed",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;

                }

            }
        });
        phoneNumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                phoneNum = phoneNumberField.getText();
                isValidPhoneNum = baseSystem.changeClientPhoneNumber(phoneNum);
                if(!isValidPhoneNum){
                    JOptionPane.showMessageDialog(mainPanel,
                            "Phone number is invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel,
                            "Phone number has been changed.",
                            "Phone number changed",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;

                }

            }
        });
    }
}
