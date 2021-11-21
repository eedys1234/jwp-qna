package qna.domain;

import org.omg.CORBA.PRIVATE_MEMBER;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer extends BaseTime {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Lob
    @Column(columnDefinition = "longtext")
    private String contents;

    @Column
    private boolean deleted = false;

    protected Answer() {
        super();
    }

    public Answer(User writer, String contents) {
        this(null, writer, contents);
    }

    public Answer(Long id, User writer, String contents) {
        super(id);

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        this.writer = writer;
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public boolean canDelete(User writer) {
        return isOwner(writer);
    }

    public DeleteHistory delete(User writer) throws CannotDeleteException {
        if(!canDelete(writer)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
        this.deleted = true;
        return recordDeleteHistory();
    }

    private DeleteHistory recordDeleteHistory() {
        return new DeleteHistory(ContentType.ANSWER, this.id, this.writer, LocalDateTime.now());
    }

    public Long getId() {
        return super.id;
    }

    public void setId(Long id) {
        super.id = id;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isNotDeleted() {
        return !deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writer=" + writer +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
