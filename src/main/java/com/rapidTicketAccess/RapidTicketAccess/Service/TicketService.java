package com.rapidTicketAccess.RapidTicketAccess.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.layout.Document;
import com.rapidTicketAccess.RapidTicketAccess.DistanceCalculation.HaversineDistanceCalculator;
import com.rapidTicketAccess.RapidTicketAccess.Request.DistanceRequest;
import com.rapidTicketAccess.RapidTicketAccess.Model.Stop;
import com.rapidTicketAccess.RapidTicketAccess.Model.Ticket;
import com.rapidTicketAccess.RapidTicketAccess.Repository.TicketRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final JourneyService journeyService;

    public TicketService(TicketRepository ticketRepository, JourneyService journeyService){
                this.ticketRepository = ticketRepository;
        this.journeyService = journeyService;
    }


    // Create new Ticket
    public Ticket createTicket(Ticket ticket){
        return ticketRepository.save(ticket);
    }

    // Get all tickets
    public List<Ticket> getAllTickets(){
        return ticketRepository.findAll();
    }

    // Modify the ticket
    public Ticket modifyTicket(Long id, Ticket newTicket){
        Ticket oldTicket = ticketRepository.getReferenceById(id);
        oldTicket.setId(newTicket.getId());
        oldTicket.setSource(newTicket.getSource());
        oldTicket.setDestination(newTicket.getDestination());
        oldTicket.setFare(newTicket.getFare());
        oldTicket.setTimestamp(newTicket.getTimestamp());

        return oldTicket;
    }

    // Get ticket by Id
    public Ticket getTicketById(long id){
        return ticketRepository.getReferenceById(id);
    }

    // Remove the ticket
    public void removeTicket(Ticket ticket){
        ticketRepository.deleteById(ticket.getId());
    }


    public Integer calculateDistance(DistanceRequest request){
        Long journeyId = request.getJourneyId();
        String source = request.getSource();
        String destination = request.getDestination();
        List<Stop> stops = journeyService.getJourneyById(journeyId).getStops();
        int sourceDistance = 0;
        int destinationDistance = 0;
        for(Stop stop : stops){
            if(sourceDistance != 0 && destinationDistance != 0) break;
            if(stop.getCurrentStop().equalsIgnoreCase(source)) sourceDistance = stop.getDistanceFromStart();
            if(stop.getCurrentStop().equalsIgnoreCase(destination)) destinationDistance = stop.getDistanceFromStart();
        }
        return Math.abs(sourceDistance - destinationDistance);
    }

//    public Ticket generateTicket(DistanceRequest request){
//        Long journeyId = request.getJourneyId();
//        String source = request.getSource();
//        String destination = request.getDestination();
//        double fare = calculateDistance(request);
//        String paymentStatus = "Paid";
//        LocalDateTime timestamp = LocalDateTime.now();
//        Ticket ticket = new Ticket(source, destination, fare, paymentStatus, timestamp);
//
//        createTicket(ticket);
//        return ticket;
//    }





//========================================  Generate PDF receipt   ================================================




    private static final String RECEIPT_DIRECTORY = "receipts/";
    private static final String TEMPLATE_PATH = "templates/receipt_template.html";

    public String generatePdfReceipt(String source, String destination, int numberOfPassengers) {
        try {
            // Read the HTML template
            String htmlContent = loadHtmlTemplate();


            String str = calculateDistanceUsingOpenStreetMap(source, destination);
            double distance = 0;
            if(!str.equalsIgnoreCase("Invalid source or destination")){
                String[] arr = str.split(" ");
                distance = Double.parseDouble(arr[arr.length - 1]);
            }
            double amount = distance * numberOfPassengers;

            Ticket ticket = new Ticket();
            ticket.setSource(source);
            ticket.setDestination(destination);
            ticket.setNumberOfPassengers(numberOfPassengers);
            ticket.setFare(amount);
            ticket.setTimestamp(LocalDateTime.now());

            // Ensure the ticket gets an ID (if using database)
            ticket = createTicket(ticket);

            String ticketId = (ticket.getId() != null) ? ticket.getId().toString() : "N/A";



            // Replace placeholders  ticket details
            htmlContent = htmlContent
                    .replace("{{TICKET_ID}}", ticketId)
                    .replace("{{SOURCE}}", source)
                    .replace("{{DESTINATION}}", destination)
                    .replace("{{PASSENGERS}}", String.valueOf(numberOfPassengers))
                    .replace("{{PRICE}}", "Rs." + amount)
                    .replace("{{DATETIME}}", ticket.getTimestamp().toString());


            // Ensure the directory exists
            File directory = new File(RECEIPT_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate unique file name
            String filePath = RECEIPT_DIRECTORY + "receipt_" + System.currentTimeMillis() + ".pdf";

            // Convert HTML to PDF
            convertHtmlToPdf(htmlContent, filePath);
            return filePath;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String loadHtmlTemplate() throws IOException {
        try (InputStream inputStream = new ClassPathResource(TEMPLATE_PATH).getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    private void convertHtmlToPdf(String htmlContent, String filePath) throws Exception {
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            HtmlConverter.convertToPdf(htmlContent, outputStream);
        }
    }


    // Generate the url for payment
    public String generatePaymentUrl(String source, String destination, int numberOfPassengers, double amount) {
        // Simulating payment gateway integration (Replace with actual payment logic)
        String paymentGatewayUrl = "https://payment.example.com/pay"; // Replace with actual provider

        // Create a unique payment link (in a real case, you'd generate an order ID & signature)
        String paymentUrl = paymentGatewayUrl + "?source=" + source + "&destination=" + destination +
                "&passengers=" + numberOfPassengers + "&amount=" + amount;

        return paymentUrl;
    }




//========================================================================================

  // Calculate the distance using the Haversine Distance algorithm with latitudes and longitudes of the two places


    @Autowired
    private HaversineDistanceCalculator haversineDistanceCalculator;

    @Autowired
    private OpenStreetMapService openStreetMapService;

    public String calculateDistanceUsingOpenStreetMap(String source, String destination){
        String latLon1 = openStreetMapService.getLatLong(source);
        String latLon2 = openStreetMapService.getLatLong(destination);

        if(latLon1.equals("Location not found!") || latLon2.equals("Location not found!"))
            return "Invalid source or destination";

        String[] location1 = latLon1.split(",");
        String[] location2 = latLon2.split(",");

        //Latitude and longitude for source
        double lat1 = Double.parseDouble(location1[0]);
        double lon1 = Double.parseDouble(location1[1]);

        //Latitude and longitude for destination
        double lat2 = Double.parseDouble(location2[0]);
        double lon2 = Double.parseDouble(location2[1]);

        //Calculating the distance using Haversine Distance Calculator
        double distance = HaversineDistanceCalculator.calculateDistance(lat1, lon1, lat2, lon2);
        return "Distance between " + source + " and " + destination + " is " + Math.round(distance);
    }







}
