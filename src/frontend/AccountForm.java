package src.frontend;

import src.backend.systems.BaseSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountForm extends JDialog {
    private JPanel mainPanel;
    private JLabel hotelLabel;
    private JTabbedPane dataPane;
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
    private JComboBox speciesComboBox;
    private JLabel drugsLabel;
    private JLabel allergyLabel;

    private char[] newPassword;
    private String newEmail;
    private boolean isValidEmail = false;
    private String newPhoneNum;
    private boolean isValidPhoneNum = false;

    private String newPetName;
    private String newPetDrugs;
    private String newPetAllergy;
    private int newPetSpecies;
    private boolean isValidNewPetData = false;

    public AccountForm(JFrame parent, BaseSystem baseSystem) {
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
                newPassword = passwordField.getPassword();
                baseSystem.changeClientPassword(newPassword);
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
                newEmail = emailField.getText();
                isValidEmail = baseSystem.changeClientEmail(newEmail);
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
                newPhoneNum = phoneNumberField.getText();
                isValidPhoneNum = baseSystem.changeClientPhoneNumber(newPhoneNum);
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
        addPetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newPetName = petNameField.getText();
                newPetSpecies = speciesComboBox.getSelectedIndex() + 1;
                newPetDrugs = drugsField.getText();
                newPetAllergy = allergyField.getText();
                isValidNewPetData = baseSystem.addNewPet(newPetName, newPetSpecies, newPetDrugs, newPetAllergy);
                if(!isValidNewPetData){
                    JOptionPane.showMessageDialog(mainPanel,
                            "Pet data is invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel,
                            "Pet data has been added.",
                            "Pet added",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;

                }

            }


        });
    }
}
