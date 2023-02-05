package com.pteam.kotlin.kade.chapter4.code

// 인터페이스에선 아무 상태도 가질 수 엇다
interface Member {
    val nickname: String
}


/**
 * 인터페이스 구현하는 방법
 *
 * 1. 생성자에 있는 프로퍼티
 * 2. 커스텀 게터
 * 3. 프로퍼티 초기화
 */
class PrivateMember(override val nickname: String) : Member
class SubscribingMember(val email: String) : Member {
    override val nickname: String
        get() = email.substringBefore('@')
}
//class Facebook(val accountId: Int) : Member {
//    override val nickname: String = getFacebookName(accountId)
//}

class LengthCounter {
    var counter: Int = 0
        private set         // 외부에서 set을 막는다 domain entity 사용할 때 필요할 것으로 보인다

    fun addWord(word: String) {
        counter += word.length
    }
}