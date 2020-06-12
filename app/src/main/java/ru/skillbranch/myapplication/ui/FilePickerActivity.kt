package ru.skillbranch.myapplication.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.myapplication.R
import ru.skillbranch.myapplication.model.FileManager
import ru.skillbranch.myapplication.ui.adapters.FilesAdapter
import ru.skillbranch.myapplication.ui.adapters.FilesAdapter.OnFileClickListener
import java.io.File


class FilePickerActivity : AppCompatActivity() {
    companion object{
        const val PERMISSION_REQUEST_CODE = 1
        const val TAG = "FilePickerActivity"
        const val EXTRA_FILE_PATH = "file_path"
    }

    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    lateinit var fileManager: FileManager
    lateinit var filesAdapter: FilesAdapter
//создаем вьюхи и список
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_picker)

        val recyclerView: RecyclerView = findViewById(R.id.rv_files)
        recyclerView.layoutManager = LinearLayoutManager(this)

        filesAdapter = FilesAdapter()
        recyclerView.adapter = filesAdapter

        initFileManager()
    }

    private fun initFileManager() {
// проверяем наличие разрешения на использование СД-карты
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
//             Разрешение предоставлено
            fileManager = FileManager(this)
            updateFileList()
        } else {
            requestPermissions()
        }

    }

    // метод обновляющий содержимое ресайкла(списка файлов и директорий)
    private fun updateFileList() {
        val files: List<File>? = fileManager.getFiles()
        filesAdapter.setFiles(files as MutableList<File>)
        filesAdapter.notifyDataSetChanged()
    }
    // запрашиваем разрешение на использование СД-карты
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted!");
                initFileManager();
            } else {
                Log.i(TAG, "Permission denied");
                requestPermissions(); // Запрашиваем ещё раз
            }
        }
    }

    // запрашиваем разрешение на использование СД-карты
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            permission,
            PERMISSION_REQUEST_CODE
        )
    }
// обработка кликов по папкам и файлам
    private val onFileClickListener: OnFileClickListener = object : OnFileClickListener {
        override fun onFileClick(file: File?) {
            if (file != null) {
                if (file.isDirectory()) {  // если это папка - открываем через обновление списка
                    fileManager.navigateTo(file)
                    updateFileList()
                }else {   // если это файл нужного расширения через интент запускаем ативити (возвращаем результат)
                    if (file.getName().endsWith(".txt")) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_FILE_PATH, file.absolutePath)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }
    }
// устанавливаем лисенер
    override fun onStart() {
        super.onStart()
        filesAdapter.setOnFileClickListener(onFileClickListener)
    }
    // убираем лисенер дабы не обрабатывать клики если ресайкла уже не существует
    override fun onStop() {
        filesAdapter.setOnFileClickListener(null)
        super.onStop()
    }
// реализуем возврат навверх по дереву файлов при нажатии кнопки назад (не выкидывает в пред. активити)
    override fun onBackPressed() {
        if (fileManager != null && fileManager.navigateUp()) {
            updateFileList();
        } else {
            super.onBackPressed();
        }
    }

}

