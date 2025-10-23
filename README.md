# AREP-Secure-Application-Design

María Paula Sánchez Macías

## Descripción

Aplicación web full-stack para gestión de propiedades inmobiliarias con características de seguridad empresarial. Implementa una arquitectura de microservicios distribuida en AWS con tres instancias EC2 separadas, autenticación segura con BCrypt, y comunicación cifrada entre componentes.

---

## Video

https://youtu.be/NnJEaZIbblg

---

## Requisitos Previos

- Cuenta de AWS con acceso a EC2
- Clave SSH (.pem) para acceso a instancias
- Git instalado localmente
- Maven 3.8+ (para desarrollo local)
- Java 17+ (para desarrollo local)

## Estructura del Proyecto
```
AREP-Secure-Application-Design/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── edu/escuelaing/arep/property_management/
│   │   │       ├── config/
│   │   │       │   └── WebSecurityConfig.java
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java
│   │   │       │   └── PropertyController.java
│   │   │       ├── dto/
│   │   │       │   ├── AuthRequest.java
│   │   │       │   └── AuthResponse.java
│   │   │       ├── model/
│   │   │       │   ├── Property.java
│   │   │       │   └── User.java
│   │   │       ├── repository/
│   │   │       │   ├── PropertyRepository.java
│   │   │       │   └── UserRepository.java
│   │   │       ├── service/
│   │   │       │   └── AuthService.java
│   │   │       └── PropertyManagementApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html
│   │       │   ├── script.js
│   │       │   └── style.css
|   |       └── templates/
|   |       |   └── login.html
│   │       │   ├── login.js
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

# ARQUITECTURA

## ARQUITECTURA GENERAL AWS

La infraestructura está desplegada en Amazon Web Services (AWS) utilizando una Virtual Private Cloud (VPC) personalizada que proporciona un entorno de red aislado y seguro. La arquitectura de red implementa las mejores prácticas de AWS, incluyendo el uso de subnets públicas, Internet Gateway para conectividad externa, y Security Groups como firewalls virtuales.

![](https://github.com/hakki17/AREP-Secure-Application-Design/blob/main/img/arquitectura.png)

## Flujo de Comunicación

El sistema implementa una arquitectura de tres capas distribuida en la nube de Amazon Web Services (AWS), diseñada bajo los principios de separación de responsabilidades y seguridad por capas. La arquitectura se compone de tres componentes principales que se comunican exclusivamente a través de conexiones cifradas HTTPS, garantizando la confidencialidad e integridad de los datos en tránsito.

![](https://github.com/hakki17/AREP-Secure-Application-Design/blob/main/img/Diagrama1.jpg)

- Cliente (Navegador Web)
- Servidor Apache (EC2 Instance)
- Servidor Spring Boot (EC2 Instance)

![](https://github.com/hakki17/AREP-Secure-Application-Design/blob/main/img/ec21.png)

![](https://github.com/hakki17/AREP-Secure-Application-Design/blob/main/img/ec22.png)

## Componentes del Backend

![](https://github.com/hakki17/AREP-Secure-Application-Design/blob/main/img/Arquitectura%20general.jpg)

---

### Flujo de Datos

1. **Usuario** → Accede a Frontend (Apache)
2. **Frontend** → Muestra login.html
3. **Usuario** → Ingresa credenciales
4. **Frontend** → POST a Backend `/api/auth/login`
5. **Backend** → Valida con BCrypt contra MySQL
6. **Backend** → Retorna respuesta de autenticación
7. **Frontend** → Redirige a index.html
8. **Usuario** → Realiza operaciones CRUD
9. **Frontend** → Envía peticiones a `/api/properties`
10. **Backend** → Procesa y consulta MySQL
11. **MySQL** → Retorna datos
12. **Backend** → Envía respuesta JSON
13. **Frontend** → Renderiza interfaz

---

## Tecnologías

### Backend
- **Spring Boot 3.3.5** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **BCrypt** - Hash de contraseñas
- **MySQL Connector** - Driver de base de datos
- **Maven** - Gestión de dependencias

### Frontend
- **HTML5/CSS3** - Estructura y estilos
- **JavaScript (ES6+)** - Lógica del cliente
- **Fetch API** - Comunicación asíncrona con backend

### Infraestructura
- **AWS EC2** - Instancias virtuales
- **Amazon Linux 2023** - Sistema operativo
- **Apache HTTP Server 2.4** - Servidor web
- **MySQL 8.0** - Base de datos relacional
- **Docker** - Contenedorización de MySQL

---

## Instalación y Despliegue

### 1. Crear Instancias EC2

Crear 3 instancias t2.micro con Amazon Linux 2023:

**Instancia MySQL:**
- Security Group: SSH (22), MySQL (3306)

**Instancia Backend:**
- Security Group: SSH (22), HTTP (80), Custom TCP (8080)

**Instancia Frontend:**
- Security Group: SSH (22), HTTP (80), HTTPS (443)

### 2. Configurar MySQL
```bash
# Conectarse a la instancia MySQL
ssh -i AREP-key.pem ec2-user@

# Instalar Docker
sudo yum update -y
sudo yum install docker -y
sudo service docker start
sudo usermod -a -G docker ec2-user

# Salir y volver a entrar
exit
ssh -i AREP-key.pem ec2-user@

# Crear contenedor MySQL
docker run -p 3306:3306 --name mysqlproperties \
  -e MYSQL_ROOT_PASSWORD=secret \
  -e MYSQL_DATABASE=property_management \
  -e MYSQL_ROOT_HOST='%' \
  -d mysql:latest

# Esperar 30 segundos
sleep 30

# Crear usuario de aplicación
docker exec -it mysqlproperties mysql -u root -psecret

# Dentro de MySQL:
CREATE USER 'propertyuser'@'%' IDENTIFIED BY 'Property123!';
GRANT ALL PRIVILEGES ON property_management.* TO 'propertyuser'@'%';
FLUSH PRIVILEGES;
EXIT;
```

### 3. Configurar Backend
```bash
# Conectarse a la instancia Backend
ssh -i AREP-key.pem ec2-user@

# Instalar Java, Maven y Git
sudo yum update -y
sudo yum install java-17-amazon-corretto-devel maven git -y

# Clonar repositorio
git clone https://github.com/hakki17/AREP-Secure-Application-Design.git
cd AREP-Secure-Application-Design

# Editar application.properties con la IP correcta de MySQL
nano src/main/resources/application.properties
# Cambiar: spring.datasource.url=jdbc:mysql://:3306/property_management

# Compilar
mvn clean package -DskipTests

# Ejecutar
nohup java -jar target/property-management-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

# Verificar logs
tail -f app.log
```

### 4. Configurar Frontend
```bash
# Conectarse a la instancia Frontend
ssh -i AREP-key.pem ec2-user@

# Instalar Apache y Git
sudo yum update -y
sudo yum install httpd git -y
sudo systemctl start httpd
sudo systemctl enable httpd

# Clonar repositorio
git clone https://github.com/hakki17/AREP-Secure-Application-Design.git
cd AREP-Secure-Application-Design

# Editar archivos JS con la IP correcta del Backend
nano src/main/resources/static/script.js
# Cambiar: const API_URL = 'http://:8080/api/properties';

nano src/main/resources/static/login.js
# Cambiar: const API_URL = 'http://:8080/api/auth';

# Copiar archivos a Apache
sudo cp src/main/resources/static/* /var/www/html/

# Reiniciar Apache
sudo systemctl restart httpd
```

### 5. Verificar Despliegue

Abrir en navegador:
```
http://<IP_FRONTEND>/login.html
```
---

## Uso

### Registro de Usuario

![](https://github.com/hakki17/AREP-Secure-Application-Design/blob/main/img/paginalogin.png)

![](https://github.com/hakki17/AREP-Secure-Application-Design/blob/main/img/usuarios.png)

1. Ir a `http://<IP_FRONTEND>/login.html`
2. Click en tab "Registrarse"
3. Ingresar usuario y contraseña
4. Click en "Registrarse"
5. Automáticamente cambia a tab "Iniciar Sesión"

### Login

![](https://github.com/hakki17/AREP-Secure-Application-Design/blob/main/img/loginIniciarSesion.png)

![](https://github.com/hakki17/AREP-Secure-Application-Design/blob/main/img/loginIniciarSesionSuccesfull.png)

1. Ingresar credenciales
2. Click en "Iniciar Sesión"
3. Redirige a la aplicación principal

### Gestión de Propiedades

**Crear Propiedad:**

![](https://github.com/hakki17/AREP-Secure-Application-Design/blob/main/img/propiedadcreada.png)

1. Completar formulario (Dirección, Precio, Tamaño, Descripción)
2. Click en "Agregar Propiedad"

**Editar Propiedad:**

![](https://github.com/hakki17/AREP-Secure-Application-Design/blob/main/img/listapropiedades.png)

1. Click en botón "Editar" de la propiedad deseada
2. Modificar campos
3. Click en "Actualizar"

**Eliminar Propiedad:**

![](https://github.com/hakki17/AREP-Secure-Application-Design/blob/main/img/propiedadeliminada.png)

![](https://github.com/hakki17/AREP-Secure-Application-Design/blob/main/img/propiedadeliminada2.png)

1. Click en botón "Eliminar"
2. Confirmar acción

---
