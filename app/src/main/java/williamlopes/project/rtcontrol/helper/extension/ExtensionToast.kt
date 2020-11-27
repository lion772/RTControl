package williamlopes.project.rtcontrol.helper.extension

import android.app.Activity
import android.content.Context
import android.widget.Toast

fun Context.toast(str:String){
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}