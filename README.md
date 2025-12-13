SmartWallet
Autor: Ahian Quesada Guadamuz
Curso: Desarrollo de Aplicaciones para Dispositivos Móviles
Profesor: Ever Iván Barahona
Lenguaje: Kotlin
IDE: Android Studio

Descripción:
SmartWallet es una aplicación móvil diseñada para ofrecer un control de gastos simple, eficiente y visual. Permite al usuario definir un saldo inicial de una "billetera" y registrar todos sus egresos, ofreciendo una visión clara y en tiempo real de su dinero disponible. El principal objetivo es facilitar la gestión financiera personal y el seguimiento de cada transacción.

Funcionalidades Clave:
Esta aplicación cumple con todos los requisitos del proyecto, ofreciendo las siguientes funcionalidades:

Gestión de Transacciones (CRUD Completo):
Crear: Registro de nuevos gastos (monto y descripción).
Leer: Visualización del Historial de gastos en la pantalla principal.
Actualizar (Editar): Opción para modificar la descripción o el monto de un gasto existente.
Eliminar: Permite borrar transacciones del historial.

Control y Visualización:
Saldo Dinámico: Muestra el saldo actual de la billetera.
Alerta Visual de Saldo: El saldo cambia de color (Verde/Amarillo/Rojo) según el monto disponible.
Restablecer Billetera: Opción en configuración para borrar el historial y definir un nuevo saldo inicial.

Requisitos Adicionales del Proyecto:
Listas Personalizadas (Búsqueda): Función de búsqueda (Lupa) en la pantalla principal para filtrar el historial por descripción, monto o fecha.
Imágenes/Video: Al registrar un gasto, el usuario puede adjuntar una imagen del comprobante o factura, usando la Cámara o seleccionándola desde la Galería.
Diálogos: Implementación de diálogos de confirmación antes de eliminar una transacción o restablecer la billetera.

Mockups [(imagen)](/mockup.png)

Tecnologías:
Lenguaje Principal: Kotlin
IDE: Android Studio, Android 13
Persistencia de Datos: [MySQLITE]
Interfaz de Usuario: Material Design
Listas: RecyclerView
Imágenes: ContentResolver / Intent para Cámara y Galería

Estructura Inicial (prevista):
MainActivity.kt: Pantalla principal y controlador del saldo.
NewExpenseActivity.kt: Formulario para registrar nuevos gastos (CRUD: Create/Update).
SettingsActivity.kt: Manejo de saldo inicial y opción de restablecimiento.
ExpenseAdapter.kt: Adaptador del RecyclerView para el Historial de gastos.
ExpenseDatabase.kt / ExpenseDao.kt: Definición de la base de datos y métodos de acceso.
Expense.kt: Clase de datos (Modelo) de la transacción (monto, descripción, fecha, ruta de imagen).

Link de Endpoints de API-WALLET: (https://ahianquesadaguadamuz-6940486.postman.co/workspace/Ahian-Quesada's-Workspace~3888e9c9-ad91-44c1-8dd2-dbcd70160762/collection/50495192-9dd6ab35-cc8a-4040-80e8-eb72e5c8469e?action=share&creator=50495192&live=65bfvyrrr9)

Documentación: https://documenter.getpostman.com/view/50495192/2sB3dSRpKw
