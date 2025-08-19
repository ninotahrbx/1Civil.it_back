package fr.civilIteam.IncivilitiesTrack.service;

import fr.civilIteam.IncivilitiesTrack.dto.RequestName;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseRole;

import java.util.List;
import java.util.UUID;

public interface IRoleService {

    public List<ResponseRole> getAll();
    public ResponseRole  getByUUID (UUID uuid );
    public ResponseRole getByName (String name );
    public ResponseRole addNew (RequestName requestName);
    public ResponseRole update (UUID uuid ,RequestName name );
    public boolean delete (UUID uuid);
}
