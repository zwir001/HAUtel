package src.frontend;

import src.backend.systems.BaseSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterForm extends JDialog{
    private JPanel mainPanel;
    private JTextField nameTf;
    private JTextField surnameTf;
    private JLabel hotelLabel;
    private JLabel signInLabel;
    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel emailLabel;
    private JTextField emailTf;
    private JLabel phoneLabel;
    private JTextField phoneNumTf;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JTextField petNameTf;
    private JLabel petNameLabel;
    private JLabel speciesLabel;
    private JLabel drugsLabel;
    private JTextField drugsTf;
    private JLabel allergyLabel;
    private JTextField allergyTf;
    private JButton registerButton;
    private JButton returnButton;
    private JComboBox speciesComboBox;
    BaseSystem baseSystem = new BaseSystem();

    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private char[] password;
    private String petName;
    private String petDrugs;
    private String petAllergy;
    private int species;


    public RegisterForm(JFrame parent) {
        super(parent);
        setTitle("Create a new account");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(600, 700));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();

            }
        });
        setVisible(true);
    }

    private void registerUser(){
        name = nameTf.getText();
        surname = surnameTf.getText();
        email = emailTf.getText();
        phoneNumber = phoneNumTf.getText();
        password = passwordField.getPassword();
        petName = petNameTf.getText();
        species = speciesComboBox.getSelectedIndex();
        petDrugs = drugsTf.getText();
        petAllergy = allergyTf.getText();
        baseSystem.addNewClient(name, surname, email, phoneNumber, password, petName, species, petDrugs, petAllergy);



    }
}