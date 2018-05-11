package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.Hashtable;

public class SceneBuilder {

    private static Scene localScene = null;
    public static Hashtable<String, Node> graphicContainer =
            new Hashtable<String, Node>();

    public static ObservableList globalEventArray = null;

    public SceneBuilder()
    {

    }

    public static Scene createScene()
    {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(15, 20, 15, 20));
        gridPane.setVgap(30);
        gridPane.setHgap(30);

        //:::::::::First Section::::::::::
        Button buttonSelectImage = new Button();
        buttonSelectImage.setText("Select Image");
        gridPane.add(buttonSelectImage, 0,0);
        graphicContainer.put("buttonSelectImage", buttonSelectImage);

        Button buttonLoadImage = new Button();
        buttonLoadImage.setText("Load Image");
        gridPane.add(buttonLoadImage, 0,2);
        graphicContainer.put("buttonLoadImage", buttonLoadImage);

        Label selectImageType = new Label("Select Image type:");
        gridPane.add(selectImageType, 1, 0);

        ChoiceBox ImageTypeSelector = new ChoiceBox();
        gridPane.add(ImageTypeSelector, 2,0);
        graphicContainer.put("ImageTypeSelector", ImageTypeSelector);

        Label labelImagePath = new Label("Path of the image:");
        gridPane.add(labelImagePath,0,1);

        TextField textImagePath = new TextField("No image selected");
        gridPane.add(textImagePath, 1,1);
        graphicContainer.put("textImagePath", textImagePath);

        ProgressBar loadRawImageProgressbar = new ProgressBar(0.0);
        gridPane.add(loadRawImageProgressbar, 2,2);
        graphicContainer.put("loadRawImageProgressbar", loadRawImageProgressbar);

        Label labelProgress = new Label("Image loading status:");
        gridPane.add(labelProgress, 1,2);
        graphicContainer.put("labelProgress", labelProgress);


        //:::::::::Second Section::::::::::
        Button buttonTransformImage = new Button();
        buttonTransformImage.setText("Transform Image");
        gridPane.add(buttonTransformImage, 0,3);
        graphicContainer.put("buttonTransformImage", buttonTransformImage);

        RadioButton buttonSelectDarker = new RadioButton();
        buttonSelectDarker.setText("Darker");
        gridPane.add(buttonSelectDarker, 1,3);
        graphicContainer.put("buttonSelectDarker", buttonSelectDarker);

        RadioButton buttonSelectEdgeDetection = new RadioButton();
        buttonSelectEdgeDetection.setText("Edge");
        gridPane.add(buttonSelectEdgeDetection, 2,3);
        graphicContainer.put("buttonSelectEdgeDetection", buttonSelectEdgeDetection);

        Hyperlink linkSobelInfo = new Hyperlink();
        gridPane.add(linkSobelInfo, 0,4);
        graphicContainer.put("linkSobelInfo", linkSobelInfo);

        Button buttonSaveFile = new Button();
        buttonSaveFile.setText("Save Image");
        gridPane.add(buttonSaveFile, 0,5);
        graphicContainer.put("buttonSaveFile", buttonSaveFile);

        Label labelSaveFile = new Label("Save File Name:");
        gridPane.add(labelSaveFile, 1,5);

        TextField textfieldImageSaveName = new TextField();
        textfieldImageSaveName.setText("DefaultName");
        gridPane.add(textfieldImageSaveName, 2, 5);
        graphicContainer.put("textfieldImageSaveName", textfieldImageSaveName);


        //:::::::::Footer Section::::::::::
        TextArea contextualEventsTextArea = new TextArea();
        contextualEventsTextArea.setPrefHeight(50.0);
        contextualEventsTextArea.setPrefWidth(60.0);
        gridPane.add(contextualEventsTextArea, 0,6, 2,1);
        graphicContainer.put("contextualEventsTextArea", contextualEventsTextArea);

        ListView<String> eventList = new ListView<String>();
        ObservableList eventArray = FXCollections.observableArrayList();
        eventList.setItems(eventArray);
        eventList.setPrefHeight(100);
        gridPane.add(eventList, 0,7,2,1);
        globalEventArray = eventArray;

        ProgressIndicator eventsProgressIndicator = new ProgressIndicator(0.0);
        gridPane.add(eventsProgressIndicator,2,6);
        graphicContainer.put("eventsProgressIndicator", eventsProgressIndicator);

        //:::::::::Image Section::::::::::
        ImageView imageDisplayView = new ImageView();
        gridPane.add(imageDisplayView, 3,2,2,2);
        graphicContainer.put("imageDisplayView", imageDisplayView);

        SceneBuilder.localScene = new Scene(gridPane, 900, 600);

        return SceneBuilder.localScene;
    }

}
