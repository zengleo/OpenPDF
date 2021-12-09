package com.lowagie.toolbox.plugins;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.imageio.ImageIO;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;

/**
 * Exports an image of a pdf page
 *
 * @author Leo Zeng
 */

public class WriteToImage {

    private File src;
    private File out;
    private int selection;

    /**
     * Reads a pdf page and exports an image file
     *
     * @param src location of the pdf file to be exported into an image.
     * @param out location of the image file to be exported.
     * @param selection page of the pdf file to be exported into an image.
     */

    public WriteToImage(String src, String out, int selection){
        this.src = new File(src);
        this.out = new File(out);
        this.selection = selection;

    }

    /**
     * Run to export image file
     *
     * @throws IOException
     */

    public void main() throws IOException {
        PdfReader reader = new PdfReader(src.getAbsolutePath());
        final BufferedImage image = renderImage(reader, selection);
        writePNG(image,out);
    }

    /**
     * Builds image object to be exported
     *
     * @param reader PdfReader object pointing to the pdf file
     * @param index page of the pdf file to be exported as an image
     */

    private BufferedImage renderImage(PdfReader reader, int index){
        Rectangle box = reader.getCropBox(index);
        float widthPt = box.getWidth();
        float heightPt = box.getHeight();

        int widthPx = (int) Math.max(Math.floor(widthPt), 1);
        int heightPx = (int) Math.max(Math.floor(heightPt), 1);

        int rotationAngle = box.getRotation();

        int bimType = BufferedImage.TYPE_INT_ARGB;

        BufferedImage image;

        if (rotationAngle == 90 || rotationAngle == 270)
        {
            image = new BufferedImage(heightPx, widthPx, bimType);
        }
        else
        {
            image = new BufferedImage(widthPx, heightPx, bimType);
        }

        Graphics2D g = image.createGraphics();
        if (image.getType() == BufferedImage.TYPE_INT_ARGB)
        {
            g.setBackground(new Color(0, 0, 0, 0));
        }
        else
        {
            g.setBackground(Color.WHITE);
        }
        g.clearRect(0, 0, image.getWidth(), image.getHeight());

        // implement page drawer

        g.dispose();

        //image = ImageIO.read(new File("Capture.PNG"));

        return image;
    }

    /**
     * Exports image object to image file
     *
     * @param image image object of the pdf page
     * @param out location of export file
     */

    private static void writePNG(BufferedImage image, File out){
        try {
            RenderedImage ri = image;
            ImageIO.write(ri, "PNG", out);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
