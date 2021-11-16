package com.example.app_point.interfaces

interface OnItemClickRecycler {
    fun clickEdit(id: Int)
    fun clickRemove(id: Int, name: String, position: Int)
    fun clickNext(name: String, date: String, position: Int)
    fun clickBack(name: String, date: String, position: Int)
    fun clickHour(name: String, date: String, positionHour: Int, hour: String, position: Int)
    fun clickImage(image: ByteArray, name: String)
}