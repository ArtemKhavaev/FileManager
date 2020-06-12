package ru.skillbranch.myapplication.model

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class FileManager {
    companion object{
        const val TAG = "FileManager"
    }
    lateinit var currentDirectory: File
    private val rootDirectory: File?


    constructor(context: Context){
        var directory = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState())
            Environment.getExternalStorageDirectory() else ContextCompat.getDataDir(context)
        rootDirectory = directory
        if (directory != null) {
            navigateTo(directory)
        }
    }

    fun navigateTo(directory: File): Boolean {
        // Проверим, является ли файл директорией
        if (!directory.isDirectory) {
            Log.e(TAG, directory.absolutePath + " is not a directory!")
            return false
        }
        // Проверим, не поднялись ли мы выше rootDirectory
        if (directory != rootDirectory &&
            rootDirectory!!.absolutePath.contains(directory.absolutePath)
        ) {
            Log.w(TAG,"Trying to navigate upper than root directory to " + directory.absolutePath)
            return false
        }
        currentDirectory = directory
        return true
    }
    fun navigateUp(): Boolean {
        return navigateTo(currentDirectory!!.parentFile)
    }
    fun getFiles(): List<File>? {
        val files: MutableList<File> = ArrayList()
        files.addAll(currentDirectory!!.listFiles().toMutableList())
        return files
    }



}

