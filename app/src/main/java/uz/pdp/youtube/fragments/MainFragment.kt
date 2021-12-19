package uz.pdp.youtube.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.pdp.youtube.R
import uz.pdp.youtube.adapters.OpenHelper
import uz.pdp.youtube.adapters.RecAdapter
import uz.pdp.youtube.adapters.ViewAdapter
import uz.pdp.youtube.classes.chennals.ChannelsClass
import uz.pdp.youtube.classes.comment.CommentClass
import uz.pdp.youtube.classes.comment.Item
import uz.pdp.youtube.classes.video.VideoData
import uz.pdp.youtube.databinding.FragmentMainBinding
import uz.pdp.youtube.network.NetworkHelper
import uz.pdp.youtube.retro.ApiClient

class MainFragment : Fragment(R.layout.fragment_main) {


    private val binding by viewBinding(FragmentMainBinding::bind)
    private lateinit var viewAdapter: ViewAdapter
    private lateinit var recAdapter: RecAdapter
    private var lastPosition = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            recAdapter = RecAdapter(requireContext())
            binding.commentsRv.adapter = recAdapter


            cardAdd.setOnClickListener {
                changeView(2, imgAdd)
            }

            cardHome.setOnClickListener {
                changeView(0, imgHome)
            }

            cardLibrary.setOnClickListener {
                changeView(4, imgLib)
            }

            cardShorts.setOnClickListener {
                findNavController().navigate(R.id.shortsFragment)
            }

            cardSubscription.setOnClickListener {
                changeView(3, imgSub)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        binding.apply {

            viewAdapter = ViewAdapter(requireActivity(), object : OpenHelper {
                override fun openView(videoID: String, channel : String) {
                    bidLayout.transitionToState(R.id.middle)
                    recAdapter.submitList(ArrayList<Item>())
                    loadPlay(videoID, channel)

                    youtubePlayerView.initialize({
                        it.addListener(object : AbstractYouTubePlayerListener() {
                            override fun onReady() {
                                it.removeListener(this)
                                it.loadVideo(videoID, 0f)

                            }
                        })
                    }, true)

                }
            })

            viewPager.adapter = viewAdapter
            getPosition(lastPosition)
            viewPager.isUserInputEnabled = false

        }
    }

    private fun loadPlay(videoID: String, channel : String) {
        binding.apply {

            commentsLayout.setOnClickListener {
                getComment(videoID)
            }

            val networkHelper = NetworkHelper(requireContext())


            if (networkHelper.isNetworkConnect()) {
                ApiClient.appService.getChannelData(channel)
                    .enqueue(object : Callback<ChannelsClass> {
                        override fun onResponse(
                            call: Call<ChannelsClass>,
                            response: Response<ChannelsClass>
                        ) {
                            if (response.isSuccessful) {
                                val item = response.body()!!.items[0]
                                Glide.with(requireContext()).load(item.snippet.thumbnails.medium.url).into(accountImage)
                                channelSubscriberCountTv.text = item.statistics.subscriberCount
                                channelNameTv.text = item.snippet.title
                            }
                        }

                        override fun onFailure(call: Call<ChannelsClass>, t: Throwable) {

                        }

                    })
            }

            if (networkHelper.isNetworkConnect()) {
                ApiClient.appService.getVideoData(videoID)
                    .enqueue(object : Callback<VideoData> {
                        override fun onResponse(
                            call: Call<VideoData>,
                            response: Response<VideoData>
                        ) {
                            if (response.isSuccessful) {
                                val item = response.body()!!.items[0]
                                val commentCount = item.statistics.commentCount
                                val dislikeCount = item.statistics.dislikeCount
                                val likeCount = item.statistics.likeCount
                                val viewCount = item.statistics.viewCount
                                val date = item.snippet.publishedAt.substring(0, 10)
                                binding.likeCountTv.text = likeCount
                                binding.dislikeCountTv.text = dislikeCount
                                binding.viewCountTv.text = "$viewCount views • Premiered $date"
                                binding.commentsCountTv.text = "Comments $commentCount"
                                videoNameTv.text = item.snippet.title
                            }
                        }

                        override fun onFailure(call: Call<VideoData>, t: Throwable) {

                        }

                    })
            }


        }
    }

    private fun getPosition(currentItem: Int) {
        binding.apply {
            viewPager.currentItem = lastPosition
            when(lastPosition) {
                0 -> {
                    imgHome.setImageResource(R.drawable.ic_home_true)
                }
                3 -> {
                    imgSub.setImageResource(R.drawable.ic_subscription_true)
                }
                4 ->{
                    imgLib.setImageResource(R.drawable.ic_library_true)
                }
            }

        }
    }

    private fun changeView(n: Int, img: ImageView) {
        binding.apply {

            if (n != lastPosition) {
                iconFalse(lastPosition)
                iconTrue(n, img)
                lastPosition = n
                viewPager.currentItem = lastPosition
            }

        }
    }

    private fun iconTrue(n: Int, img: ImageView) {
        binding.apply {

            when(n) {
                0 -> {
                    img.setImageResource(R.drawable.ic_home_true)
                }
                3 -> {
                    img.setImageResource(R.drawable.ic_subscription_true)
                }
                4 ->{
                    img.setImageResource(R.drawable.ic_library_true)
                }
            }

        }
    }

    private fun iconFalse(last: Int) {
        binding.apply {
            when(last) {
                0 -> {
                    imgHome.setImageResource(R.drawable.ic_home)
                }
                1 -> {
                    imgShorts.setImageResource(R.drawable.ic_shorts)
                }
                2 -> {

                }
                3 -> {
                    imgSub.setImageResource(R.drawable.ic_subcription)
                }
                4 ->{
                    imgLib.setImageResource(R.drawable.ic_library)
                }
            }
        }
    }

    private fun getComment(videoID: String) {
        val networkHelper = NetworkHelper(requireContext())
        if (networkHelper.isNetworkConnect()) {
            ApiClient.appService.getComments(videoID)
                .enqueue(object : Callback<CommentClass> {
                    override fun onResponse(
                        call: Call<CommentClass>,
                        response: Response<CommentClass>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "keldi ${response.body()!!.items.size}", Toast.LENGTH_SHORT).show()

                            recAdapter.submitList(response.body()!!.items)
                        }
                    }

                    override fun onFailure(call: Call<CommentClass>, t: Throwable) {

                    }

                })
        }
    }
}