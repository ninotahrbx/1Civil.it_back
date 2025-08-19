package fr.civilIteam.IncivilitiesTrack.service.impl;

import fr.civilIteam.IncivilitiesTrack.dto.RequestStatus;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseStatus;
import fr.civilIteam.IncivilitiesTrack.dto.mapper.IStatusMapper;
import fr.civilIteam.IncivilitiesTrack.exception.NotModifiedException;
import fr.civilIteam.IncivilitiesTrack.exception.StatusAlreadyExistException;
import fr.civilIteam.IncivilitiesTrack.exception.StatusAlreadyUsedException;
import fr.civilIteam.IncivilitiesTrack.exception.StatusNotFoundException;
import fr.civilIteam.IncivilitiesTrack.models.Status;
import fr.civilIteam.IncivilitiesTrack.repository.IStatusRepository;
import fr.civilIteam.IncivilitiesTrack.service.IStatusService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class StatusServiceImpl implements IStatusService {

    private final IStatusRepository statusRepository;

    public StatusServiceImpl(IStatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    /**
     * Allow to create a status
     * @param requestStatus DTO with parameters
     * @return Status saved with ID and UUID generated
     * @throws StatusAlreadyExistException if a status with the same name already exists in DB
     */
    @Override
    public Status addStatus(RequestStatus requestStatus) throws StatusAlreadyExistException {
        // Vérifier si le statut existe déjà en BDD
        Optional<Status> statusExist = statusRepository.findByNameIgnoreCase(requestStatus.name());
        if (statusExist.isPresent()) {
            // Si c'est le cas, retourner une erreur
            throw new StatusAlreadyExistException();
        } else {
            // Sinon sauvegarder en BDD
            Status status = IStatusMapper.INSTANCE.requestStatusToStatus(requestStatus);
            return statusRepository.save(status);
        }
    }

    /**
     * Allow to patch a specific status
     * @param uuid UUID of the status
     * @param requestStatus DTO with parameters
     * @return status with modifications
     * @throws NotModifiedException if the status's name in request already exists in DB
     * @throws StatusAlreadyExistException if the status's name already exists in DB
     * @throws StatusNotFoundException if the status's UUID doesn't exist in DB
     */
    @Override
    public Status updateStatus(UUID uuid, RequestStatus requestStatus) throws NotModifiedException, StatusAlreadyExistException, StatusNotFoundException {
        // Vérifier que l'uuid existe en BDD
        Optional<Status> statusExist = statusRepository.findByUuid(uuid);
        if (statusExist.isPresent()) {
            Status existingStatus = statusExist.get();
            // Vérifier que le statut entré n'est pas identique à celui récupéré
            if (existingStatus.getName().equalsIgnoreCase(requestStatus.name())) {
                throw new NotModifiedException("Status non modifié");
            }
            // Vérifier que le statut entré n'existe pas déjà en BDD
            Optional<Status> statusInDb = statusRepository.findByNameIgnoreCase(requestStatus.name());
            if (statusInDb.isPresent()) {
                throw new StatusAlreadyExistException();
            }
            // Sauvegarder l'objet mis à jour en BDD
            existingStatus.setName(requestStatus.name());
            return statusRepository.save(existingStatus);
        } else {
            throw new StatusNotFoundException();
        }
    }

    /**
     * Allow to delete a specific status
     * @param uuid UUID of the status
     * @return the uuid of the deleted status
     * @throws StatusNotFoundException if the status's UUID doesn't exist in DB
     * @throws StatusAlreadyUsedException if the status is present in Report table
     */
    @Override
    public UUID deleteStatus(UUID uuid) throws StatusNotFoundException, StatusAlreadyUsedException {
        // Vérifier que l'uuid existe en BDD
        Optional<Status> statusExist = statusRepository.findByUuid(uuid);
        if (statusExist.isEmpty()) {
            throw new StatusNotFoundException();
        }

        // Vérifier si le statut est déjà utilisé dans des signalements
        Optional<Status> isStatusInReports = statusRepository.findByReports_Uuid(uuid);
        if (isStatusInReports.isPresent()) {
            throw new StatusAlreadyUsedException();
        }

        Status status = statusExist.get();
        statusRepository.delete(status);

        return uuid;
    }

    /**
     * Allow to get the list of statuses
     * @return the whole list of statuses
     */
    @Override
    public List<ResponseStatus> getAll() {
        List<ResponseStatus> statuses = new ArrayList<>();
        for (Status status : statusRepository.findAll()) {
            statuses.add(IStatusMapper.INSTANCE.statusToResponseStatus(status));
        }
        return statuses;
    }

    /**
     * Allow to get a specific status
     * @param uuid UUID of the status
     * @return a single status
     * @throws StatusNotFoundException if the status's UUID doesn't exist in DB
     */
    @Override
    public ResponseStatus getSingleStatus(UUID uuid) throws StatusNotFoundException {
        return IStatusMapper.INSTANCE.statusToResponseStatus(statusRepository.findByUuid(uuid).orElseThrow(StatusNotFoundException::new));
    }
}