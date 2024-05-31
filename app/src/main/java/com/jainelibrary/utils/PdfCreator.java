package com.jainelibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.jainelibrary.Constantss;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PdfCreator {

    public static final String FONT = "resources/fonts/hindi_texts.ttf";
    Context context;

    public static String createBitmapImagesToPDF(String filePath, String FileName, ArrayList<Bitmap> files) throws Exception {
        String strPdfFile = filePath + "/" + FileName + ".pdf";
        Log.e("PDFCREATOR--", "strPdfFile--" + strPdfFile);

//        Document document = new Document();
//        int width =files.get(0).getWidth();
//        int height = files.get(0).getHeight();
//        if (height > 10000) {
//            width =(int) files.get(0).getWidth() * (10000 / files.get(0).getHeight());
//            height = 10000;
//        }
//        Rectangle pageSize = new Rectangle(width+60, height+90);
//        document.setPageSize(pageSize);
        Document document = new Document(PageSize.A4, 30, 30, 45, 45);


        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(strPdfFile)));
        document.open();
        for (Bitmap file : files) {
            Bitmap f = file;
            document.newPage();
            Image image = Image.getInstance(getBytesFromBitmap(file));
            image.setBorderWidth(0); //image.scaleAbsolute(300,600);
            image.scaleToFit(document.getPageSize().getWidth() - 60, document.getPageSize().getHeight() - 90);
            document.add(image);
        }
        document.close();
        return strPdfFile;
    }

    private static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static String creatPdf(String filePath, String FileName, List<String> files) throws Exception {
        String strPdfFile = filePath + "/" + FileName + ".pdf";
        Log.e("PDFCREATOR--", "strPdfFile--" + strPdfFile);
        //HeaderFooter event = new HeaderFooter();
        Document document = new Document(PageSize.A4, -30, -30, 45, 45);
        // Document document =new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(strPdfFile)));
        writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
       // writer.setPageEvent(event);
        document.open();
        for (String f : files) {
            document.newPage();
            Image image = Image.getInstance(new File(f).getAbsolutePath());
            image.setAbsolutePosition(15, 15); //image.setAbsolutePosition(40F, 70f);
            image.setBorderWidth(0); //image.scaleAbsolute(300,600);
            image.scaleAbsolute(new Rectangle(36, 54, 559, 788)); //image.scaleAbsolute(PageSize.A4,-30,-30,-45,-45);
            document.add(image);
        }
        document.close();
        return strPdfFile;
    }


    public static String createTextPdf(String filePath, String FileName, List<String> files) {
        String strPdfFile = filePath + "/" + FileName + ".pdf";
        Log.e("PDFCREATOR--", "strPdfFile--" + strPdfFile);
        Log.e("PDFCREATOR--", "files--" + files.get(0));
        HeaderFooters event = new HeaderFooters();
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(strPdfFile)));
            writer.setBoxSize("ABC", PageSize.A4);
            writer.setPageEvent(event);
            //  Font paraFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            document.open();
            //   Font paraFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
            Paragraph paragraph = new Paragraph();
            Font paraFont = new Font(Font.FontFamily.COURIER);
            BaseFont unicode = null;
            BaseFont baseFont = BaseFont.createFont("assets/fonts/hindi_texts.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            //      unicode =   BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, true, false);
            Font font = new Font(baseFont, 14, Font.BOLD);
            //    files = new ArrayList<>();
            //   files.add("चंद्रलेखा चोपाई");
            int i = 0;
            for (String f : files) {
                Log.e("PDFCERATOR--", "files--" + f);
                Log.e("f", f);
                //  paragraph.add(new Paragraph(f, font));
                i++;
                Paragraph p = new Paragraph("(" + i + ") " + f, font);
                p.setSpacingBefore(2.5F);
                document.add(p);
            }
            //         document.add(paragraph);
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("FileNotDFound--", "files--" + e.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException--", "e--" + e.getMessage());
        } catch (DocumentException e) {
            e.printStackTrace();
            Log.e("DocumentException--", "e--" + e.getMessage());
        }
        return strPdfFile;
    }

    public static String stringtopdf(String filePath, String FileName, List<String> files) {
        String strPdfFile = filePath + "/" + FileName + ".pdf";
        try {
            FileOutputStream fOut = new FileOutputStream(new File(strPdfFile));
            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new
                    PdfDocument.PageInfo.Builder(100, 100, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            /*for (String f : files) {

            }*/
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            canvas.drawText(files.get(0), 10, 10, paint);
            document.finishPage(page);
            document.writeTo(fOut);
            document.close();

        } catch (IOException e) {
            Log.i("error", e.getLocalizedMessage());
        }
        return strPdfFile;
    }

    public static String createFile(String filePath, String fileName, List<String> files) {
        String strTextFile = filePath + "/" + fileName + ".txt";
        Log.e("strTextFile--", "strTextFile--" + strTextFile);
        int num = 0;

        FileOutputStream fileout = null;
        try {
            fileout = new FileOutputStream(new File(strTextFile));
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            for (String f : files) {

                num++;
                outputWriter.write(num + ". " + f + "\n");
            }
            outputWriter.flush();
            outputWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("FileNotDFound--", "files--" + e.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException--", "e--" + e.getMessage());
        }
        return strTextFile;
    }

    static class HeaderFooter extends PdfPageEventHelper {
        /**
         * Alternating phrase for the header.
         */
        Phrase[] header = new Phrase[2];
        /**
         * Current page number (will be reset for every chapter).
         */
        PdfReader reader;
        int pages = 0;

        {
            Log.e("PdfCreator " , "FileNamePDf :  : " + (Constantss.FILE_NAME_PDF));

            try {

                reader = new PdfReader(Constantss.FILE_NAME_PDF);
                pages = reader.getNumberOfPages();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("PdfCreator " , "Exception : " + e.getMessage());
                Log.e("pagenumber", reader.getNumberOfPages() + "");
                Log.e("IOException", pages + "");
            }
        }


        /**
         * Initialize one of the headers.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onOpenDocument(PdfWriter writer, Document document) {
            header[0] = new Phrase("JRL");
        }

        /**
         * Initialize one of the headers, based on the chapter title;
         * reset the page number.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onChapter(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document, float,
         * com.itextpdf.text.Paragraph)
         */
        public void onChapter(PdfWriter writer, Document document,
                              float paragraphPosition, Paragraph title) {

        }

        /**
         * Increase the page number.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onStartPage(PdfWriter writer, Document document) {
        }

        /**
         * Adds the header and the footer.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            String dateToStr = format.format(today);
        /*    ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(String.format(Constantss.FILE_NAME))
                    , ((rect.getLeft() + rect.getRight()) / 2), rect.getBottom() + 15, 0);*/
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase(String.format(Constantss.FILE_NAME_PDF) + " " + dateToStr),
                    (rect.getLeft() + 20), rect.getTop() - 30, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase(document.getPageNumber() + " of " + Constantss.PAGE_COUNT),
                    (rect.getRight() - 20), rect.getTop() - 30, 0);
            Log.e("pagenumber", +document.getPageNumber() + " of " + pages);
        }
    }

    static class HeaderFooters extends PdfPageEventHelper {
        PdfReader reader;
        int pages = 0;
        /**
         * com/jainelibrary/utils/PdfCreator.java:251
         * Alternating phrase for the header.
         */
        Phrase[] header = new Phrase[2];
        /**
         * Current page number (will be reset for every chapter).
         */
        int pagenumber;

        {
            try {
                String strFile = Constantss.FILE_NAME_PDF;

                if(strFile != null && strFile.length() > 0){
                    reader = new PdfReader(strFile);
                    pages = reader.getNumberOfPages();
                }

            } catch (IOException e) {
                Log.e("pagenumber", pages + "");
            }
        }

        /**
         * Initialize one of the headers.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onOpenDocument(PdfWriter writer, Document document) {
            header[0] = new Phrase("JRL");
        }

        /**
         * Initialize one of the headers, based on the chapter title;
         * reset the page number.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onChapter(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document, float,
         * com.itextpdf.text.Paragraph)
         */
        public void onChapter(PdfWriter writer, Document document,
                              float paragraphPosition, Paragraph title) {
        }

        /**
         * Increase the page number.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onStartPage(PdfWriter writer, Document document) {
        }

        /**
         * Adds the header and the footer.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("ABC");
            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            String dateToStr = format.format(today);
            System.out.println(dateToStr);

            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(String.format("")),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getTop() - 30, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase(""+ String.format(Constantss.FILE_NAME_PDF) + " " + dateToStr),
                    (rect.getLeft() + 20), rect.getTop() - 30, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase(document.getPageNumber() + " of " + pages),
                    (rect.getRight() - 20), rect.getTop() - 30, 0);
            Log.e("pagenumber", document.getPageNumber() + document.getPageNumber() + " of " + Constantss.PAGE_COUNT);

            /*  ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(String.format("")),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() + 15, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase(String.format("")),
                    (rect.getLeft()  +20), rect.getBottom()  + 15, 0);*/
         /*   ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase(Constantss.FILE_NAME),
                    (rect.getRight() - 20), rect.getBottom() + 15, 0);*/
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(String.format(Constantss.FILE_NAME_PDF))
                    , ((rect.getLeft() + rect.getRight()) / 2), rect.getBottom() + 15, 0);
        }
    }
}
