package iGuard.Server.Service;


import iGuard.Server.Entity.Shelter;
import iGuard.Server.Entity.User;
import iGuard.Server.Entity.VisitedShelter;
import iGuard.Server.Repository.ShelterRepository;
import iGuard.Server.Repository.UserRepository;
import iGuard.Server.Repository.VisitedShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class KioskService {

    @Autowired
    ShelterRepository sr;

    @Autowired
    UserRepository ur;

    @Autowired
    VisitedShelterRepository vr;


    public void enter(int shelterid, String phoneNumber){
       Shelter s = sr.findByShelterId(shelterid)
            .orElseThrow(()-> new RuntimeException("Shelter not found"));

        User user = ur.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));


        VisitedShelter visitedShelter = new VisitedShelter();
        visitedShelter.setShelter(s);
        visitedShelter.setUser(user);
        visitedShelter.setDate(LocalDateTime.now());


        if (s.getCurrentOccupancy() == null) {
            s.setCurrentOccupancy(1);
        } else {
            s.setCurrentOccupancy(s.getCurrentOccupancy() + 1);
        }

        vr.save(visitedShelter);
        sr.save(s);
    }

    public void exit(int shelterid, String phoneNumber){
        Shelter s = sr.findByShelterId(shelterid)
                .orElseThrow(()-> new RuntimeException("Shelter not found"));

        User user = ur.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(s.getCurrentOccupancy()==0){
            return;
        }else{
            s.setCurrentOccupancy(s.getCurrentOccupancy()-1);
        }
        sr.save(s);
    }

}
