package main;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

public class ImageFilter {

    private Image originalImage;
    private int[] imageBitmapBuffer;
    private ImageView imageDisplayView;

    private double width = 0;
    private double height = 0;

    private static int[][] filter = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
    };

    public ImageFilter(Image image, ImageView imageDisplayView)
    {
        this.originalImage  = image;
        this.imageDisplayView = imageDisplayView;

        this.width = this.originalImage.getWidth();
        this.height = this.originalImage.getHeight();
    }

    public void transformDarker()
    {
        WritableImage wImage = new WritableImage((int)this.width, (int)this.height);

        PixelReader pixelReader = this.originalImage.getPixelReader();
        PixelWriter pixelWriter = wImage.getPixelWriter();

        //Reading the color of the image
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                //Retrieving the color of the pixel of the loaded image
                Color color = pixelReader.getColor(x, y);

                //Setting the color to the writable image
                pixelWriter.setColor(x, y, color.darker());
            }
        }

        //Setting the view for the writable image
        this.imageDisplayView.setImage(wImage);
    }

    public void applyFilter()
    {
        // TODO implement filter transformation here
    }
}
