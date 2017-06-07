/**
 * Created by Shauny on 31-May-17.
 */

import java.io.IOException;
import java.text.ParseException;
import java.util.StringTokenizer;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

public class Controller extends VBox implements View {
    private Stage stage;
    private Client client;
    private boolean isImage=false;

    @FXML
    public Button search;
    @FXML
    public TextField IP;
    @FXML
    public TextField Port;
    @FXML
    public TextField URL;
    @FXML
    public WebView webView;
    @FXML
    public ImageView imageView;

    public Controller() {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
//                "view.fxml"));
//        //fxmlLoader.setRoot(this);
//        //fxmlLoader.setController(this);
//
//        try {
//            fxmlLoader.load();
//        } catch (IOException exception) {
//            throw new RuntimeException(exception);
//        }
        client = new Client(this);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


//    public String getText() {
//        return textProperty().get();
//    }
//
//    public void setText(String value) {
//        textProperty().set(value);
//    }

//    public StringProperty textProperty() {
//        return textField.textProperty();
//    }

    @FXML
    protected void search() {
        if (IP.getText().length() > 0 && Port.getText().length() > 0 && URL.getText().length() > 0) {

            client.setAdr(IP.getText());
            client.setPort(Integer.parseInt(Port.getText()));
            client.setUrl(URL.getText());
            new Thread(client).start();

        }
    }

    @Override
    public void notifyError() {

    }

    @Override
    public void notifySuccess(String result) {
        if(isImage){
            imageView.setImage(new Image(result));
        }
        else {
            Platform.runLater(() -> {
                WebEngine engine = webView.getEngine();
                engine.loadContent(result);
            });
        }

    }

    @Override
    public void notifyHeader(String header) {
        StringTokenizer tokenizer = new StringTokenizer(header);
        while(tokenizer.hasMoreTokens()){
            String s = tokenizer.nextToken();
            if("Content-Type:".equals(s)){
                String type = tokenizer.nextToken();
                if(type.contains("image")){
                    isImage=true;
                    //render imageview rather than webview
                    webView.setVisible(!isImage);
                    imageView.setVisible(isImage);
                } else {
                    isImage=false;
                    webView.setVisible(isImage);
                    imageView.setVisible(!isImage);
                }
            }
        }

    }
}
