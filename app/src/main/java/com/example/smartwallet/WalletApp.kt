package com.example.smartwallet

import Controller.WalletController
import android.app.Application
import android.content.Context

class WalletApp : Application() {
    companion object {
        lateinit var controller: WalletController
        fun getController(context: Context): WalletController {
            if (!::controller.isInitialized) {
                controller = WalletController(context.applicationContext)
            }
            return controller
        }
    }
}