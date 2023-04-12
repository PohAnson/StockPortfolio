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
}