package uz.pdp.youtube.classes.comment

data class Snippet(
    val canReply: Boolean,
    val isPublic: Boolean,
    val topLevelComment: TopLevelComment,
    val totalReplyCount: Int,
    val videoId: String
)