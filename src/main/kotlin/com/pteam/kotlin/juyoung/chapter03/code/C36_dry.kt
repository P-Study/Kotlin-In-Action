package com.pteam.kotlin.juyoung.chapter03.code

class User(
    val id: Int,
    val name: String,
    val address: String
)

// 필드 검증 중복
fun saveUserDupl(user: User) {
    if(user.name.isEmpty()) {
        throw IllegalArgumentException("Can't save user ${user.id}: empty Name")
    }

    if(user.address.isEmpty()) {
        throw IllegalArgumentException("Can't save user ${user.id}: empty Name")
    }
}

fun saveUser(user: User) {
    // 로컬 함수
    fun validate(user: User, value: String, fieldName: String) {
        if(value.isEmpty()) {
            throw IllegalArgumentException("Can't save user ${user.id}: empty Name")
        }
    }

    // 로컬 함수 호출해서 각 필드 검증
    validate(user, user.name, "Name")
    validate(user, user.address, "Address")
}

fun saveUser2(user: User) {
    // 로컬 함수
    fun validate(value: String, fieldName: String) {
        if(value.isEmpty()) {
            throw IllegalArgumentException("Can't save user ${user.id}: empty $fieldName")
        }
    }

    // 로컬 함수 호출해서 각 필드 검증
    validate(user.name, "Name")
    validate(user.address, "Address")
}

// User클래스를 확장한 함수
fun User.validateBeforeSave() {
    // 로컬 함수
    fun validate(value: String, fieldName: String) {
        if(value.isEmpty()) {
            throw IllegalArgumentException("Can't save user ${id}: empty $fieldName")
        }
    }

    // 로컬 함수 호출해서 각 필드 검증
    validate(name, "Name")
    validate(address, "Address")
}

fun saveUser3(user: User) {
    user.validateBeforeSave()   // 확장 함수 호출
}