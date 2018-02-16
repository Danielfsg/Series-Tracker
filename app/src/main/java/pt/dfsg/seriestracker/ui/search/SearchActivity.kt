package pt.dfsg.seriestracker.ui.search

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_list.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.ui.BaseActivity
import java.util.concurrent.TimeUnit


class SearchActivity : BaseActivity(), SearchAdapter.ClickCallBack {

    private lateinit var disposable: Disposable

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = null

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener())

        searchAdapter = SearchAdapter(this)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = searchAdapter

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        setSearchTextObservable()

    }

    private fun setSearchTextObservable() {
        disposable = searchViewObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { progress(true) }
            .subscribe({
                viewModel.getShowRx(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        searchAdapter.setData(it)
                        for (item in it) Log.d("[SHOW]: ", "$item")
                        progress(false)
                    }, { handleError(it) })
            }, { handleError(it) })
    }

    private fun searchViewObservable(): Observable<String> {
        val textChangeObservable = Observable.create<String> { emitter ->
            val textWatcher = object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { emitter.onNext(it) }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { emitter.onNext(it) }
                    return false
                }
            }
            searchView.setOnQueryTextListener(textWatcher)
            emitter.setCancellable { searchView.setOnQueryTextListener(null) }
        }
        return textChangeObservable
            .filter { it.length >= 5 }
            .debounce(1000, TimeUnit.MILLISECONDS)
    }

    private fun progress(visibility: Boolean) {
        progressBar.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    override fun onItemClick(show: Show) {
        Toast.makeText(this, "${show.name}", Toast.LENGTH_SHORT).show()
        viewModel.addShow(show)
    }

    override fun onStop() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onStop()
    }

    /* old observables
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
*/
}
