package fr.civilIteam.IncivilitiesTrack.service;


import fr.civilIteam.IncivilitiesTrack.dto.Process.ProcessDTO;

import java.util.List;
import java.util.UUID;

public interface IPRocessService {

   public List< ProcessDTO> getAllActive();
   public boolean UpdateStatusReport(UUID uuidProcess, UUID uuidStatus);
}
