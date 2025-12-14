package com.ck.deskspace.services;

import com.ck.deskspace.dto.WorkSpaceRequestDTO;
import com.ck.deskspace.dto.WorkSpaceResponseDTO;

import java.util.List;

public interface IWorkSpaceService {

    WorkSpaceResponseDTO createWorkspace(WorkSpaceRequestDTO workspaceRequest);

    List<WorkSpaceResponseDTO> getAllWorkspaces();

}
