package com.example.owlio.ui.screen

import com.example.owlio.R


enum class Screens(val label: String, val route: String, val icon: Int? = null) {
    PORTFOLIO(
        "Portfolio",
        "portfolio",
        R.drawable.business_center,
    ),
    TRANSACTION(
        "Transaction",
        "transaction",
        R.drawable.description,
    ),
    PNL(
        "P/L Report",
        "pnl",
        R.drawable.assessment,
    ),
    NEWTRANSACTION("New Transaction", "newTransaction"),
    EDITTRANSACTION("Edit Transaction", "editTransaction/")
}