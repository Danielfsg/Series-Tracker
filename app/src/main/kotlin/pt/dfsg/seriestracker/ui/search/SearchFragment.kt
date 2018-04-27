package pt.dfsg.seriestracker.ui.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_list.*
import kotlinx.android.synthetic.main.fragment_search.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.ui.SeriesViewModel
import pt.dfsg.seriestracker.utils.handleError
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(), SearchAdapter.ClickCallBack {

    private lateinit var disposable: Disposable

    private lateinit var searchAdapter: SearchAdapter

    private var seriesViewModel: SeriesViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onStart() {
        super.onStart()

        searchAdapter = SearchAdapter(this)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = searchAdapter


        setSearchTextObservable()
    }

    private fun initViewModel() {
        seriesViewModel = ViewModelProviders.of(this).get(SeriesViewModel::class.java)
        seriesViewModel?.let { lifecycle.addObserver(it) }
    }

    private fun setSearchTextObservable() {
        disposable = searchViewObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { progress(true) }
            .subscribe({
                seriesViewModel?.searchShow(it)?.observe(this, Observer { showList ->
                    if (showList != null) {
                        searchAdapter.setData(showList)
                    }
                    progress(false)
                })


            }, { handleError(it) })
    }

    private fun searchViewObservable(): Observable<String> {
        val textChangeObservable = Observable.create<String> { emitter ->
            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let { emitter.onNext(it.toString()) }
                }
            }
            searchView.addTextChangedListener(textWatcher)
            emitter.setCancellable { searchView.addTextChangedListener(null) }
        }
        return textChangeObservable
            .filter { it.length >= 5 }
            .debounce(1000, TimeUnit.MILLISECONDS)
    }

    private fun progress(visibility: Boolean) {
        progressBar.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    override fun onItemClick(show: Show) {
        seriesViewModel?.addShow(show)
    }

    override fun onStop() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onStop()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}
