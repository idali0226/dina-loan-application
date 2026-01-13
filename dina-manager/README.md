# Dina Manager Module

## Overview
The `dina-manager` module encapsulates the core business logic and data management services for the Dina Loan Application. It serves as the bridge between the web interfaces and the data storage layers.

## Technologies
*   **Target Container**: EJB / Thorntail
*   **Persistence**: JPA 2.2 (EclipseLink 2.7.4)
*   **Validation**: Hibernate Validator 5.1.2
*   **Utilities**: Apache Commons Lang 2.2

## Responsibilities
*   **Data Validation**: Ensures data integrity before persistence.
*   **Transaction Management**: Handles atomic operations across different resources.
*   **Business Rules**: Implements specific logic for loan approval workflows.
