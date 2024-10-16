# GameStation 🎮

### Descripción
**GameStation** es una aplicación móvil desarrollada en **Kotlin** para la plataforma Android, enfocada en la gestión y experiencia de los usuarios de videojuegos. 

### Tecnologías Utilizadas
- **Kotlin**: Lenguaje principal para el desarrollo de la aplicación móvil, aprovechando su compatibilidad con Android y su capacidad de crear código conciso y seguro.
- **Jetpack Compose**: Utilizado para construir una interfaz de usuario declarativa y moderna, simplificando el desarrollo y mejorando la escalabilidad del diseño.
- **Android Studio**: Entorno de desarrollo utilizado para la creación de la app y la compilación de APKs para dispositivos Android.
- **SQLite con DAO**: Implementación de **DAO (Data Access Object)** para gestionar el acceso a la base de datos SQLite, proporcionando una abstracción clara entre la base de datos y la lógica de la aplicación.
- **API REST**: La aplicación se conecta a una API externa para obtener información.
- **Material Design**: Implementado junto con Jetpack Compose para ofrecer una interfaz limpia y moderna, mejorando la experiencia de usuario.

### Funcionalidades Clave 
1. **Login y Registro de Usuarios**:
   - La aplicación permite a los usuarios **registrarse** proporcionando su nombre, correo electrónico y una contraseña. 
   - Los usuarios también pueden iniciar sesión una vez registrados para acceder a todas las funcionalidades de la aplicación.

2. **Inicio y Gestión de Productos**:
   - Después de iniciar sesión, los usuarios son dirigidos a la pantalla de inicio, donde pueden **revisar los productos** que han añadido o **añadir nuevos productos**. Los productos incluyen un nombre, descripción y precio.
   - Se puede ver una lista de productos añadidos, con la posibilidad de eliminarlos si ya no son necesarios.

3. **Carrito de Compras**:
   - La aplicación ofrece un **carrito de compras** para que los usuarios puedan seleccionar varios productos y ver el total de su compra.
   - Los usuarios pueden proceder a realizar la compra o cancelar los productos seleccionados.

4. **Edición de Perfil**:
   - Los usuarios pueden acceder a la pantalla de **edición de perfil**, donde pueden modificar su información personal como nombre y contraseña.
  

### Beneficios de la Aplicación
- **Interfaz Moderna con Jetpack Compose**: La aplicación utiliza **Jetpack Compose** para construir una interfaz declarativa, proporcionando un diseño flexible, dinámico y fácil de mantener.
- **Gestión de Datos con DAO**: El uso de **DAO** asegura una separación clara entre la lógica de acceso a la base de datos y la lógica de la aplicación, mejorando la mantenibilidad y la escalabilidad de la app.
- **Optimización**: El uso de Kotlin, Jetpack Compose y DAO asegura que la app sea eficiente en el consumo de recursos, optimizando tanto el rendimiento como la duración de la batería.

### Conclusión
**GameStation** es una aplicación completa para los amantes de los videojuegos, permitiendo a los usuarios gestionar su biblioteca, añadir nuevos productos, realizar compras y actualizar su información personal. 
La integración con **Jetpack Compose** para la interfaz de usuario, el uso de **DAO** para la gestión de la base de datos, junto con APIs externas, asegura una experiencia fluida y continua, manteniendo a los jugadores conectados con su pasión en todo momento.
