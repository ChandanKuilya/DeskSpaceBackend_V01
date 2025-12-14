package com.ck.deskspace.controllers;

import com.ck.deskspace.dto.WorkSpaceRequestDTO;
import com.ck.deskspace.dto.WorkSpaceResponseDTO;
import com.ck.deskspace.services.IWorkSpaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
public class WorkSpaceController {

    private final IWorkSpaceService workspaceService;

    public WorkSpaceController(IWorkSpaceService workspaceService) {
        this.workspaceService = workspaceService;
    }


    // GET: http://localhost:8080/api/workspaces
    @GetMapping
    public ResponseEntity<List<WorkSpaceResponseDTO>> getAllWorkspaces() {
        return ResponseEntity.ok(workspaceService.getAllWorkspaces());
    }


    // POST: http://localhost:8080/api/workspaces
    @PostMapping
    public ResponseEntity<WorkSpaceResponseDTO> createWorkspace(@RequestBody WorkSpaceRequestDTO request) {
        WorkSpaceResponseDTO newWorkspace = workspaceService.createWorkspace(request);
        return new ResponseEntity<>(newWorkspace, HttpStatus.CREATED);
    }

}
