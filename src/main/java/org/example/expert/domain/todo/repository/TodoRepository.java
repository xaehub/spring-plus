package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryV2 {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

//    @Query("SELECT t FROM Todo t " +          // 이 부분 JPQL로 작성된 findByIdWithUser 를 QueryDSL로 변경
//            "LEFT JOIN t.user " +
//            "WHERE t.id = :todoId")
//    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    @Query("select t from Todo t where t.weather = :weather")
    Page<Todo> findByWeather(@Param("weather") String weather, Pageable pageable);

    @Query("select t from Todo t where t.modifiedAt between :start and :end")
    Page<Todo> findByStartEnd(@Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        Pageable pageable);

    @Query("select t from Todo t where t.weather = :weather and t.modifiedAt between :start and :end")
    Page<Todo> findByWeatherStartEnd(@Param("weather") String weather,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        Pageable pageable);
}
