# VideojuegosApp - Aplicación Android con Kotlin

Una aplicación Android desarrollada con Kotlin que permite buscar y explorar videojuegos utilizando la API de RAWG.

## Características Implementadas

### Enunciado Principal ✅
- Pantalla principal con lista de videojuegos
- Pantalla de filtros (plataforma, género, ordenamiento)
- Indicadores de carga y manejo de errores
- Navegación entre pantallas

### Desafíos Opcionales ✅
- **Detalles de videojuego**: Pantalla completa con información detallada
- **Guardar filtros**: Los filtros se guardan automáticamente usando DataStore
- **Búsqueda por título**: Campo de búsqueda en la pantalla principal
- **Paginado de resultados**: Implementado con Paging 3
- **Lista de deseados**: Sistema completo con tabs y base de datos local
- **UI adaptativa**: Soporte para orientación landscape y modo oscuro
- **Interacción con otras apps**: Compartir juegos y abrir sitios web

## Configuración

### 1. Obtener API Key de RAWG

1. Ve a [RAWG API](https://rawg.io/apidocs)
2. Regístrate para obtener una API key gratuita
3. Copia tu API key

### 2. Configurar la API Key

Abre el archivo `app/src/main/java/com/seminario/videojuegosapp/di/NetworkModule.kt` y reemplaza:

```kotlin
private const val API_KEY = "YOUR_API_KEY_HERE"
```

Con tu API key real:

```kotlin
private const val API_KEY = "tu_api_key_aqui"
```

### 3. Compilar y Ejecutar

1. Abre el proyecto en Android Studio
2. Sincroniza las dependencias de Gradle
3. Ejecuta la aplicación en un dispositivo o emulador

## Arquitectura

La aplicación sigue los principios de **MVVM** y **Repository Pattern**:

- **Model**: Clases de datos para la API y base de datos local
- **View**: Pantallas de Compose UI
- **ViewModel**: Lógica de presentación y estado
- **Repository**: Capa de abstracción para datos
- **API**: Servicios de red con Retrofit
- **Database**: Base de datos local con Room
- **Preferences**: Configuración con DataStore

## Tecnologías Utilizadas

- **Kotlin** - Lenguaje principal
- **Jetpack Compose** - UI moderna
- **MVVM** - Patrón de arquitectura
- **Repository Pattern** - Gestión de datos
- **Hilt** - Inyección de dependencias
- **Retrofit** - Cliente HTTP
- **Room** - Base de datos local
- **Paging 3** - Paginación eficiente
- **DataStore** - Preferencias modernas
- **Coil** - Carga de imágenes
- **Navigation Compose** - Navegación

## Estructura del Proyecto

```
app/src/main/java/com/seminario/videojuegosapp/
├── data/
│   ├── api/           # Servicios de red
│   ├── local/         # Base de datos Room
│   ├── model/         # Modelos de datos
│   ├── paging/        # PagingSource
│   ├── preferences/   # DataStore
│   └── repository/    # Repository Pattern
├── di/                # Módulos de Hilt
├── ui/
│   ├── components/    # Componentes reutilizables
│   ├── navigation/    # Sistema de navegación
│   ├── screens/       # Pantallas principales
│   ├── theme/         # Temas y colores
│   └── viewmodel/     # ViewModels
└── VideojuegosApplication.kt
```

## Funcionalidades

### Pantalla Principal
- Lista paginada de videojuegos
- Búsqueda en tiempo real
- Indicadores de carga
- Manejo de errores de red
- UI adaptativa (portrait/landscape)

### Filtros
- Filtrar por plataforma
- Filtrar por género
- Ordenar por nombre, fecha, rating
- Filtros se guardan automáticamente

### Detalles del Juego
- Información completa del videojuego
- Agregar/quitar de lista de deseados
- Compartir juego
- Abrir sitio web oficial

### Lista de Deseados
- Tab separada en la pantalla principal
- Base de datos local persistente
- Navegación a detalles desde la lista

## Requisitos

- Android Studio Hedgehog o superior
- Android SDK 24+
- Kotlin 1.9.10+
- Compose BOM 2023.10.01+

## Licencia

Este proyecto es parte de un seminario académico y está destinado únicamente para fines educativos.


