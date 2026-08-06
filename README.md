1. Start the Complete Stack 

   docker-compose up -d

This command will start all two services:

 - Gym CRM App: http://localhost:8080
 - Prometheus: http://localhost:9090

2. Access Services
   Application:

 - URL: http://localhost:8080
 - Swagger UI: http://localhost:8080/swagger-ui.html
 - Actuator Health: http://localhost:8080/actuator/health
 - Actuator Metrics: http://localhost:8080/actuator/metrics
 - Prometheus Metrics: http://localhost:8080/actuator/prometheus

Prometheus:

 - URL: http://localhost:9090
 - View Metrics: http://localhost:9090/graph
 - Targets: http://localhost:9090/targets
 - 
   Available Metrics
   Custom Metrics
   Training Metrics:

 1. gym_trainings_created_total - Total number of trainings created
 2. gym_trainings_active - Number of active trainings

   User Metrics:

 1. gym_users_registered_total - Total number of registered users
 2. gym_users_logins_total - Total number of logins
 3. gym_users_active - Number of active users

   Request Metrics:

 1. gym_requests_total - Total number of HTTP requests
 2. gym_requests_errors_total - Number of errors
 3. gym_requests_duration_seconds - Request duration

  Health Indicators
  Database Health:

 1. MySQL connection status
 2. Number of training types in database
 3. Disk Space Health:

  Free disk space

 1. Usage percentage
 2. Threshold: 10 GB

  External Service Health:

 1. External service status (Notification Service)

  Prometheus Queries
  Examples of useful queries:

 - Training creation rate over the last minute
rate(gym_trainings_created_total[1m])

 - Average request duration over 5 minutes
rate(gym_requests_duration_seconds_sum[5m]) / rate(gym_requests_duration_seconds_count[5m])

 - Error rate percentage
rate(gym_requests_errors_total[1m]) / rate(gym_requests_total[1m]) * 100

 - Active users
gym_users_active