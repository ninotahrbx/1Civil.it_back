package fr.civilIteam.IncivilitiesTrack.repository;

import fr.civilIteam.IncivilitiesTrack.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICommentRepository extends JpaRepository<Comment,Long> {
}
