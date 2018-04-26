package main;

import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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

    @FXML
    private ProgressBar loadRawImageProgressbar;

    @FXML
    private TextArea contextualEventsTextArea;

    @FXML
    private ChoiceBox ImageTypeSelector;

    @FXML
    private RadioButton buttonSelectDarker;

    @FXML
    private RadioButton buttonSelectEdgeDetection;

    @FXML
    private Button buttonSaveFile;

    @FXML
    private Label labelProgress;

    @FXML
    private TextField textfieldImageSaveName;

    @FXML
    private Hyperlink linkSobelInfo;

    // Normal non FXML members
    private String selectedImagePath = null;
    private File selctedImageFile = null;
    private Image selectedImage = null;
    private TransformationType selectedTransformationType = TransformationType.DARKER;

    private Image processedImage = null;

    private static String[] availableTypes = {"jpg",  "bmp", "png"};

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // Populate the ImageTypeSelector woth elements
        this.ImageTypeSelector.setItems(FXCollections.observableArrayList(
                "All", "*.jpg", "*.bmp", "*.png"
                ));

        this.ImageTypeSelector.getSelectionModel().select(0);
        this.selectedTransformationType = TransformationType.DARKER;
        this.buttonSelectDarker.setSelected(true);

        Image imageSaveIcon = new Image(getClass().getResourceAsStream("./assets/Save_x20.png"));
        this.buttonSaveFile.setGraphic(new ImageView(imageSaveIcon));

        Image imageSelectIcon = new Image(getClass().getResourceAsStream("./assets/Open_x20.png"));
        this.buttonSelectImage.setGraphic(new ImageView(imageSelectIcon));

        Image imageLabelProgress = new Image(getClass().getResourceAsStream("./assets/Work_x20.png"));
        this.labelProgress.setGraphic(new ImageView(imageLabelProgress));

        // Add event handler on link to open on default browser
        try
        {
            URI url = new URI("https://en.wikipedia.org/wiki/Sobel_operator");
            this.linkSobelInfo.setOnMouseClicked((event)->{
                try
                {
                    Desktop.getDesktop().browse(url);
                    this.log("::Clicked On Info Link");
                }
                catch (IOException e)
                {
                    // DO something
                }
            });
        }
        catch(URISyntaxException e)
        {
            this.log("::URI syntax problem");
        }
    }

    /**
     * Function used to select an image and save tha path to the location.
     */
    public void selectImage()
    {
        FileChooser fileChooser = new FileChooser();

        // Create the dialog Scene
        Stage dialogFileChooser = new Stage();
        dialogFileChooser.setTitle("Load Image");
        dialogFileChooser.initModality(Modality.WINDOW_MODAL);
        dialogFileChooser.initOwner(Main.getPrimaryStage());

        File selectedFile = fileChooser.showOpenDialog(dialogFileChooser);

        if(this.checkTypeSelected(selectedFile))
        {
            this.selectedImagePath = selectedFile.getAbsolutePath();
            this.textImagePath.setText(this.selectedImagePath);
        }
        else
        {
            this.log("The file and the extemsion do not match");
        }
    }

    /**
     * Used to check if the selected image type is the same with that of the selected image.
     *
     * @param selectedFile
     * @return
     */
    private boolean checkTypeSelected(File selectedFile)
    {
        String extension = selectedFile.getName().split("\\.")[1];

        if(this.ImageTypeSelector.getSelectionModel().getSelectedItem().toString().equals("All"))
        {
            return true;
        }

        return extension.equals(this.ImageTypeSelector.getSelectionModel().getSelectedItem().toString().substring(2));
    }

    /**
     * Loads the image to the imageView and displays it.
     */
    public void loadImageToDisplay()
    {
        if(this.selectedImagePath == null)
        {
            this.contextualEventsTextArea.setText("An image must be selected first");
            this.contextualEventsTextArea.setStyle("-fx-fill: red; -fx-font: 16px \"Serif\"");
        }
        else
        {
            this.selectedImage = new Image(this.buildLoadingPath(this.selectedImagePath), 800, 600, true, true, true);

            this.log("Before thread");
            // Spawn a new thread to update the progress bar based on the percentage of image loaded
            this.updateTask(this.loadRawImageProgressbar, () -> this.selectedImage.getProgress());
            this.log("After thread");

            this.imageDisplayView.setImage(this.selectedImage);
        }

    }

    /**
     * Use this to decide which transformation was selected and call tha appropriate function
     */
    public void transformImage()
    {
        switch(this.selectedTransformationType)
        {
            case DARKER:
                this.transformImageDarker();
                break;
            case EDGE_DETECTION:
                this.transformImageEdgeDetection();
                break;
             default:
                 this.log("Transformation Problem");
                 break;
        }
    }

    /**
     * Function responsible with making the image darker.
     * It uses a default function to lower the brightness of each pixel.
     */
    public void transformImageDarker()
    {
        if(this.selectedImage == null)
        {
            this.contextualEventsTextArea.setText("An image must be selected first");
            this.contextualEventsTextArea.setStyle("-fx-fill: red; -fx-font: 16px \"Serif\"");
        }
        else
        {
            this.log("::Transforming Image -> Darker");
            ImageFilter imageFilter = new ImageFilter(this.selectedImage, this.imageDisplayView);
            imageFilter.transformDarker();
        }
    }

    /**
     * Function responsible with edge detection using the Sobel–Feldman operator
     */
    public void transformImageEdgeDetection()
    {
        if (this.selectedImage == null)
        {
            this.contextualEventsTextArea.setText("An image must be selected first");
            this.contextualEventsTextArea.setStyle("-fx-fill: red; -fx-font: 16px \"Serif\"");
        }
        else
        {
            this.log("::Transforming Image -> Edge Detection");
            ImageFilter imageFilter = new ImageFilter(this.selectedImage, this.imageDisplayView);
            imageFilter.transformEdge();
        }
    }

    /**
     * Utility function o parse a path
     *
     * @param strImagePath
     * @return
     */
    private String buildLoadingPath(String strImagePath)
    {
        this.selctedImageFile = new File(strImagePath);
        String parsedPath = this.selctedImageFile.toURI().toString();

        return parsedPath;
    }

    /**
     * Function used to update the loading bar in a separate thread in order to keep the UI responsive.
     *
     * @param progressBarElement
     * @param progressBarCallable
     * @return
     */
    private Thread updateTask(ProgressBar progressBarElement, ProgressBarInterface progressBarCallable)
    {
        Thread thread = new  Thread(() -> {
            double currentValue = 0;

            while(currentValue != 1)
            {
                currentValue = progressBarCallable.getProgress();
                mainController.this.log(String.valueOf(currentValue));
                progressBarElement.setProgress(currentValue);

                try
                {
                    Thread.sleep(10);
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
            }

            mainController.this.log("::Loading image Completed");
        });

        thread.setDaemon(true);
        thread.start();

        return thread;
    }

    /**
     * Function that saves the current image in the imageView to a specified path.
     */
    public void saveNewImage()
    {
        String imageSaveName = this.textfieldImageSaveName.getText();
        File file = new File("./" + imageSaveName + ".png");

        try
        {
            ImageIO.write(SwingFXUtils.fromFXImage(this.imageDisplayView.getImage(), null), "png", file);
            this.log("::Saved New Image");
        }
        catch(IOException e)
        {
            this.log(e.toString());
        }
    }

    public void selectTransTypeDarker()
    {
        this.selectedTransformationType = TransformationType.DARKER;
        this.buttonSelectEdgeDetection.setSelected(false);
        this.log("::Selected Darker Transformation");
    }

    public void selectTransTypeEdge()
    {
        this.selectedTransformationType = TransformationType.EDGE_DETECTION;
        this.buttonSelectDarker.setSelected(false);
        this.log("::Selected Edge Detection Transformation");
    }

    /**
     * Utility function to log stuff to both stdout and a special area in the GUI.
     *
     * @param output
     */
    private void log(String output)
    {
        this.contextualEventsTextArea.setStyle("-fx-fill: red; -fx-font: 16px \"Serif\"");
        this.contextualEventsTextArea.setText(output);
        System.out.println(output);
    }

    // Just for testing, do not use because there is no need to create a thread everytime, one thred is enough
//    private void updateTaskExecutor()
//    {
//        double progress = 0;
//
//        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//
//        Runnable task = () -> {
//          try
//          {
//              TimeUnit.MILLISECONDS.sleep(10);
//              System.out.println("Hello");
//          }
//          catch (InterruptedException e)
//          {
//              System.err.println("Tasked has stopped for some reason");
//              System.err.println(e.getCause());
//          }
//
//        };
//
//        executor.scheduleWithFixedDelay(task, 0, 1000, TimeUnit.MILLISECONDS);
//    }

}
