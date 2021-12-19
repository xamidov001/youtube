package uz.pdp.youtube.classes

data class PlayVideo(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo,
    val prevPageToken: String,
    val regionCode: String
)