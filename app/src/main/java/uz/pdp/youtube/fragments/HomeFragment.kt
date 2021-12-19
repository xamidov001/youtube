package uz.pdp.youtube.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import uz.pdp.youtube.R
import uz.pdp.youtube.adapters.OpenHelper
import uz.pdp.youtube.adapters.PagerAdapter
import uz.pdp.youtube.adapters.RecAdapter
import uz.pdp.youtube.classes.Item
import uz.pdp.youtube.databinding.FragmentHomeBinding
import uz.pdp.youtube.models.PaginationScrollListener
import uz.pdp.youtube.network.NetworkHelper
import uz.pdp.youtube.utils.YoutubeResource
import uz.pdp.youtube.viewmodel.ViewModelFactory
import uz.pdp.youtube.viewmodel.YoutubeViewModel
import kotlin.coroutines.CoroutineContext

class HomeFragment(var listener: OpenHelper) : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var recAdapter: PagerAdapter
    private lateinit var youtubeViewModel: YoutubeViewModel
    private var PAGE = ""
    private var newPage = ""
    private lateinit var list: ArrayList<Item>
    private var isLoading = false
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = ArrayList()

        binding.apply {
            toolbar.setOnMenuItemClickListener {
                onOptionsItemSelected(it)
            }
            recAdapter = PagerAdapter(requireContext(), list, object : PagerAdapter.OnMyClickListener{
                override fun onClick(position: Item) {
                    listener.openView(position.id.videoId, position.snippet.channelId)

                }

            })

            val linearLayoutManager = LinearLayoutManager(requireContext())
            recycle.layoutManager = linearLayoutManager
            recycle.adapter = recAdapter

            val networkHelper = NetworkHelper(requireContext())
            youtubeViewModel = ViewModelProvider(this@HomeFragment, ViewModelFactory(networkHelper))[YoutubeViewModel::class.java]
            youtubeViewModel.fetchVideo("", PAGE).observe(viewLifecycleOwner, {
                when(it) {
                    is YoutubeResource.Loading -> {
                        recycle.visibility = View.GONE
                    }
                    is YoutubeResource.Success -> {
                        spinKit.visibility = View.GONE
                        recycle.visibility = View.VISIBLE
                        recAdapter.adAll(it.list.items)
                        newPage = it.list.nextPageToken

                        if (PAGE != "0") {
                            recAdapter.addLoading()
                        } else {
                            isLastPage = true
                        }
                    }
                    is YoutubeResource.Error -> {
                        spinKit.visibility = View.GONE
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


            recycle.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
                override fun loadMoreItems() {
                    isLoading = true
                    PAGE = newPage
                    getNext(PAGE)
                }

                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }

            })


        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_search -> {
                findNavController().navigate(R.id.searchFragment)
            }
        }

        return true
    }

    fun getNext(page: String) {
//        binding.apply {
//            val networkHelper = NetworkHelper(requireContext())
//            youtubeViewModel = ViewModelProvider(
//                this@HomeFragment,
//                ViewModelFactory(networkHelper)
//            )[YoutubeViewModel::class.java]
//            youtubeViewModel.fetchVideo("", page).observe(viewLifecycleOwner, {
//                when (it) {
//                    is YoutubeResource.Loading -> {
//                    }
//                    is YoutubeResource.Success -> {
//                        isLoading = false
//                        recAdapter.adAll(it.list.items)
//                        newPage = it.list.nextPageToken
//
//                        if (PAGE != "0") {
//                            recAdapter.addLoading()
//                        } else {
//                            isLastPage = true
//                        }
//                    }
//                    is YoutubeResource.Error -> {
//
//                    }
//                }
//            })
//
//        }
    }

}