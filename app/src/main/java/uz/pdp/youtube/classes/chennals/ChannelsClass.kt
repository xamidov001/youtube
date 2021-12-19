package uz.pdp.youtube.classes.chennals

data class ChannelsClass(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val pageInfo: PageInfo
)