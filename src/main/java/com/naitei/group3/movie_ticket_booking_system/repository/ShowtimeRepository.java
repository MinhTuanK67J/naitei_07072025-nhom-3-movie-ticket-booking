package com.naitei.group3.movie_ticket_booking_system.repository;

import com.naitei.group3.movie_ticket_booking_system.entity.Cinema;
import com.naitei.group3.movie_ticket_booking_system.entity.Showtime;
import com.naitei.group3.movie_ticket_booking_system.enums.ShowtimeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

        @Query("""
                        SELECT s FROM Showtime s
                        JOIN s.movie m
                        JOIN s.hall h
                        JOIN h.cinema c
                        WHERE (:keyword IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
                          AND (:cinemaName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :cinemaName, '%')))
                          AND (:status IS NULL OR s.status = :status)
                          AND (:showDate IS NULL OR FUNCTION('DATE', s.startTime) = :showDate)
                        ORDER BY s.startTime desc
                        """)
        Page<Showtime> filterShowtimes(
                        @Param("keyword") String keyword,
                        @Param("cinemaName") String cinemaName,
                        @Param("status") ShowtimeStatus status,
                        @Param("showDate") LocalDate showDate,
                        Pageable pageable);

        Page<Showtime> findByMovieIdAndHall_CinemaIdAndStatusOrderByStartTime(
                Long movieId,
                Long cinemaId,
                ShowtimeStatus status,
                Pageable pageable
        );
        List<Showtime> findByHall_CinemaAndStartTimeAfter(Cinema cinema, LocalDateTime now);

}
