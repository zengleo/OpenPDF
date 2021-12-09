package com.lowagie.text.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlphaTest {

    @Test
    @Disabled
    void alphaTest() throws IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("target/testAlphaBoxes.pdf"));
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();

        document.setPageSize(PageSize.A4);
        document.newPage();

        //draw first rectangle
        cb.saveState();
        cb.setColorFill(new Color(255, 0, 0, 50));   //alpha value = 50
        cb.setColorStroke(Color.RED);
        cb.rectangle(200, 400, 30, 30);
        cb.fillStroke();
        cb.restoreState();

        //draw second rectangle
        cb.saveState();
        cb.setColorFill(new Color(0, 0, 255, 50));   //same alpha value (50)
        cb.setColorStroke(Color.BLUE);
        cb.rectangle(300, 400, 30, 30);
        cb.fillStroke();
        cb.restoreState();

        document.close();
        outputStream.close();

        File original = new File(getClass().getClassLoader().getResource("AlphaBoxes.pdf").getFile());
        File current = new File("target/testAlphaBoxes.pdf");

        assertTrue(FileUtils.contentEquals(original, current));

    }


}
