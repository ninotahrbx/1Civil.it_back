package fr.civilIteam.IncivilitiesTrack.dto.mapper;

import fr.civilIteam.IncivilitiesTrack.dto.HistoryDTO;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseHistory;
import fr.civilIteam.IncivilitiesTrack.models.History;
import org.mapstruct.factory.Mappers;

public interface IHistoryMapper {
    IHistoryMapper INSTANCE = Mappers.getMapper(IHistoryMapper.class);
    HistoryDTO toDTO(History history);
    History toEntity(HistoryDTO historyDTO);
    ResponseHistory entityToResponse(History history);
}
