package com.rapidTicketAccess.RapidTicketAccess.Controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.rapidTicketAccess.RapidTicketAccess.Model.*;
import com.rapidTicketAccess.RapidTicketAccess.Request.DistanceRequest;
import com.rapidTicketAccess.RapidTicketAccess.Service.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final OpenStreetMapService openStreetMapService;

    public TicketController(TicketService ticketService, OpenStreetMapService openStreetMapService) {
        this.ticketService = ticketService;
        this.openStreetMapService = openStreetMapService;
    }



    // Create new ticket
    @PostMapping("/create")
    public Ticket createTicket(@RequestBody Ticket ticket){
        return ticketService.createTicket(ticket);
    }

    // Get all Tickets
    @GetMapping("/all")
    public List<Ticket> getAllTickets(){
        return ticketService.getAllTickets();
    }


    // Get ticket by id
    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Long id){
        return ticketService.getTicketById(id);
    }

    // Delete Ticket by id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long id){

        Ticket ticket = ticketService.getTicketById(id);
        if(ticket == null) return ResponseEntity.ofNullable("No ticket found");

        ticketService.removeTicket(ticket);
        return ResponseEntity.ok("Ticket removed successfully");
    }


    // Calculating the distance manually
    @PostMapping("/calculate-distance")
    public String calculateDistance(@RequestBody DistanceRequest request){
        return ticketService.calculateDistance(request).toString();
    }

    //============== QR Code generation =================

    @PostMapping("/generate")
    public ResponseEntity<String> generateQR(@RequestBody TicketRequest ticketRequest) throws WriterException {
        int numberOfPassengers = Integer.parseInt(ticketRequest.getNumberOfPassengers());

        String source = ticketRequest.getSource();
        String destination = ticketRequest.getDestination();

        String distance = ticketService.calculateDistanceUsingOpenStreetMap(source, destination);
        double amount = 0;
        if(!distance.equals("Invalid source or destination")){
            String[] arr = distance.split(" ");
            amount = Double.parseDouble(arr[arr.length - 1]);
        }

        String paymentUrl = "http://localhost:8080/api/tickets/payment?source=" + source +
                "&destination=" + destination +
                "&numberOfPassengers=" + numberOfPassengers +
                "&amount=" + amount;

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(paymentUrl, BarcodeFormat.QR_CODE, 250, 250);

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            String base64QRCode = Base64.getEncoder().encodeToString(baos.toByteArray());

            Map<String, String> response = new HashMap<>();
            response.put("qrCode", "data:image/png;base64," + base64QRCode);
            response.put("paymentUrl", paymentUrl);

            return ResponseEntity.ok(response.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //================================
    @PostMapping("/generateQRCode")
    public ResponseEntity<?> generateQRCode(@RequestBody TicketRequest request) {
        try {
            // Generate payment URL
            String paymentUrl = "http://localhost:8080/api/tickets/payment?source=" + request.getSource() +
                    "&destination=" + request.getDestination() +
                    "&numberOfPassengers=" + request.getNumberOfPassengers();

            // Generate QR Code (replace with actual QR code generation logic)
            String qrCodeImage = "data:image/png;base64," + Base64.getEncoder().encodeToString(paymentUrl.getBytes());

            // Return JSON response
            Map<String, String> response = new HashMap<>();
            response.put("qrCode", qrCodeImage);
            response.put("paymentUrl", paymentUrl);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Failed to generate QR Code\"}");
        }
    }





    //============== Process Payment ==============

    @GetMapping("/payment")
    public ResponseEntity<String> processPayment(@RequestParam String source,
                                                 @RequestParam String destination,
                                                 @RequestParam int numberOfPassengers) {
        // Simulate payment success
        String pdfPath = "receipts/ticket_" + System.currentTimeMillis() + ".pdf";
        generatePdfReceipt(pdfPath, source, destination, numberOfPassengers);

        return ResponseEntity.ok("http://localhost:8080/api/tickets/download?file=" + pdfPath);
    }

    private void generatePdfReceipt(String filePath, String source, String destination, int passengers) {
        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            String distance = ticketService.calculateDistanceUsingOpenStreetMap(source, destination);
            double amount = 0;
            if(!distance.equals("Invalid source or destination")){
                String[] arr = distance.split(" ");
                amount = Double.parseDouble(arr[arr.length - 1]);
            }


            document.add(new Paragraph("Bus Ticket Receipt"));
            document.add(new Paragraph("Source: " + source));
            document.add(new Paragraph("Destination: " + destination));
            document.add(new Paragraph("Passengers: " + passengers));
            document.add(new Paragraph("Amount: " + amount * passengers));
            document.add(new Paragraph("Payment Status: SUCCESS"));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadReceipt(@RequestParam String file) {
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



    //====================== Calculating the distance using the Open Street Map api =========================

    //Get the Latitude and longitude of the location
    @GetMapping("/location/{name}")
    public String getLatLong(@PathVariable String name){
        return openStreetMapService.getLatLong(name);
    }

    //Calculate the distance by using Open Street Map api
    @PostMapping("/calculate-distance-api")
    public String calculateDistanceUsingOpenStreetMap(@RequestBody DistanceRequest request){
        return ticketService.calculateDistanceUsingOpenStreetMap(request.getSource(), request.getDestination());
    }

}


