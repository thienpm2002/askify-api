package com.thienpm.askify.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thienpm.askify.api.dto.projection.QuestionFlatDTO;
import com.thienpm.askify.api.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query(value = """
            SELECT new com.thienpm.askify.api.dto.projection.QuestionFlatDTO(
                q.id,
                q.title,
                q.content,
                q.voteCount,
                q.createdAt,
                u.id,
                u.userName,
                u.avatarUrl
            )
            FROM Question q
            JOIN q.user u
            WHERE (:keyword IS NULL OR LOWER(q.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """, countQuery = """
            SELECT COUNT(q) FROM Question q
            WHERE (:keyword IS NULL OR LOWER(q.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<QuestionFlatDTO> searchQuestionByTitle(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
            SELECT q.id, t.name
            FROM Question q
            JOIN q.tags t
            WHERE q.id IN :ids
            """)
    List<Object[]> findTagsByQuestionIds(@Param("ids") List<Integer> ids);
}
