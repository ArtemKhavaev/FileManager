package ru.skillbranch.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.myapplication.R

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        private const val REQUEST_CODE_PICK_APK = 1
        const val TAG = "MainActivity"
    }
    lateinit var FMBtn: Button
    lateinit var TInp: EditText
// создаем вьюхи
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FMBtn = button
        TInp = et_enter_text
        FMBtn.setOnClickListener(this)
    }
// метод определяющий что делать при клике на кнопку (запускает файл менеджер от которого ждет результат)
    override fun onClick(v: View?) {
        if(v?.id == R.id.button){
            val intent = Intent(this, FilePickerActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_PICK_APK)
        }
    }
// проверяем что ответ пришел из нужной активити
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_PICK_APK && resultCode == RESULT_OK) {
            var apkPath: String? = data?.getStringExtra(FilePickerActivity.EXTRA_FILE_PATH)
            Log.i(TAG, "APK: " + apkPath);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//
//    }
}

