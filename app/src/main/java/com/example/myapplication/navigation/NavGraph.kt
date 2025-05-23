package com.example.myapplication.navigation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.ui.screens.auth.CreateAccountScreen
import com.example.myapplication.ui.screens.auth.NewPasswordScreen
import com.example.myapplication.ui.screens.auth.SignInScreen
import com.example.myapplication.ui.screens.BookingCar.CarDetailsScreen
import com.example.myapplication.ui.screens.BookingCar.GalleryScreen
import com.example.myapplication.ui.screens.home.HomeScreen
import com.example.myapplication.ui.screens.home.NotificationScreen
import com.example.myapplication.ui.screens.bookings.CompletedBookingsScreen
import com.example.myapplication.ui.screens.bookings.MyBookingsScreen
import com.example.myapplication.ui.screens.payment.BillScreen
import com.example.myapplication.ui.screens.bookings.CancelationScreen
import com.example.myapplication.ui.screens.payment.EdahabiaScreen
import com.example.myapplication.ui.screens.payment.FavoriteScreen
import com.example.myapplication.ui.screens.payment.PaymentDoneScreen
import com.example.myapplication.ui.screens.payment.PaymentMethodScreen
import com.example.myapplication.ui.screens.payment.PaymentPending
import com.example.myapplication.ui.screens.payment.UnsuccessfulPaymentScreen
import com.example.myapplication.ui.screens.profile.HelpCenterScreen
import com.example.myapplication.ui.screens.profile.NotificationSettingsScreen
import com.example.myapplication.ui.screens.profile.PrivacyPolicyScreen
import com.example.myapplication.ui.screens.profile.ProfileScreen
import com.example.myapplication.ui.screens.profile.UpdateProfileLocationScreen
import com.example.myapplication.ui.screens.profile.UpdateProfileScreen
import com.example.myapplication.ui.screens.profile.logoutScreen
import com.example.myapplication.ui.screens.settings.SettingsScreen
import com.example.myapplication.ui.screens.welcome.WelcomeScreen
import com.example.myapplication.ui.screens.welcome.SecondScreen
import com.example.myapplication.ui.screens.welcome.ThirdScreen
import com.example.myapplication.ui.screens.splashscreen.SplashScreenSequence
import com.example.myapplication.ui.screens.auth.CompleteProfileScreen
import com.example.myapplication.ui.screens.BookingCar.CarBookingScreen
import com.example.myapplication.ui.screens.password.ChangePasswordScreen
import com.example.myapplication.ui.screens.auth.OTPVerificationScreen
import com.example.myapplication.ui.screens.auth.ForgotPasswordScreen
import com.example.myapplication.ui.screens.auth.ResetPasswordScreen
import com.example.myapplication.ui.screens.home.CompleteYourBookingScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.preference.AuthPreferenceManager
import com.example.myapplication.ui.screens.home.BookingViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.ui.screens.home.FavoritesScreen

/**
 * Enum class that contains all the possible screens in our app
 * This makes it easier to manage screen routes and prevents typos
 */
enum class AppScreen {
    // Splash Screen Sequence
    SplashSequence,

    // Onboarding Screens
    Welcome,
    Second,
    Third,
    
    // Authentication Screens
    SignIn,
    CreateAccount,
    ForgotPassword,
    ResetPassword,
    NewPassword,
    CompleteProfile,
    OTPVerification,
    
    // Main Screens
    Home,
    CarDetails,
    Filter,
    Notification,
    Profile,
    Gallery,
    
    // Booking Screens
    MyBooking,
    CompletedBooking,
    CompleteYourBooking,
    CarBooking,
    
    // Favorite Screen
    Favorite,
    
    // Settings Screens
    Settings,
    NotificationSettings,
    PasswordManager,
    
    // Payment Screens
    PaymentMethod,
    Edahabia,
    PaymentDone,
    PaymentPending,
    UnsuccessfulPayment,
    Bill,
    Cancelation,
    
    // Profile Screens
    HelpCenter,
    PrivacyPolicy,
    Logout,
    ProfileGeneral,
    ProfileLocation,
    Favorites
}

/**
 * The main navigation graph of the application
 * This sets up all possible navigation paths between screens
 *
 * @param navController The navigation controller that handles the navigation
 * @param startDestination The screen to show when the app first launches
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: AppScreen = AppScreen.SplashSequence
) {
    // Add global navigation logging
    navController.addOnDestinationChangedListener { _, destination, _ ->
        Log.d("Navigation", "Navigated to: ${destination.route}")
    }

    // Get the context for creating the AuthPreferenceManager
    val context = LocalContext.current

    // Create a shared CarViewModel that will be used by both Home and Filter screens
    val sharedCarViewModel = viewModel<com.example.myapplication.ui.screens.home.CarViewModel>()
    
    // Create a shared BookingViewModel for the booking flow
    val sharedBookingViewModel = viewModel<BookingViewModel>()

    NavHost(
        navController = navController,
        startDestination = startDestination.name
    ) {
        // Splash Screen Sequence
        composable(AppScreen.SplashSequence.name) {
            // Return to original flow: Splash → Welcome
            SplashScreenSequence(
                onNavigateToWelcome = { 
                    // Navigate to Welcome screen as originally intended
                    navController.navigate(AppScreen.Welcome.name)
                }
            )
        }

        // Onboarding Screens
        composable(AppScreen.Welcome.name) {
            WelcomeScreen(
                onNextClick = { 
                    // Navigate to Second screen as originally intended
                    navController.navigate(AppScreen.Second.name)
                }
            )
        }

        composable(AppScreen.Second.name) {
            SecondScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { navController.navigate(AppScreen.Third.name) },
                onSkipClick = { navController.navigate(AppScreen.SignIn.name) }
            )
        }

        composable(AppScreen.Third.name) {
            ThirdScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { 
                    Log.d("Navigation", "ThirdScreen: Next button clicked, navigating to SignIn")
                    navController.navigate(AppScreen.SignIn.name) {
                        // Clear back stack up to Third screen
                        popUpTo(AppScreen.Third.name) { inclusive = true }
                    }
                }
            )
        }

        // Authentication Screens
        composable(AppScreen.SignIn.name) {
            SignInScreen(
                onNavigateToRegister = { navController.navigate(AppScreen.CreateAccount.name) },
                onNavigateToForgotPassword = { navController.navigate(AppScreen.ForgotPassword.name) },
                onSignInSuccess = { navController.navigateAndClear(AppScreen.Home.name) }
            )
        }

        composable(AppScreen.CreateAccount.name) {
            CreateAccountScreen(
                onSignInClick = { navController.navigate(AppScreen.SignIn.name) },
                onCreateAccountSuccess = { 
                    // Add debug logging
                    Log.d("NavGraph", "Navigating to OTP verification after successful registration")
                    // Navigate to OTP verification and remove CreateAccount from back stack
                    navController.navigate("${AppScreen.OTPVerification.name}?fromForgotPassword=false") {
                        popUpTo(AppScreen.CreateAccount.name) { inclusive = true }
                    }
                }
            )
        }

        composable(AppScreen.ForgotPassword.name) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
                onResetSent = { 
                    // Navigate to reset password screen
                    navController.navigate("${AppScreen.ResetPassword.name}") {
                        popUpTo(AppScreen.ForgotPassword.name) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "${AppScreen.ResetPassword.name}?email={email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            
            ResetPasswordScreen(
                email = email,
                onBack = { navController.popBackStack() },
                onResetSuccess = { 
                    // Navigate to sign in screen after successful password reset
                    navController.navigate(AppScreen.SignIn.name) {
                        popUpTo(AppScreen.ResetPassword.name) { inclusive = true }
                    }
                }
            )
        }

        composable(AppScreen.NewPassword.name) {
            NewPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onPasswordResetSuccess = { navController.navigate(AppScreen.SignIn.name) }
            )
        }

        composable(AppScreen.CompleteProfile.name) {
            CompleteProfileScreen(
                onBackClick = { navController.popBackStack() },
                onProfileCompleted = { navController.navigateAndClear(AppScreen.Home.name) }
            )
        }

        composable(
            route = "${AppScreen.OTPVerification.name}?fromForgotPassword={fromForgotPassword}",
            arguments = listOf(
                navArgument("fromForgotPassword") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val fromForgotPassword = backStackEntry.arguments?.getBoolean("fromForgotPassword") ?: false
            
            OTPVerificationScreen(
                onBackClick = { navController.popBackStack() },
                onVerifySuccess = { 
                    if (fromForgotPassword) {
                        navController.navigate(AppScreen.NewPassword.name)
                    } else {
                        navController.navigate(AppScreen.CompleteProfile.name)
                    }
                },
                fromForgotPassword = fromForgotPassword
            )
        }

        // Main Screens
        composable(AppScreen.Home.name) {
            HomeScreen(
                onCarClick = { carId -> 
                    navController.navigate("${AppScreen.CarDetails.name}/$carId") 
                },
                onProfileClick = { navController.navigate(AppScreen.Profile.name) },
                onFavoriteClick = { navController.navigate(AppScreen.Favorites.name) },
                onSignOut = { navController.navigateAndClear(AppScreen.SignIn.name) },
                onNotificationClick = { navController.navigate(AppScreen.Notification.name) },
                onCatalogClick = { navController.navigate(AppScreen.MyBooking.name) }
            )
        }

        composable(
            route = "${AppScreen.CarDetails.name}/{carId}",
            arguments = listOf(navArgument("carId") { type = NavType.StringType })
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId") ?: ""
            CarDetailsScreen(
                carId = carId,
                onBackPressed = { navController.popBackStack() },
                onGalleryClick = { navController.navigate("${AppScreen.Gallery.name}/$carId") },
                onBookNowClick = { 
                    Log.d("NavGraph", "CarDetailsScreen: Navigating to CarBookingScreen with carId: $carId")
                    navController.navigate("${AppScreen.CarBooking.name}/$carId") // Pass carId to CarBookingScreen
                }
            )
        }

        // Filter Screen - no longer needed since we're handling filters in the HomeScreen
        // composable(AppScreen.Filter.name) {
        //     Filter(
        //         viewModel = sharedCarViewModel,
        //         onBackClick = { navController.popBackStack() }
        //     )
        // }

        composable(AppScreen.Notification.name) {
            NotificationScreen(
                onBackPressed = { navController.popBackStack() }
            )
        }

        composable(
            route = "${AppScreen.Gallery.name}/{carId}",
            arguments = listOf(navArgument("carId") { type = NavType.StringType })
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId")
            GalleryScreen(
                carId = carId,
                onBackPressed = { navController.popBackStack() },
                onAboutClick = { 
                    if (carId != null) {
                        navController.navigate("${AppScreen.CarDetails.name}/$carId") {
                            popUpTo("${AppScreen.Gallery.name}/$carId") { inclusive = true }
                        }
                    } else {
                        navController.popBackStack()
                    }
                },
                onBookNowClick = { 
                    Log.d("NavGraph", "GalleryScreen: Navigating to CarBookingScreen with carId: $carId")
                    if (carId != null) {
                        navController.navigate("${AppScreen.CarBooking.name}/$carId") // Pass carId to CarBookingScreen
                    } else {
                        navController.navigate(AppScreen.CarBooking.name)
                    }
                }
            )
        }

        composable(AppScreen.Profile.name) {
            ProfileScreen(
                navController = navController,
                onHomeClick = { navController.navigateAndClear(AppScreen.Home.name) },
                onBookingsClick = { navController.navigate(AppScreen.MyBooking.name) },
                onFavoriteClick = { navController.navigate(AppScreen.Favorites.name) },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Booking Screens
        composable(AppScreen.MyBooking.name) {
            MyBookingsScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() },
                onHomeClick = { navController.navigateAndClear(AppScreen.Home.name) },
                onFavoriteClick = { navController.navigate(AppScreen.Favorites.name) },
                onProfileClick = { navController.navigate(AppScreen.Profile.name) },
                onCompletedTabClick = { navController.navigate(AppScreen.CompletedBooking.name) }
            )
        }

        composable(AppScreen.CompletedBooking.name) {
            CompletedBookingsScreen(
                onBackClick = { navController.popBackStack() },
                onHomeClick = { navController.navigateAndClear(AppScreen.Home.name) },
                onMyBookingsClick = { navController.navigate(AppScreen.MyBooking.name) },
                onFavoriteClick = { navController.navigate(AppScreen.Favorites.name) },
                onProfileClick = { navController.navigate(AppScreen.Profile.name) },
                onUpcomingTabClick = { navController.navigate(AppScreen.MyBooking.name) },
                onRebookClick = { carId -> 
                    navController.navigate("${AppScreen.CarBooking.name}/$carId") 
                }
            )
        }

        // Favorite Screen
        composable(AppScreen.Favorite.name) {
            // Redirect to new Favorites screen
            LaunchedEffect(Unit) {
                navController.navigate(AppScreen.Favorites.name) {
                    popUpTo(AppScreen.Favorite.name) { inclusive = true }
                }
            }
        }

        // Payment Screens
        composable(AppScreen.PaymentMethod.name) {
            PaymentMethodScreen(
                onBackClick = { navController.popBackStack() },
                onEdahabiaClick = { 
                    // Extract car and reservation ID from booking view model
                    val carId = sharedBookingViewModel.carId
                    // Navigate to Edahabia screen with reservation ID
                    navController.navigate(AppScreen.Edahabia.name)
                },
                onCashClick = { 
                    // Navigate to Payment Pending when Cash payment option is selected
                    navController.navigate(AppScreen.PaymentPending.name)
                },
                viewModel = sharedBookingViewModel
            )
        }

        composable(AppScreen.Edahabia.name) {
            EdahabiaScreen(
                onBackClick = { navController.popBackStack() },
                onContinueClick = { 
                    // Navigate to Bill screen
                    navController.navigate(AppScreen.Bill.name)
        }
                // No need to explicitly pass BookingViewModel as it's provided via viewModel()
                // and PaymentViewModel is injected by Hilt
            )
        }

        composable(AppScreen.Bill.name) {
            BillScreen(
                onBackClick = { navController.popBackStack() },
                onContinueClick = { 
                    // Navigate to PaymentDone or UnsuccessfulPayment based on payment result
                    // For demo purposes, we'll always go to PaymentDone
                    navController.navigate(AppScreen.PaymentDone.name)
                },
                viewModel = sharedBookingViewModel,
                reservationViewModel = hiltViewModel()
            )
        }

        composable(
            route = "${AppScreen.Cancelation.name}?reservationId={reservationId}",
            arguments = listOf(
                navArgument("reservationId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val reservationId = backStackEntry.arguments?.getLong("reservationId") ?: 0L
            CancelationScreen(
                onBackClick = { navController.popBackStack() },
                reservationId = reservationId
            )
        }

        // Profile Screens
        composable(AppScreen.Settings.name) {
            SettingsScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppScreen.NotificationSettings.name) {
            NotificationSettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppScreen.HelpCenter.name) {
            HelpCenterScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppScreen.PrivacyPolicy.name) {
            PrivacyPolicyScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppScreen.Logout.name) {
            logoutScreen(
                navController = navController,
                onHomeClick = { navController.navigateAndClear(AppScreen.Home.name) },
                onBookingsClick = { navController.navigate(AppScreen.MyBooking.name) },
                onFavoriteClick = { navController.navigate(AppScreen.Favorites.name) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppScreen.ProfileGeneral.name) {
            UpdateProfileScreen(
                navController = navController
            )
        }

        composable(AppScreen.ProfileLocation.name) {
            UpdateProfileLocationScreen(
                navController = navController
            )
        }

        composable(AppScreen.PasswordManager.name) {
            ChangePasswordScreen(
                onBackClick = { navController.popBackStack() },
                onPasswordChangeSuccess = { navController.popBackStack() }
            )
        }

        // CarBookingScreen route (with/without driver selection) - updated to accept carId
        composable(
            route = "${AppScreen.CarBooking.name}/{carId}",
            arguments = listOf(navArgument("carId") { 
                type = NavType.StringType 
                defaultValue = ""
                nullable = true
            })
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId")
            
            CarBookingScreen(
                carId = carId,
                onBackPressed = { navController.popBackStack() },
                onContinue = {
                    // Navigate to CompleteYourBookingScreen now
                    Log.d("NavGraph", "CarBookingScreen: Continue clicked, navigating to CompleteYourBooking")
                    navController.navigate(AppScreen.CompleteYourBooking.name)
                },
                bookingViewModel = sharedBookingViewModel,
                reservationViewModel = hiltViewModel()
            )
        }
        
        // Add the CompleteYourBookingScreen route back
        composable(AppScreen.CompleteYourBooking.name) {
            CompleteYourBookingScreen(
                onBackPressed = { navController.popBackStack() },
                onContinue = { 
                    // Navigate to payment method screen
                    Log.d("NavGraph", "CompleteYourBookingScreen: Continue clicked, navigating to PaymentMethod")
                    navController.navigate(AppScreen.PaymentMethod.name)
                },
                bookingViewModel = sharedBookingViewModel
            )
        }

        composable(AppScreen.PaymentPending.name) {
            PaymentPending(
                onBackToHomeClick = { navController.navigateAndClear(AppScreen.Home.name) }
            )
        }

        composable(AppScreen.PaymentDone.name) {
            PaymentDoneScreen(
                onBackToHomeClick = { 
                    // Navigate back to home screen and clear back stack
                    navController.navigate(AppScreen.Home.name) {
                        popUpTo(AppScreen.Home.name) { inclusive = true }
                    }
                }
            )
        }

        composable(AppScreen.UnsuccessfulPayment.name) {
            UnsuccessfulPaymentScreen(
                onTryAgainClick = {
                    // Go back to previous screen to try again 
                    navController.popBackStack()
                },
                onCancelClick = {
                    // Navigate back to home screen and clear back stack
                    navController.navigate(AppScreen.Home.name) {
                        popUpTo(AppScreen.Home.name) { inclusive = true }
                    }
                }
            )
        }

        // Favorite Screen
        composable(AppScreen.Favorites.name) {
            FavoritesScreen(
                onBackClick = { navController.popBackStack() },
                onCarClick = { carId -> 
                    navController.navigate("${AppScreen.CarDetails.name}/$carId")
                }
            )
        }
    }
}

/**
 * Extension function to help with navigation and clearing the back stack
 * Use this when you want to navigate to a screen and remove all previous screens from history
 */
fun NavHostController.navigateAndClear(route: String) {
    navigate(route) {
        popUpTo(0) {
            inclusive = true
        }
    }
} 