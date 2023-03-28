package com.pteam.kotlin.kade.chapter10.code

class AnnotaionApply {

    @Deprecated("Use descriptKotlinSummary(description) instead.", ReplaceWith("descriptKotlinSummary(description)"))
    fun presentKotlinSummary(summary: String) {}

    fun callPresentKotlinSummary() {
        presentKotlinSummary("Summary")
    }
}