package com.ssafy.relpl.util.common

import com.ssafy.relpl.db.postgre.entity.RoadHash

data class TmapData(val roads: List<Road>, val roadsHash: List<RoadHash>)
