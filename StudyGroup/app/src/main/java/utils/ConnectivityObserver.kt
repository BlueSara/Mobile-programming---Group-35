package utils

// Gives feedback on if we're connected to a network
interface ConnectivityObserver{
    fun observe(): kotlinx.coroutines.flow.Flow<Status>

    enum class Status{
        Losing, Lost, Available, Unavailable
    }
}