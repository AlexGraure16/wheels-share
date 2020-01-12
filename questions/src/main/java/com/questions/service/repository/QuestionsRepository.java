package com.questions.service.repository;

import com.questions.service.domain.Questions;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Spring Data  repository for the Questions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Long> {

    List<Questions> findByAnswerIsNullOrderByIdAsc();

    @Transactional
    @Modifying
    @Query(value = "UPDATE Questions q SET q.answer = :answer WHERE q.id = :questionId")
    void updateQuestion(@Param("questionId") Long questionId,
                        @Param("answer") String answer);


}
