package com.example.owlio.ui.screen

import com.example.owlio.R


enum class Screens(val label: String, val icon: Int, val route: String) {
    PORTFOLIO(
        "Portfolio",
        R.drawable.business_center,
        "portfolio",
    ),
    TRANSACTION("Transaction", R.drawable.description, "transaction"), PNL(
        "P/L Report",
        R.drawable.assessment,
        "pnl",
    )

}