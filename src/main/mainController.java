package main;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    // TODO use specific image formats to make a slect bar or something (exe: select png, fpg, bmp, etc.)
    // Normal non FXML members
    private String selectedImagePath = null;
    private File selctedImageFile = null;
    private Image selectedImage = null;

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

    private boolean checkTypeSelected(File selectedFile)
    {
        String extension = selectedFile.getName().split("\\.")[1];
        return extension.equals(this.ImageTypeSelector.getSelectionModel().getSelectedItem().toString().substring(2));
    }

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
            imageFilter.transformEdgeDetection();
        }
    }

    private String buildLoadingPath(String strImagePath)
    {
        this.selctedImageFile = new File(strImagePath);
        String parsedPath = this.selctedImageFile.toURI().toString();

        return parsedPath;
    }

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

    private void log(String output)
    {
        this.contextualEventsTextArea.setStyle("-fx-fill: red; -fx-font: 16px \"Serif\"");
        this.contextualEventsTextArea.setText(output);
        System.out.println(output);
    }
}
