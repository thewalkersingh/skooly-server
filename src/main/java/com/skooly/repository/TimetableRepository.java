package com.skooly.repository;
import com.skooly.model.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
	List<Timetable> findBySchoolClassIdAndSectionId(Long classId, Long sectionId);
	
	List<Timetable> findByTeacherId(Long teacherId);
	
	List<Timetable> findByDayOfWeek(Timetable.DayOfWeek dayOfWeek);
	
	List<Timetable> findBySchoolClassIdAndSectionIdAndDayOfWeek(Long classId, Long sectionId,
			Timetable.DayOfWeek dayOfWeek);
	
	// Check for teacher conflict (same teacher, same day, overlapping time)
	@Query("""
			    SELECT t FROM Timetable t
			    WHERE t.teacher.id = :teacherId
			    AND t.dayOfWeek = :day
			    AND t.id != :excludeId
			    AND (t.startTime < :endTime AND t.endTime > :startTime)
			""")
	List<Timetable> findTeacherConflicts(
			@Param("teacherId") Long teacherId,
			@Param("day") Timetable.DayOfWeek day,
			@Param("startTime") LocalTime startTime,
			@Param("endTime") LocalTime endTime,
			@Param("excludeId") Long excludeId);
	
	// Check for room conflict (same room, same day, overlapping time)
	@Query("""
			    SELECT t FROM Timetable t
			    WHERE t.room.id = :roomId
			    AND t.dayOfWeek = :day
			    AND t.id != :excludeId
			    AND (t.startTime < :endTime AND t.endTime > :startTime)
			""")
	List<Timetable> findRoomConflicts(
			@Param("roomId") Long roomId,
			@Param("day") Timetable.DayOfWeek day,
			@Param("startTime") LocalTime startTime,
			@Param("endTime") LocalTime endTime,
			@Param("excludeId") Long excludeId);
}