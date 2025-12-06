import android.content.Context
import androidx.room.withTransaction
import dataLayer.room.AppDatabase
import dataLayer.room.Groups
import dataLayer.room.Messages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



/*
* Saves a groups metadata and its messages to the local Room Database
*
* The function is intended for offline data storage. Whenever new data is
* fetched from the API, it is written to the local DB so that the user can
* view it without internett connection
*
 */
fun saveToLocalDb(
    context: Context,
    groupID: String,
    group: Map<String, Any>,
    messages: List<Map<String, Any>>,
    participants: Int,
    helpers: Int
){
    // Run DB write operations on the IO dispatcher
    CoroutineScope(Dispatchers.IO).launch {
        val db = AppDatabase.getInstance(context)

        // A room transaction ensures all insert succeed or none do
        db.withTransaction {
            // Do nothing if API returned no valid data
            if (group.isEmpty() || messages.isEmpty()) return@withTransaction

            // Converting the raw api map into the room entity.
            // If any keys are missing, they are safely handeled and converted to null
            val groupEntity = Groups(
                groupID = groupID,
                postID = group["postID"]?.toString(),
                title = group["title"]?.toString(),
                subject = group["subject"]?.toString(),
                subjectCode = group["subjectCode"]?.toString(),
                topic = group["topic"]?.toString(),
                participants = participants,
                helpers = helpers
            )
            // insert or replace group entry in local storage
            db.groupsDao().insertGroup(groupEntity)

            // COnverting each message map from the API responses into the room
            val messages = messages.map { msg ->
                Messages(
                    messageID = msg["messageID"].toString(),
                    groupID = msg["groupID"]?.toString(),
                    time = msg["time"].toString(),
                    location = msg["location"]?.toString(),
                    building = msg["building"]?.toString(),
                    room = msg["room"].toString(),
                    comment = msg["comment"].toString(),
                    isSelected = msg["isSelected"] as? Boolean,
                    userAgreed = msg["userAgreed"] as? Boolean,
                    ownerAgreed = msg["ownerAgreed"] as? Boolean,
                    assistAgreed = msg["assistAgreed"] as? Boolean
                )
            }
            // insert or replace all messages in local storage
            db.messagesDao().insertMessages(messages)
        }
    }
}