package com.booking.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.booking.feign.PropertyFeign;
import com.booking.payload.PdfDetailsDto;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import lombok.AllArgsConstructor;

@Component
public class PDFUtil {
	
	private final AmazonS3 amazons3;
	
	@Value("${aws.bucket-name}")
	private String bucketName;
	
	public PDFUtil(AmazonS3 amazonS3){
		this.amazons3 = amazonS3;
	}
	
	public String pdfInvoiceGenerator(PdfDetailsDto bookingDto){
		File tempFile = null;
        try {
        	String uniqueFileName = UUID.randomUUID()+".pdf";
            tempFile = File.createTempFile("invoice-pdf", uniqueFileName);

            PdfWriter writer = new PdfWriter(tempFile);
            DeviceRgb deviceRgb = new DeviceRgb(255, 90, 95);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            PdfPage page = pdf.addNewPage();
            SolidLine solidLine = new SolidLine(5f);
            solidLine.setColor(deviceRgb);

            LineSeparator separator = new LineSeparator(solidLine);
            separator.setMarginTop(60).setOpacity(0.4f);
            separator.setMarginBottom(10);
            document.add(separator);

            PdfCanvas canvas = new PdfCanvas(page);
            float x = 10;
            float y = 10;
            float width = page.getPageSize().getWidth() - 2 * x;
            float height = page.getPageSize().getHeight() - 2 * y;
            canvas.setStrokeColor(new DeviceRgb(214, 214, 214));
            canvas.setLineWidth(3);
            canvas.rectangle(x, y, width, height);
            canvas.stroke();

            //String logoPath = "src/main/resources/static/logo.png";


            PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);

//            ImageData imageData = ImageDataFactory.create(logoPath);
//            Image logo = new Image(imageData);
//            float logoSize = 50;
//            logo.setWidth(logoSize);
//            logo.setFixedPosition(36, page.getPageSize().getTop() - 80);
//            document.add(logo);


            Paragraph paragraph = new Paragraph("AirBnb Service")
                    .setFont(font)
                    .setFontSize(20)
                    .setFontColor(ColorConstants.BLACK)
                    .setFixedPosition(430, page.getPageSize().getTop() - 65, 200);
            document.add(paragraph);

            Paragraph subTitle = new Paragraph("Hotel Reservation System")
                    .setFontSize(10)
                    .setBackgroundColor(deviceRgb)
                    .setPaddingLeft(10)
                    .setPaddingRight(10)
                    .setFontColor(ColorConstants.WHITE)
                    .setFixedPosition(430, page.getPageSize().getTop() - 70, 116);
            document.add(subTitle);

            Paragraph billingDetails = new Paragraph("Billing Details")
                    .setMultipliedLeading(1.4f)
                    .setFontSize(12)
                    .setMarginLeft(10)
                    .setFont(font)
                    .setFontColor(deviceRgb);
            document.add(billingDetails);

            Paragraph name = new Paragraph("Name : " + bookingDto.getGuestName())
                    .setMultipliedLeading(0.4f)
                    .setFontSize(10)
                    .setFont(font)
                    .setMarginLeft(10)
                    .setFontColor(ColorConstants.BLACK);
            document.add(name);

            Paragraph propertyName = new Paragraph("Property :" + bookingDto.getPropertyName())
                    .setMultipliedLeading(0.4f)
                    .setFontSize(10)
                    .setFont(font)
                    .setMarginLeft(10)
                    .setFontColor(ColorConstants.BLACK);
            document.add(propertyName);

            Paragraph booingNumber = new Paragraph("Booking Number :" + bookingDto.getBookingId())
                    .setMultipliedLeading(0.4f)
                    .setFontSize(10)
                    .setMarginLeft(10)
                    .setFont(font)
                    .setFontColor(ColorConstants.BLACK);
            document.add(booingNumber);

            Paragraph checkInDate = new Paragraph("Check In Date :" + bookingDto.getCheckInDate())
                    .setMultipliedLeading(0.4f)
                    .setFontSize(10)
                    .setMarginLeft(10)
                    .setFont(font)
                    .setFontColor(ColorConstants.BLACK);
            document.add(checkInDate);

            Paragraph checkOutDate = new Paragraph("Check Out Date :" + bookingDto.getCheckoutDate())
                    .setMultipliedLeading(0.4f)
                    .setMarginLeft(10)
                    .setFontSize(10)
                    .setFont(font)
                    .setFontColor(ColorConstants.BLACK);
            document.add(checkOutDate);

            Paragraph address = new Paragraph("Address :"+bookingDto.getPropertyAddress())
                    .setMultipliedLeading(0.4f)
                    .setMarginLeft(10)
                    .setFontSize(10)
                    .setFont(font)
                    .setFontColor(ColorConstants.BLACK);
            document.add(address);

            document.add(separator.setMarginTop(15).setOpacity(0.4f));

            float[] columnWidths = {0.35f, 0.2f, 0.2f, 0.2f, 0.2f, 0.2f};
            Table table = new Table(columnWidths, true);
            table.addCell("Item");
            table.addCell("Quantity");
            table.addCell("Nights");
            table.addCell("Price/Night");
            table.addCell("Gst");
            table.addCell("Total").setTextAlignment(TextAlignment.CENTER);
            table.setWidth(UnitValue.createPercentValue(100));
            table.setBackgroundColor(new DeviceRgb(191, 227, 255), 1f).setFontSize(13);
            document.add(table);

            float[] columnWidth = {0.35f, 0.2f, 0.2f, 0.2f, 0.2f, 0.2f};
            Table table1 = new Table(columnWidth, true).setTextAlignment(TextAlignment.CENTER);
            table1.addCell(new Cell().add(new Paragraph(bookingDto.getPropertyName())));
            table1.addCell(new Cell().add(new Paragraph(String.valueOf(bookingDto.getRoomCount()))));
            table1.addCell(String.valueOf(bookingDto.getNightCount()));
            table1.addCell(String.valueOf(bookingDto.getRoom().getPricePerNight()));
            table1.addCell(new Cell().add(new Paragraph(String.valueOf(bookingDto.getGst()))));
            table1.addCell(new Cell().add(new Paragraph(String.valueOf(bookingDto.getTotalAmount())).setPaddingRight(4)));
            table1.setWidth(UnitValue.createPercentValue(100));
            table1.setBackgroundColor(new DeviceRgb(232, 236, 250), 0.1f);
            document.add(table1);

            float[] footerWidth = {0.35f, 0.2f, 0.2f, 0.2f, 0.2f};
            Table footer = new Table(UnitValue.createPercentArray(footerWidth)).setMarginTop(3);
            footer.setBackgroundColor(new DeviceRgb(232, 236, 250), 0.5f);
            footer.addFooterCell(new Cell().add(new Paragraph("Total")
                            .setBold()
                            .setFontColor(ColorConstants.BLACK))
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPaddingLeft(25));

            footer.addFooterCell(new Cell().add(new Paragraph("     "))
                    .setBorder(Border.NO_BORDER));

            footer.addFooterCell(new Cell().add(new Paragraph("     "))
                    .setBorder(Border.NO_BORDER));

            footer.addFooterCell(new Cell().add(new Paragraph("     "))
                    .setBorder(Border.NO_BORDER));

            footer.addFooterCell(new Cell().add(new Paragraph(String.valueOf(bookingDto.getTotalAmount()))
                            .setBold()
                            .setFontColor(ColorConstants.BLACK))
                    .setBorder(Border.NO_BORDER)
                    .setPaddingLeft(90)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPaddingRight(20));

            document.add(footer);
            document.close();
            String url = uploadPdf(tempFile, uniqueFileName);
            tempFile.deleteOnExit();
            return url;
        }catch(IOException e) {
        	return null;
        }finally{
        	if(tempFile.exists()) {
        		tempFile.delete();
        	}
        }
    }
	
	private String uploadPdf(File file,String key) {
		amazons3.putObject(bucketName, key, file);
		URL url = amazons3.getUrl(bucketName, key);
		return url.toString();
	}
}
