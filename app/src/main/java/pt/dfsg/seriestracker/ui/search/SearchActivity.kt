package pt.dfsg.seriestracker.ui.search

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.remote.RepositoryProvider
import java.util.concurrent.TimeUnit


class SearchActivity : AppCompatActivity() {

    private val repository = RepositoryProvider.provideSearchRepository()

    private lateinit var disposable: Disposable
    private var disposableContainer = CompositeDisposable()
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchAdapter = SearchAdapter()
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = searchAdapter

    }

    override fun onStart() {
        super.onStart()

        val buttonClickStream = buttonClickObservable()
        val textChangeStream = textChangeObservable()

        val searchTextObservable = Observable.merge<String>(buttonClickStream, textChangeStream)

        disposable = searchTextObservable
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { showProgress() }
            .doOnComplete { disposableContainer.clear() }
            .subscribe({
                repository.fetchShowRx(it)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        searchAdapter.setData(it)
                        hideProgress()
                    }, { error ->
                        error.printStackTrace()
                        Log.d("error2", "${error.message}")
                    }
                    )
            }, { error ->
                error.printStackTrace()
                Log.d("error1", "${error.message}")
            }
            )

    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun buttonClickObservable(): Observable<String> {
        return Observable.create { emitter ->
            searchButton.setOnClickListener { emitter.onNext(queryEditText.text.toString()) }
            emitter.setCancellable { searchButton.setOnClickListener(null) }
        }
    }

    private fun textChangeObservable(): Observable<String> {
        val textChangeObservable = Observable.create<String> { emitter ->
            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.toString()?.let { emitter.onNext(it) }
                }
            }
            queryEditText.addTextChangedListener(textWatcher)
            emitter.setCancellable { queryEditText.removeTextChangedListener(textWatcher) }
        }

        return textChangeObservable
            .filter { it.length >= 5 }
            .debounce(2000, TimeUnit.MILLISECONDS)
    }

    override fun onStop() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        disposableContainer.clear()
        super.onStop()
    }
}
