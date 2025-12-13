## WalletTracker

Una aplicación **Android** para gestionar tus finanzas personales de forma **simple, visual y siempre sincronizada**.

---

## 📱 Descripción

**WalletTracker** es una app diseñada para llevar un control claro y rápido de tus **ingresos y gastos diarios**. Toda la información se almacena en la nube, por lo que tus datos están siempre disponibles y seguros. La misma esta montada sobre **RENDER** como backend gratuito.

---

##  Características principales

* Registro de **gastos** con opción de adjuntar **foto del recibo**
* Registro de **ingresos** (salarios, ventas, regalos, etc.)
* **Lista única de movimientos**:

  * Ingresos en **verde** con signo `+`
  * Gastos en **rojo** con signo `-`
* **Edición y eliminación** individual de cualquier movimiento
* **Cambio de moneda**:

  * Colones (CRC)
  * Dólares (USD)
  * Euros (EUR)
* **Saldo actual** siempre visible en la pantalla principal
* **Onboarding** que aparece solo la primera vez para definir el saldo inicial
* **Búsqueda rápida** de movimientos
* Opción de **borrar todos los movimientos manteniendo el saldo**
* Diseño moderno basado en **Material Design 3**

---

## 🛠️ Tecnologías utilizadas

### Frontend (Android – Kotlin)

* ViewBinding
* Coroutines + `lifecycleScope`
* RecyclerView con **swipe-to-delete**
* Material Components:

  * Toolbar
  * CardView
  * FloatingActionButton
  * TextInputLayout
* OkHttp para comunicación con la API
* SharedPreferences para detectar la **primera ejecución**

### Backend

* Node.js con Express
* Almacenamiento en archivo **JSON** (`expenses.json`)
* Desplegado en **Render** como servicio gratuito
* API **RESTful** para todas las operaciones

---

## 🗂️ Arquitectura de datos

Todos los movimientos (**gastos e ingresos**) se almacenan en **una única lista**:

* **Gastos**: `amount` positivo
* **Ingresos**: `amount` negativo + campo `isIncome: true`

Este enfoque permite mantener **una sola fuente de verdad** y simplificar la lógica del cálculo del saldo.

---

## 🌐 API – Endpoints

**Base URL:**

```
https://wallet-api-m312.onrender.com
```

| Método | Endpoint               | Descripción                        |
| ------ | ---------------------- | ---------------------------------- |
| GET    | `/`                    | Mensaje de bienvenida              |
| GET    | `/balance`             | Obtiene el saldo actual            |
| GET    | `/expenses`            | Lista completa de movimientos      |
| POST   | `/initial-balance`     | Establece el saldo inicial         |
| POST   | `/add-income`          | Añade un ingreso                   |
| POST   | `/expenses`            | Añade un gasto (con o sin foto)    |
| PUT    | `/expenses/{id}`       | Edita un movimiento                |
| DELETE | `/expenses/{id}`       | Elimina un movimiento              |
| DELETE | `/reset-expenses-only` | Borra movimientos (mantiene saldo) |
| DELETE | `/reset`               | Borra todo (saldo + movimientos)   |

---

## ✅ Requisitos

* Android **7.0** (API 24) o superior
* Conexión a internet

---

## 🚀 Instalación

1. Clona el repositorio:

   ```bash
   git clone https://github.com/Project-Mobile-Devices-Ahian-Quesada/WalletTracker.git
   ```
2. Abre el proyecto en **Android Studio**
3. Sincroniza **Gradle**
4. Ejecuta la app en un dispositivo físico o emulador

---

## 📁 Estructura del proyecto

```
app/
├── src/main/java/com/example/smartwallet/
│   ├── MainActivity.kt
│   ├── AddEditExpenseActivity.kt
│   ├── AddIncomeActivity.kt
│   ├── OnboardingActivity.kt
│   ├── SettingsActivity.kt
│   ├── Controller/
│   │   └── WalletController.kt
│   ├── Entity/
│   │   └── Expense.kt
│   ├── Util/
│   │   ├── CurrencyHelper.kt
│   │   └── FirstTimeHelper.kt
│   └── adapter/
│       └── ExpenseAdapter.kt
└── res/
    ├── layout/        
    ├── menu/
    └── values/
        └── strings.xml
```

---


---

## 👤 Autor

Desarrollado con pasión por **[Ahian Quesada]**.

---

## 🙌 Agradecimientos

¡Gracias por usar **WalletTracker**!

Una herramienta simple para mantener tus finanzas bajo control.

**¡Controla tu dinero, vive mejor! 💰**

Link de video demostrativo: https://screenrec.com/share/CMAVEBSlzZ