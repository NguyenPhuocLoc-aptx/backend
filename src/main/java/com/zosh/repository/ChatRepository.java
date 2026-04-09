package com.zosh.repository;

import com.zosh.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// ── Chat ───────────────────────────────────────────────────────────
@Repository
public interface ChatRepository extends JpaRepository<Chat, String> {
    List<Chat> findAllByProjectId(String projectId);

    @Query("""
        SELECT c FROM Chat c
        JOIN c.members m
        WHERE m.user.id = :userId
    """)
    List<Chat> findAllByMemberUserId(@Param("userId") String userId);
}
