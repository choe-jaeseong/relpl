package com.gdd.domain.model.relay

import com.gdd.domain.model.Point

data class PathRelayInfo(
    val projectId: Long,
    val projectName: String,
    val totalContributor: Int,
    val totalDistance: String,
    val remainDistance: String,
    val createDate: String,
    val endDate: String,
    val isPath: Boolean,
    val stopCoordinate: Point,
    val progress: Int,
    val memo: String,
    val route: List<Point>
)