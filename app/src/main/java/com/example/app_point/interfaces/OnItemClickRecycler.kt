package com.example.app_point.interfaces

interface OnItemClickRecycler {
    fun clickRemove(id: Int, name: String, position: Int)
    fun clickNext(id: Int, date: String, position: Int)
    fun clickBack(id: Int, date: String, position: Int)
    fun clickHour(id: Int, date: String, positionHour: Int, hour: String, position: Int)
    fun clickImage(image: ByteArray, id: Int)
    fun employeeRemoved()
}