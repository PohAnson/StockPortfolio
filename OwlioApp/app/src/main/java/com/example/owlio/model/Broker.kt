package com.example.owlio.model

sealed class Broker(val name: String) {
    abstract fun calculateFees()

    object Poems : Broker("Poems") {
        override fun calculateFees() {
            TODO("Not yet implemented")
        }
    }

    object Moomoo : Broker("Moomoo") {
        override fun calculateFees() {
            TODO("Not yet implemented")
        }
    }

    object INVALID : Broker("INVALID") {
        init {
            throw Exception("Invalid Broker")
        }

        override fun calculateFees() {
            throw Exception("Invalid Broker")
        }
    }

    companion object {
        fun brokerFromString(brokerString: String): Broker {
            return when (brokerString) {
                "Poems" -> Poems
                "Moomoo" -> Moomoo
                else -> {
                    throw Exception("Invalid Broker String '$brokerString' given.")
                }
            }
        }
    }
}