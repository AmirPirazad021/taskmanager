package ir.badesaba.taskmanaer.utils

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStoreManager {

    private const val DATASTORE_NAME = "task_manager_preferences"

    private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

    suspend fun <T> saveValue(context: Context, key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun <T> getValue(context: Context, key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return context.dataStore.data
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    val FETCHED_DATA_FROM_SERVER = booleanPreferencesKey("fetched_data_from_server")
}
