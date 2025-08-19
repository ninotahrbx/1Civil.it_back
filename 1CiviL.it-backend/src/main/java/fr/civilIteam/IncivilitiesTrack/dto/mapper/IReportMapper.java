package fr.civilIteam.IncivilitiesTrack.dto.mapper;

import fr.civilIteam.IncivilitiesTrack.dto.ReportResponseDTO;
import fr.civilIteam.IncivilitiesTrack.dto.ReportDTO;
import fr.civilIteam.IncivilitiesTrack.dto.*;
import fr.civilIteam.IncivilitiesTrack.models.Comment;
import fr.civilIteam.IncivilitiesTrack.models.History;
import fr.civilIteam.IncivilitiesTrack.models.Report;
import fr.civilIteam.IncivilitiesTrack.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IReportMapper {

    IReportMapper INSTANCE = Mappers.getMapper(IReportMapper.class);

    @Mapping(source = "author.uuid", target = "authorUuid")
    @Mapping(source = "type.uuid", target = "typeUuid")
    @Mapping(source = "status.uuid", target = "statusUuid")
    @Mapping(source = "geolocation.uuid", target = "geolocationUuid")
    ReportDTO toDTO(Report report);

    @Mapping(source = "authorUuid", target = "author.uuid")
    @Mapping(source = "typeUuid", target = "type.uuid")
    @Mapping(source = "statusUuid", target = "status.uuid")
    @Mapping(source = "geolocationUuid", target = "geolocation.uuid")
    Report toEntity(ReportDTO reportDTO);

    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "type.name", target = "typeName")
    @Mapping(source = "status.name", target = "statusName")
    @Mapping(expression = "java(report.getGeolocation().getLatitude() + \", \" + report.getGeolocation().getLongitude())", target = "geolocationDetails")
    ReportResponseDTO toResponseDTO(Report report);

    ResponseReport entityToResponse(Report report);

    default ResponseHistory historyToResponse(History history )
    {  return Mappers.getMapper(IHistoryMapper.class).entityToResponse(history);}

    default ResponseComment commentToResponse(Comment comment )
    {  return Mappers.getMapper(ICommentMapper.class).entityToResponse(comment);}

    /*ResponseReport reportToResponseReport(Report report);
    Report requestReportToReport(RequestReport requestReport);*/
}
