package com.rapidTicketAccess.RapidTicketAccess.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.rapidTicketAccess.RapidTicketAccess.DistanceCalculation.HaversineDistanceCalculator;
import com.rapidTicketAccess.RapidTicketAccess.Request.DistanceRequest;
import com.rapidTicketAccess.RapidTicketAccess.Model.Stop;
import com.rapidTicketAccess.RapidTicketAccess.Model.Ticket;
import com.rapidTicketAccess.RapidTicketAccess.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

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
        oldTicket.setPaymentStatus(newTicket.getPaymentStatus());

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

//========================================================================================

    public String generatePdfReceipt(String source, String destination, int numberOfPassengers) {
        try {

            String distanceUsingOpenStreetMap = calculateDistanceUsingOpenStreetMap(source, destination);

            double amount = 0;

            if(!distanceUsingOpenStreetMap.equalsIgnoreCase("Invalid source or destination")){
                String[] arr = distanceUsingOpenStreetMap.split(" ");
                amount = Double.parseDouble(arr[arr.length - 1]);
            }
            amount = amount * numberOfPassengers;
            Ticket ticket = new Ticket(source, destination, numberOfPassengers, amount, LocalDateTime.now());

            // Add ticket details in the database
            createTicket(ticket);

            long ticketId = ticket.getId();

            // Read HTML template
            String htmlContent = readHtmlTemplate(htmlFilePath, Long.toString(ticketId), source, destination, numberOfPassengers, amount, ticket.getTimestamp().toString());

            // Create PDF Document
            Document document = new Document(PageSize.A6);
            document.open();

            // Convert HTML to PDF
            InputStream htmlStream = new ByteArrayInputStream(htmlContent.getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, htmlStream);

            // Generate QR Code and add it to the PDF
            String qrPath = "qrcode.png";
            String qrData = "Ticket ID: " + ticketId + "\nSource: " + source + "\nDestination: " + destination +
                    "\nPassengers: " + numberOfPassengers + "\nPrice: ₹" + amount + "\nDate & Time: " + ticket.getTimestamp().toString();
            generateQRCode(qrData, qrPath, 150, 150);

            Image qrImage = Image.getInstance(qrPath);
            qrImage.scaleToFit(120, 120);
            qrImage.setAlignment(Element.ALIGN_CENTER);
            document.add(qrImage);

            document.close();

            System.out.println("Receipt PDF generated from HTML successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Read HTML file and replace placeholders with actual ticket details
    private String readHtmlTemplate(String htmlFilePath, String ticketId, String source, String destination,
                                    int numberOfPassengers, double price, String dateTime) throws IOException {
        StringBuffer content = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(htmlFilePath));
        String line;
        while((line = bufferedReader.readLine()) != null) {
            content.append(line).append("\n");
        }
        bufferedReader.close();

        // Replace placeholders with actual values

        return content.toString()
                .replace("{{TICKET_ID}}", ticketId)
                .replace("{{SOURCE}}", source)
                .replace("{{DESTINATION}}", destination)
                .replace("{{PASSENGERS}}", String.valueOf(numberOfPassengers))
                .replace("{{PRICE}}", "₹" + price)
                .replace("{{DATETIME}}", dateTime);
    }

    //Generate QR Code
    private void generateQRCode(String data, String filePath, int width, int height) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(matrix, "PNG", path);
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
