# distance-service
##This web service (api) returns road distance between two latitudes-longitudes.

Have used data released by OSM [https://www.openstreetmap.org/]. 
Data can be downloaded from https://mapzen.com/data/metro-extracts/.

The routing engine used is GraphHopper Routing Engine.[https://github.com/graphhopper/graphhopper]

The code is written in Java using spring framework.  
The build system used in gradle.  
This is used in production.

The configuration can be found at:  
distance-service/src/main/webapp/WEB-INF/properties/distance-service.properties

It is currently configured for 8 Indian cities.
Below script can be used to download the data.  
distance-service/blob/master/osmData.sh

### Request & Response format
Request format json: An array of sources and destinations. 

```
{
  "sources": [
    {
      "lat": 12.9716,
      "lng": 77.5946
    }
  ],
  "destinations": [
    {
      "lat": 12.9726,
      "lng": 77.5926
    }
  ]
}

```

Response format json: Road distance between all the sources and destination.
```
{
  "status": 0,
  "rows": [
    {
      "elements": [
        {
          "status": 0,
          "distance": 235.43280410062306
        }
      ]
    }
  ]
}
```

    


