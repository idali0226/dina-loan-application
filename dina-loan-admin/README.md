# Loan Admin Module

## Overview
The `dina-loan-admin` module provides the administrative interface for museum staff to manage and process loan requests submitted through the web portal.

## Technologies
*   **Framework**: JSF 2.1 (JavaServer Faces)
*   **Component Library**: PrimeFaces 7.0
*   **Runtime**: Thorntail (WildFly Swarm)
*   **Security**: JBossSX / JAAS for authentication and authorization

## Key Features
*   **Loan Processing**: Review, approve, or deny loan requests.
*   **User Management**: Administration of system users.
*   **Reporting**: specific reporting capabilities using POI.

## Running
This module is packaged utilizing Thorntail and can be run independently:
```bash
java -jar target/loan-admin-thorntail.jar
```
