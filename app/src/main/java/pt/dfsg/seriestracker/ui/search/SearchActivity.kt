package pt.dfsg.seriestracker.ui.search

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*
import org.jetbrains.anko.toast
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.ui.BaseActivity
import java.util.concurrent.TimeUnit


class SearchActivity : BaseActivity(), SearchAdapter.ClickCallBack {

    private lateinit var disposable: Disposable
    private var disposableContainer = CompositeDisposable()

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener())

        searchAdapter = SearchAdapter(this)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = searchAdapter

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        setSearchTextObservable()

    }

    private fun setSearchTextObservable() {
        val buttonClickStream = buttonClickObservable()
        val textChangeStream = textChangeObservable()
        val searchTextObservable = Observable.merge<String>(buttonClickStream, textChangeStream)

        disposable = searchTextObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { progress(true) }
            .doOnComplete { disposableContainer.clear() }
            .subscribe({
                viewModel.getShowRx(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        searchAdapter.setData(it)
                        for (item in it) Log.d("[SHOW] :", "$item")
                        progress(false)
                    }, { handleError(it) })
            }, { handleError(it) })
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

    private fun progress(visibility: Boolean) {
        progressBar.visibility = if (visibility) View.VISIBLE else View.GONE
    }


    override fun onItemClick(show: Show) {
        toast(show.toString())
        viewModel.addShow(show)
    }

    override fun onStop() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        disposableContainer.clear()
        super.onStop()
    }
}
