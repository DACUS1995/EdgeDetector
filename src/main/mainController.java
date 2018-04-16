package main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable
{

    @FXML
    private Button buttonSelectImage;

    @FXML
    private Button buttonLoadImage;

    @FXML
    private TextField textImagePath;

    @FXML
    private ImageView imageDisplayView;

    // Normal non FXML members
    private String selectedImagePath = null;
    private Image selectedImage = null;
    private Image ProcessedImage = null;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    public void selectImage()
    {
        FileChooser fileChooser = new FileChooser();

        // Create the dialog Scene
        Stage dialogFileChooser = new Stage();
        dialogFileChooser.setTitle("Load Image");
        dialogFileChooser.initModality(Modality.WINDOW_MODAL);
        dialogFileChooser.initOwner(Main.getPrimaryStage());

        File selectedFile = fileChooser.showOpenDialog(dialogFileChooser);

        if(selectedFile == null)
        {
            this.textImagePath.setText("No image selected");
        }
        else
        {
            this.selectedImagePath = selectedFile.getAbsolutePath();
            this.textImagePath.setText(this.selectedImagePath);
            System.out.println(this.selectedImagePath);
        }

    }

    public void loadImageToDisplay()
    {
        this.selectedImage = new Image(this.parsePath(this.selectedImagePath), 400, 300, false, true, true);
        this.imageDisplayView.setImage(this.selectedImage);
    }

    private String parsePath(String strPath)
    {
        // TODO use a fixed path to test why it doesn't work
        // Look at the image class in https://docs.oracle.com/javase/8/javafx/api/javafx/scene/image/Image.html
        // You can edit the image bit map
        // Also the progress of the loading
        String parsedPath = strPath.split(":")[1];
        System.out.println(parsedPath);

        return parsedPath;
    }
}
