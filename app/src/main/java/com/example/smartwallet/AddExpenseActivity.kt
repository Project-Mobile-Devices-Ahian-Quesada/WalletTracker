package com.example.smartwallet

import Controller.WalletController
import Entity.Expense
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwallet.databinding.ActivityAddExpenseBinding
import com.google.android.material.snackbar.Snackbar

class AddEditExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExpenseBinding
    private lateinit var controller: WalletController
    private var currentBitmap: Bitmap? = null
    private var editingExpense: Expense? = null

    // Cámara: toma foto pequeña (preview)
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            currentBitmap = it
            binding.ivPhoto.setImageBitmap(it)
            binding.ivPhoto.visibility = android.view.View.VISIBLE
        }
    }

    // Galería: elige imagen
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            currentBitmap = bitmap
            binding.ivPhoto.setImageBitmap(bitmap)
            binding.ivPhoto.visibility = android.view.View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = WalletController(this)
        editingExpense = intent.getSerializableExtra("expense") as? Expense

        if (editingExpense != null) {
            binding.etDescription.setText(editingExpense!!.Description)
            binding.etAmount.setText(editingExpense!!.Amount.toString())
            currentBitmap = editingExpense!!.PhotoBitmap
            if (currentBitmap != null) {
                binding.ivPhoto.setImageBitmap(currentBitmap)
                binding.ivPhoto.visibility = android.view.View.VISIBLE
            }
            binding.btnSave.text = "Guardar cambios"
        }

        binding.btnCamera.setOnClickListener {
            cameraLauncher.launch(null)
        }

        binding.btnGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            val desc = binding.etDescription.text.toString().trim()
            val amountText = binding.etAmount.text.toString()

            if (desc.isEmpty() || amountText.toDoubleOrNull() == null) {
                Snackbar.make(binding.root, "Completa los campos", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDouble()

            if (editingExpense != null) {
                // Editar
                editingExpense!!.Description = desc
                editingExpense!!.Amount = amount
                editingExpense!!.PhotoBitmap = currentBitmap
            } else {
                // Nuevo
                val expense = Expense(
                    description = desc,
                    amount = amount,
                    photoBitmap = currentBitmap
                )
                controller.addExpense(expense.Description, expense.Amount, expense.PhotoBitmap)
            }
            finish()
        }
    }
}