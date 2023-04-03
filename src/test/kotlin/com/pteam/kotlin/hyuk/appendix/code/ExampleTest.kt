package com.pteam.kotlin.hyuk.appendix.code

import io.kotest.core.spec.style.DescribeSpec

class ExampleTest : DescribeSpec(){
    init {
        describe("launch") {
            it("launch의 GlobalScope은 메인 스레드가 실행 중에만 실행을 보장한다.") {
                log("main() started")
                launchGlobalScope()
                log("launchGlobalScope() executed")

                Thread.sleep(5000L)
                log("main() terminated")
            }

            it("runBlocking은 코루틴의 실행이 끝날 때까지 현재 스레드를 블록시킨다.") {
                log("main() started")
                runBlockingExample()
                log("runBlockingExample() executed")

                Thread.sleep(5000L)
                log("main() terminated")
            }

            it("yield로 협력하기") {
                log("main() started")
                yieldExample()
                log("yieldExample() executed")

                Thread.sleep(5000L)
                log("main() terminated")
            }
        }

        describe("async") {
            it("async를 이용하면 스레드 하나로 스레드 병렬처리 비슷한 속도를 낼 수 있다.") {
                sumAll()
            }
        }
    }
}