# AgroIntelligence

This is one-evening project that presents a conception of creating application that would help to automaticaly manage such aspects of agriculture like fields irrigation etc.

A general concept cover much more sophisticated solution that would consist of dozen of sensors and plenty of embedded systems. In this repo you can find a scratch of backend to manage whole infrastructure.

## Features
The app is able to collect data (humidity measurements) via REST API. What is more one could assign an irrigation strategy for a field and then - water cannons placed on the field could manage water resources so that ground humidity required by strategy was achieved.

## API

### GET
* /field/getById/${id} - returns info about selected field
* /field/fetchAllFields - returns info about all fields
* /field/getCannonsByFieldId/${id} - returns info about all cannons assigned to field
* /field/getCurrentStrategyByFieldId/${id} - returns current strategy for given field **WARNING!** Becouse of the simplicity of project - if you assign more than one strategy for a slot time - behaviour is undefined
* /field/getStrategiesByFieldId/${id} - returns all strategies for given field
* /measurements/getLatestForSensorId/${id} - returns all measurements for given sensor
* /measurements/getAllMeasurements - returns all measurements

**ANOTHER ENDPOINTS THAT WOULD BE USEFUL BUT ARE UNIMPLEMENTED:**
* /waterCannons/getLastMeasurementsFromAllAssignedSensors/${cannonId}
* /waterCannons/fetchAll
* /sesnors/fetchAllSensors


### POST
* /field/createField - expects JSON in format {"fieldName":String}
* /field/editField - expects JSON in format {"id":Int, "fieldName":String}
* /measurements/addMeasurement - expects JSON in format {"time":"hh:mm:ss", "sensorId":Int, "value":Float}
* /sensors/createSensor - expects JSON in format {"ids": [Int, ...]} that represent ids of cannons the sensor is assigned to
* /strategies/createStrategy - expects JSON in format {"name":String, "startTime":"hh:mm::ss", "endTime":"hh:mm:ss", "minHumidity":Float, "maxHumidity":Float}
* /strategies/addStrategyToField - expects JSON in format {"primaryId":Int, "secondaryId":Int} where first ID is strategyID and second one is FieldID
* /waterCannons/createCannon - expects JSON in format {"fieldId":Int} - where fieldID represents field the cannon is assigned to
* /waterCannons/addCannonToField - expects JSON in format {"primaryId":Int, "secondaryId":Int} where first ID represens cannon and the second one - field. Beware that it can be only one field assigned to cannon.

## Database
The database used is Apache Derby
