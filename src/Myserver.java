/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package myserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.omg.CORBA.PRIVATE_MEMBER;

/**
 *
 * @author Tara
 */
public class Myserver extends Application {
    int port=9999;
    ServerSocket serverSocket;
    Socket socket;

    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    
    ImageView sendButton;

    
    public void run() {
        initializeServer();
        tryConnecting();
        getIOStreamsFromSocket();
        receiveMessage();
    }
    
    
    private void initializeServer() {
        try {
            serverSocket = new ServerSocket(9999);
            System.out.println("Server set up successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    
    private void tryConnecting() {
        try {
            System.out.println("Waiting for a client");
            socket = serverSocket.accept();
            System.out.println("A client connected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private void getIOStreamsFromSocket() {
        try {
            System.out.println("Getting IOStreams.");
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Getting IOStreams finished");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    
    private void receiveMessage() {
        System.out.println("Start processing");
        new Thread() {
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
        }.start();
        
        System.out.println("processing ended");

    }
    
    
    private void printMessage(String message) {
        textArea.appendText(message + "\n");
    }
    
    private void sendMessage(String message) {

        try {
            objectOutputStream.writeObject("Server : " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        printMessage("Server : " + message);
        textField.setText("");
        
    }


    TextField textField = new TextField();
    TextArea textArea = new TextArea();
    //Button button = new Button("Send");
    private ImageView bg;

    Stage stage;
    // Start scene
    Group root=new Group();
    Scene scene =new Scene(root,400, 800);

    private Parent makeMainSceneContent(){
        Group root = new Group();

       // ImageView background = new ImageView(new Image(getClass()
       //         .getResource(address).toExternalForm()));


        return root;
    }
    
    
    public void start(Stage primaryStage) {

        //this.run();
        run();
        String address="res/menu/bg/background.jpg";
        bg=new ImageView(new Image(getClass()
                 .getResource(address).toExternalForm()));
        bg.setFitWidth(400);
        bg.setFitHeight(800);
        bg.setVisible(true);
        
        
       //button.setOnAction(event -> {
       //     sendMessage(textField.getText());
       // });
        
       // button.setLayoutX(325);
       // button.setLayoutY(710);
       
       
       String address2="res/menu/item/Exit.png";
       
       //sendButton=new ImageView(new Image(getClass()
       //          .getResource(address2).toExternalForm()));
       MenuItem sendBtn=new MenuItem(new Image(getClass()
                 .getResource(address2).toExternalForm()),60,25);
       
       sendBtn.setDim(325, 710);
       sendBtn.setOnAction(() -> {
            
          sendMessage(textField.getText());
            
        });
      // sendButton.setFitHeight(25);
       //sendButton.setFitWidth(60);
       //sendButton.setLayoutX(325);
       //sendButton.setLayoutY(710);
       //sendButton.setOnMouseClicked(event -> {
            
       //    sendMessage(textField.getText());
            
       // });
       
       
       
       
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
        
        root.getChildren().addAll(bg,sendBtn,textField,textArea);
        
        stage = primaryStage;
        //makeStartscene(); p
        stage.setTitle("Server ");
        stage.setScene(scene);
        stage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
