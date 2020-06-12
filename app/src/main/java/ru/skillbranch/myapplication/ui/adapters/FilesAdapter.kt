package ru.skillbranch.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.myapplication.R
import java.io.File


class FilesAdapter : RecyclerView.Adapter<FilesAdapter.ViewHolder>() {

    private val TYPE_DIRECTORY = 0
    private val TYPE_FILE = 1
    private var files: MutableList<File> = ArrayList<File>()

    private var onFileClickListener: OnFileClickListener? = null

    fun setOnFileClickListener(onFileClickListener: OnFileClickListener?) {
        this.onFileClickListener = onFileClickListener
    }

    fun setFiles(files: MutableList<File>) {
        this.files = files
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        var layoutInflater: LayoutInflater  = LayoutInflater.from(parent.getContext())

        var view: View = if (viewType == TYPE_DIRECTORY) layoutInflater.inflate(R.layout.view_item_directory, parent, false)
        else layoutInflater.inflate(R.layout.view_item_file, parent, false)

        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var file = files.get(position)
        holder.nameTv.setText(file.getName())
        holder.itemView.setTag(file)
    }


    override fun getItemCount() : Int{
        return files.size
    }

    override fun getItemViewType(position : Int) : Int{
        var file: File = files.get(position)
        return if (file.isDirectory()) TYPE_DIRECTORY else TYPE_FILE
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val nameTv: TextView

        init {
            nameTv = itemView.findViewById(R.id.tv_name)

            itemView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(view: View?) {
                    var file: File = view?.getTag() as File
                    onFileClickListener?.onFileClick(file)
                }
            })
        }
    }

    interface OnFileClickListener {
        fun onFileClick(file: File?)
    }


}

