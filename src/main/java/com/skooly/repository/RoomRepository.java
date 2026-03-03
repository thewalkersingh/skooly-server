package com.skooly.repository;
import com.skooly.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
	@Query("""
			    SELECT r FROM Room r
			    WHERE (:type IS NULL OR r.type = :type)
			    AND (:status IS NULL OR r.status = :status)
			    AND (:search IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')))
			""")
	Page<Room> findWithFilters(
			@Param("type") Room.RoomType type,
			@Param("status") Room.Status status,
			@Param("search") String search,
			Pageable pageable);
}