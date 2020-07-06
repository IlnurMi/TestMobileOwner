package com.ilnur.mobileowner.interfaces

interface TextChangeListener {
    fun textChange(newText: String)
    fun closeList()
    fun closeSearchView()
}