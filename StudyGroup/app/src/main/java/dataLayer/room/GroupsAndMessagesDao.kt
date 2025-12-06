package dataLayer.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/*
 * Data Acess Object (DAO)
 *
 * DAO lets us do CRUD operations (Create, Read, Update and Delete)
 *
 */
@Dao
interface GroupsDao {

    /*
    * Insert a list of groups into the room database
    * onConflict = Replace means that if a group with the same primary key
    * already exists, the old row is replaced with the new one.
    */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<Groups>)

    // Insert a single group
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: Groups)

    /*
    * Returns all the groups in the database
    * Flow<List<Groups>> keeps emitting updates whenever the group table changes.
    */
    @Query("SELECT * FROM groupsTable")
    fun getAllGroups(): Flow<List<Groups>>


    // Returns a single group based on its id.
    @Query("SELECT * FROM groupsTable WHERE groupID = :id")
    fun getGroupById(id: String): Flow<Groups>?
}

@Dao
interface MessagesDao {

    /*
    *   Inserts a list of messages
    *   REPLACE ensures that updates versions overwrite older ones.
    */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<Messages>)

    // Insert a single message
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Messages)

    // Retrieves all messages that belong to a group.
    @Query("SELECT * FROM messages WHERE groupID = :groupId")
    fun getMessagesForGroup(groupId: String): Flow<List<Messages>>

    // Retrieves a single message by its id.
    @Query("SELECT * FROM messages WHERE messageID = :id")
    fun getMessageById(id: String): Flow<Messages>?
}

