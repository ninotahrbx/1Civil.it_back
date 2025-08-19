package fr.civilIteam.IncivilitiesTrack.dto.mapper;

import fr.civilIteam.IncivilitiesTrack.dto.CommentDTO;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseComment;
import fr.civilIteam.IncivilitiesTrack.models.Comment;
import org.mapstruct.factory.Mappers;

public interface ICommentMapper {
    ICommentMapper INSTANCE = Mappers.getMapper(ICommentMapper.class);
    CommentDTO toDTO(Comment comment);
    Comment toEntity(CommentDTO commentDTO);
    ResponseComment entityToResponse(Comment comment);
}
