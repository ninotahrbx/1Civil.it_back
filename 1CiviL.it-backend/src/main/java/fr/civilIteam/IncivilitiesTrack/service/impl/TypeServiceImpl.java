package fr.civilIteam.IncivilitiesTrack.service.impl;

import fr.civilIteam.IncivilitiesTrack.dto.RequestType;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseType;
import fr.civilIteam.IncivilitiesTrack.dto.mapper.ITypeMapper;
import fr.civilIteam.IncivilitiesTrack.exception.NotModifiedException;
import fr.civilIteam.IncivilitiesTrack.exception.TypeAlreadyExistException;
import fr.civilIteam.IncivilitiesTrack.exception.TypeAlreadyUsedException;
import fr.civilIteam.IncivilitiesTrack.exception.TypeNotFoundException;
import fr.civilIteam.IncivilitiesTrack.models.Type;
import fr.civilIteam.IncivilitiesTrack.repository.ITypeRepository;
import fr.civilIteam.IncivilitiesTrack.service.ITypeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TypeServiceImpl implements ITypeService {

    private final ITypeRepository typeRepository;

    public TypeServiceImpl(ITypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    /**
     * Allow to create a type
     * @param requestType DTO with parameters
     * @return Type saved with ID and UUID generated
     * @throws TypeAlreadyExistException if a type with the same name already exist in DB
     */
    @Override
    public Type addType(RequestType requestType) throws TypeAlreadyExistException {
        //Vérifier si  le type existe déjà en BDD
        Optional<Type> typeExist = typeRepository.findByNameIgnoreCase(requestType.name());
        if (typeExist.isPresent()) {
            // Si c'est le cas, retourner une erreur
            throw new TypeAlreadyExistException();
        } else {
            // Sinon sauvegarder en BDD
            Type type = ITypeMapper.INSTANCE.requestTypeToType(requestType);
            return typeRepository.save(type);
        }
    }

    /**
     * Allow to patch a specific type
     * @param uuid UUID of the type
     * @param requestType DTO with parameters
     * @return type with modifications
     * @throws NotModifiedException if the type's name in request already exist in DB
     * @throws TypeAlreadyExistException if the type's name already exist in DB
     * @throws TypeNotFoundException if the type's UUID doesn't exist in DB
     */
    @Override
    public Type updateType(UUID uuid, RequestType requestType) throws NotModifiedException, TypeAlreadyExistException, TypeNotFoundException {
        // Vérifier que l'uuid existe en BDD
        Optional<Type> typeExist = typeRepository.findByUuid(uuid);
        if (typeExist.isPresent()) {
            Type existingType = typeExist.get();
            // Vérifier que le type entré n'est pas identique à celui récupéré
            if (existingType.getName().equalsIgnoreCase(requestType.name())) {
                throw new NotModifiedException("Type non modifié");
            }
            // Vérifier que le type entré n'existe pas déjà en BDD
            Optional<Type> typeInDb = typeRepository.findByNameIgnoreCase(requestType.name());
            if (typeInDb.isPresent()) {
                throw new TypeAlreadyExistException();
            }
            // Sauvegarder l'objet mis à jour en BDD
            existingType.setName(requestType.name());
            return typeRepository.save(existingType);
        } else {
            throw new TypeNotFoundException();
        }
    }

    /**
     * Allow to delete a specific type
     * @param uuid UUID of the type
     * @return the uuid of the deleted type
     * @throws TypeNotFoundException if the type's UUID doesn't exist in DB
     * @throws TypeAlreadyUsedException if the type is present in Report table
     */
    @Override
    public UUID deleteType(UUID uuid) throws  TypeNotFoundException, TypeAlreadyUsedException {
        // Vérifier que l'uuid existe en BDD
        Optional<Type> typeExist = typeRepository.findByUuid(uuid);
        if (typeExist.isEmpty()) {
            throw new TypeNotFoundException();
        }

        // Vérifier si le type est déjà utilisé dans des signalements
        boolean isTypeInreports = typeRepository.existsByReports_Uuid(uuid);
        if (isTypeInreports) {
            throw new TypeAlreadyUsedException();
        }

        Type type = typeExist.get();
        typeRepository.delete(type);

        return uuid;
    }

    /**
     * Allow to get the list of types
     * @return the whole list of types
     */
    @Override
    public List<ResponseType> getAll() {
        List<ResponseType> types = new ArrayList<>();
        for (Type type : typeRepository.findAll()) {
            types.add(ITypeMapper.INSTANCE.typeToResponseType(type));
        }
        return types;
    }

    /**
     * Allow to get a specific type
     * @param uuid UUId of the type
     * @return a single type
     * @throws TypeNotFoundException if the type's UUID doesn't exist in DB
     */
    @Override
    public ResponseType getSingleType(UUID uuid) throws TypeNotFoundException{
        return ITypeMapper.INSTANCE.typeToResponseType(typeRepository.findByUuid(uuid).orElseThrow(TypeNotFoundException::new));
    }

}
