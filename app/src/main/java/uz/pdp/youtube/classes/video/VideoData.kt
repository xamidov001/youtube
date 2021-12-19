package uz.pdp.youtube.classes.video

data class VideoData(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val pageInfo: PageInfo
)