package src.frontend;

import src.backend.systems.BaseSystem;
import src.backend.systems.reservations.RequestedServiceStatus;
import src.model.Client;
import src.model.Pet;
import src.model.ReservationClientView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    private JList petsList;
    private JPanel dataPanel;
    private JLabel userNameLabel;
    private JLabel userSurnameLabel;
    private JLabel userEmailLabel;
    private JLabel userPhoneNumLabel;
    private JLabel petsLabel;
    private JLabel userDataLabel;
    private JTextField monthField;
    private JTextField yearField;
    private JLabel petIDLabel;
    private JButton confirmButton;
    private JTextField petIDField;
    private JTextField dayField;
    private JLabel dateLabel;
    private JLabel durationLabel;
    private JTextField durationField;
    private JLabel dayLabel;
    private JLabel monthLabel;
    private JLabel yearLabel;
    private JLabel chargesLabel;
    private JLabel reservationsLabel;
    private JList reservationsList;
    private JTextField reservationIDField;
    private JCheckBox groomerBox;
    private JCheckBox massageBox;
    private JCheckBox trainerBox;
    private JCheckBox bathBox;
    private JButton confirmServicesButton;
    private JLabel reservationIDLabel;
    private JLabel servicesLabel;
    private JLabel priceLabel;
    private JLabel groomerPrice;
    private JLabel massageLabel;
    private JLabel trainerLabel;
    private JLabel bathLabel;
    private JButton confirmReservationButton;

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

    private int petIDReservation;
    private int dayReservation;
    private int monthReservation;
    private int yearReservation;
    private int durationReservation;
    private boolean isCorrectReservation = false;

    private int reservationID;


    public AccountForm(JFrame parent, BaseSystem baseSystem) {
        super(parent);
        setTitle("Your account");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(800, 700));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        changeData(baseSystem);
        addNewPet(baseSystem);
        showData(baseSystem);
        makeReservation(baseSystem);
        addServices(baseSystem);



    }


    private void changeData(BaseSystem baseSystem){
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

    }
    private void addNewPet(BaseSystem baseSystem){
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
    private void showData(BaseSystem baseSystem){

        Optional<Client> client;
        client = baseSystem.getClientData();
        Client curClient = client.get();


        userNameLabel.setText("Name: " + curClient.getName());
        userSurnameLabel.setText("Surname: " + curClient.getSecondName());
        userEmailLabel.setText("Email address: " + curClient.getEmail());
        userPhoneNumLabel.setText("Phone number: " + curClient.getPhoneNumber());
        chargesLabel.setText("Your charges: " + curClient.getCharges());

        ArrayList<Pet> pets;
        pets = (ArrayList<Pet>) baseSystem.getClientPets(curClient.getId());
        DefaultListModel listModel = new DefaultListModel();
        for(Pet pet : pets){
            listModel.addElement("Pet ID: " + pet.getId() + ", pet's Name: " + pet.getName() + ", owner's ID: " + pet.getOwnerId()
                    + ", species: " + getSpecies(pet) + getDrugs(pet) + getAllergies(pet));
        }
        petsList.setModel(listModel);

        //show reservations

        var reservations = new ArrayList<ReservationClientView>();

        baseSystem.getClientReservations(curClient.getId()).values().forEach(reservations::addAll);


        //baseSystem.getClientReservations().values().forEach(reservations::addAll);

        DefaultListModel model = new DefaultListModel();
        for(ReservationClientView reservation : reservations){
            model.addElement("Reservation ID: " + reservation.getId() + ", date: " + reservation.getStartDate()
                    + ", duration " + reservation.getDuration() + ", value: " + reservation.getValue() + ", status: " + reservation.getStatus());

        }
        reservationsList.setModel(model);






    }
    private String getSpecies(Pet pet){
        int speciesID = pet.getSpeciesId();
        if(speciesID == 1){
            return "Dog";
        }
        if(speciesID == 2){
            return "Cat";
        }
        if(speciesID == 3){
            return "Rabbit";
        }
        if(speciesID == 4){
            return "Hamster";
        }
        if(speciesID == 5){
            return "Parrot";
        }
        else{
            return "Guinea pig";
        }
    }
    private String getDrugs(Pet pet){
        String drugs = pet.getDrugs();
        if(drugs.isEmpty()){
            return ", no pet's drugs";
        }
        else {
            return ", pet's drugs: " + drugs;
        }
    }
    private String getAllergies(Pet pet){
        String allergies = pet.getAllergy();
        if(allergies.isEmpty()){
            return ", no pet's allergies";
        }
        else {
            return ", pet's allergies: " + allergies;
        }
    }

    private void makeReservation(BaseSystem baseSystem){

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateReservation;
                petIDReservation = Integer.parseInt(petIDField.getText());
                dayReservation = Integer.parseInt((dayField.getText()));
                monthReservation = Integer.parseInt((monthField.getText()));
                yearReservation = Integer.parseInt((yearField.getText()));
                YearMonth yearMonthObject = YearMonth.of(yearReservation, monthReservation);
                int daysInMonth = yearMonthObject.lengthOfMonth();
                durationReservation = Integer.parseInt(durationField.getText());
                if(dayReservation < 1 || dayReservation > daysInMonth){
                    JOptionPane.showMessageDialog(mainPanel,
                            "Reservation day is invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(monthReservation < 1 || monthReservation > 13){
                    JOptionPane.showMessageDialog(mainPanel,
                            "Reservation month is invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(yearReservation < 2000 || yearReservation > 2100){
                    JOptionPane.showMessageDialog(mainPanel,
                            "Reservation year is invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                dateReservation = (dayReservation) + "-" + (monthReservation) + "-" + yearReservation;
                isCorrectReservation = baseSystem.addNewReservation(petIDReservation, dateReservation, durationReservation);
                if(!isCorrectReservation){
                    JOptionPane.showMessageDialog(mainPanel,
                            "Reservation data is invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel,
                            "Reservation has been added.",
                            "Reservation added",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;

                }

            }
        });

    }

    private void addServices(BaseSystem baseSystem){
        confirmServicesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<Integer, RequestedServiceStatus> errorsServices = new HashMap<Integer, RequestedServiceStatus>();
                reservationID = Integer.parseInt(reservationIDField.getText());
                ArrayList<Integer> serviceIDs = new ArrayList<>();
                if(groomerBox.isSelected()){
                    serviceIDs.add(1);
                }
                if(massageBox.isSelected()){
                    serviceIDs.add(2);
                }
                if(trainerBox.isSelected()){
                    serviceIDs.add(3);
                }
                if(bathBox.isSelected()){
                    serviceIDs.add(4);
                }
                errorsServices = baseSystem.orderAdditionalServices(reservationID, serviceIDs);
                errorsServices.forEach((k, v) -> {
                    if(v == RequestedServiceStatus.ALREADY_ORDERED){
                        JOptionPane.showMessageDialog(mainPanel,
                                "Services are already ordered",
                                "Try again",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(v == RequestedServiceStatus.ADDED){
                        JOptionPane.showMessageDialog(mainPanel,
                                "Services has been added.",
                                "Services added",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    if(v == RequestedServiceStatus.NOT_AVAILABLE){
                        JOptionPane.showMessageDialog(mainPanel,
                                "Services are not available",
                                "Try again",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(v == RequestedServiceStatus.UNKNOWN_ID){
                        JOptionPane.showMessageDialog(mainPanel,
                                "One of the services has an unknown id",
                                "Try again",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                });





            }
        });
    }



}
