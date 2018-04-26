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

    private static double[][] orizontalFilter = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
    };

    private static double[][] verticalFilter = {
            {1, 2, 1},
            {0, 0, 0},
            {-1, -2, -1}
    };

    public ImageFilter(Image image, ImageView imageDisplayView) {
        this.originalImage = image;
        this.imageDisplayView = imageDisplayView;

        this.width = this.originalImage.getWidth();
        this.height = this.originalImage.getHeight();
    }

    public void transformEdge() {
        WritableImage wImage = new WritableImage((int) this.width, (int) this.height);

        PixelReader pixelReader = this.originalImage.getPixelReader();
        PixelWriter pixelWriter = wImage.getPixelWriter();

        //Reading the color of the image
        for (int y = 0; y < height - 2; y++) {
            for (int x = 0; x < width - 2; x++) {
                //Retrieving the matrix portion of the image
                Color[][] colorMatrix = new Color[3][3];

                colorMatrix[0][0] = pixelReader.getColor(x, y);
                colorMatrix[0][1] = pixelReader.getColor(x, y + 1);
                colorMatrix[0][2] = pixelReader.getColor(x, y + 2);
                colorMatrix[1][0] = pixelReader.getColor(x + 1, y);
                colorMatrix[1][1] = pixelReader.getColor(x + 1, y + 1);
                colorMatrix[1][2] = pixelReader.getColor(x + 1, y + 2);
                colorMatrix[2][0] = pixelReader.getColor(x + 2, y);
                colorMatrix[2][1] = pixelReader.getColor(x + 2, y + 1);
                colorMatrix[2][2] = pixelReader.getColor(x + 2, y + 2);

                // Compute gradient with Sobel Operator
                double localComputedValue = this.applySobel(colorMatrix);

                //Setting the new value to the writable image
                pixelWriter.setColor(x, y, Color.gray(localComputedValue));
            }
        }

        //Setting the view for the writable image
        this.imageDisplayView.setImage(wImage);
    }


    public void transformDarker() {
        WritableImage wImage = new WritableImage((int) this.width, (int) this.height);

        PixelReader pixelReader = this.originalImage.getPixelReader();
        PixelWriter pixelWriter = wImage.getPixelWriter();

        //Reading the color of the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //Retrieving the color of the pixel of the loaded image
                Color color = pixelReader.getColor(x, y);

                //Setting the color to the writable image
                pixelWriter.setColor(x, y, color.darker());
            }
        }

        //Setting the view for the writable image
        this.imageDisplayView.setImage(wImage);
    }


    public double applySobel(Color[][] convStep) {
        double resultOne = 0;
        double resultTwo = 0;
        double magnitude = 0;
        double greyValue = 0;

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                greyValue = convStep[x][y].grayscale().getRed();

                resultOne += greyValue * orizontalFilter[y][x];
                resultTwo += greyValue * verticalFilter[y][x];
            }
        }

        magnitude = Math.sqrt(Math.pow(resultOne, 2) + Math.pow(resultTwo, 2));

        return magnitude > 1.0 ? 1 : magnitude;
    }

    public double toGray(double R, double G, double B) {
        return (double) 0.2989 * R + (double) 0.5870 * G + (double) 0.1140 * B;
    }
}

/*
    -----------------CODE CEMETERY------------------
    public void transformEdge()
    {
    WritableImage wImage = new WritableImage((int)this.width, (int)this.height);

    PixelReader pixelReader = this.originalImage.getPixelReader();
    PixelWriter pixelWriter = wImage.getPixelWriter();

    //Reading the color of the image
    for(int y = 0; y < height - 2 ; y++) {
    for(int x = 0; x < width - 2; x++) {
    //Retrieving the matrix portion of the image
    int size = 3 * 3 * 4;
    int[] localMatrix = new int[size];
    System.out.println(x);
    pixelReader.getPixels(x, y, 2, 2, WritablePixelFormat.getIntArgbInstance(), localMatrix, 0,  (int)width * 4);

    // Compute gradient with Sobel Operator
    double localComputedValue = this.applySobel(localMatrix);

    //Setting the new value to the writable image
    pixelWriter.setColor(x, y, Color.grayRgb((int)localComputedValue));
    }
    }

    //Setting the view for the writable image
    this.imageDisplayView.setImage(wImage);
    }
    public double applySobel(int[] convStep)
    {
    double resultOne = 0;
    double resultTwo = 0;
    double magnitude = 0;

    for(int y = 0; y < 3; y++)
    {
    for(int x = 0; x < 3; x++)
    {
    double greyValue = this.toGray(convStep[y * 12 + x + 1], convStep[y * 12 + x + 2], convStep[y * 12 + x + 3]);
    resultOne += greyValue * orizontalFilter[y][x];
    resultTwo += greyValue * verticalFilter[y][x];
    }
    }

    magnitude = Math.sqrt(Math.pow(resultOne, 2)) + Math.sqrt(Math.pow(resultTwo, 2));

    return magnitude;
    }
    */

