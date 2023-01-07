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

    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private char[] password;
    private String petName;
    private String petDrugs;
    private String petAllergy;
    private int species;
    private boolean isValidData = false;




    public RegisterForm(JFrame parent, BaseSystem baseSystem) {
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
                registerUser(baseSystem);


            }
        });
        setVisible(false);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                LoginForm loginForm = new LoginForm(null);
                dispose();
            }
        });
    }

    private void registerUser(BaseSystem baseSystem){
        name = nameTf.getText();
        surname = surnameTf.getText();
        email = emailTf.getText();
        phoneNumber = phoneNumTf.getText();
        password = passwordField.getPassword();
        petName = petNameTf.getText();
        species = speciesComboBox.getSelectedIndex() + 1;
        petDrugs = drugsTf.getText();
        petAllergy = allergyTf.getText();
        isValidData = baseSystem.addNewClient(name, surname, email, phoneNumber, password, petName, species, petDrugs, petAllergy);
        if(!isValidData){
            JOptionPane.showMessageDialog(this,
                    "Invalid data",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        else{
            JOptionPane.showMessageDialog(this,
                    "You have successfully created an account",
                    "You created an account",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }

    public boolean getIsValidData(){
        return isValidData;
    }


}
