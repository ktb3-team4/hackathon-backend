package com.example.team4backend.repository;

import com.example.team4backend.domain.TargetPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.List;

public interface TargetPersonRepository extends JpaRepository<TargetPerson, Long> {
    @Modifying
    @Query("UPDATE TargetPerson t SET t.deletedAt = CURRENT_TIMESTAMP WHERE t.id = :id AND t.user.id = :userId AND t.deletedAt IS NULL")
    int softDeleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT t FROM TargetPerson t " +
            "JOIN FETCH t.user " +
            "JOIN FETCH t.relationship " +
            "JOIN FETCH t.chatStyle " +
            "WHERE t.id = :id AND t.deletedAt IS NULL")
    Optional<TargetPerson> findByIdWithUserAndDetailsAndDeletedAtIsNull(@Param("id") Long id);

    @Query("SELECT t FROM TargetPerson t " +
            "JOIN FETCH t.user " +
            "JOIN FETCH t.relationship " +
            "JOIN FETCH t.chatStyle " +
            "WHERE t.user.id = :userId AND t.deletedAt IS NULL")
    List<TargetPerson> findAllByUserIdWithDetailsAndDeletedAtIsNull(@Param("userId") Long userId);

    @Query("SELECT t.user.id FROM TargetPerson t WHERE t.id = :id AND t.deletedAt IS NULL")
    Optional<Long> findOwnerIdByIdAndDeletedAtIsNull(@Param("id") Long id);

    @Modifying
    @Query("UPDATE TargetPerson t SET t.lastMessageDate = :date WHERE t.id = :targetId AND t.user.id = :userId AND t.deletedAt IS NULL")
    int updateLastMessageDate(@Param("targetId") Long targetId,
                              @Param("userId") Long userId,
                              @Param("date") Instant date);

    @Modifying
    @Query("UPDATE TargetPerson t SET t.recommendedOpening = :message WHERE t.id = :targetId AND t.deletedAt IS NULL")
    int updateRecommendedOpening(@Param("targetId") Long targetId,
                                  @Param("message") String message);
}
