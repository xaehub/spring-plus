package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;

public class TodoRepositoryV2Impl implements TodoRepositoryV2{

    private final JPAQueryFactory queryFactory;

    public TodoRepositoryV2Impl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        QTodo todo = QTodo.todo;

        Todo result = queryFactory.selectFrom(todo)
            .leftJoin(todo.user).fetchJoin()
            .where(todo.id.eq(todoId)).fetchOne();

        return Optional.ofNullable(result);
    }

}
