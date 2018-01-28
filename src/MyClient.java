/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package myserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Tara
 */
public class MyClient extends Application {
    
    
    Stage stage;
    // Start scene
    Group root=new Group();
    Scene scene =new Scene(root,400, 800);
    
    int port=9999;
    //ServerSocket serverSocket;
    Socket thisClient;

    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    
    
    TextField textField = new TextField();
    TextArea textArea = new TextArea();
    Button button = new Button("Send");
    

    
    
    public void run() {
        //initializeServer();
        tryConnecting();
        getIOStreamsFromSocket();
        receiveMessage();
    }
    
    

    
    private void tryConnecting() {
        try {
            System.out.println("Trying to connect to server");
            thisClient  = new Socket("localhost", 9999);
            System.out.println("Connected to Server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private void getIOStreamsFromSocket() {
        try {
            System.out.println("Getting IOStreams.");
            objectOutputStream = new ObjectOutputStream(thisClient.getOutputStream());
            objectInputStream = new ObjectInputStream(thisClient.getInputStream());
            System.out.println("Getting IOStreams finished");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    
    private void receiveMessage() {
        System.out.println("Start processing");
        Thread th=new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        
                            String message = (String) (objectInputStream.readObject());
                            printMessage(message);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        th.start();
        System.out.println("processing ended");
    }
    
    
    private void printMessage(String message) {
        textArea.appendText(message + "\n");
    }
    
    private void sendMessage(String message) {

        try {
            objectOutputStream.writeObject("Client : " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        printMessage("Client : " + message);
        textField.setText("");
        
    }

    
    
     
    
    @Override
    public void start(Stage primaryStage) {
        
        //this.run();
        
        //String address="/Users/Apple/Documents/TaraFiles/University/term 7/JAVA/project4/team6/src/res/menu/bgchatBG.jpg";
        //bg=new ImageView(new Image(getClass()
          //       .getResource(address).toExternalForm()));
        //String address="res/menu/bg/chatBG.png";
       // ImageView bg=new ImageView(new Image(getClass()
         //        .getResource(address).toExternalForm()));
        /*
        Image image=null; 
        try {
            image = new Image(new FileInputStream("/Users/Apple/Documents/TaraFiles/University/term 7/JAVA/project4/team6/src/res/menu/bg/chatBG.png"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        ImageView bg=new ImageView(image);
        bg.setFitWidth(400);
        bg.setFitHeight(800);
        bg.setVisible(true);
        /*/
        
        button.setOnAction(event -> {
            sendMessage(textField.getText());
        });
        
        button.setLayoutX(325);
        button.setLayoutY(710);
        
        
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sendMessage(textField.getText());
            }
        });
        textField.setLayoutX(50);
        textField.setLayoutY(700);
        textField.setPrefHeight(50);
        textField.setPrefWidth(250);
        
        textArea.setEditable(false);
        textArea.setLayoutX(50);
        textArea.setLayoutY(50);
        textArea.setPrefHeight(600);  //sets height of the TextArea to 400 pixels 
        textArea.setPrefWidth(300);    //sets width of the TextArea to 300 pixels 
        root.getChildren().addAll(button,textField,textArea);
        
        stage = primaryStage;
        //makeStartscene(); p
        stage.setTitle("Clinet");
        stage.setScene(scene);
        stage.show();
        /*stage.setX(200);
        stage.setY(400);*/
        run();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
