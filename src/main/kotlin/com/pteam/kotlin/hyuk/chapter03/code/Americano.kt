package com.pteam.kotlin.hyuk.chapter03.code

class Americano private constructor() {

    var water: Int = 0
        private set

    var caffeine: Int = 0
        private set

    var ice: Int = 0
        private set

    companion object {
        fun IceAmericano(water: Int, caffeine: Int, ice: Int): Americano {
            fun validate(ingredient: Int, amount: Int) {
                if (ingredient <= amount) {
                    throw IllegalArgumentException("Wrong recipe")
                }
            }

            validate(water, 5)
            validate(caffeine, 20)
            validate(ice, 10)

            val americano = Americano()
            americano.water = water
            americano.caffeine = caffeine
            americano.ice = ice

            return americano
        }
    }
}