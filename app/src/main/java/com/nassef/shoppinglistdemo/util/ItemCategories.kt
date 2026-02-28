package com.nassef.shoppinglistdemo.util

import androidx.compose.ui.graphics.toArgb
import com.nassef.domain.entities.Category
import com.nassef.shoppinglistdemo.R
import com.nassef.shoppinglistdemo.ui.theme.Blueish
import com.nassef.shoppinglistdemo.ui.theme.Brown
import com.nassef.shoppinglistdemo.ui.theme.Green
import com.nassef.shoppinglistdemo.ui.theme.Pink
import com.nassef.shoppinglistdemo.ui.theme.Red

val categories = listOf(
    Category("Milk", R.drawable.milk_bottle , Blueish.toArgb()),
    Category("Vegetable", R.drawable.carrot , Green.toArgb()),
    Category("Fruits", R.drawable.apple , Pink.toArgb()),
    Category("Breads", R.drawable.white_bread , Brown.toArgb()),
    Category("Meats", R.drawable.meat , Red.toArgb()),
)
fun getItemCategoryByName(name: String) : Category?{
    return categories.firstOrNull { it.name == name }
}