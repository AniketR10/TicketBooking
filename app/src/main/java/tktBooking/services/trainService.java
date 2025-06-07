package tktBooking.services;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import tktBooking.entities.*;

public class trainService { 

    List<Train> trainList;
   private ObjectMapper objectMapper = new ObjectMapper();
   private static final String TRAINS_PATH =  "src/main/java/tktBooking/localDB/trains.json";

   public trainService() throws IOException {
     loadTrains();
   }
   
   private List<Train> loadTrains() throws IOException{
    File trains = new File(TRAINS_PATH);
    if(!trains.exists()){
        System.out.println("NO trains are there presently...");
        trainList = new ArrayList<>();
        return trainList;
    }
    
    trainList = objectMapper.readValue(trains, new TypeReference<List<Train>>(){});
    //   for(Train t : trainList){
    //     System.out.println("Train ID:" + t.getTrainId() + " stations : " + t.getStations());
    //   }
    
    return trainList;

   }

  
    public List<Train> searchTrains(String src, String dest){
       return trainList.stream().filter(train1 -> validTrains(src, dest, train1)).collect(Collectors.toList());
    }

    private Boolean validTrains(String src, String dest, Train train){
        List<String> stations = train.getStations();
        int srcIdx = stations.indexOf(src.toLowerCase());
        int destIdx = stations.indexOf(dest.toLowerCase());

        return srcIdx != -1 && destIdx != -1 && srcIdx < destIdx;
    }

    public void updateTrains(Train tr) throws IOException{
        File trains = new File(TRAINS_PATH);
       System.out.println("update Trains called...");
        
        boolean found = false;
         for(Train t : trainList) {
            if(t.getTrainId().equals(tr.getTrainId()) && t.getTrainNo().equals(tr.getTrainNo())){
                t.setSeats(tr.getSeats());
                found = true;
                break;
            }
         }
         if(found == false){
            System.out.println("Train not found");
            return;
         }
        
      objectMapper.writeValue(trains, trainList);

    }


}
