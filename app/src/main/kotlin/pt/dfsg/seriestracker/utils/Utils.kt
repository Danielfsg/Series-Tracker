package pt.dfsg.seriestracker.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import java.net.SocketTimeoutException


fun handleError(throwable: Throwable) {
    when (throwable) {
        is HttpException -> throwable.printStackTrace() // handle http exceptions
        is SocketTimeoutException -> throwable.printStackTrace() // handle timeout from Retrofit
        is IOException -> throwable.printStackTrace() // file was not found, do something
    }
}

val picasso: Picasso
    get() = Picasso.get()

fun ImageView.load(path: String) {
    picasso
        .load(path)
        .networkPolicy(NetworkPolicy.OFFLINE)
        .into(this, object : Callback {
            override fun onSuccess() {
                //Do nothing
            }

            override fun onError(e: Exception?) {
                picasso.load(path)
                    .into(this@load)
            }
        })
}

fun AppCompatActivity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}