package com.ck.deskspace.repository;

import com.ck.deskspace.models.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface IAmenityRepository extends JpaRepository<Amenity, Long> {
    // We will use this to find amenities by a list of IDs (e.g., [1, 3])
     Set<Amenity> findAllByIdIn(Set<Long> ids);
}
