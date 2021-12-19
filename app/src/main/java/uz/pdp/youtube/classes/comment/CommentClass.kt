package uz.pdp.youtube.classes.comment

data class CommentClass(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo
)