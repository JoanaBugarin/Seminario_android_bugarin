# Instrucciones de Configuración - VideojuegosApp

## Pasos para Configurar y Ejecutar la Aplicación

### 1. Configurar API Key de RAWG

**IMPORTANTE**: Antes de ejecutar la aplicación, debes configurar tu API key de RAWG.

1. Ve a [https://rawg.io/apidocs](https://rawg.io/apidocs)
2. Regístrate para obtener una API key gratuita
3. Abre el archivo `app/src/main/java/com/seminario/videojuegosapp/di/NetworkModule.kt`
4. Busca la línea:
   ```kotlin
   private const val API_KEY = "YOUR_API_KEY_HERE"
   ```
5. Reemplaza `"YOUR_API_KEY_HERE"` con tu API key real:
   ```kotlin
   private const val API_KEY = "tu_api_key_aqui"
   ```

### 2. Configurar Android Studio

1. Abre Android Studio
2. Selecciona "Open an existing project"
3. Navega a la carpeta del proyecto y selecciónala
4. Espera a que Android Studio sincronice las dependencias

### 3. Configurar el Emulador o Dispositivo

#### Opción A: Emulador
1. Ve a Tools > Device Manager
2. Crea un nuevo Virtual Device
3. Selecciona un dispositivo con API level 24 o superior
4. Descarga y selecciona una imagen del sistema (recomendado: API 34)

#### Opción B: Dispositivo Físico
1. Habilita "Opciones de desarrollador" en tu dispositivo Android
2. Habilita "Depuración USB"
3. Conecta tu dispositivo por USB

### 4. Ejecutar la Aplicación

1. Haz clic en el botón "Run"
2. La aplicación se compilará e instalará automáticamente

## Características de la Aplicación

### Pantalla Principal
- **Lista de Videojuegos**: Muestra videojuegos populares con paginación automática
- **Búsqueda**: Campo de búsqueda para encontrar videojuegos específicos
- **Tabs**: Cambia entre "Lista de Videojuegos" y "Lista de Deseados"
- **Filtros**: Botón para acceder a la pantalla de filtros

### Pantalla de Filtros
- **Plataformas**: Filtra por consolas/dispositivos
- **Géneros**: Filtra por tipo de juego
- **Ordenamiento**: Ordena por nombre, fecha, rating, etc.
- **Persistencia**: Los filtros se guardan automáticamente

### Pantalla de Detalles
- **Información Completa**: Nombre, imagen, descripción, rating, etc.
- **Lista de Deseados**: Agregar/quitar juegos favoritos
- **Compartir**: Compartir información del juego
- **Sitio Web**: Abrir la página oficial del juego

### Lista de Deseados
- **Persistencia Local**: Los juegos se guardan en la base de datos local
- **Navegación**: Toca un juego para ver sus detalles
- **Eliminación**: Botón para quitar juegos de la lista

## Solución de Problemas

### Error de API Key
```
Error: Invalid API key
```
**Solución**: Verifica que hayas configurado correctamente tu API key en `NetworkModule.kt`

### Error de Red
```
Error del servidor: 401/403
```
**Solución**: 
- Verifica tu conexión a internet
- Asegúrate de que tu API key sea válida
- Verifica que no hayas excedido el límite de requests

### Error de Compilación
```
Could not find method implementation()
```
**Solución**: 
- Sincroniza el proyecto con Gradle Files
- Ve a File > Sync Project with Gradle Files

### La aplicación no muestra videojuegos
**Solución**:
- Verifica que tu API key esté configurada correctamente
- Asegúrate de tener conexión a internet
- Verifica que la API de RAWG esté funcionando

## Arquitectura Técnica

### Patrones Utilizados
- **MVVM**: Model-View-ViewModel para separación de responsabilidades
- **Repository Pattern**: Abstracción de la capa de datos
- **Dependency Injection**: Hilt para inyección de dependencias

### Tecnologías
- **Kotlin**: Lenguaje de programación principal
- **Jetpack Compose**: Framework de UI moderno
- **Retrofit**: Cliente HTTP para APIs
- **Room**: Base de datos local
- **Paging 3**: Paginación eficiente
- **DataStore**: Almacenamiento de preferencias
- **Coil**: Carga de imágenes

## Estructura de Archivos

```
app/src/main/java/com/seminario/videojuegosapp/
├── data/
│   ├── api/           # Servicios de red (Retrofit)
│   ├── local/         # Base de datos local (Room)
│   ├── model/         # Modelos de datos
│   ├── paging/        # Paginación
│   ├── preferences/   # Preferencias (DataStore)
│   └── repository/    # Repository Pattern
├── di/                # Módulos de inyección de dependencias
├── ui/
│   ├── components/    # Componentes reutilizables de UI
│   ├── navigation/    # Sistema de navegación
│   ├── screens/       # Pantallas principales
│   ├── theme/         # Temas y estilos
│   └── viewmodel/     # ViewModels
└── VideojuegosApplication.kt
```

## Evaluación del Proyecto

Este proyecto demuestra:
- Uso correcto de patrones MVVM y Repository
- Manejo robusto de errores y estados de carga
- UI intuitiva con mensajes de error apropiados
- Implementación de múltiples desafíos opcionales
- Código limpio y bien estructurado
- Uso de tecnologías modernas de Android

## Contacto

Si tienes problemas con la configuración o ejecución de la aplicación, revisa:
1. Que todas las dependencias estén sincronizadas
2. Que tu API key de RAWG sea válida
3. Que tengas conexión a internet
4. Que tu dispositivo/emulador tenga API level 24 o superior

