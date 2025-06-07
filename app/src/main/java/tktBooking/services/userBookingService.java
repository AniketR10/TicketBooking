package tktBooking.services;
import java.util.*;

import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import tktBooking.Util.UserServiceUtil;
import tktBooking.entities.*;


public class userBookingService {
    private User user;

    private List<User> userList;

    private ObjectMapper objectMapper = new ObjectMapper();

    public final String USERS_PATH = "src/main/java/tktBooking/localDB/users.json";

   public userBookingService(User user) throws IOException{
        this.user = user;
      loadUsers();
    } 

    public userBookingService() throws IOException{
       loadUsers();
    }

   private List<User> loadUsers() throws IOException {
    File users = new File(USERS_PATH);
    System.out.println("Trying to load users from: " + users.getAbsolutePath());
    if (!users.exists()) {
        System.out.println("User file does not exist!");
        // Optionally create a new empty list to avoid null errors
        userList = new ArrayList<>();
        return userList;
    }
    userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    return userList;
}
            

    public Boolean loginUser(){
       Optional<User> foundUser = userList.stream().filter(user1 -> {
  return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user1.getPassword(), user.getHashedPassword());
}).findFirst();

    //    for(User u : userList){
    //     System.out.println("User name: " + u.getName());
    //     System.out.println("User password:" + u.getPassword());
    //    }
     if(foundUser.isPresent()){
        this.user = foundUser.get();
        System.out.println("Successfully Logged in...");
        return true;
     }

     System.out.println("The user is not present...");
     return false;

    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException e){
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(usersFile, userList); // serialization
    }

    public void fetchBooking(){
        user.printTickets();
    }

    private void displayTkts(){
        List<Ticket> tkts = user.getTicketsBooked();
        for(Ticket t : tkts){
            System.out.println("Ticket ID: " + t.getTicketId());
            System.out.println("Source: " + t.getSource());
            System.out.println("Destination: " + t.getDestination());
            System.out.println("Date of travel: " + t.getDateOfTravel());
        }
    }

    private void updateTkts(User user) throws IOException{
         for(User us : userList){
                if(us.getUserId().equals(user.getUserId())){
                    us.setTicketsBooked(user.getTicketsBooked());
                    this.user = us;
                    break;
                }
            }

         File updatedTktList = new File(USERS_PATH);
        objectMapper.writeValue(updatedTktList, userList);
    }
 
    public Boolean cancelBooking(){
        Scanner s = new Scanner(System.in);
        System.out.println("Here are you booked tickets");
            displayTkts();
        System.out.println("Enter the tkt id that you want to cancel");
        String tktId = s.nextLine();  

        if(tktId == null || tktId.isEmpty()){
            System.out.println("Tkt id cannot be null or empty.");
            return Boolean.FALSE;
        }

        Boolean removed = user.getTicketsBooked().removeIf(currId -> currId.getTicketId().equals(tktId));
        if(removed){
            System.out.println("Tkt with id:" + tktId + "has been cancelled...");
            
            try {
                 updateTkts(user);
            } catch(IOException e){
                System.out.println("There was an error updating the ticket data" + e);
                return Boolean.FALSE;
            }
            
            return Boolean.TRUE;
        } else {
            System.out.println("No tkt found with Id :" + tktId);
            return Boolean.FALSE;
        }

    }

    public List<Train> getTrains(String src, String dest){
        try{
            trainService ts = new trainService();
            return ts.searchTrains(src,dest);
        } catch(Exception ex){
            System.out.println("Error in fetching the info." + ex);
            return new ArrayList<>();
        }
    }

    public void bookSeats(Integer row, Integer col,Train tr, Ticket newTkt) throws Exception{
        System.out.println("Books seats called");
        List<List<Integer>> seat = tr.getSeats();
        if(seat.get(row).get(col) == 1){
            System.out.println("Seat is already booked.");
        } else {
            seat.get(row).set(col,1);
            tr.setSeats(seat);
            trainService ts = new trainService();
            ts.updateTrains(tr);
        }

        // adding tkt to the user database
        newTkt.setTicketId(UUID.randomUUID().toString());
        newTkt.setUserId(user.getUserId());
        newTkt.setSource(tr.getStations().get(0));
        newTkt.setDestination(tr.getStations().get(tr.getStations().size()-1));
        //newTkt.setDateOfTravel() already done
        newTkt.setTrain(tr);

        addTkt(newTkt);

    }

    public void addTkt(Ticket newTkt)throws IOException{
        File usersToUpdate = new File(USERS_PATH);
        
        for(User us : userList){
            if(us.getUserId().equals(user.getUserId())){
                List<Ticket> tkts = us.getTicketsBooked();
                tkts.add(newTkt);
                us.setTicketsBooked(tkts);
                this.user = us;
                break;
            }
        }
        
            objectMapper.writeValue(usersToUpdate, userList);
            
    }



}
