package iGuard.Server.Repository;


import iGuard.Server.Entity.Job;
import iGuard.Server.Enum.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("customJobRepository")
public interface JobRepository extends JpaRepository<Job, Integer> {

    // 기존의 위치 기반 일자리 검색 메서드
    @Query("SELECT j FROM Job j JOIN j.shelter s WHERE "
            + "(6371 * acos(cos(radians(:lat)) * cos(radians(s.latitude)) * cos(radians(s.longitude) - radians(:lon)) + sin(radians(:lat)) * sin(radians(s.latitude)))) < :distance")
    List<Job> findJobsNearLocation(@Param("lat") float lat, @Param("lon") float lon, @Param("distance") float distance);

    // 특정 쉼터의 모든 일자리 찾기
    List<Job> findByShelterShelterId(Integer shelterId);

    // 특정 일자리 유형의 모든 일자리 찾기
    List<Job> findByJobType(JobType jobType);

    // 특정 쉼터의 특정 유형의 일자리 찾기
    List<Job> findByShelterShelterIdAndJobType(Integer shelterId, JobType jobType);

    // 특정 위치 주변의 특정 유형의 일자리 찾기
    @Query("SELECT j FROM Job j JOIN j.shelter s WHERE "
            + "(6371 * acos(cos(radians(:lat)) * cos(radians(s.latitude)) * cos(radians(s.longitude) - radians(:lon)) + sin(radians(:lat)) * sin(radians(s.latitude)))) < :distance "
            + "AND j.jobType = :jobType")
    List<Job> findJobsNearLocationByType(@Param("lat") float lat, @Param("lon") float lon, @Param("distance") float distance, @Param("jobType") JobType jobType);

    // 일자리 지원자 수가 가장 적은 순으로 일자리 찾기
    @Query("SELECT j FROM Job j LEFT JOIN j.jobApplications ja GROUP BY j ORDER BY COUNT(ja) ASC")
    List<Job> findJobsOrderByApplicationCountAsc();

    // 특정 쉼터의 일자리 수 카운트
    @Query("SELECT COUNT(j) FROM Job j WHERE j.shelter.shelterId = :shelterId")
    Long countJobsByShelter(@Param("shelterId") Integer shelterId);
}