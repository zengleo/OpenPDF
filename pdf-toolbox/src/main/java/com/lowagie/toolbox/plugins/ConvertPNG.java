/*
 * $Id: SelectedPages.java 3271 2008-04-18 20:39:42Z xlv $
 * Copyright (c) 2005-2007 Bruno Lowagie, Carsten Hammer
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

/*
 * This class was originally published under the MPL by Bruno Lowagie
 * and Carsten Hammer.
 * It was a part of iText, a Java-PDF library. You can now use it under
 * the MIT License; for backward compatibility you can also use it under
 * the MPL version 1.1: http://www.mozilla.org/MPL/
 * A copy of the MPL license is bundled with the source code FYI.
 */

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

/**
 * This tool lets you convert a PDF into a PNG.
 */

public class ConvertPNG extends AbstractTool {
    /**
     * Constructs a ConvertPNG object.
     */
    public ConvertPNG() {
        menuoptions = MENU_EXECUTE | MENU_EXECUTE_SHOW;
        arguments.add(new FileArgument(this, "srcfile", "The file you want to convert", false, new PdfFilter()));
        arguments.add(new FileArgument(this, "destfile", "The image file", true));
        arguments.add(new IntegerArgument(this, "selection", "Which page to convert"));
        //arguments.add(new StringArgument(this, "resolution", "Resolution of image (dpi)"));
    }

    /**
     * @see com.lowagie.toolbox.AbstractTool#createFrame()
     */
    protected void createFrame() {
        internalFrame = new JInternalFrame("ConvertPNG", true, false, true);
        internalFrame.setSize(300, 80);
        internalFrame.setJMenuBar(getMenubar());
        System.out.println("=== ConvertPNG OPENED ===");
    }

    /**
     * @see com.lowagie.toolbox.AbstractTool#execute()
     */
    public void execute(){
        try{
            if (getValue("srcfile") == null) throw new InstantiationException("You need to choose a source file");
            File src = (File)getValue("srcfile");
            if (getValue("destfile") == null) throw new InstantiationException("You need to choose a destination file");
            File dest = (File)getValue("destfile");
            String selection = (String)getValue("selection");
            //String resolution = (String)getValue("resolution");

            PdfReader reader = new PdfReader(src.getAbsolutePath());

            final RenderedImage image = renderImage(reader, Integer.parseInt(selection));

            final BufferedImage image2 = ImageIO.read(new File("Capture.PNG"));
            ImageIO.write(image2, "PNG", dest);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * converts selected page into a BufferedImage
     */
    public BufferedImage renderImage(PdfReader reader, int index){

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
        return image;
    }

    /**
     * Generates a PNG from the selected page.
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        SelectedPages tool = new SelectedPages();
        if (args.length < 4) {
            System.err.println(tool.getUsage());
        }
        tool.setMainArguments(args);
        tool.execute();
    }

    /**
     *
     * @see com.lowagie.toolbox.AbstractTool#valueHasChanged(com.lowagie.toolbox.arguments.AbstractArgument)
     * @param arg StringArgument
     */
    public void valueHasChanged(AbstractArgument arg) {
        if (internalFrame == null) {
            // if the internal frame is null, the tool was called from the command line
            return;
        }
        // represent the changes of the argument in the internal frame
    }

    /**
     *
     * @see com.lowagie.toolbox.AbstractTool#getDestPathPDF()
     * @throws InstantiationException on error
     * @return File
     */
    protected File getDestPathPDF() throws InstantiationException {
        return (File)getValue("destfile");
    }


}
