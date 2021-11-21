package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Embeddable
public class Answers {

    @OneToMany
    @JoinColumn(name = "question_id")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {

    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public List<Answer> notDeletedAnswers() {
        return this.answers.stream()
                .filter(a -> a.isNotDeleted())
                .collect(toList());
    }
}
