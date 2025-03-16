package com.rapidTicketAccess.RapidTicketAccess.Controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class paymentController {

    @GetMapping("payment")
    public ResponseEntity<String> processPayment(@RequestParam String source,
                                                 @RequestParam String destination,
                                                 @RequestParam int passengers) {
        // Simulate payment success
        String pdfPath = "recipts/ticket_" + System.currentTimeMillis() + ".pdf";
        generatePdfReceipt(pdfPath, source, destination, passengers);

        return ResponseEntity.ok("http://localhost:8080/api/tickets/download?file=" + pdfPath);
    }

    private void generatePdfReceipt(String pdfPath, String source, String destination, int passengers) {
        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfPath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Bus Ticket Receipt"));
            document.add(new Paragraph("Source: " + source));
            document.add(new Paragraph("Destination: " + destination));
            document.add(new Paragraph("Passengers: " + passengers));
            document.add(new Paragraph("Payment Status: SUCCESS"));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadRecipt(@RequestParam String file) {
        try {
            Path path = Paths.get(file);
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receipt.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }





    @GetMapping("/upi")
    public ResponseEntity<String> generateUPILink(
            @RequestParam String upiId,
            @RequestParam String name,
            @RequestParam String amount,
            @RequestParam String orderId
    ){
        String upiUrl = String.format(
                "upi://pay?pa=%s&pn=%s&mc=&tid=%s&tr=%s&tn=Ticket Payment&am=%s&cu=INR",
                upiId, name, UUID.randomUUID().toString(), orderId, amount
        );
        return ResponseEntity.ok(upiUrl);
    }
}
