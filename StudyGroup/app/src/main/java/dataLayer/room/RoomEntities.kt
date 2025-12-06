package dataLayer.room


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


/*
 * Group table in the local room database
 *
 * Each row is corresponding to a single study group. This entity stores
 * only the information required for offline mode.
*/
@Entity(tableName = "groupsTable")
data class Groups(
    @PrimaryKey
    @ColumnInfo(name = "groupID") val groupID: String,          // unique ID of the group (PK)
    @ColumnInfo(name = "postID") val postID: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "subject") val subject: String?,
    @ColumnInfo(name = "subjectCode") val subjectCode: String?,
    @ColumnInfo(name = "topic") val topic: String?,
    @ColumnInfo(name = "participants") val participants: Int?,
    @ColumnInfo(name = "helpers") val helpers: Int?

)

/*
 * Messages table in the local room database
 *
 * Each row is corresponding to a single message/
 * sugguestion made inside a group.

*/
@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = Groups::class,
            parentColumns = ["groupID"],
            childColumns = ["groupID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Messages(
    @PrimaryKey
    @ColumnInfo (name = "messageID") val messageID: String,     // Unique id of the message
    @ColumnInfo(name = "groupID", index = true) val groupID: String?,       // Id of the group the message belongs to
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "location") val location: String?,
    @ColumnInfo(name = "building") val building: String?,
    @ColumnInfo(name = "room") val room: String?,
    @ColumnInfo(name = "comment") val comment: String?,
    @ColumnInfo(name = "isSelected") val isSelected: Boolean?,
    @ColumnInfo(name = "userAgreed") val userAgreed: Boolean?,
    @ColumnInfo(name = "ownerAgreed") val ownerAgreed: Boolean?,
    @ColumnInfo(name = "assistAgreed") val assistAgreed: Boolean?
)
