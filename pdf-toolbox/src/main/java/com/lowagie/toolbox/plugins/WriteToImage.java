package com.lowagie.toolbox.plugins;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;

import java.util.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.RenderingHints;

import javax.swing.JInternalFrame;

import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.toolbox.AbstractTool;
import com.lowagie.toolbox.arguments.AbstractArgument;
import com.lowagie.toolbox.arguments.FileArgument;
import com.lowagie.toolbox.arguments.IntegerArgument;
import com.lowagie.toolbox.arguments.StringArgument;
import com.lowagie.toolbox.arguments.filters.PdfFilter;

public class WriteToImage {

    private File src;
    private File out;
    private int selection;
    private RenderingHints renderingHints = null;

    public WriteToImage(String src, String out, int selection){
        this.src = new File(src);
        this.out = new File(out);
        this.selection = selection;

    }

    public void main() throws IOException {
        PdfReader reader = new PdfReader(src.getAbsolutePath());
        final BufferedImage image = renderImage(reader, selection);
        writePNG(image,out);
    }

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

        g.dispose();

        //image = ImageIO.read(new File("Capture.PNG"));

        return image;
    }

    private static void writePNG(BufferedImage image, File out){
        try {
            RenderedImage ri = image;
            ImageIO.write(ri, "PNG", out);

        } catch ( IOException e) {
            e.printStackTrace();
        }
    }



}
