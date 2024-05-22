package jp.co.rakuten.htmxpoc

import android.content.Context
import java.io.FileNotFoundException

class Loader(mCtx: Context) {

    private val ctx: Context = mCtx;

    fun load (fileName:String):String {
        return try {
            val str = ctx.assets.open(fileName).bufferedReader().use {
                it.readText()
            }
            str
        } catch (e: FileNotFoundException){
            println("Couldn't found the file")
            ""
        }
    }

    fun loadView(fileName: String)  :String {
        val path = "view/$fileName.html"
        return load(path)
    }
}