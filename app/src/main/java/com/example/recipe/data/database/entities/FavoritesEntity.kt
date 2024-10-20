package com.example.recipe.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipe.models.Result
import com.example.recipe.utils.Constant.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var result: Result
) {
}