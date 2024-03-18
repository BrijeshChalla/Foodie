package com.learnandroid.foodie.model

data class MenuItem(
    val foodName : String ?= null,
    val foodPrice : String ?= null,
    val foodInfo : String ?= null,
    val foodImage : String ?= null,
    val foodIngredient : String ?= null
)
