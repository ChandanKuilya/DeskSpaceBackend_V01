package com.ck.deskspace.config;

import com.ck.deskspace.models.Amenity;
import com.ck.deskspace.repository.IAmenityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final IAmenityRepository amenityRepository;

    public DataLoader(IAmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // If the table is empty, add some default amenities
        if (amenityRepository.count() == 0) {
            List<Amenity> amenities = List.of(
                    new Amenity(null, "High-Speed WiFi"),   // ID 1
                    new Amenity(null, "Projector"),         // ID 2
                    new Amenity(null, "Whiteboard"),        // ID 3
                    new Amenity(null, "Coffee Machine"),    // ID 4
                    new Amenity(null, "Sound System")       // ID 5
            );
            amenityRepository.saveAll(amenities);
            System.out.println("Default Amenities loaded into Database.");
        }
    }

}
