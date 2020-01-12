package com.questions.service.repository;

import com.questions.service.domain.Questions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Questions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Long> {

    @Query(value = "select q from Questions q where q.answer is null")
    List<Questions> findPendingQuestions();

}
