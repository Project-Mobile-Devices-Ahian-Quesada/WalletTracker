package com.example.smartwallet

import Controller.WalletController
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwallet.databinding.ActivityAgregarGastoBinding
import android.Manifest
import androidx.core.content.FileProvider
import java.io.File
import android.widget.Toast

class AgregarGasto : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarGastoBinding
    private lateinit var controller: WalletController
    private var imageUri: Uri? = null
    private var photoUri: Uri? = null

    // Launcher para CÁMARA
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && photoUri != null) {
            imageUri = photoUri
            binding.imgPreview.setImageURI(photoUri)
            binding.imgPreview.visibility = android.view.View.VISIBLE
        }
    }

    // Launcher para GALERÍA
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            binding.imgPreview.setImageURI(it)
            binding.imgPreview.visibility = android.view.View.VISIBLE
        }
    }

    // Launcher para PERMISO
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(this, "Permiso denegado para acceder a la galería", Toast.LENGTH_SHORT).show()
        }
    }

    // Crea URI temporal
    private fun createPhotoUri(): Uri {
        val file = File(externalCacheDir, "photo_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(this, "com.example.smartwallet.provider", file)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarGastoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = WalletApp.getController(this)

        // BOTÓN GALERÍA
        binding.floatingActionButton.setOnClickListener {
            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }

        // BOTÓN CÁMARA
        binding.floatingActionButton4.setOnClickListener {
            photoUri = createPhotoUri()
            cameraLauncher.launch(photoUri!!)
        }

        // BOTÓN GUARDAR
        binding.btnGuardarGasto.setOnClickListener {
            val desc = binding.txtdescripcionAddgasto.text.toString().trim()
            val monto = binding.txtmontoAddgasto.text.toString().toDoubleOrNull() ?: 0.0
            if (desc.isEmpty()) {
                Toast.makeText(this, "Ingresa una descripción", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (monto <= 0) {
                Toast.makeText(this, "Ingresa un monto válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            controller.addExpense(desc, monto, imageUri)
            Toast.makeText(this, "Gasto agregado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}