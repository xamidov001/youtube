package uz.pdp.youtube.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.pdp.youtube.R
import uz.pdp.youtube.adapters.RecAdapterSearch
import uz.pdp.youtube.classes.Item
import uz.pdp.youtube.databinding.FragmentSearchBinding
import uz.pdp.youtube.network.NetworkHelper
import uz.pdp.youtube.utils.YoutubeResource
import uz.pdp.youtube.viewmodel.ViewModelFactory
import uz.pdp.youtube.viewmodel.YoutubeViewModel
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private lateinit var recAdapterSearch: RecAdapterSearch
    private lateinit var viewModel: YoutubeViewModel

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            recAdapterSearch = RecAdapterSearch(requireContext(), object : RecAdapterSearch.OnMyClickListener{
                override fun onClick(position: Item) {

                }

            })

            toolbar.setOnClickListener {
                findNavController().popBackStack()
            }

            recycle.adapter = recAdapterSearch

            createObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loadVideo(it)
                }){

                }



        }
    }

    private fun loadVideo(it: String?) {
        binding.apply {

            val networkHelper = NetworkHelper(requireContext())
            viewModel = ViewModelProvider(this@SearchFragment, ViewModelFactory(networkHelper))[YoutubeViewModel::class.java]

            viewModel.fetchVideoSearch(it?:"messi").observe(viewLifecycleOwner, {
                when(it) {
                    is YoutubeResource.Loading -> {
                        progress.visibility = View.VISIBLE
                    }
                    is YoutubeResource.Success -> {
                        Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                        progress.visibility = View.GONE
                        recycle.visibility = View.VISIBLE
                        recAdapterSearch.submitList(it.list.items)
                    }
                    is YoutubeResource.Error -> {
                        recycle.visibility = View.GONE
                        val textview = TextView(requireContext())
                        val layoutParam = ConstraintLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParam.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                        layoutParam.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
                        layoutParam.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
                        layoutParam.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                        textview.text = it.message
                        textview.layoutParams = layoutParam
                    }
                }
            })

        }

    }

    private fun createObservable(): Observable<String> {
        return Observable.create{ emitter ->
//            binding.editTxt.addTextChangedListener {
//                emitter.onNext(it.toString())
//            }

            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Toast.makeText(requireContext(), "query", Toast.LENGTH_SHORT).show()
                    emitter.onNext(query.toString())
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
//                    emitter.onNext(newText.toString())
                    return false
                }

            })
        }
    }
}