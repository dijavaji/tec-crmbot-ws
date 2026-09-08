---
description: Guía de verificación y testing - niveles de pruebas, comandos de verificación y trazabilidad de requisitos
alwaysApply: false
---

# Verificación y Testing

> Este documento define **cómo verificar que tu trabajo funciona** antes de declarar
> una tarea como `done`. Incluye niveles de testing, comandos de verificación y
> trazabilidad de requisitos.

---

## 🚨 Regla de Oro

**NO declares una tarea `done` sin pruebas verdes.**

Ejecuta `./mvnw clean verify` y asegúrate de que todos los tests pasan al 100%.

---

## Niveles de Testing

### Nivel 1: Pruebas Unitarias

**Objetivo:** Verificar componentes individuales de forma aislada.

**Características:**
- Sin dependencias externas (base de datos, APIs, mensajería)
- Usar mocks para dependencias (`Mockito`)
- Rápidas de ejecutar (< 1 segundo por test)
- Alta cobertura de casos edge

**Componentes a testear:**
- Services (lógica de negocio pura)
- Validators
- Mappers / DTOs
- Utility classes

**Framework:** JUnit 5

**Ejemplo:**
```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findUserById_WhenUserExists_ReturnsUser() {
        // Arrange
        Long userId = 1L;
        User expectedUser = new User(userId, "John Doe");
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = userService.findUserById(userId);

        // Assert
        assertEquals(expectedUser, result);
        verify(userRepository).findById(userId);
    }

    @Test
    void findUserById_WhenUserNotFound_ThrowsException() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            userService.findUserById(userId);
        });
    }
}
Comando:

```bash

./mvnw test`


