package tktBooking.entities;
import java.util.*;

public class User {
    private String userId;
    private String name;
    private String password;
    private String hashPassword;

    private List<Ticket> tktBooked;
    
   public User(String userId, String name, String password, String hashPassword, List<Ticket>tktBooked){
        this.name = name;
        this.userId = userId;
        this.hashPassword = hashPassword;
        this.password = password;
        this.tktBooked = tktBooked;
    }

    public User(){}

    public String getName(){
        return this.name;
    }

    public String getPassword(){
        return this.password;
    }

    public String getHashedPassword() {
        return this.hashPassword;
    }

    public List<Ticket> getTicketsBooked() {
        return this.tktBooked;
    }

    public void printTickets(){
        if(tktBooked.size() == 0){
            System.out.println("No tickets booked");
            return;
        }
        for (int i = 0; i<tktBooked.size(); i++){
            System.out.println(tktBooked.get(i).getTicketInfo());
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashPassword = hashedPassword;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.tktBooked = ticketsBooked;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
