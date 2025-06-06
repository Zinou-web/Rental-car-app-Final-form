package com.example.myapplication.ui.screens.payment

// Removed AnimatedVisibility import
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteYourBookingScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val topPadding = with(LocalDensity.current) {
        WindowInsets.statusBars.getTop(this).toDp()
    }

    // Form state
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedWilaya by remember { mutableStateOf("") }
    var hasDriverLicense by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Form validation state
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var wilayaError by remember { mutableStateOf<String?>(null) }
    var driverLicenseError by remember { mutableStateOf<String?>(null) }

    // Validation functions
    fun validateFullName(): Boolean {
        return if (fullName.isBlank()) {
            fullNameError = "Name is required"
            false
        } else if (fullName.length < 3) {
            fullNameError = "Name is too short"
            false
        } else {
            fullNameError = null
            true
        }
    }

    fun validatePhoneNumber(): Boolean {
        return if (phoneNumber.isBlank()) {
            phoneNumberError = "Phone number is required"
            false
        } else if (!phoneNumber.matches(Regex("^\\+?[0-9]{10,13}$"))) {
            phoneNumberError = "Enter a valid phone number"
            false
        } else {
            phoneNumberError = null
            true
        }
    }

    fun validateEmail(): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return if (email.isBlank()) {
            emailError = "Email is required"
            false
        } else if (!email.matches(emailRegex)) {
            emailError = "Enter a valid email address"
            false
        } else {
            emailError = null
            true
        }
    }

    fun validateWilaya(): Boolean {
        return if (selectedWilaya.isBlank()) {
            wilayaError = "Please select a wilaya"
            false
        } else {
            wilayaError = null
            true
        }
    }

    fun validateDriverLicense(): Boolean {
        return if (!hasDriverLicense) {
            driverLicenseError = "Driver license is required"
            false
        } else {
            driverLicenseError = null
            true
        }
    }

    fun validateForm(): Boolean {
        val nameValid = validateFullName()
        val phoneValid = validatePhoneNumber()
        val emailValid = validateEmail()
        val wilayaValid = validateWilaya()
        val licenseValid = validateDriverLicense()

        return nameValid && phoneValid && emailValid && wilayaValid && licenseValid
    }

    // Available wilayas
    val wilayas = listOf("Algiers", "Oran", "Blida", "Setif", "Constantine", "Annaba", "Tlemcen", "Batna")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F5FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 30.dp)
                ) {
                    // Back button
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(45.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFFFFF))
                            .clickable { onBackClick() }
                    ) {
                        IconButton(
                            onClick = { onBackClick() },
                            modifier = Modifier.size(45.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    // Title "My Bookings" at the center
                    Text(
                        text = "My Bookings",
                        fontSize = 23.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Form Section Title
                Card(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Text(
                        text = "Renter Information",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name Field
                Text(
                    text = "Full Name",
                    fontSize = 16.sp,
                    fontFamily = poppins,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { 
                        fullName = it
                        if (fullNameError != null) validateFullName()
                    },
                    placeholder = { 
                        Text(
                            text = "Enter your full name",
                            fontFamily = poppins,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.Gray
                        ) 
                    },
                    isError = fullNameError != null,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF149459),
                        unfocusedBorderColor = Color.LightGray,
                        errorBorderColor = Color.Red,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                if (fullNameError != null) {
                    Text(
                        text = fullNameError ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Phone Number Field
                Text(
                    text = "Phone Number",
                    fontSize = 16.sp,
                    fontFamily = poppins,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { 
                        phoneNumber = it
                        if (phoneNumberError != null) validatePhoneNumber()
                    },
                    placeholder = { 
                        Text(
                            text = "Enter your phone number",
                            fontFamily = poppins,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.Gray
                        ) 
                    },
                    leadingIcon = {
                        Text(
                            text = "+213 |",
                            fontFamily = poppins,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    },
                    isError = phoneNumberError != null,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF149459),
                        unfocusedBorderColor = Color.LightGray,
                        errorBorderColor = Color.Red,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    )
                )

                if (phoneNumberError != null) {
                    Text(
                        text = phoneNumberError ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Email Field
                Text(
                    text = "Email",
                    fontSize = 16.sp,
                    fontFamily = poppins,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        if (emailError != null) validateEmail()
                    },
                    placeholder = { 
                        Text(
                            text = "Example@gmail.com",
                            fontFamily = poppins,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.Gray
                        ) 
                    },
                    isError = emailError != null,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF149459),
                        unfocusedBorderColor = Color.LightGray,
                        errorBorderColor = Color.Red,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                if (emailError != null) {
                    Text(
                        text = emailError ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Wilaya Dropdown
                Text(
                    text = "Wilaya",
                    fontSize = 16.sp,
                    fontFamily = poppins,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedWilaya,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { 
                            Text(
                                text = "Select your wilaya",
                                fontFamily = poppins,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Color.Gray
                            ) 
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .menuAnchor(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF149459),
                            unfocusedBorderColor = Color.LightGray,
                            errorBorderColor = Color.Red,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        isError = wilayaError != null
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        wilayas.forEach { wilaya ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = wilaya,
                                        fontFamily = poppins,
                                        fontSize = 14.sp
                                    )
                                },
                                onClick = {
                                    selectedWilaya = wilaya
                                    expanded = false
                                    wilayaError = null
                                }
                            )
                        }
                    }
                }

                if (wilayaError != null) {
                    Text(
                        text = wilayaError ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Driver License Upload
                Text(
                    text = "Driver License",
                    fontSize = 16.sp,
                    fontFamily = poppins,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (driverLicenseError != null) Color(0xFFFFEDED) else Color.White
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = if (driverLicenseError != null) {
                            androidx.compose.ui.graphics.SolidColor(Color.Red)
                        } else {
                            androidx.compose.ui.graphics.SolidColor(Color.LightGray)
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                hasDriverLicense = true
                                driverLicenseError = null
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = if (hasDriverLicense) "License_scan.pdf" else "Scan your driver license",
                                fontFamily = poppins,
                                fontSize = 14.sp
                            )
                            if (hasDriverLicense) {
                                Text(
                                    text = "Uploaded successfully",
                                    fontFamily = poppins,
                                    fontSize = 12.sp,
                                    color = Color(0xFF149459)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (hasDriverLicense) Color(0xFF149459) else Color(0xFFE0E0E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Upload",
                                tint = if (hasDriverLicense) Color.White else Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                if (driverLicenseError != null) {
                    Text(
                        text = driverLicenseError ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Bottom button fixed to the bottom
            Button(
                onClick = {
                    if (validateForm()) {
                        isSubmitting = true
                        onContinueClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF149459),
                    disabledContainerColor = Color(0xFFABD6C2)
                ),
                enabled = !isSubmitting
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (!isSubmitting) {
                        Text(
                            text = "Continue",
                            fontSize = 18.sp,
                            fontFamily = poppins,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    } else {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompleteYourBookingScreenPreview() {
    CompleteYourBookingScreen(
        onBackClick = {},
        onContinueClick = {}
    )
} 