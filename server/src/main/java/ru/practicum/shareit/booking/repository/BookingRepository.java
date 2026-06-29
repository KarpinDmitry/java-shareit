package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b JOIN FETCH b.item JOIN FETCH b.booker" +
            " WHERE b.booker.id = :userId AND b.start > :now ORDER BY b.start DESC")
    List<Booking> findByBookerIdAndStartAfter(@Param("userId") Long userId,
                                              @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b JOIN FETCH b.item JOIN FETCH b.booker" +
            " WHERE b.booker.id = :userId AND b.end < :now ORDER BY b.start DESC")
    List<Booking> findByBookerIdAndEndBefore(@Param("userId") Long userId,
                                              @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b JOIN FETCH b.item JOIN FETCH b.booker WHERE" +
            " b.booker.id = :userId AND b.start < :now  AND b.end > :now ORDER BY b.start DESC")
    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(@Param("userId") Long userId,
                                              @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b"
            + " JOIN FETCH b.item JOIN FETCH b.booker"
            + " WHERE b.booker.id = :userId"
            + " AND b.status = :status"
            + " ORDER BY b.start DESC")
    List<Booking> findBookingsByUserAndStatus(@Param("userId") Long userId,@Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b JOIN FETCH b.item JOIN FETCH b.booker"
            + " WHERE b.booker.id = :bookerId ORDER BY b.start DESC")
    List<Booking> findByBookerIdOrderByStartDesc(@Param("bookerId") Long bookerId);

    // state = ALL
    @Query("SELECT b FROM Booking b JOIN FETCH b.item JOIN FETCH b.booker " +
            "WHERE b.item.id IN :itemId ORDER BY b.start DESC")
    List<Booking> findByItemIdsOrderByStartDesc(@Param("itemId") List<Long> itemId);

    // state = CURRENT
    @Query("SELECT b FROM Booking b JOIN FETCH b.item JOIN FETCH b.booker" +
            " WHERE b.item.id IN :itemId AND b.start <= :now AND b.end > :now ORDER BY b.start DESC")
    List<Booking> findByItemIdsAndCurrent(@Param("itemId") List<Long> itemId,
                                          @Param("now") LocalDateTime now);

    // state = PAST
    @Query("SELECT b FROM Booking b JOIN FETCH b.item JOIN FETCH b.booker" +
            " WHERE b.item.id IN :itemId AND b.end < :now ORDER BY b.start DESC")
    List<Booking> findByItemIdsAndPast(@Param("itemId") List<Long> itemId,
                                       @Param("now") LocalDateTime now);

    // state = FUTURE
    @Query("SELECT b FROM Booking b JOIN FETCH b.item JOIN FETCH b.booker" +
            " WHERE b.item.id IN :itemId AND b.start > :now ORDER BY b.start DESC")
    List<Booking> findByItemIdsAndFuture(@Param("itemId") List<Long> itemId,
                                         @Param("now") LocalDateTime now);

    // state = WAITING / REJECTED
    @Query("SELECT b FROM Booking b JOIN FETCH b.item JOIN FETCH b.booker" +
            " WHERE b.item.id IN :itemId AND b.status = :status ORDER BY b.start DESC")
    List<Booking> findByItemIdsAndStatus(@Param("itemId") List<Long> itemId,
                                         @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b JOIN FETCH b.item JOIN FETCH b.booker" +
            " WHERE b.item.id = :itemId AND b.end < :now AND b.status = 'APPROVED' ORDER BY b.end DESC")
    Optional<Booking> findByItemIdLastBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b JOIN FETCH b.item JOIN FETCH b.booker" +
            " WHERE b.item.id = :itemId AND b.start > :now AND b.status = 'APPROVED' ORDER BY b.end ASC")
    Optional<Booking> findByItemIdNextBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b JOIN FETCH b.item JOIN FETCH b.booker" +
            " WHERE b.item.id = :itemId AND b.booker.id = :bookerId AND b.status = 'APPROVED' AND b.end <= :now")
    Optional<Booking> findCanceledByItemAndBooker(@Param("itemId") Long itemId,
                                                  @Param("bookerId") Long bookerId,
                                                  @Param("now") LocalDateTime now);
}
