
# Air Quality Restful API

Get the air quality category for the 3 air pollutants (O3, SO2, CO).
You can only query for the Ankara, London, Mumbai, Tokyo and Barcelona cities.
You can only query dates from 27-11-2020 to today.
## Features

- Get air quality category by city name and start and end dates.
- Air quality categories: Good, Satisfactory, Moderate, Poor, Severe, Hazardous.
- Add a city to expand allowed cities.
- Look at the logs to determine if data comes from the weather API or the Database.
- It sends logs to the log saver micro service using RabbitMQ.
## Tech Stack

**Server:** Java, Spring Boot, Postgres, Docker, RabbitMQ.

## Log Saver Service Repo
#### https://github.com/aalpturkay/logsaver

## API Routes

### Get Air Quality
/air_quality?city=city&startDate=dd-MM-yyyy&endDate=dd-MM-yyyy

![getairqualityankara](https://user-images.githubusercontent.com/44681322/177865254-fcef0425-6116-4781-8088-21f54c0d1bd5.png)

![ankaraairqualityresult](https://user-images.githubusercontent.com/44681322/177865607-1d25bf6c-171e-48c2-af01-cd9353cd5611.png)

#### Info Log:
![ankaralog](https://user-images.githubusercontent.com/44681322/177865792-7780ae54-6079-4036-ab82-56bb69b94306.png)

#### When start and end date is null, it gets air quality for last one week.
![ankaradatesnull](https://user-images.githubusercontent.com/44681322/177866332-90951c39-ed3b-4031-b0f1-89d547575d14.png)
![ankaradatesnullresponse](https://user-images.githubusercontent.com/44681322/177866569-8ba72812-0649-4ed6-8357-b151ac412702.png)

#### When dates are not between restrictions
![ankaraerrreq](https://user-images.githubusercontent.com/44681322/177867448-6836c09b-c13d-4123-ac51-6af6f84d5ede.png)
![ankaraerrorres](https://user-images.githubusercontent.com/44681322/177867556-d46d7b52-fc06-4318-bc65-4c77db36ef2b.png)
#### Air quality data for Ankara, 12-03-2021 and 22-03-2021 and logs
![ankara12-22](https://user-images.githubusercontent.com/44681322/177869740-b80a0c8c-3ef7-4afa-89e1-f80b046ba231.png)
![12-22-03ankara](https://user-images.githubusercontent.com/44681322/177869822-06d1e784-b6f5-40ce-b443-8ee09e974591.png)
