package com.pteam.kotlin.kade.chapter3.code

class User(val id: Int, val name: String, val address: String)
fun saveUser(user: User) {
    user.validateBeforeSave()
}

fun saveUserRefactoring(user: User) {
    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException("Can't save user 쿄쿄쿄쿄쿄쿄쿄 $(user.id): $fieldName")
        }
    }

    validate(user.name, "Name")
    validate(user.address, "Address")
}

fun User.validateBeforeSave() {
    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException("Can't save user 쿄쿄쿄쿄쿄쿄쿄 $(user.id): $fieldName")
        }
    }

    validate(name, "Name")
    validate(address, "Address")
}