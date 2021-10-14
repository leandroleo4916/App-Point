package com.example.app_point.interfaces

interface OnItemClickRecycler {
    fun clickEdit(id: Int)
    fun clickRemove(id: Int, name: String)
    fun clickNext(name: String, position: Int)
    fun clickBack(name: String, position: Int)
}