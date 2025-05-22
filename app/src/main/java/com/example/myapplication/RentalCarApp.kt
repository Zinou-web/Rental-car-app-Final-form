package com.example.myapplication

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.myapplication.data.preference.AuthPreferenceManager
import com.example.myapplication.utils.PreferenceManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class RentalCarApp : Application(), Configuration.Provider {
    
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    
    @Inject
    lateinit var preferenceManager: PreferenceManager
    
    @Inject
    lateinit var authPreferenceManager: AuthPreferenceManager
    
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        // MultiDex.install(this) // Uncomment if using multidex
    }
    
    override fun onCreate() {
        super.onCreate()
        
        // Enable strict mode in debug builds
        if (BuildConfig.DEBUG) {
            enableStrictMode()
            Timber.plant(Timber.DebugTree())
        }
        
        // Initialize app components
        initializeApp()
        
        Timber.d("Application started")
    }
    
    private fun initializeApp() {
        // Initialize any third-party libraries here
        // FirebaseApp.initializeApp(this)
        
        // Check if we need to migrate data from old preferences to new auth preferences
        migratePreferencesToAuth()
        
        // No need to manually initialize WorkManager when implementing Configuration.Provider
        // WorkManager will be initialized automatically using getWorkManagerConfiguration()
    }
    
    private fun migratePreferencesToAuth() {
        // If user is logged in with old preference system, migrate to new auth system
        if (preferenceManager.isLoggedIn && !authPreferenceManager.isLoggedIn()) {
            preferenceManager.authToken?.let { token ->
                authPreferenceManager.saveAuthToken(token)
                preferenceManager.userId?.let { userId ->
                    authPreferenceManager.saveUserId(userId)
                }
                authPreferenceManager.setLoggedIn(true)
                // Set a default expiry time (1 day)
                authPreferenceManager.saveTokenExpiry(86400)
                
                Timber.d("Migrated user authentication from old preferences to secure storage")
            }
        }
    }
    
    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build()
        )
        
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .build()
        )
    }
    
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}
