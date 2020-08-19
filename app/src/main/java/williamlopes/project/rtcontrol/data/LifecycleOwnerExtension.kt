package williamlopes.project.rtcontrol.data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T:Any> LifecycleOwner.bind(data: LiveData<T>, function: (id:T) -> Unit){
    data.observerSmart(this){function.invoke(it)}
}