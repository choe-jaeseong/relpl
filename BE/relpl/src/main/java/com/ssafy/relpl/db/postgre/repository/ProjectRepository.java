package com.ssafy.relpl.db.postgre.repository;

import com.ssafy.relpl.db.postgre.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {


    /**
     * x와 y좌표를 주었을 때 제공된 위치 이내에 distance 만큼 떨어진 중단된 프로젝트를 10개 가져오는 함수
     * @param x
     * @param y
     * @param distance
     * @return 가장 가까운 순으로 정렬된 프로젝트 10개
     */
    @Query(value =
            "SELECT " +
            "* FROM project " +
            "WHERE ST_DWithin" +
                "(project_stop_coordinate " +
                ", ST_SetSRID(ST_MakePoint(:x, :y), 4326), :distance, true)" +
            "ORDER BY" +
            "    ST_Distance(" +
            "        ST_Transform(project_stop_coordinate, 4326), " +
            "        ST_SetSRID(ST_MakePoint(:x, :y), 4326)\n" +
            "    ) ASC " +
            "LIMIT 10;"
            , nativeQuery = true)
    List<Project> findNearProject10(@Param("x") double x, @Param("y") double y, @Param("distance") int distance);
}