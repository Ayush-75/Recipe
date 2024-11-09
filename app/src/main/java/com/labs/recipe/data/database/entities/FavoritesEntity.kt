package com.labs.recipe.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.labs.recipe.models.Result
import com.labs.recipe.utils.Constant.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var result: Result
) {
}