package com.example.app_point.interfaces

interface OnItemClickRecycler {
    fun clickEdit(id: Int)
    fun clickRemove(id: Int, name: String)
    fun clickNext(name: String, date: String, position: Int)
    fun clickBack(name: String, date: String, position: Int)
}