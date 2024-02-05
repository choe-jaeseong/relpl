package com.ssafy.relpl.controller.rest;

import com.ssafy.relpl.business.ProjectCreateRouteBusiness;
import com.ssafy.relpl.business.ProjectRecommendBusiness;
import com.ssafy.relpl.config.GeomFactoryConfig;
import com.ssafy.relpl.dto.request.ProjectCreateDistanceRequest;
import com.ssafy.relpl.dto.request.ProjectCreateRouteRequest;
import com.ssafy.relpl.dto.request.ProjectJoinRequest;
import com.ssafy.relpl.dto.request.ProjectRecommendRequest;
import com.ssafy.relpl.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/api/project")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProjectController {

    private final ProjectRecommendBusiness projectRecommendBusiness;
    private final GeomFactoryConfig geomFactoryConfig;
    private final ProjectService projectService;
    private final ProjectCreateRouteBusiness projectCreateRouteBusiness;

    @PostMapping("/recommend")
    public ResponseEntity<?> recommendProject(@RequestBody ProjectRecommendRequest request) {
        Point startPoint = geomFactoryConfig.getGeometryFactory().createPoint(
                new Coordinate(request.getStartCoordinate().getX(), request.getStartCoordinate().getY()
                )
        );
        Point endPoint = geomFactoryConfig.getGeometryFactory().createPoint(
                new Coordinate(request.getEndCoordinate().getX(), request.getEndCoordinate().getY()
                )
        );
        return projectRecommendBusiness.recommendProject(startPoint, endPoint);
    }

    @GetMapping("/exist/{x}/{y}")
    public ResponseEntity<?> projectExist(@PathVariable double x, @PathVariable double y) {
        return projectService.projectExist(x, y, 50);
    }

    @PostMapping("/create/distance")
    public ResponseEntity<?> projectCreateDistance(@RequestBody ProjectCreateDistanceRequest request) {
        return projectService.createDistanceProject(request);
    }

    @PostMapping("/create/route")
    public ResponseEntity<?> projectCreateRoute(@RequestBody ProjectCreateRouteRequest request) {
        return projectCreateRouteBusiness.createRouteProjectBusiness(request);
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody ProjectJoinRequest request) {
        return projectService.join(request);
    }
//    @PostMapping("/project/create/route")
//    public ResponseEntity<?> projectRoute(@RequestBody ProjectCreateRouteRequest request) {
//
//    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProjectList() {
        return projectService.getAllProjectList();
    }

}