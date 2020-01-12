package com.questions.service.domain;

public class AnswerObject {

    private Long id;
    private String answer;

    public AnswerObject() {

    }

    public AnswerObject(Long id, String answer) {
        this.id = id;
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
