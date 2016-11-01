package com.swiggy.distance;

import java.util.Arrays;

import com.swiggy.distance.requests.DistanceTupleRequest;
import com.swiggy.distance.requests.Tuples;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.TraversalMode;
import com.swiggy.distance.common.GraphConfig;
import com.swiggy.distance.requests.DistanceRequest;
import com.swiggy.distance.requests.Point;
import com.swiggy.distance.responses.DistanceResponse;
import com.swiggy.distance.responses.Status;
import com.swiggy.distance.services.DistanceService;
import java.util.List;

public class TestDistanceService {
	
	private DistanceService distanceService;
	
	@Before
	public void setUp() {
		String currentDirectory = this.getClass().getClassLoader().getResource("").getPath();
		String rootDirectory = currentDirectory.substring(0, currentDirectory.indexOf("distance-service") + "distance-service".length());
		String pbfFile = rootDirectory +"/src/test/resources/files/pune_india.pbf";
		String graphDirectory = rootDirectory + "/src/test/resources/files/";
		distanceService = new DistanceService();
		distanceService.setGraphConfigs(Arrays.asList(new GraphConfig(6, pbfFile, graphDirectory, "car,motorcycle", "shortest", true)));
		distanceService.loadGraphs();
	}
	
	@Test
	public void testValidSingleSourceAndSingleDestination() {
		DistanceRequest distanceRequest = new DistanceRequest(
				Arrays.asList(new Point(18.573261,73.914440)),
				Arrays.asList(new Point(18.566791,73.918359))
				);
		DistanceResponse distanceResponse = distanceService.getDistanceResponse(distanceRequest);
		System.out.println("Distance Response : " + distanceResponse);
		Assert.assertEquals(Status.SUCCESS.getCode(), distanceResponse.getStatus());
		Assert.assertEquals(1, distanceResponse.getRows().size());
		Assert.assertEquals(1, distanceResponse.getRows().get(0).getElements().size());
		Assert.assertEquals(1789, (int) distanceResponse.getRows().get(0).getElements().get(0).getDistance());
	}


	
	@Test
	public void testValidSingleSourceAndMultipleDestination() {
		DistanceRequest distanceRequest = new DistanceRequest(
				Arrays.asList(new Point(18.573261,73.914440)),
				Arrays.asList(new Point(18.566791,73.918359), new Point(18.565884,73.915200))
				);
		DistanceResponse distanceResponse = distanceService.getDistanceResponse(distanceRequest);
		System.out.println("Distance Response : " + distanceResponse);
		Assert.assertEquals(Status.SUCCESS.getCode(), distanceResponse.getStatus());
		Assert.assertEquals(1, distanceResponse.getRows().size());
		Assert.assertEquals(2, distanceResponse.getRows().get(0).getElements().size());
		Assert.assertEquals(1789, (int) distanceResponse.getRows().get(0).getElements().get(0).getDistance());
		Assert.assertEquals(1611, (int) distanceResponse.getRows().get(0).getElements().get(1).getDistance());
	}
	
	@Test
	public void testValidMultiSourceAndSingleDestination() {
		DistanceRequest distanceRequest = new DistanceRequest(
				Arrays.asList(new Point(18.573261,73.914440), new Point(18.544739,73.905212)),
				Arrays.asList(new Point(18.566791,73.918359))
				);
		DistanceResponse distanceResponse = distanceService.getDistanceResponse(distanceRequest);
		System.out.println("Distance Response : " + distanceResponse);
		Assert.assertEquals(Status.SUCCESS.getCode(), distanceResponse.getStatus());
		Assert.assertEquals(2, distanceResponse.getRows().size());
		Assert.assertEquals(1, distanceResponse.getRows().get(0).getElements().size());
		Assert.assertEquals(1, distanceResponse.getRows().get(1).getElements().size());
		Assert.assertEquals(1789, (int) distanceResponse.getRows().get(0).getElements().get(0).getDistance());
		Assert.assertEquals(3684, (int) distanceResponse.getRows().get(1).getElements().get(0).getDistance());
	}
	
	@Test
	public void testValidSingleSourceAndInvalidMultiDestination() {
		DistanceRequest distanceRequest = new DistanceRequest(
				Arrays.asList(new Point(18.573261,73.914440)),
				Arrays.asList(new Point(18.566791,73.918359), new Point(12.933357, 77.613925))
				);
		DistanceResponse distanceResponse = distanceService.getDistanceResponse(distanceRequest);
		System.out.println("Distance Response : " + distanceResponse);
		Assert.assertEquals(Status.SUCCESS.getCode(), distanceResponse.getStatus());
		Assert.assertEquals(1, distanceResponse.getRows().size());
		Assert.assertEquals(2, distanceResponse.getRows().get(0).getElements().size());
		Assert.assertEquals(1789, (int) distanceResponse.getRows().get(0).getElements().get(0).getDistance());
		Assert.assertEquals(Status.NOT_SUPPORTED.getCode(), (int) distanceResponse.getRows().get(0).getElements().get(1).getStatus());
	}
	
	@Test
	public void testInvalidSingleSourceAndValidMultiDestination() {
		DistanceRequest distanceRequest = new DistanceRequest(
				Arrays.asList(new Point(12.933357, 77.613925)),
				Arrays.asList(new Point(18.566791,73.918359), new Point(18.565884,73.915200))
				);
		DistanceResponse distanceResponse = distanceService.getDistanceResponse(distanceRequest);
		System.out.println("Distance Response : " + distanceResponse);
		Assert.assertEquals(Status.SUCCESS.getCode(), distanceResponse.getStatus());
		Assert.assertEquals(1, distanceResponse.getRows().size());
		Assert.assertEquals(2, distanceResponse.getRows().get(0).getElements().size());
		Assert.assertEquals(Status.NOT_SUPPORTED.getCode(), distanceResponse.getRows().get(0).getElements().get(0).getStatus());
		Assert.assertEquals(Status.NOT_SUPPORTED.getCode(), distanceResponse.getRows().get(0).getElements().get(1).getStatus());
	}

	@Test
	public void testValidArrayOfSizeOneForTuple() {
		DistanceTupleRequest distanceTupleRequest = new DistanceTupleRequest(Arrays.asList(new Tuples(
				Arrays.asList(new Point(18.573261,73.914440),(new Point(18.566791,73.918359))))));
		DistanceResponse distanceResponse = distanceService.getDistanceResponse(distanceTupleRequest);
		System.out.println("Distance Response : " + distanceResponse);
		Assert.assertEquals(Status.SUCCESS.getCode(), distanceResponse.getStatus());
		Assert.assertEquals(1, distanceResponse.getRows().size());
		Assert.assertEquals(1, distanceResponse.getRows().get(0).getElements().size());
		Assert.assertEquals(1789, (int) distanceResponse.getRows().get(0).getElements().get(0).getDistance());
	}


	@Test
	public void testValidMultipleElementForTuple() {
		DistanceTupleRequest distanceTupleRequest = new DistanceTupleRequest(Arrays.asList(new Tuples(
						Arrays.asList(new Point(18.573261,73.914440),(new Point(18.566791,73.918359)))),
				new Tuples(
						Arrays.asList(new Point(18.573261,73.914440),(new Point(18.565884,73.915200))))));
		DistanceResponse distanceResponse = distanceService.getDistanceResponse(distanceTupleRequest);
		System.out.println("Distance Response : " + distanceResponse);
		Assert.assertEquals(Status.SUCCESS.getCode(), distanceResponse.getStatus());
		Assert.assertEquals(1, distanceResponse.getRows().size());
		Assert.assertEquals(2, distanceResponse.getRows().get(0).getElements().size());
		Assert.assertEquals(1789, (int) distanceResponse.getRows().get(0).getElements().get(0).getDistance());
		Assert.assertEquals(1611, (int) distanceResponse.getRows().get(0).getElements().get(1).getDistance());
	}


	@Test
	public void testInvalidAndValidMultipleElementForTuple() {
		DistanceTupleRequest distanceTupleRequest = new DistanceTupleRequest(Arrays.asList(new Tuples(
						Arrays.asList(new Point(12.933357, 77.6139250),(new Point(18.566791,73.918359)))),
				new Tuples(
						Arrays.asList(new Point(18.573261,73.914440),(new Point(18.565884,73.915200))))));

		DistanceResponse distanceResponse = distanceService.getDistanceResponse(distanceTupleRequest);
		System.out.println("Distance Response : " + distanceResponse);
		Assert.assertEquals(Status.SUCCESS.getCode(), distanceResponse.getStatus());
		Assert.assertEquals(1, distanceResponse.getRows().size());
		Assert.assertEquals(2, distanceResponse.getRows().get(0).getElements().size());
		Assert.assertEquals(Status.NOT_SUPPORTED.getCode(), distanceResponse.getRows().get(0).getElements().get(0).getStatus());
		Assert.assertEquals(Status.SUCCESS.getCode(), distanceResponse.getRows().get(0).getElements().get(1).getStatus());
	}

	

	public static void main(String[] args) {
		GraphHopper graphHopper = new GraphHopper()
			.setOSMFile("/opt/osm/files/bengaluru_india.pbf")
			.setCHEnable(true)
			.setCHWeighting("shortest")
			.setTraversalMode(TraversalMode.NODE_BASED)
			.setGraphHopperLocation("/opt/osm/bengaluru_gh/")
			.setEncodingManager(new EncodingManager("car, motorcycle"))
			//.setMinNetworkSize(1500, 1500)
			//.setSortGraph(true)
			.forServer();
		//graphHopper.setDefaultWeightLimit(5000.0);
		graphHopper.importOrLoad();
		System.out.println("Traversal mode : " + graphHopper.getTraversalMode());
		
		double fromLat = 12.937018;
		double fromLong = 77.626500;
		
		
		double[][] toLatLongs =  {
				{12.93238481102055,77.63203200190446},
				{12.932891,77.63051300000006},
				{12.934581,77.61628199999996},
				{12.933111,77.61389200000008},
				{12.935155308917174,77.61368652659758},
				{12.933506,77.63047699999993},
				{12.936358,77.61514599999998},
				{12.930837,77.63292200000001},
				{12.937087,77.61947800000007},
				{12.937661,77.62431800000002},
				{12.936902,77.61498299999994},
				{12.929566,77.63441599999999},
				{12.934233,77.62937499999998},
				{12.932998,77.614238},
				{12.934748,77.615995},
				{12.93675273,77.62017803000003},
				{12.916256,77.61226299999998},
				{12.934186,77.629908},
				{12.93328,77.62712899999997},
				{12.934866,77.62799599999994},
				{12.933714,77.62088399999993},
				{12.926989,77.63782300000003},
				{12.934858,77.61579400000005},
				{12.934747,77.619866},
				{12.934513,77.613381},
				{12.935013,77.62446799999998},
				{12.93704,77.61952100000008},
				{12.938724,77.62602600000002},
				{12.934663,77.61119899999994},
				{12.925102,77.63654799999995},
				{12.935966,77.62145299999997},
				{12.932809,77.63113499999997},
				{12.919394,77.61287700000003},
				{12.935048,77.61363600000004},
				{12.93662,77.62662899999998},
				{12.926667,77.638059},
				{12.936759,77.62456299999997},
				{12.943389,77.627472},
				{12.933463,77.61545699999999},
				{12.929783,77.63291800000002},
				{12.93662,77.61444699999993},
				{12.933586,77.626983},
				{12.934756,77.61303700000008},
				{12.916703,77.61629800000003},
				{12.934267,77.61676},
				{12.930786,77.63275699999997},
				{12.934378,77.61340399999995},
				{12.940528,77.619978},
				{12.940598,77.620947},
				{12.93375,77.61975099999995},
				{12.934748,77.62799599999994},
				{12.936828,77.62633499999993},
				{12.931691,77.62273400000004},
				{12.936308,77.62022999999999},
				{12.933362,77.615681},
				{12.929805,77.63316600000007},
				{12.935438,77.61597000000006},
				{12.933652,77.61854100000005},
				{12.941672,77.62136099999998},
				{12.925861,77.63355899999999},
				{12.934756,77.613037},
				{12.930121,77.63302299999998},
				{12.926326,77.60980900000004},
				{12.93348,77.61493799999994},
				{12.934149,77.61660299999994},
				{12.929671,77.63405399999999},
				{12.932168,77.622974},
				{12.934816,77.61580500000002},
				{12.936244,77.62763500000005},
				{12.937165,77.62000999999998},
				{12.934579,77.61638800000003},
				{12.934301,77.625676},
				{12.934646,77.61254400000007},
				{12.93444,77.61128400000007},
				{12.935998,77.61482699999999},
				{12.916674,77.62025700000004},
				{12.93375,77.61975099999995},
				{12.92648,77.61582999999996},
				{12.935268,77.61524299999996},
				{12.931242,77.63279999999997},
				{12.935878,77.61260199999992},
				{12.934362,77.61598300000003},
				{12.941597,77.62368800000002},
				{12.932895,77.61410899999998},
				{12.936852,77.62737700000002},
				{12.922126,77.61045999999999},
				{12.934754,77.61270000000002},
				{12.935999,77.621395},
				{12.930847,77.62273200000004},
				{12.933536,77.61528599999997},
				{12.943993,77.62090999999998},
				{12.934911,77.62941799999999},
				{12.936701,77.61488299999996},
				{12.926291,77.633374},
				{12.925965,77.633418},
				{12.936322,77.62760100000003},
				{12.936107,77.61967200000004},
				{12.933705,77.61842899999999},
				{12.931199,77.62863600000003},
				{12.935638,77.60908199999994},
				{12.926513,77.63615500000003},
				{12.937254,77.62441899999999},
				{12.919126,77.61278500000003},
				{12.933698,77.61569800000007},
				{12.933772,77.61926399999993},
				{12.926308,77.616806},
				{12.942008,77.621085},
				{12.915665,77.60995100000002},
				{12.914613,77.63821000000007},
				{12.933329,77.61614499999996},
				{12.925129,77.63380600000005},
				{12.934847,77.61612300000002},
				{12.936822,77.62486799999999},
				{12.935463,77.62120299999992},
				{12.936802,77.624594},
				{12.935975,77.61602300000004},
				{12.936402,77.61376700000005},
				{12.936933,77.61451699999998},
				{12.927239,77.63781300000005},
				{12.936615,77.61497600000007},
				{12.935581,77.62860599999999},
				{12.935327,77.60915399999999},
				{12.934552,77.62969299999997},
				{12.935244,77.612798},
				{12.9299237181172,77.63300707969279},
				{12.940391,77.62007399999993},
				{12.92907,77.635041},
				{12.922977,77.61440700000003},
				{12.93324,77.62341000000004},
				{12.924183,77.61804200000006},
				{12.9174,77.61540000000002},
				{12.921572,77.62033900000006},
				{12.93874,77.62613799999997},
				{12.93647,77.61508400000002},
				{12.932725,77.62309400000004},
				{12.936913,77.620092},
				{12.934552,77.62969299999997},
				{12.941838,77.62161300000002},
				{12.935781,77.60833300000002},
				{12.934149,77.61660299999994},
				{12.91569,77.61005999999998},
				{12.936347,77.61486200000002},
				{12.916375,77.61723000000006},
				{12.95827,77.61449100000004},
				{12.936216,77.61476399999992},
				{12.937484,77.62628399999994},
				{12.926063,77.61550599999998},
				{12.925064,77.634004},
				{12.93004,77.63341000000003},
				{12.93657,77.61501299999998},
				{12.93934,77.62628500000005},
				{12.936068,77.62140299999999},
				{12.926501,77.63358700000003},
				{12.941811,77.62320399999999},
				{12.9223666667,77.61023055559997},
				{12.934068,77.62919499999998},
				{12.933927,77.61539399999992},
				{12.936445,77.61524199999997},
				{12.933629,77.62228600000003},
				{12.932541,77.62810300000001},
				{12.939659,77.62585999999999},
				{12.936244,77.62763500000005},
				{12.932998,77.614238},
				{12.936238,77.62132799999995},
				{12.936314,77.61282699999992},
				{12.937071,77.62660099999994},
				{12.935964,77.61838399999999},
				{12.934526,77.63034100000004},
				{12.934292,77.61275599999999},
				{12.932976,77.61455000000001},
				{12.91835,77.63808799999993},
				{12.923689,77.61467300000004},
				{12.933715,77.61886000000004},
				{12.955109,77.64151900000002},
				{12.914136,77.63831600000003},
				{12.914375,77.63831800000003},
				{12.936858,77.61292800000001},
				{12.9396509,77.62620470000002},
				{12.933645,77.62708900000007},
				{12.939455,77.62626799999998},
				{12.934444,77.61130600000001},
				{12.9307056,77.63261039999998},
				{12.933602,77.62696200000005},
				{12.936323,77.61488699999995},
				{12.9362,77.61608999999999},
				{12.936483,77.61504500000001}
		};
		int times = 1000;
		long totalTime = 0;
		for (int j = 1; j <= times; j++) {
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < toLatLongs.length ; i++) {
				//GHRequest req = new GHRequest(fromLat, fromLong, toLatLongs[i][0], toLatLongs[i][1])
				GHRequest req = new GHRequest(toLatLongs[i][0], toLatLongs[i][1], fromLat, fromLong)
					.setWeighting("shortest")
					.setVehicle("car")
					.setAlgorithm(AlgorithmOptions.ASTAR_BI);
				GHResponse resp = graphHopper.route(req);
				//System.out.println("Distance : " + resp.getDistance());
				if (resp.hasErrors()) {
					System.out.println("Errors");
				}
			}
			long endTime = System.currentTimeMillis();
			totalTime += (endTime - startTime);
			System.out.println("Time taken : " + (endTime - startTime));
		}
		System.out.println("Total time taken : " + totalTime + " avg time : " + totalTime/times);
	}
}
