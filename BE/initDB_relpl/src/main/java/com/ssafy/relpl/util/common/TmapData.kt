package com.ssafy.relpl.util.common

import com.ssafy.relpl.db.mongo.entity.TmapRoad
import com.ssafy.relpl.db.postgre.entity.RoadHash

data class TmapData(val roads: List<TmapRoad>, val roadsHash: List<RoadHash>)
