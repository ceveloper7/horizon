# Documentación del Proyecto Horizon

## 1. Visión General del Proyecto

**Nombre**: Horizon  
**Descripción**: API RESTful para gestión de productos de e-commerce  
**Stack**: Spring Boot 4.0.1 + Java 21 + PostgreSQL + JPA + MapStruct  
**Arquitectura**: Clean Architecture

---

## 2. Estructura de Paquetes

```
src/main/java/com/gba/horizon/
├── productapi/
│   ├── domain/                      # Capa de Dominio
│   │   └── Product.java
│   ├── usecase/                    # Capa de Aplicación (Puerto)
│   │   ├── ProductsQueryUseCase.java
│   │   ├── ProductsCommandUseCase.java
│   │   └── dto/
│   │       └── CreatedProduct.java
│   └── adapter/
│       ├── inbound/rest/           # Capa de Presentación (Entradas)
│       │   ├── ProductsApi.java
│       │   ├── ProductsApiController.java
│       │   ├── dtos/
│       │   │   ├── ProductInput.java
│       │   │   ├── ProductOutput.java
│       │   │   └── ProductDescriptionInput.java
│       │   ├── mapper/
│       │   │   └── ProductMapper.java
│       │   └── configuration/
│       │       └── ValidSku.java
│       ├── outbound/database/      # Capa de Infraestructura (Salidas)
│       │   ├── ProductsQueryUseCaseImpl.java
│       │   ├── ProductsCommandUseCaseImpl.java
│       │   ├── ProductsRepository.java
│       │   └── entity/
│       │       └── ProductEntity.java
│       └── exception/
│           ├── EntityNotFoundException.java
│           └── GlobalExceptionHandler.java
├── controller/                     # ⚠️ LEGACY - No sigue Clean Architecture
│   └── CategoryController.java
├── entity/                         # ⚠️ LEGACY
│   └── Category.java
├── service/                       # ⚠️ LEGACY
│   ├── CategoryService.java
│   └── CategoryServiceImpl.java
└── HorizonApplication.java         # Entry Point
```

---

## 3. Capas de Clean Architecture

### 3.1 Capa de Dominio (`domain/`)

#### Product.java
- **Ubicación**: `com.gba.horizon.productapi.domain.Product`
- **Propósito**: Entidad de dominio pura que representa un producto
- **Responsabilidad**: Mantiene el estado del negocio sin dependencias externas
- **Atributos**:
  - `name` (String): Nombre del producto
  - `sku` (String): Identificador único del producto (usado como clave en equals/hashCode)
  - `description` (String): Descripción del producto
  - `price` (BigDecimal): Precio del producto

---

### 3.2 Capa de Aplicación (`usecase/` - Puertos)

Son las **interfaces** que definen los casos de uso. Son el "puerto" hacia el exterior.

#### ProductsQueryUseCase.java
```java
public interface ProductsQueryUseCase {
    Product getProductById(String productId);
    List<? extends Product> getAllProducts();
}
```
- **Propósito**: Puerto para consultas (lectura) de productos

#### ProductsCommandUseCase.java
```java
public interface ProductsCommandUseCase {
    CreatedProduct createProduct(Product product);
    void deleteProduct(String productId);
    Product updateProductDescription(String productId, String description);
}
```
- **Propósito**: Puerto para comandos (escritura) de productos

#### DTOs de Uso
- **CreatedProduct.java**: Record que contiene el producto creado y un flag `isNewProduct` indicando si es nuevo o existente

---

### 3.3 Capa de Adaptadores - Entrada (`adapter/inbound/rest/`)

#### ProductsApi.java (Interfaz de Contrato)
- **Ubicación**: `com.gba.horizon.productapi.adapter.inbound.rest.ProductsApi`
- **Propósito**: Define el contrato de la API REST (openapi/specification)
- **Endpoints**:
  | Método | Endpoint | Descripción |
  |--------|----------|-------------|
  | PUT | `/api/products/{productId}` | Crear/actualizar producto |
  | DELETE | `/api/products/{productId}` | Eliminar producto |
  | PATCH | `/api/products/{productId}` | Actualizar solo descripción |
  | GET | `/api/products/{productId}` | Obtener producto por ID |
  | GET | `/api/products` | Listar todos los productos |

#### ProductsApiController.java
- **Ubicación**: `com.gba.horizon.productapi.adapter.inbound.rest.ProductsApiController`
- **Propósito**: Implementa el contrato `ProductsApi`. Convierte requests HTTP en llamadas a los casos de uso.
- **Anotaciones**: 
  - `@RestController` → Bean de Spring
  - `@RequestMapping("/api/products")` → Ruta base
- **Dependencias inyectadas**:
  - `ProductsQueryUseCase` (consultas)
  - `ProductsCommandUseCase` (comandos)
  - `ProductMapper` (transformación DTOs)

#### DTOs (Data Transfer Objects)

| Clase | Propósito |
|-------|-----------|
| **ProductInput** | Request para crear/actualizar producto. Validaciones: name (3-255), description (10-255), price (>0) |
| **ProductOutput** | Response de producto. Solo lectura |
| **ProductDescriptionInput** | Request para actualizar descripción. Validación: description (10-255) |

#### Mapeador
- **ProductMapper.java**: Interfaz MapStruct para transformar `Product` → `ProductOutput`
- **Implementación auto-generada**: `ProductMapperImpl.java` (en `target/generated-sources`)

#### Validaciones
- **ValidSku.java**: Anotación personalizada para validar formato de SKU
  - **Pattern**: `[A-Za-z]{2}[0-9]{5}` (ej: AB12345)
  - **Mensaje**: "SKU must follow the pattern AA99999"

---

### 3.4 Capa de Adaptadores - Salida (`adapter/outbound/database/`)

Son las **implementaciones** de los puertos (adaptadores).

#### ProductsQueryUseCaseImpl.java
- **Propósito**: Implementa `ProductsQueryUseCase`
- **Dependencias**: `ProductsRepository`
- **Métodos**:
  - `getProductById()` → busca por SKU, lanza `EntityNotFoundException` si no existe
  - `getAllProducts()` → retorna todos los productos

#### ProductsCommandUseCaseImpl.java
- **Propósito**: Implementa `ProductsCommandUseCase`
- **Anotaciones**: 
  - `@Transactional` → maneja transacciones
  - `@Service` → bean de Spring
- **Métodos**:
  - `createProduct()` → guarda producto, retorna `CreatedProduct` con flag `isNewProduct`
  - `deleteProduct()` → elimina por SKU
  - `updateProductDescription()` → actualiza solo descripción

#### ProductsRepository.java
- **Propósito**: Repositorio JPA para persistencia
- **Extiende**: `JpaRepository<ProductEntity, String>` (la clave es el SKU)
- **Método personalizado**: `updateDescriptionById()` con Query JPQL

#### ProductEntity.java
- **Propósito**: Entidad JPA que mapea a la tabla `TB_PRODUCT`
- **Herencia**: Extiende `Product` (herencia cuestionable desde punto de vista de Clean Architecture)
- **Mapeo**:
  - Tabla: `TB_PRODUCT`
  - SKU → `CO_SKU` (clave primaria)
  - name → `NAME`
  - description → `DESCRIPTION`
  - price → `PRICE`
- **Método**: `fromProduct(Product)` → factory method para convertir dominio a entidad

---

### 3.5 Capa de Excepciones (`adapter/exception/`)

#### EntityNotFoundException.java
- **Propósito**: Excepción personalizada para entidad no encontrada
- **Tipo**: RuntimeException

#### GlobalExceptionHandler.java
- **Propósito**: Manejador centralizado de excepciones
- **Anotación**: `@ControllerAdvice`
- **Manejo de excepciones**:
  | Excepción | Status HTTP | Respuesta |
  |-----------|-------------|-----------|
  | `EntityNotFoundException` | 404 | ProblemDetail con mensaje |
  | `ConstraintViolationException` | 400 | Lista de errores de validación |
  | `MethodArgumentNotValidException` | 400 | Lista de errores de @Valid |

---

## 4. Flujo de una Solicitud (Ejemplo: GET /api/products/{sku})

```
HTTP Request (GET /api/products/AB12345)
         ↓
ProductsApiController.getProductById()
         ↓
    (valida SKU con @ValidSku)
         ↓
ProductsQueryUseCase.getProductById()
         ↓
ProductsQueryUseCaseImpl.getProductById()
         ↓
ProductsRepository.findById()
         ↓
    (si no existe → EntityNotFoundException)
         ↓
ProductMapper.toProductOutput()
         ↓
HTTP Response (200 OK + ProductOutput JSON)
```

---

## 5. Tecnologías y Dependencias

| Dependencia | Propósito |
|-------------|-----------|
| Spring Boot 4.0.1 | Framework principal |
| spring-boot-starter-webmvc | REST API |
| spring-boot-starter-data-jpa | Persistencia JPA |
| spring-boot-starter-validation | Validaciones (@Valid) |
| spring-boot-starter-webmvc-test | Testing |
| postgresql | Driver de base de datos |
| mapstruct 1.5.5.Final | Mapper de objetos |
| springdoc-openapi-starter-webmvc-ui 2.5.0 | Documentación OpenAPI/Swagger |

---

## 6. Observaciones y Mejoras Sugeridas

### ⚠️ Problemas Identificados

1. **Herencia Entity-Domain**: `ProductEntity extends Product` es una práctica cuestionable. En Clean Architecture, la Entity y el Domain deberían ser objetos distintos con un mapper entre ellos.

2. **Código Legacy**: Los paquetes `controller/`, `entity/`, `service/` no siguen la Clean Architecture del resto del proyecto.

3. **ValidSku**: Usa `@Pattern` directamente en lugar de un `ConstraintValidator` personalizado, lo cual funciona pero es limitado.

4. **Ausencia de UseCase separado para cada acción**: Los métodos podrían dividirse en use cases más pequeños y específicos.

### ✅ Prácticas Correctas

- Separación clara de capas (domain, use case, adapters)
- Uso de interfaces para definir puertos
- Inyección de dependencias vía constructor
- Validaciones en DTOs de entrada
- Excepciones personalizadas
- Manejo centralizado de errores con @ControllerAdvice

---

## 7. Endpoints Resumidos

| Método | Ruta | Input | Output |
|--------|------|-------|--------|
| GET | `/api/products` | - | `List<ProductOutput>` |
| GET | `/api/products/{sku}` | SKU (path) | `ProductOutput` |
| PUT | `/api/products/{sku}` | SKU + `ProductInput` | `ProductOutput` (201/200) |
| PATCH | `/api/products/{sku}` | SKU + `ProductDescriptionInput` | `ProductOutput` |
| DELETE | `/api/products/{sku}` | SKU (path) | 204 No Content |