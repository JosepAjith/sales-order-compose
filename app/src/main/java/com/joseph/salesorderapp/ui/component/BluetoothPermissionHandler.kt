package com.joseph.salesorderapp.ui.component

import android.Manifest.permission
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
import android.content.pm.PackageManager
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BluetoothPermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    val permission = permission.BLUETOOTH_CONNECT

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) onPermissionGranted() else onPermissionDenied()
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) launcher.launch(permission)
            else onPermissionGranted()
        } else {
            onPermissionGranted()
        }
    }
}
