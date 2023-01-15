package src;

import src.frontend.LoginForm;

import java.awt.*;


public class App {

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{
                    final LoginForm loginForm = new LoginForm(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
