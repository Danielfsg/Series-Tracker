package pt.dfsg.seriestracker.utils

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/*
fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}
*/

fun handleError(throwable: Throwable) {
    when (throwable) {
        is HttpException -> throwable.printStackTrace() //
        is SocketTimeoutException -> throwable.printStackTrace() // handle timeout from Retrofit
        is IOException -> throwable.printStackTrace() // file was not found, do something
    }
}