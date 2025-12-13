package com.example.smartwallet

import Controller.WalletController
import Entity.Expense
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.smartwallet.databinding.ActivityAddExpenseBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

/**
 * Activity para añadir o editar un GASTO
 * - Soporta foto desde cámara o galería
 * - Permite cambiar entre gasto e ingreso desde la toolbar
 */
class AddEditExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExpenseBinding
    private lateinit var controller: WalletController

    private var currentBitmap: Bitmap? = null
    private var editingExpense: Expense? = null

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            currentBitmap = it
            binding.ivPhoto.setImageBitmap(it)
            binding.ivPhoto.visibility = View.VISIBLE
        }
    }

    // GALERÍA SIMPLE
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = android.provider.MediaStore.Images.Media.getBitmap(contentResolver, uri)
            currentBitmap = bitmap
            binding.ivPhoto.setImageBitmap(bitmap)
            binding.ivPhoto.visibility = View.VISIBLE
        }
    }
    private val requestCameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            cameraLauncher.launch(null)
        } else {
            Snackbar.make(binding.root, "Se necesita permiso de cámara", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = WalletController(this)
        editingExpense = intent.getSerializableExtra("expense") as? Expense

        setupToolbar()
        loadExpenseDataIfEditing()
        setupPhotoButtons()
        setupSaveButton()
    }

    /** Configura la toolbar con título y botón de volver */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (editingExpense != null)
            getString(R.string.edit_expense_title)
        else
            getString(R.string.add_expense_title)
    }

    /** Si estamos editando, carga los datos del gasto */
    private fun loadExpenseDataIfEditing() {
        editingExpense?.let { expense ->
            binding.etDescription.setText(expense.description)
            binding.etAmount.setText(expense.amount.toString())

            expense.getPhotoBitmap()?.let { bitmap ->
                currentBitmap = bitmap
                binding.ivPhoto.setImageBitmap(bitmap)
                binding.ivPhoto.visibility = View.VISIBLE
            }

            binding.btnSave.text = getString(R.string.save_changes)
        }
    }

    /** Configura los botones de cámara y galería */
    private fun setupPhotoButtons() {
        binding.btnCamera.setOnClickListener {
            // PIDE PERMISO ANTES DE ABRIR CÁMARA
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(null)
            } else {
                requestCameraPermission.launch(Manifest.permission.CAMERA)
            }
        }

        binding.btnGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
    }

    /** Valida y guarda el gasto (nuevo o editado) */
    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val description = binding.etDescription.text.toString().trim()
            val amountText = binding.etAmount.text.toString().trim()

            if (description.isEmpty() || amountText.isEmpty()) {
                Snackbar.make(binding.root, R.string.empty_fields, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                Snackbar.make(binding.root, R.string.invalid_amount, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    if (editingExpense != null) {
                        editingExpense!!.apply {
                            this.description = description
                            this.amount = amount
                            setPhotoFromBitmap(currentBitmap)
                        }
                        controller.updateExpense(editingExpense!!)
                    } else {
                        controller.addExpense(description, amount, currentBitmap)
                    }
                    finish()
                } catch (e: Exception) {
                    Snackbar.make(binding.root, R.string.network_error, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    /** Infla el menú con los botones (Gasto) (Ingreso) */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_type, menu)
        return true
    }

    /** Maneja la navegación entre gasto e ingreso */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { finish(); true }
            R.id.action_expense -> true
            R.id.action_income -> {
                startActivity(Intent(this, AddIncomeActivity::class.java).apply {
                    putExtra("expense", editingExpense?.copy(isIncome = true))
                })
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}