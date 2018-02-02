/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package myserver;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

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

    TextField textField = new TextField();
    TextField imageField = new TextField();
    TextField audioField = new TextField();
    TextField gifField = new TextField();

    ImageView imageView = new ImageView();

    TextArea textArea = new TextArea();

    MyMenuItem send_txt_btn = new MyMenuItem(new Image(getClass()
            .getResource("res/menu/item/Send.png").toExternalForm()),75,25);
    MyMenuItem send_img_btn = new MyMenuItem(new Image(getClass()
            .getResource("res/menu/item/Send.png").toExternalForm()),75,25);
    MyMenuItem send_aud_btn = new MyMenuItem(new Image(getClass()
            .getResource("res/menu/item/Send.png").toExternalForm()),75,25);
    MyMenuItem send_gif_btn = new MyMenuItem(new Image(getClass()
            .getResource("res/menu/item/Send.png").toExternalForm()),75,25);

    MenuBar menuBar = new MenuBar();



    //Button button = new Button("Send");
    private ImageView bg;

    Stage stage;
    // Start scene
    Group root=new Group();
    Scene scene =new Scene(root,400, 800);


    public void run() {
        setUpServer();
        waitForClient();
        getIOStreams();
        receive();
    }

    private void setUpServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server set up successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void waitForClient() {
        try {
            System.out.println("Waiting for a client");
            socket = serverSocket.accept();
            System.out.println("A client connected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void getIOStreams() {
        try {
            System.out.println("Getting IOStreams.");
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Getting IOStreams finished");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void receive() {
        System.out.println("Start processing");
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String order = (String) (objectInputStream.readObject());
                        if (order.equals("string")) {
                            String message = (String) (objectInputStream.readObject());
                            console(message);
                            Media sound=new Media(getClass().getResource("res/sound/click.wav").toExternalForm());
                            MediaPlayer player=new MediaPlayer(sound);
                            player.play();
                        } else if (order.equals("image")) {
                            receive_image();
                            Media sound=new Media(getClass().getResource("res/sound/click.wav").toExternalForm());
                            MediaPlayer player=new MediaPlayer(sound);
                            player.play();
                        } else if (order.equals("audio")) {
                            receive_audio();
                            Media sound=new Media(getClass().getResource("res/sound/click.wav").toExternalForm());
                            MediaPlayer player=new MediaPlayer(sound);
                            player.play();
                        } else if (order.equals("gif")) {
                            receive_gif();
                            Media sound=new Media(getClass().getResource("res/sound/click.wav").toExternalForm());
                            MediaPlayer player=new MediaPlayer(sound);
                            player.play();
                        }
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }
    private void receive_audio() {
        try {
            byte[] bytes = (byte[]) objectInputStream.readObject();
            FileOutputStream fos = new FileOutputStream(audioField.getText());
            fos.write(bytes);
            fos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        Media hit = null;
        try {
            hit = new Media(new File(audioField.getText()).toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }

    private void receive_gif() {
        try {
            byte[] bytes = (byte[]) objectInputStream.readObject();
            FileOutputStream fos = new FileOutputStream(gifField.getText());
            fos.write(bytes);
            fos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        Image image = null;
        try {
            image = new Image(new File(gifField.getText()).toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        imageView.setImage(image);
    }

    private void receive_image() {
        try {
            byte[] bytes = (byte[]) objectInputStream.readObject();
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
            ImageIO.write(img, "png", new File(imageField.getText()));
        } catch (IOException e) {
            System.out.println("IO Exception");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Image image = null;
        try {
            image = new Image(new File(imageField.getText()).toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        imageView.setImage(image);

    }

    private void send_image() {
        sendOrder("image");
        try {
            BufferedImage image = ImageIO.read(new File(imageField.getText()));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream);
            byteArrayOutputStream.flush();
            byte[] img = byteArrayOutputStream.toByteArray();
            objectOutputStream.writeObject(img);
            byteArrayOutputStream.close();
        } catch (IOException e) {
            System.out.println("IO Exception or Interrupted");
        }
        console("Server : Image Sent");
    }

    private void send_message(String message) {
        sendOrder("string");
        try {
            objectOutputStream.writeObject("Server : " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        console("Server : " + message);
        textField.setText("");
    }

    private void send_gif() {
        sendOrder("gif");
        try {
            byte[] array = Files.readAllBytes(new File(gifField.getText()).toPath());
            objectOutputStream.writeObject(array);
        } catch (IOException e) {
            e.printStackTrace();
        }
        console("Server : Gif Sent");
    }
    private void send_audio(){
        sendOrder("audio");
        try {
            byte[] array = Files.readAllBytes(new File(audioField.getText()).toPath());
            objectOutputStream.writeObject(array);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void sendOrder(String order) {
        try {
            objectOutputStream.writeObject(order);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void console(String message) {
        textArea.appendText(message + "\n");
    }
    
    
    public void start(Stage primaryStage) {

        this.port = 9999;
        run();

        String address="res/menu/bg/background.jpg";
        bg=new ImageView(new Image(getClass()
                 .getResource(address).toExternalForm()));
        bg.setFitWidth(400);
        bg.setFitHeight(800);
        bg.setVisible(true);



        send_img_btn.setDim(310, 550 + 50);
        /*send_img_btn.setOnAction(new Runnable() {
            @Override
            public void run() {
                send_image();
            }
        });*/
        send_img_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                send_image();
            }
        });
        Tooltip img_t = new Tooltip("image");
        Tooltip.install(send_img_btn, img_t);
        imageField.setLayoutX(50);
        imageField.setLayoutY(520 + 25 + 50);
        imageField.setPrefHeight(30);
        imageField.setPrefWidth(250);
        imageField.setStyle("-fx-font-weight: bold");
        imageField.setStyle("-fx-text-fill: black");
        imageField.setStyle("-fx-highlight-text-fill: black");
        imageField.setStyle("-fx-background-color: #aae1e6;");
        imageField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                send_image();
            }
        });


        send_aud_btn.setDim(310, 600 + 50);
        /*send_aud_btn.setOnAction(new Runnable() {
            @Override
            public void run() {
                send_audio();
            }
        });*/
        send_aud_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                send_audio();
            }
        });
        Tooltip aud_t = new Tooltip("audio");
        Tooltip.install(send_aud_btn, aud_t);
        audioField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                send_audio();
            }
        });
    //    audioField.setText("Enter audio address.");
        audioField.setLayoutX(50);
        audioField.setLayoutY(570 + 25 + 50);
        audioField.setPrefHeight(30);
        audioField.setPrefWidth(250);
        audioField.setStyle("-fx-font-weight: bold");
        audioField.setStyle("-fx-text-fill: black");
        audioField.setStyle("-fx-highlight-text-fill: black");
        audioField.setStyle("-fx-background-color: #aae1e6;");

        send_gif_btn.setDim(310, 650 + 50);
        /*send_gif_btn.setOnAction(new Runnable() {
            @Override
            public void run() {
                send_gif();
            }
        });*/
        send_gif_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                send_gif();
            }
        });
        Tooltip gif_t = new Tooltip("gif");
        Tooltip.install(send_gif_btn, gif_t);
    //    gifField.setText("Enter gif address.");
        gifField.setLayoutX(50);
        gifField.setLayoutY(620 + 25 + 50);
        gifField.setPrefHeight(30);
        gifField.setPrefWidth(250);
        gifField.setStyle("-fx-font-weight: bold");
        gifField.setStyle("-fx-text-fill: black");
        gifField.setStyle("-fx-highlight-text-fill: black");
        gifField.setStyle("-fx-background-color: #aae1e6;");
        gifField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                send_gif();
            }
        });

        send_txt_btn.setDim(310, 700 + 50);
        /*send_txt_btn.setOnAction(() -> {

            send_message(textField.getText());

        });*/
        send_txt_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                send_message(textField.getText());
            }
        });
        Tooltip text_t = new Tooltip("text");
        Tooltip.install(send_txt_btn, text_t);
  //      textField.setText("Enter message.");
        textField.setLayoutX(50);
        textField.setLayoutY(670 + 25 + 50);
        textField.setPrefHeight(30);
        textField.setPrefWidth(250);
        textField.setStyle("-fx-font-weight: bold");
        textField.setStyle("-fx-text-fill: black");
        textField.setStyle("-fx-highlight-text-fill: black");
        textField.setStyle("-fx-background-color: #aae1e6;");
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                send_message(textField.getText());
            }
        });


        textArea.setEditable(false);
        textArea.setLayoutX(50);
        textArea.setLayoutY(50);
        textArea.setPrefHeight(400);  //sets height of the TextArea to 400 pixels
        textArea.setPrefWidth(300);    //sets width of the TextArea to 300 pixels 
        textArea.setOpacity(0.5);
       // textArea.setStyle("-fx-font-weight: bolder;");
        textArea.setStyle("-fx-font: bold");
        textArea.setStyle("-fx-font-weight: bold");


        imageView = new ImageView();
        imageView.relocate(130, 450 + 20);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);

        menuBar.prefWidthProperty().bind(scene.widthProperty());
        Menu fastreply = new Menu("Fast Reply");
        menuBar.getMenus().addAll(fastreply);

        MenuItem f1 = new MenuItem("Fast Reply 1");
        f1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               // textField.setText("f1");
                send_message("be right back.");
            }
        });

        MenuItem f2 = new MenuItem("Fast Reply 1");
        f2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
             //   textField.setText("f1");
                send_message("good game!");
            }
        });

        MenuItem f3 = new MenuItem("Fast Reply 1");
        f3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              //  textField.setText("f1");
                send_message("oh my god!");
            }
        });

        MenuItem f4 = new MenuItem("Fast Reply 1");
        f4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              //  textField.setText("f1");
                send_message("talk to you later.");
            }
        });

        fastreply.getItems().addAll(f1, f2, f3, f4);
        
        root.getChildren().addAll(bg, send_txt_btn, send_gif_btn, send_img_btn, send_aud_btn,
                textField, imageField, audioField, gifField, textArea, imageView, menuBar);
        
        stage = primaryStage;
        //makeStartscene(); p
        stage.setTitle("Server ");
        stage.setScene(scene);
        stage.setX(400);

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
