package dataLayer.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/*
 * Main room database class
 *
 * THe class defines:
 *  - the tables in the database (entities)
 *  - the DAO that provides access to the tables (GroupsAndMessagesDao.kt)
 */
@Database(
    entities = [Groups::class, Messages::class],    // the tables managed by Room
    version = 1                                     // Database schema version
)
abstract class AppDatabase : RoomDatabase() {
    /*
     * Functions that are to be exposed to the rest of the app
     */

    abstract fun groupsDao(): GroupsDao
    abstract fun messagesDao(): MessagesDao

    companion object {
        // @Volatile ensures that all threads see the latest value.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns the existing database instance or creates a new one if it doesnt exist.
         *
         * synchronized prevents multiple threads from creating multiple DB instances at once.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // If INSTANCE is still null after entering the synchronized block,
                // we build the database here.
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "studygroupDB"      // FIle name for the room database
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { INSTANCE = it } // Store the instance for future use
            }
        }
    }
}