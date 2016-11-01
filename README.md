# distance-service
##This service returns road distance between two latitudes-longitudes.

Have used data released by OSM [https://www.openstreetmap.org/]. 
Data can be downloaded from https://mapzen.com/data/metro-extracts/.

The routing engine used is GraphHopper Routing Engine.

The code is written in Java using spring framework.
This is used in production.

The configuration can be found at:  
distance-service/src/main/webapp/WEB-INF/properties/distance-service.properties

It is currently configured for 8 Indian cities.
Below script can be used to download the data.  
distance-service/blob/master/osmData.sh

    


