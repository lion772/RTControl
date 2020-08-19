package williamlopes.project.rtcontrol.data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observerSmart(owner: LifecycleOwner, observer:(T)-> Unit){
    observe(owner, Observer { it?.also{source -> observer(source)} })
}