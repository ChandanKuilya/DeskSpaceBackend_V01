package com.ck.deskspace.services;

import com.ck.deskspace.dto.AmenityDTO;
import com.ck.deskspace.dto.WorkSpaceRequestDTO;
import com.ck.deskspace.dto.WorkSpaceResponseDTO;
import com.ck.deskspace.models.Amenity;
import com.ck.deskspace.models.WorkSpace;
import com.ck.deskspace.repository.IAmenityRepository;
import com.ck.deskspace.repository.IWorkSpaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WorkSpaceServiceImpl implements IWorkSpaceService {

    private static final Logger logger = LoggerFactory.getLogger(WorkSpaceServiceImpl.class);


    private final IWorkSpaceRepository workspaceRepository;
    private final IAmenityRepository amenityRepository;

    // Constructor Injection (Best Practice)
    public WorkSpaceServiceImpl(IWorkSpaceRepository _workspaceRepository, IAmenityRepository _amenityRepository) {
        this.workspaceRepository = _workspaceRepository;
        this.amenityRepository = _amenityRepository;
    }

    @Override
    @CacheEvict(value = "workspaces", allEntries = true) // <--- CLEAR CACHE
    public WorkSpaceResponseDTO createWorkspace(WorkSpaceRequestDTO request) {
        // 1. Fetch Amenities from DB based on IDs sent by Admin
        Set<Amenity> amenities = amenityRepository.findAllByIdIn(request.getAmenityIds());

        // 2. Map DTO to Entity
        WorkSpace workspace = new WorkSpace();
        workspace.setName(request.getName());
        workspace.setType(request.getType());
        workspace.setCapacity(request.getCapacity());
        workspace.setPricePerHour(request.getPricePerHour());


        workspace.setAmenities(amenities); // Link the relationships!

        // 3. Save to DB
        WorkSpace savedworkSpace = workspaceRepository.save(workspace);

        // 4. Map Entity back to Response DTO
        return mapToResponseDTO(savedworkSpace);
    }

    @Override
    @Cacheable(value = "workspaces") // <--- USE CACHE
    public List<WorkSpaceResponseDTO> getAllWorkspaces() {

        // Simulate slowness (Optional: uncomment to feel the speed difference)
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }

        return workspaceRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

    }



    //-----------------------Helper Methods Below: -----------------------------------------------
    // Helper method to convert Entity -> DTO
    private WorkSpaceResponseDTO mapToResponseDTO(WorkSpace workspace) {

        WorkSpaceResponseDTO response = new WorkSpaceResponseDTO();

        response.setId(workspace.getId());
        response.setName(workspace.getName());
        response.setType(workspace.getType());
        response.setCapacity(workspace.getCapacity());
        response.setPricePerHour(workspace.getPricePerHour());

        // Convert Amenity Entities to Amenity DTOs
        Set<AmenityDTO> amenityDTOs = workspace.getAmenities().stream().map(amenity -> {
            AmenityDTO dto = new AmenityDTO();
            dto.setId(amenity.getId());
            dto.setName(amenity.getName());
            return dto;
        }).collect(Collectors.toSet());


        response.setAmenities(amenityDTOs);

        return response;
    }
}
