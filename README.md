# AREP-Secure-Application-Design

María Paula Sánchez Macías

# ARQUITECTURA

## Vista General del Sistema

El sistema implementa una arquitectura de tres capas distribuida en la nube de Amazon Web Services (AWS), diseñada bajo los principios de separación de responsabilidades y seguridad por capas. La arquitectura se compone de tres componentes principales que se comunican exclusivamente a través de conexiones cifradas HTTPS, garantizando la confidencialidad e integridad de los datos en tránsito.

![]()

- Cliente (Navegador Web)
- Servidor Apache (EC2 Instance)
- Servidor Spring Boot (EC2 Instance)

## ARQUITECTURA DE INFRAESTRUCTURA AWS

La infraestructura está desplegada en Amazon Web Services (AWS) utilizando una Virtual Private Cloud (VPC) personalizada que proporciona un entorno de red aislado y seguro. La arquitectura de red implementa las mejores prácticas de AWS, incluyendo el uso de subnets públicas, Internet Gateway para conectividad externa, y Security Groups como firewalls virtuales.

