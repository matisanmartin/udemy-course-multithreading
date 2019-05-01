package class9;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static String SOURCE_FILE = "./resources/flowers.jpg";
    public static String FINAL_FILE = "./out/flowers.jpg";

    public static void main(String args[]) throws IOException {

        BufferedImage image = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        long start = System.currentTimeMillis();

        //recolorSingleThread(image, result);
        int numberOfThreads = 2;
        recolorMultiThread(image, result, numberOfThreads);

        long end = System.currentTimeMillis();
        long duration = end - start;

        File output = new File(FINAL_FILE);
        ImageIO.write(result, "jpg", output);

        System.out.println(duration);
    }

    public static void recolorMultiThread(BufferedImage original, BufferedImage result, int numberOfThreads) {
        List<Thread> threads = new ArrayList<>();
        int width = original.getWidth();
        int height = original.getHeight()/numberOfThreads;

        for(int i = 0; i < numberOfThreads; i++) {
            final int  threadMultiplier = i;

            Thread thread = new Thread(() -> {
                int leftCorner = 0;
                int topCorner = height * threadMultiplier;

                recolorImage(original, result, leftCorner, topCorner, width, height);
            });

            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for(Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {

            }
        }


    }

    public static void recolorSingleThread(BufferedImage original, BufferedImage result) {
        recolorImage(original, result, 0, 0, original.getWidth(), original.getHeight());
    }

    public static void recolorImage(BufferedImage original, BufferedImage result, int leftCorner, int topCorner, int width, int height) {
        for(int x = leftCorner; x < leftCorner + width && x < original.getWidth(); x++) {
            for(int y = topCorner; y < topCorner + height && x < original.getHeight(); y++) {
                recolorPixel(original, result, x, y);
            }
        }
    }

    public static void recolorPixel(BufferedImage original, BufferedImage destination, int x, int y) {
        int rgb = original.getRGB(x, y);

        int red = getRed(rgb);
        int blue = getBlue(rgb);
        int green = getGreen(rgb);

        int newRed;
        int newBlue;
        int newGreen;

        if(isShadeOfGray(red, green, blue)) {
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newBlue = blue;
            newGreen = green;
        }

        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        setRgb(destination, x, y, newRGB);
    }

    public static void setRgb(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    public static boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
    }
    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;
        rgb |= 0xFF000000;
        return rgb;
    }
    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }
    public static int getGreen(int rgb) {
        return (rgb & 0x000000FF00) >> 8;
    }

    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }
}
