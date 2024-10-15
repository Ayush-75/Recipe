package com.example.recipe.di

import android.content.Context
import androidx.room.Room
import com.example.recipe.data.database.RecipeDao
import com.example.recipe.data.database.RecipesDatabase
import com.example.recipe.data.database.RecipesTypeConverter
import com.example.recipe.utils.Constant.Companion.DATABASE_NAME
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRecipesDatabase(
        @ApplicationContext context: Context,
        moshi: Moshi
    ): RecipesDatabase {
        return Room.databaseBuilder(
            context,
            RecipesDatabase::class.java,
            "recipes_database"
        ).addTypeConverter(RecipesTypeConverter(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provide(database: RecipesDatabase) : RecipeDao {
        return database.recipesDao()
    }
}