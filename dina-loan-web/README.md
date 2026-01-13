# Dina Loan Web Module

## Overview
The `dina-loan-web` module is the main public-facing web application for the Dina Loan Application. It allows users to submit loan requests to the Swedish Museum of Natural History.

## Technologies
*   **Framework**: JSF 2.1 (JavaServer Faces)
*   **Component Library**: PrimeFaces 7.0
*   **Runtime**: Thorntail (WildFly Swarm) with Undertow
*   **Context Dependency Injection**: CDI
*   **Persistence**: JPA (via `dina-manager`)

## Configuration
This module contains the primary web configuration (`web.xml` equivalent in `WEB-INF`) and JSF managed beans for handling user interactions.

## Running
This module is packaged as utilizing Thorntail and can be run independently:
```bash
java -jar target/loan-thorntail.jar
```
