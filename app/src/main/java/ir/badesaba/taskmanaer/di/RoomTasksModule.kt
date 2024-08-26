package ir.badesaba.taskmanaer.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.badesaba.taskmanaer.data.local.MyDatabase
import ir.badesaba.taskmanaer.data.local.TodoDao
import ir.badesaba.taskmanaer.data.TodoRepositoryImpl
import ir.badesaba.taskmanaer.domain.tasks.TaskRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RoomTasksModule {

    companion object {

        @Singleton
        @Provides
        fun provideMenuDatabase(
            @ApplicationContext context: Context
        ): MyDatabase = Room.databaseBuilder(
            context, MyDatabase::class.java, "task_manager"
        ).build()

        @Provides
        fun provideTodoDao(
            db: MyDatabase
        ): TodoDao = db.TodoDao()
//
//        @Provides
//        fun provideMenuApi(
//            retrofit: Retrofit
//        ): MenuApi = retrofit.create(MenuApi::class.java)

    }

    @Binds
    abstract fun bindTaskRepository(impl: TodoRepositoryImpl): TaskRepository

}