package com.ck.deskspace.repository;

import com.ck.deskspace.models.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWorkSpaceRepository extends JpaRepository<WorkSpace, Long> {
    // We will add the complex "Filter by Amenity" query here later.


    // For now, the standard methods (save, findAll, findById) are enough.
}
